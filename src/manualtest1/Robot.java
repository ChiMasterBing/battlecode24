package manualtest1;

import battlecode.common.*;

import java.util.Arrays;
import java.util.Random;

public abstract class Robot {
    

    static RobotController rc;

    static int myMoveNumber;
    MapLocation[] myFlags = {null, null, null};

    public Robot(RobotController rc) throws GameActionException {
        //TODO: First turn bytecode usage is high. can spread across first 5
        this.rc = rc;
        bugNav.init(rc);
        macroPath.init(rc);
        Debug.init(rc);
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
            System.out.println(id + " " + (i-1));
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


    public void turn() throws GameActionException {
        MapLocation target = new MapLocation(30, 15);
        bugNav.move(target);
        MapLocation center = new MapLocation(29, 15);
        if (rc.canBuild(TrapType.EXPLOSIVE, target)) {
            rc.build(TrapType.EXPLOSIVE, center);
        }
    }
}