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
    static BugNav bugNav;
    static MapInfo[] mapInfo;
    static SectorInfo[] sectorInfo;

    // self info
    static MapLocation targetLocation;
    static boolean hasPriority;
    static boolean isStuck;
    static boolean isBugging;

    public Navigation(RobotController r) {
        rc = r;
        bugNav = new BugNav(r);

    }

}

class Grid2x2 {
// 

}