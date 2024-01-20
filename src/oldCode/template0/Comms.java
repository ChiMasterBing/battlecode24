package template0;

// battlecode package
import battlecode.common.*;

// custom package
import template0.java_utils.*;
import template0.Comms.*;
import template0.Debug.*;
import template0.Utils.*;

public class Comms {
    RobotController rc;

    // channel constants
    static final int DUCK_CHANNEL = 0;


    // status constants
    static final int ATTACK_MOVE = 1;
    static final int RETREAT_STUCK = 2;
    static final int FORTIFY_BUILD = 3;

    public Comms(RobotController r) throws GameActionException {
        rc = r;
    }


}
