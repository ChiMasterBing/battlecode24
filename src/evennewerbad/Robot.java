package evennewerbad;

import battlecode.common.*;
import evennewerbad.fast.FastLocSet;

public abstract class Robot {
    

    static RobotController rc;

    int myMoveNumber;
    MapLocation[] myFlags = {null, null, null};

    public Robot(RobotController rc) throws GameActionException {
        //TODO: First turn bytecode usage is high. can spread across first 5
        this.rc = rc;
        bugNav.init(rc);
        teammateTracker.init(rc);
        macroPath.init(rc);
        Debug.init(rc);
        Comms.init(rc);
        //init move ordering
        myMoveNumber = rc.readSharedArray(0);
        //Debug.println("My move order is " + myMoveNumber);
        rc.writeSharedArray(0, myMoveNumber+1);
        rc.writeSharedArray(myMoveNumber+1, rc.getID());
        initFlagStatus();
    }

    void populateTeamIDS() throws GameActionException {
        for (int i=1; i<=50; i++) {
            int id = rc.readSharedArray(i);
            teammateTracker.IDtoMoveOrder.add(id, i-1);
            System.out.println(id + " " + (i-1));
            teammateTracker.initTeammate(id);
        }
    }

    public void initFlagStatus() {
        FastLocSet locs = new FastLocSet();
        for (MapLocation m:rc.getAllySpawnLocations()) {
            locs.add(m);
        }
        for (MapLocation m:rc.getAllySpawnLocations()) {
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

    public void updateFlagStatus() throws GameActionException {
        boolean empty = rc.senseNearbyFlags(-1, rc.getTeam()).length == 0;
        if (empty) {
            if (rc.canSenseLocation(myFlags[0])) {
                Comms.writeToBufferPool(1, Comms.read(1) | 1);

            }
            if (rc.canSenseLocation(myFlags[1])) {
                Comms.writeToBufferPool(1, Comms.read(1) | 2);

            }
            if (rc.canSenseLocation(myFlags[2])) {
                Comms.writeToBufferPool(1, Comms.read(1) | 4);

            }
        }
    }

    public void turn() throws GameActionException {
        if (rc.canBuyGlobal(GlobalUpgrade.ACTION)) {
            rc.buyGlobal(GlobalUpgrade.ACTION);
        }
        if (rc.canBuyGlobal(GlobalUpgrade.HEALING)) {
            rc.buyGlobal(GlobalUpgrade.HEALING);
        }
        if (rc.canBuyGlobal(GlobalUpgrade.CAPTURING)) {
            rc.buyGlobal(GlobalUpgrade.CAPTURING);
        }

        teammateTracker.preTurn();
        
        //if (rc.getRoundNum() > 100) rc.resign();
        updateFlagStatus();

        macroPath.scout();
        macroPath.updateSymm();

        if (myMoveNumber < 1) {
            if (rc.getRoundNum() % 50 == 0) {
                Debug.println(Comms.readSymmetry() + " <-- Symm");
                Debug.println(Comms.read(0) + " ");
            }
        }


        teammateTracker.postTurn();
        Comms.flushBufferPool();
    }
}