package template0;

import com.google.flatbuffers.FlexBuffers.Map;

// battlecode package
import battlecode.common.*;

// custom package
import template0.java_utils.*;
import template0.Comms.*;
import template0.Debug.*;
import template0.Utils.*;

public class Navigation {
    static RobotController rc;
    static MapData mapInfo[];
    static int symmetry;

    // self info
    static MapLocation targetLocation;
    static boolean hasPriority;
    static boolean isStuck;
    static boolean isBugging;

    // ally info
    static RobotInfo robots[];
    static RobotInfo teammates[];
    static MapLocation moveOrderToLocation[];
    
    // constants
    static final int UNKNOWN = 0;
    static final int EMPTY = 1;
    static final int WATER = 2;
    static final int WALL = 3;
    public static final int H_SYM = 0;
    public static final int V_SYM = 1;
    public static final int R_SYM = 2;
    static final MapLocation nullLoc = new MapLocation(-1, -1);

    public void init(RobotController r) throws GameActionException {
        rc = r;
        BugNav.init(rc);
        symmetry = 0;
    }

}

class MapData {
    MapInfo mapInfo;
    
}


// unused
class Grid2x2 {
// 

}