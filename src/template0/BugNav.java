package template0;

import com.google.flatbuffers.FlexBuffers.Map;

// battlecode package
import battlecode.common.*;

// custom package
import template0.java_utils.*;
import template0.Comms.*;
import template0.Debug.*;
import template0.Utils.*;

public class BugNav {
    static RobotController rc;

    // constants
    static boolean lineNav;
    static boolean detectDeadEnds;
    static boolean radiusNav;
    static boolean preferLeftHand;

    // running info
    static MapLocation target;
    static boolean isBugging;
    static BugNavTracker bugNavTracker;

    // init
    public static void init(RobotController r) throws GameActionException {
        rc = r;

        lineNav = true;
        detectDeadEnds = false;
        radiusNav = false;
        preferLeftHand = true;

        target = null;
        isBugging = false;
        bugNavTracker = null;
    }

    // returns previous target
    public static MapLocation setTarget(MapLocation newTarget) throws GameActionException {
        if (isBugging) endBugging();
        MapLocation oldTarget = target;
        target = newTarget;
        return oldTarget;
    }

    public static Direction getGreedyMove() throws GameActionException {
        Direction bestDir = rc.getLocation().directionTo(target);
        if (bestDir.equals(Direction.CENTER)) {
            return null;
        }
        if (rc.canMove(bestDir)) {
            return bestDir;
        }
        Direction leftDir = bestDir.rotateLeft();
        if (rc.canMove(leftDir)) {
            return leftDir;
        }
        Direction rightDir = bestDir.rotateRight();
        if (rc.canMove(rightDir)) {
            return rightDir;
        }
        return null;
    }

    public static Direction navigate() throws GameActionException {
        assert target != null : "No target";
        Direction directionToMove = Direction.CENTER;

        // try 3 greedy moves
        if (!isBugging) {
            directionToMove = getGreedyMove();
            if (getGreedyMove() != null && rc.canMove(directionToMove)) return directionToMove;
        }

        // only reaches if blocked or if bugging
        startBugging(rc.getLocation().directionTo(target));
        return directionToMove;
    }



    public static void startBugging(MapLocation pivot) throws GameActionException {
        assert isBugging == false : "can't start bugging if u already bugging";
        isBugging = true;
        bugNavTracker = new BugNavTracker(pivot);
    }

    public static void endBugging() throws GameActionException {
        assert isBugging == true : "can't stop bugging if u not bugging";
        isBugging = false;
        bugNavTracker = null;
    }

    
}

class BugNavTracker {
    MapLocation wallPivot;
    boolean leftHand;
    boolean rotateReverse;

    public BugNavTracker(MapLocation wallPivot) {
        this.wallPivot = wallPivot;
        this.leftHand = BugNav.preferLeftHand;
        rotateReverse = false;
    }
}
