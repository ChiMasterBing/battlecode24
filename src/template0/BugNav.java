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
    static boolean lineNav = true;
    static boolean detectDeadEnds = false;
    static boolean radiusNav = false;

    // running info
    static boolean isBugging = false;
    static MapLocation target = null;
    static MapLocation wallPivot = null;
    static boolean rotateLeft = true;
    static boolean rotateReverse = false;

    public BugNav(RobotController r) throws GameActionException {
        rc = r;
    }

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
        }

        // only reaches if blocked or if bugging

        return directionToMove;
    }

    public static void startBugging() throws GameActionException {
        assert isBugging == false : "can't start bugging if u already bugging";
        isBugging = true;
    }

    public static void endBugging() throws GameActionException {
        assert isBugging == true : "can't stop bugging if u not bugging";
        isBugging = false;
    }

}

