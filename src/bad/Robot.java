package bad;

import battlecode.common.*;

import java.util.Random;

public abstract class Robot {
    

    static RobotController rc;

    static int myMoveNumber;
    static MapLocation currentTarget = null;

    public Robot(RobotController rc) throws GameActionException {
        //TODO: First turn bytecode usage is high. can spread across first 5
        this.rc = rc;
        bugNav.init(rc);
        teammateTracker.init(rc);
        macroPath.init(rc);
        Debug.init(rc);
        //init move ordering
        myMoveNumber = rc.readSharedArray(0);
        //Debug.println("My move order is " + myMoveNumber);
        rc.writeSharedArray(0, myMoveNumber+1);
        rc.writeSharedArray(myMoveNumber+1, rc.getID());
        System.out.println("finish");
    }

    void populateTeamIDS() throws GameActionException {
        for (int i=1; i<=50; i++) {
            int id = rc.readSharedArray(i);
            teammateTracker.IDtoMoveOrder.add(id, i-1);
            System.out.println(id + " " + (i-1));
            teammateTracker.initTeammate(id);
        }
    }

    public void turn() throws GameActionException {
        teammateTracker.preTurn();

        if (rc.getRoundNum() > 500) {
            rc.resign();
        }

        currentTarget = new MapLocation(31, 27);
        //Debug.println("attemting to move");
        bugNav.move(currentTarget);
        macroPath.scout();
        macroPath.updateSymm();


        teammateTracker.postTurn();
    }
}