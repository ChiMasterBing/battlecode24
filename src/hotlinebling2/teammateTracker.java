package hotlinebling2;

import battlecode.common.*;
import hotlinebling.fast.FastIntIntMap;
import hotlinebling.fast.FastIntLocMap;
import hotlinebling.fast.FastQueue;

public class teammateTracker {
    static RobotController rc;
    public static void init(RobotController r) {
        rc = r;
    }

    public static int getPriority(RobotInfo ri) {
        int priority = 5 + ri.attackLevel + ri.healLevel + ri.buildLevel;
        if (ri.hasFlag) priority += 20;
        priority *= 1000;
        priority -= IDtoMoveOrder.getVal(ri.ID);
        //System.out.println(ri.ID + " -- " + IDtoMoveOrder.getVal(ri.ID));
        return priority;
    }

    static RobotInfo[] robots;
    static RobotInfo[] teammates = new RobotInfo[50];
    static FastIntLocMap teammateLocations = new FastIntLocMap();
    static FastQueue<Integer> prevTurnTeammates = new FastQueue<Integer>(100);
    static final MapLocation nullLoc = new MapLocation(80, 80);
    static FastIntIntMap IDtoMoveOrder = new FastIntIntMap();

    public static void initTeammate(int id) {
        teammateLocations.add(id, nullLoc);
    }

    public static void preTurn() {
        robots = rc.senseNearbyRobots();
        int ptr = 0;
        for (RobotInfo ri:robots) {
            if (ri.team == rc.getTeam()) {
                teammates[ptr] = ri;
                ptr++;
            }
        }
    }

    public static void postTurn() {
        //prevTurnTeammates contains teammates from previous turn
        while (!prevTurnTeammates.isEmpty()) {
            teammateLocations.addReplace(prevTurnTeammates.poll(), nullLoc);
        }
        
        for (RobotInfo ri:teammates) {
            if (ri == null) break;
            teammateLocations.addReplace(ri.ID, ri.location);
            prevTurnTeammates.add(ri.ID);
        }

        for (int i=0; i<50; i++) { //cann unroll later
            teammates[i] = null;
        }
    }

    public static Direction getTeammateDirection(RobotInfo ri) {
        MapLocation loc = teammateLocations.getLoc(ri.ID);
        Direction d = null;
        if (loc == null) {
            System.out.println("WHAT");
            return null;
        }
        if (loc.x != nullLoc.x) {
            d = loc.directionTo(ri.location);
        }
        return d;
    }
}
