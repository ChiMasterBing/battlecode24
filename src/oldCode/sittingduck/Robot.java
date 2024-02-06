package sittingduck;

import java.util.Arrays;

import battlecode.common.*;
import sittingduck.fast.FastLocSet;

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
        if (roundNumber > 10) { //If we are in range of a spawn zone, check if our flag is still there.
            boolean[] tmp = {false, false, false};
            for (FlagInfo f:flags) {
                if (f.getTeam() == rc.getTeam()) {
                    if (f.getID() == flagIDs[0]) tmp[0] = true;
                    else if (f.getID() == flagIDs[1]) tmp[1] = true;
                    else if (f.getID() == flagIDs[2]) tmp[2] = true;
                }
            }
            if (rc.canSenseLocation(myFlags[0])) {
                if (tmp[0]) {
                    Comms.writeMyFlag(0, 1);
                }
                else {
                    Comms.writeMyFlag(0, 0);
                    Comms.writeFlagStatus(0, 15);
                }
            }
            if (rc.canSenseLocation(myFlags[1])) {
                if (tmp[1]) {
                    Comms.writeMyFlag(1, 1);
                }
                else {
                    Comms.writeMyFlag(1, 0);
                    Comms.writeFlagStatus(1, 15);
                }
            }
            if (rc.canSenseLocation(myFlags[2])) {
                if (tmp[2]) {
                    Comms.writeMyFlag(2, 1);
                }
                else {
                    Comms.writeMyFlag(2, 0);
                    Comms.writeFlagStatus(2, 15);
                }
            }
        }
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

    public abstract void turn() throws GameActionException;
    public abstract void deadFunctions() throws GameActionException;
    public int MYTYPE, myFlagNum;

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
        if (roundNumber == 2) {
            commFlagID();
        }


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

    }

    // todo for me:
    // choose correct place
    // dont overcrowd crums

    private void buyUpgrades() throws GameActionException {
        if (roundNumber == 600 && rc.canBuyGlobal(GlobalUpgrade.ATTACK)) rc.buyGlobal(GlobalUpgrade.ATTACK);
        else if (roundNumber == 1200 && rc.canBuyGlobal(GlobalUpgrade.HEALING)) rc.buyGlobal(GlobalUpgrade.HEALING);
        else if (roundNumber == 1800 && rc.canBuyGlobal(GlobalUpgrade.CAPTURING)) rc.buyGlobal(GlobalUpgrade.CAPTURING);
    }

    private void doPreRoundTasks() throws GameActionException {
        switch (roundNumber) {
            case 2:
                populateTeamIDS();
                Comms.writeToBufferPool(0, 0);
                break;
            case 3:
                if (!rc.isSpawned()) {
                    macroPath.eliminateSpawnSymmetries(myFlags[0], myFlags[1], myFlags[2]);
                    macroPath.calculateSpawnDistribution(myFlags[0], myFlags[1], myFlags[2]);
                }
                readCommFlagID();
                break;
            case 4:
                Comms.writeToBufferPool(myMoveNumber, 0);
                if (myMoveNumber + 50 < 64) {
                    Comms.writeToBufferPool(myMoveNumber+50, 0);
                }
                break;
            case 5:
                Comms.writeFlagStatus(myMoveNumber%3, 8);
                break;
            // case 200:
            //     rc.resign();
            //     break;
            default:
                if (roundNumber % 3 == 0 && roundNumber >= 50) {
                    Comms.readAllSectorMessages();
                }
                break;
        }
    }

    int mySpawn = -1;
    private boolean trySpawn() throws GameActionException {
        if (roundNumber < 200) {
            if (roundNumber == 1) {
                if (myMoveNumber < 3) {
                    mySpawn = myMoveNumber;
                    rc.spawn(myFlags[mySpawn]);
                    //find closest you can get to your opp via dam
                }
            }
            else if (roundNumber > 5) {
                int tot = macroPath.spawnScores[0] + macroPath.spawnScores[1] + macroPath.spawnScores[2];
                //System.out.println(macroPath.spawnScores[0] + " " + macroPath.spawnScores[1] + " " + macroPath.spawnScores[2]);
                int r1 = (macroPath.spawnScores[0] * 47) / tot;
                int r2 = (macroPath.spawnScores[1] * 47) / tot;
                MapLocation center = null;
                if (myMoveNumber < r1 + 3) {
                    mySpawn = 0;
                    center = myFlags[0];
                }
                else if (myMoveNumber < r1 + r2 + 3 && myMoveNumber >= r1 + 3) {
                    mySpawn = 1;
                    center = myFlags[1];
                }
                else {
                    mySpawn = 2;
                    center = myFlags[2];
                }

                for (Direction d:allDirections) {
                    if (rc.canSpawn(center.add(d))) {
                        rc.spawn(center.add(d));
                        return true;
                    }
                }
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
                mySpawn = 0;
            }
            else if (min == b) {
                center = myFlags[1];
                mySpawn = 1;
            }
            else if (min == c) {
                center = myFlags[2];
                mySpawn = 2;
            }

            if (myMoveNumber < 6) {
                center = myFlags[myMoveNumber%3]; //always try spawning stuff at other spawns to lessen getting sniped
            }

            if ((min < 8 || roundNumber % 3 == 0) && (center != null)) {
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
            mySpawn = -1;
            if (trySpawn()) spawnedTurn();
            else deadFunctions();
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

        rc.setIndicatorString(Comms.myFlagExists(myMoveNumber%3) + " " + mySpawn + ": " + Comms.readFlagStatus(0) + " " + Comms.readFlagStatus(1) + " " + Comms.readFlagStatus(2));
    }
}