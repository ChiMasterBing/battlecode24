package macroPath;

import battlecode.common.*;

import java.util.Random;

public abstract class Robot {
    

    static RobotController rc;
    static macroPath MP;


    public Robot(RobotController rc) {
        this.rc = rc;
        bugNav.init(rc);
    }

    public void turn() throws GameActionException {

    }
}