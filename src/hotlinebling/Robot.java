package hotlinebling;

import java.util.Arrays;

import battlecode.common.*;
import hotlinebling.fast.FastLocSet;

public abstract class Robot {
    

    static RobotController rc;

    int myMoveNumber, roundNumber;
    MapLocation[] myFlags = {null, null, null};
    MapLocation[] stolenFlags = {null, null, null};
    int[] stolenFlagRounds = {0, 0, 0};
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

        for (FlagInfo f:nearbyFlags) {
            if (f.isPickedUp()) {
                if (flagIDs[0] == f.getID()) {
                    Comms.distressFlag(0, f.getLocation());
                }
                if (flagIDs[1] == f.getID()) {
                    Comms.distressFlag(1, f.getLocation());
                }
                if (flagIDs[2] == f.getID()) {
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
            if (roundNumber - info.round > 25) continue; //irrelavent
            switch (info.type) {
                case 0:
                    stolenFlags[info.flagID] = info.loc;
                    stolenFlagRounds[info.flagID] = info.round;
                    rc.setIndicatorDot(info.loc, 0, 0, 0);
                    // if (myMoveNumber == 0) {
                    //     System.out.println("STOLEN FLAG " + info.loc);
                    // }
                    break;
            
                default:
                    break;
            }

        }
    }

    public void spawnedTurn() throws GameActionException {
        teammateTracker.preTurn();
        
        turn();


        if (myMoveNumber < 1) {
            if (roundNumber % 50 == 0) {
                Debug.println(Comms.readSymmetry() + " <-- Symm");
                Debug.println(Comms.countFlagsCaptured() + "<-- flags captured");
            }
        }

        teammateTracker.postTurn();
        if (Clock.getBytecodesLeft() > 6000 && roundNumber > 10) {
            macroPath.scout();
            macroPath.updateSymm();
        }

        if (Clock.getBytecodesLeft() > 5000) {
            checkStolenFlags();
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

        roundNumber = rc.getRoundNum();
        Comms.initBufferPool(); //~300 bytecode

        if (roundNumber%3 == 0 && roundNumber >= 50) {
            Comms.readAllSectorMessages();
        }
        
        //init moveordering
        if (roundNumber == 2) {
            populateTeamIDS();
            if (rc.isSpawned()) commFlagID();
        }
        if (roundNumber == 3) {
            readCommFlagID();
        }
        if (roundNumber == 3 && myMoveNumber == 49) { 
            for (int i=0; i<64; i++) {
                rc.writeSharedArray(i, 0);
            }
        }
        

        if (!rc.isSpawned()){
            MapLocation[] spawnLocs = rc.getAllySpawnLocations();
            if (roundNumber < 200) {
                if (rc.canSpawn(spawnLocs[myMoveNumber % spawnLocs.length])) {
                    rc.spawn(spawnLocs[myMoveNumber % spawnLocs.length]);
                    spawnedTurn();
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
                    if (roundNumber % 9 == 0) center = myFlags[0]; // && Comms.isBitSet(1, 0)
                    if (roundNumber % 9 == 3) center = myFlags[1]; //  && Comms.isBitSet(1, 1)
                    if (roundNumber % 9 == 6) center = myFlags[2]; //  && Comms.isBitSet(1, 2)
                }

                for (Direction d:Direction.allDirections()) {
                    if (roundNumber % 3 == 0 && rc.canSpawn(center.add(d))) {
                        rc.spawn(center.add(d));
                        spawnedTurn();
                        break;
                    }
                }
            }
        }else {
            spawnedTurn();
        }

        //sector comms
        if (roundNumber > 50) {
            if (roundNumber % 3 == 2 && Clock.getBytecodesLeft() > 2500) {
                //OPTIMIZE THIS
                Comms.flushSectorMessageQueue();
            }
            else if (roundNumber% 3 == 1) {
                Comms.writeToBufferPool(myMoveNumber%20 + 25,0);
            }
        }

        Comms.flushBufferPool(); //~no dirty = 300 bc   

        processMessages();
    }
}