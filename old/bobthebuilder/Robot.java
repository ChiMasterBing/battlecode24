package bobthebuilder;

import java.util.Arrays;

import battlecode.common.*;
import bobthebuilder.fast.FastLocSet;

public abstract class Robot {
    RobotController rc;

    int myMoveNumber, roundNumber;
    MapLocation[] spawnLocs;
    MapLocation[] myFlags = {null, null, null};
    MapLocation[] mirrorFlags = {null, null, null};
    MapLocation[] stolenFlags = {null, null, null};
    int[] stolenFlagRounds = {0, 0, 0};
    int[] flagIDs = {0, 0, 0};

    int assumedSymmetry = macroPath.R_SYM;
    Direction[] allDirections = Direction.allDirections();

    public Robot(RobotController rc) throws GameActionException {
        this.rc = rc;
        bugNav.init(rc);
        teammateTracker.init(rc);
        macroPath.init(rc);
        Debug.init(rc);
        Comms.init(rc);
        myMoveNumber = rc.readSharedArray(0);
        rc.writeSharedArray(0, myMoveNumber+1);
        rc.writeSharedArray(myMoveNumber+1, rc.getID());

        spawnLocs = rc.getAllySpawnLocations();

        initFlagStatus(); //~4000 bytecode
        updateSymmetryComputations();
    }

    FlagInfo[] flags = null;
    public void senseGlobals() throws GameActionException {
        flags = rc.senseNearbyFlags(-1);
    }

    private void populateTeamIDS() throws GameActionException {
        for (int i=1; i<=50; i++) {
            int id = rc.readSharedArray(i);
            teammateTracker.IDtoMoveOrder.add(id, i-1);
            teammateTracker.initTeammate(id);
        }
    }

    private void initFlagStatus() {
        FastLocSet locs = new FastLocSet();
        for (MapLocation m:spawnLocs) {
            locs.add(m);
        }
        for (MapLocation m:spawnLocs) {
            boolean isCenter = true;
            for (Direction d:allDirections) {
                if (!locs.contains(m.add(d))) {
                    isCenter = false;
                    break;
                }
            }
            if (isCenter) {
                if (myFlags[0] == null) myFlags[0] = m;
                else if (myFlags[1] == null) myFlags[1] = m;
                else myFlags[2] = m;
            }
        }
    }

    private void commFlagID() throws GameActionException {
        for (FlagInfo f:flags) {
            MapLocation floc = f.getLocation();
            if (floc.equals(myFlags[0])) {
                Comms.writeToBufferPool(51, f.getID());
            }
            else if (floc.equals(myFlags[1])) {
                Comms.writeToBufferPool(52, f.getID());
            }
            else {
                Comms.writeToBufferPool(53, f.getID());
            }
        }
    }

    private void readCommFlagID() {
        flagIDs[0] = Comms.read(51);
        flagIDs[1] = Comms.read(52);
        flagIDs[2] = Comms.read(53);
    }

    public void updateSymmetryComputations() throws GameActionException {
        switch (assumedSymmetry) {
            case macroPath.R_SYM:
                mirrorFlags[0] = macroPath.getRSym(myFlags[0]);
                mirrorFlags[1] = macroPath.getRSym(myFlags[1]);
                mirrorFlags[2] = macroPath.getRSym(myFlags[2]);
                break;
            case macroPath.H_SYM:
                mirrorFlags[0] = macroPath.getHSym(myFlags[0]);
                mirrorFlags[1] = macroPath.getHSym(myFlags[1]);
                mirrorFlags[2] = macroPath.getHSym(myFlags[2]);
                break;
            case macroPath.V_SYM:
                mirrorFlags[0] = macroPath.getVSym(myFlags[0]);
                mirrorFlags[1] = macroPath.getVSym(myFlags[1]);
                mirrorFlags[2] = macroPath.getVSym(myFlags[2]);
                break;
        }
    }

    public void checkStolenFlags() throws GameActionException {

        for (FlagInfo f:flags) {
            if (f.getTeam() != rc.getTeam()) continue;
            if (f.isPickedUp()) {
                if (flagIDs[0] == f.getID()) {
                    Comms.distressFlag(0, f.getLocation());
                }
                else if (flagIDs[1] == f.getID()) {
                    Comms.distressFlag(1, f.getLocation());
                }
                else if (flagIDs[2] == f.getID()) {
                    Comms.distressFlag(2, f.getLocation());
                }
            }
        }
    }

    public abstract void turn() throws GameActionException;

    public void processMessages() {
        if (roundNumber <= 50) return;
        while (!Comms.sectorMessages.isEmpty()) {
            if (Clock.getBytecodesLeft() < 1000) break;
            SectorInfo info = Comms.decodeSectorMessage(Comms.sectorMessages.poll());
            if (roundNumber - info.round > 25) continue; //irrelevent
            switch (info.type) {
                case 0:
                    stolenFlags[info.flagID] = info.loc;
                    stolenFlagRounds[info.flagID] = info.round;
                    rc.setIndicatorDot(info.loc, 0, 0, 0);
                    break;
                default:
                    break;
            }

        }
    }

    public void spawnedTurn() throws GameActionException {
        senseGlobals();

        teammateTracker.preTurn();

        
        turn();


        teammateTracker.postTurn();

        if (roundNumber < 200) {
            if (Clock.getBytecodesLeft() > 6000 && roundNumber > 10) {
                macroPath.scout();
                macroPath.updateSymm();
            }
        }
        else {
            if (Clock.getBytecodesLeft() > 12000 && roundNumber > 10) {
                macroPath.scout();
                macroPath.updateSymm();
            }
        }

        if (Clock.getBytecodesLeft() > 5000) {
            checkStolenFlags();
        }
    }

    private void buyUpgrades() throws GameActionException {
        if (roundNumber == 600 && rc.canBuyGlobal(GlobalUpgrade.HEALING)) rc.buyGlobal(GlobalUpgrade.HEALING);
        else if (roundNumber == 1200 && rc.canBuyGlobal(GlobalUpgrade.ATTACK)) rc.buyGlobal(GlobalUpgrade.ATTACK);
        else if (roundNumber == 1800 && rc.canBuyGlobal(GlobalUpgrade.CAPTURING)) rc.buyGlobal(GlobalUpgrade.CAPTURING);
    }

    private void doPreRoundTasks() throws GameActionException {
        switch (roundNumber) {
            case 2:
                populateTeamIDS();
                if (rc.isSpawned()) commFlagID();
                break;
            case 3:
                readCommFlagID();
                break;
            case 4:
                Comms.writeToBufferPool(myMoveNumber, 0);
                if (myMoveNumber + 50 < 64) {
                    Comms.writeToBufferPool(myMoveNumber+50, 0);
                }
                break;

            default:
                if (roundNumber % 3 == 0 && roundNumber >= 50) {
                    Comms.readAllSectorMessages();
                }
                break;
        }
    }

    private boolean trySpawn() throws GameActionException {
        MapLocation choice = null;
        if (roundNumber < 200) {
            choice = spawnLocs[myMoveNumber % spawnLocs.length];
            if (rc.canSpawn(choice)) {
                rc.spawn(choice);
                return true;
            }
        }
        else {
            MapLocation center = null;
            if (roundNumber - stolenFlagRounds[0]  < 15 || roundNumber - stolenFlagRounds[1] < 15 || roundNumber - stolenFlagRounds[2] < 15) {
                int minDist = 10000;
                if (roundNumber - stolenFlagRounds[0] < 15) {
                    int d1 = myFlags[0].distanceSquaredTo(stolenFlags[0]);
                    int d2 = myFlags[1].distanceSquaredTo(stolenFlags[0]);
                    int d3 = myFlags[2].distanceSquaredTo(stolenFlags[0]);
                    if (d1 < minDist) {
                        center = myFlags[0];
                        minDist = d1;
                    }
                    if (d2 < minDist) {
                        center = myFlags[1];
                        minDist = d2;
                    }
                    if (d3 < minDist) {
                        center = myFlags[2];
                        minDist = d3;
                    }
                }
                if (roundNumber - stolenFlagRounds[1]  < 15) {
                    int d1 = myFlags[0].distanceSquaredTo(stolenFlags[1]);
                    int d2 = myFlags[1].distanceSquaredTo(stolenFlags[1]);
                    int d3 = myFlags[2].distanceSquaredTo(stolenFlags[1]);
                    if (d1 < minDist) {
                        center = myFlags[0];
                        minDist = d1;
                    }
                    if (d2 < minDist) {
                        center = myFlags[1];
                        minDist = d2;
                    }
                    if (d3 < minDist) {
                        center = myFlags[2];
                        minDist = d3;
                    }
                }
                if (roundNumber - stolenFlagRounds[2] < 15) {
                    int d1 = myFlags[0].distanceSquaredTo(stolenFlags[2]);
                    int d2 = myFlags[1].distanceSquaredTo(stolenFlags[2]);
                    int d3 = myFlags[2].distanceSquaredTo(stolenFlags[2]);
                    if (d1 < minDist) {
                        center = myFlags[0];
                        minDist = d1;
                    }
                    if (d2 < minDist) {
                        center = myFlags[1];
                        minDist = d2;
                    }
                    if (d3 < minDist) {
                        center = myFlags[2];
                        minDist = d3;
                    }
                }
            }
            else {
                if (roundNumber % 9 == 0) center = myFlags[0];
                if (roundNumber % 9 == 3) center = myFlags[1];
                if (roundNumber % 9 == 6) center = myFlags[2];
            }
            for (Direction d:allDirections) {
                if (roundNumber % 3 == 0 && rc.canSpawn(center.add(d))) {
                    rc.spawn(center.add(d));
                    return true;
                }
            }
        }
        return false;
    }

    public void play() throws GameActionException {
        roundNumber = rc.getRoundNum();

        if (myMoveNumber < 1 && roundNumber % 100 == 0) {
            Debug.println(Comms.readSymmetry() + " <-- Symm");
            Debug.println(Comms.countFlagsCaptured() + "<-- flags captured");
        }

        buyUpgrades();
        Comms.initBufferPool(); //~300 bytecode
        doPreRoundTasks();

        if (!rc.isSpawned()){
            if (trySpawn()) spawnedTurn();
        }else {
            //int start = Clock.getBytecodesLeft();
            spawnedTurn();
            // if (myMoveNumber < 1) {
            //     Debug.println((start - Clock.getBytecodesLeft()) + " ");
            // }
            
        }

        if (roundNumber > 50) {
            if (roundNumber % 3 == 2 && Clock.getBytecodesLeft() > 2500) {
                Comms.flushSectorMessageQueue(); //OPTIMIZE THIS
            }
            else if (roundNumber% 3 == 1) {
                Comms.writeToBufferPool(myMoveNumber%20 + 25,0);
            }
        }

        Comms.flushBufferPool(); //~no dirty = 300 bc   

        if (Clock.getBytecodesLeft() > 1500) {
            int symm = Comms.readSymmetry();
            if (symm != assumedSymmetry) {
                assumedSymmetry = symm;
                updateSymmetryComputations();
            }
        }

        processMessages();
    }
}