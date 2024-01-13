package evennewerbad;

import java.util.Arrays;

import battlecode.common.*;
import evennewerbad.fast.FastLocSet;

public abstract class Robot {
    

    static RobotController rc;

    int myMoveNumber;
    MapLocation[] myFlags = {null, null, null};
    int[] flagIDs = {0, 0, 0};

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

        initFlagStatus(); //~4000 bytecode
    }

    boolean initTeamIDS = false;
    void populateTeamIDS() throws GameActionException {
        for (int i=1; i<=50; i++) {
            int id = rc.readSharedArray(i);
            teammateTracker.IDtoMoveOrder.add(id, i-1);
            teammateTracker.initTeammate(id);
        }
        initTeamIDS = true;
    }
    void commFlagID() throws GameActionException {
        FlagInfo[] flags = rc.senseNearbyFlags(-1);
        for (FlagInfo f:flags) {
            if (f.getLocation().equals(myFlags[0])) {
                Comms.writeToBufferPool(51, f.getID());
            }
            else if (f.getLocation().equals(myFlags[1])) {
                Comms.writeToBufferPool(52, f.getID());
            }
            else {
                Comms.writeToBufferPool(53, f.getID());
            }
        }
    }
    void readCommFlagID() {
        flagIDs[0] = Comms.read(51);
        flagIDs[1] = Comms.read(52);
        flagIDs[2] = Comms.read(53);
    }
    public void initFlagStatus() {
        FastLocSet locs = new FastLocSet();
        MapLocation[] spawns = rc.getAllySpawnLocations();
        for (MapLocation m:spawns) {
            locs.add(m);
        }
        for (MapLocation m:spawns) {
            boolean isCenter = true;
            for (Direction d:Direction.allDirections()) {
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

    public void checkStolenFlags() throws GameActionException {
        FlagInfo[] nearbyFlags = rc.senseNearbyFlags(-1, rc.getTeam());
        boolean[] warning = {false, false, false};

        for (FlagInfo f:nearbyFlags) {
            if (f.isPickedUp()) {
                if (flagIDs[0] == f.getID()) {
                    warning[0] = true;
                }
                if (flagIDs[1] == f.getID()) {
                    warning[1] = true;
                }
                if (flagIDs[2] == f.getID()) {
                    warning[2] = true;
                }
            }
        } 

        if (warning[0]) {

        }
    }

    public abstract void turn() throws GameActionException;

    public void spawnedTurn() throws GameActionException {
        teammateTracker.preTurn();
        turn();
        if (myMoveNumber < 1) {
            if (rc.getRoundNum() % 50 == 0) {
                Debug.println(Comms.readSymmetry() + " <-- Symm");
                Debug.println(Comms.countFlagsCaptured() + "<-- flags captured");
            }
        }
        teammateTracker.postTurn();
        if (Clock.getBytecodesLeft() > 6000 && rc.getRoundNum() > 10) {
            macroPath.scout();
            macroPath.updateSymm();
        }
    }

    public void play() throws GameActionException {  
        if (rc.canBuyGlobal(GlobalUpgrade.ACTION)) {
            rc.buyGlobal(GlobalUpgrade.ACTION);
        }
        if (rc.canBuyGlobal(GlobalUpgrade.HEALING)) {
            rc.buyGlobal(GlobalUpgrade.HEALING);
        }
        if (rc.canBuyGlobal(GlobalUpgrade.CAPTURING)) {
            rc.buyGlobal(GlobalUpgrade.CAPTURING);
        }

        Comms.initBufferPool(); //~300 bytecode
        //init moveordering
        if (rc.getRoundNum() == 2) {
            populateTeamIDS();
            if (rc.isSpawned()) commFlagID();
        }
        if (rc.getRoundNum() == 3) {
            readCommFlagID();
        }
        if (rc.getRoundNum() == 3 && myMoveNumber == 50) { 
            for (int i=0; i<64; i++) {
                rc.writeSharedArray(i, 0);
            }
        }
        

        if (!rc.isSpawned()){
            MapLocation[] spawnLocs = rc.getAllySpawnLocations();
            if (rc.getRoundNum() < 200) {
                if (rc.canSpawn(spawnLocs[myMoveNumber % spawnLocs.length])) {
                    rc.spawn(spawnLocs[myMoveNumber % spawnLocs.length]);
                    spawnedTurn();
                }
            }
            else {
                MapLocation center = null;
                if (rc.getRoundNum() % 9 == 0) center = myFlags[0]; // && Comms.isBitSet(1, 0)
                if (rc.getRoundNum() % 9 == 3) center = myFlags[1]; //  && Comms.isBitSet(1, 1)
                if (rc.getRoundNum() % 9 == 6) center = myFlags[2]; //  && Comms.isBitSet(1, 2)
                for (Direction d:Direction.allDirections()) {
                    if (rc.getRoundNum() % 3 == 0 && rc.canSpawn(center.add(d))) {
                        rc.spawn(center.add(d));
                        spawnedTurn();
                        break;
                    }
                }
            }
        }else {
            spawnedTurn();
        }

        Comms.flushBufferPool();        
    }
}