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

    static boolean lineNav = true;
    static boolean detectDeadEnds = false;
    static boolean radiusNav = false;

    // running info
    static boolean isBugging = false;
    static MapLocation target = null;
    static MapLocation wallPivot = null;
    static boolean rotateLeft = true;
    static boolean rotateReverse = false;

    public BugNav(RobotController r) {
        rc = r;
    }

    public static MapLocation setTarget(MapLocation newTarget) {
        MapLocation oldTarget = target;
        target = newTarget;
        return oldTarget;
    }

    public static Direction greedyMove() {
        return rc.getLocation().directionTo(target);
    }

    public static Direction navigate() {
        assert target != null : "No target";
        Direction output = Direction.CENTER;

        // try greedy move
        if (!isBugging) {
            output = greedyMove();
            if (output.equals(Direction.CENTER) || rc.canMove(output)) return output;
            if (rc.canMove(output)) return output;
        }

        //only reaches if blocked or if bugging

        return output;
    }

    public static void startBugging() {
        assert isBugging == false : "can't start bugging if u already bugging";
        isBugging = true;
    }

    public static void endBugging() {
        assert isBugging == true : "can't stop bugging if u not bugging";
        isBugging = false;
    }

}

