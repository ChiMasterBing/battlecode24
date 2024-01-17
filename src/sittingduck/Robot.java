package sittingduck;

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
    public int MYTYPE;

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
        if (roundNumber == 750 && rc.canBuyGlobal(GlobalUpgrade.ACTION)) rc.buyGlobal(GlobalUpgrade.ACTION);
        else if (roundNumber == 1500 && rc.canBuyGlobal(GlobalUpgrade.HEALING)) rc.buyGlobal(GlobalUpgrade.HEALING);
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
        else if (roundNumber > 200) {
            MapLocation center = null;

            int a = Comms.readFlagStatus(0);
            int b = Comms.readFlagStatus(1);
            int c = Comms.readFlagStatus(2);

            int min = Math.min(a, Math.min(b, c));

            if (min == a) {
                center = myFlags[0];
            }
            else if (min == b) {
                center = myFlags[1];
            }
            else if (min == c) {
                center = myFlags[2];
            }

            if (center != null) {
                for (Direction d:allDirections) {
                    if (rc.canSpawn(center.add(d))) {
                        rc.spawn(center.add(d));
                        return true;
                    }
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
            spawnedTurn();
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