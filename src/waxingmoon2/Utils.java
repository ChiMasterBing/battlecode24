package waxingmoon2;

import java.util.Map;
import java.util.Random;

// battlecode package
import battlecode.common.*;

// custom package
import template1.java_utils.*;
import template1.Debug.*;

public class Utils {
    private static RobotController rc;

    static Random rng;
    
    // constants that vary by game
    static int roundNumber;

    static int MAP_WIDTH;
    static int MAP_HEIGHT;
    static int MAP_AREA;
    static FastIntSet spawnIntLocations = new FastIntSet();

    // constants that do not vary, e.g. specialization info
    static final Direction[] DIRECTIONS = {
        Direction.NORTH,
        Direction.NORTHEAST,
        Direction.EAST,
        Direction.SOUTHEAST,
        Direction.SOUTH,
        Direction.SOUTHWEST,
        Direction.WEST,
        Direction.NORTHWEST,
    };

    static final Direction[] DIRS_CENTER = {
        Direction.NORTH,
        Direction.NORTHEAST,
        Direction.EAST,
        Direction.SOUTHEAST,
        Direction.SOUTH,
        Direction.SOUTHWEST,
        Direction.WEST,
        Direction.NORTHWEST,
        Direction.CENTER,
    };

    static final Direction[] X_DIRECTIONS = {
        Direction.CENTER,
        Direction.NORTHEAST,
        Direction.SOUTHEAST,
        Direction.SOUTHWEST,
        Direction.NORTHWEST,
    };

    static final Direction[] CARDINAL_DIRECTIONS = {
        Direction.NORTH,
        Direction.EAST,
        Direction.SOUTH,
        Direction.WEST,
    };

    static final Direction[][] SHIFTED_DX_DY_TO_DIRECTION = {
        {Direction.SOUTHWEST, Direction.WEST, Direction.NORTHWEST},
        {Direction.SOUTH, Direction.CENTER, Direction.NORTH},
        {Direction.SOUTHEAST, Direction.EAST, Direction.NORTHEAST}
    };

    static final int[][] SHIFTED_DX_DY_TO_INT = {
        {5, 6, 7},
        {4, 8, 0},
        {3, 2, 1}
    };

    static final int[] INT_TO_DX = {
        0, 1, 1, 1, 0, -1, -1, -1, 0
    };

    static final int[] INT_TO_DY = {
        1, 1, 0, -1, -1, -1, 0, 1, 0
    };

    static final int[] BASIC_MASKS = {
        (1 << 0) - 1, (1 << 1) - 1, (1 << 2) - 1, (1 << 3) - 1, (1 << 4) - 1, (1 << 5) - 1, (1 << 6) - 1, (1 << 7) - 1, (1 << 8) - 1, (1 << 9) - 1, 
        (1 << 10) - 1, (1 << 11) - 1, (1 << 12) - 1, (1 << 13) - 1, (1 << 14) - 1, (1 << 15) - 1, (1 << 16) - 1, (1 << 17) - 1, (1 << 18) - 1, (1 << 19) - 1, 
        (1 << 20) - 1, (1 << 21) - 1, (1 << 22) - 1, (1 << 23) - 1, (1 << 24) - 1, (1 << 25) - 1, (1 << 26) - 1, (1 << 27) - 1, (1 << 28) - 1, (1 << 29) - 1, 
        (1 << 30) - 1, (1 << 31) - 1, (1 << 32) - 1, (1 << 33) - 1, (1 << 34) - 1, (1 << 35) - 1, (1 << 36) - 1, (1 << 37) - 1, (1 << 38) - 1, (1 << 39) - 1, 
        (1 << 40) - 1, (1 << 41) - 1, (1 << 42) - 1, (1 << 43) - 1, (1 << 44) - 1, (1 << 45) - 1, (1 << 46) - 1, (1 << 47) - 1, (1 << 48) - 1, (1 << 49) - 1, 
        (1 << 50) - 1, (1 << 51) - 1, (1 << 52) - 1, (1 << 53) - 1, (1 << 54) - 1, (1 << 55) - 1, (1 << 56) - 1, (1 << 57) - 1, (1 << 58) - 1, (1 << 59) - 1, 
        (1 << 60) - 1, (1 << 61) - 1, (1 << 62) - 1, (1 << 63) - 1, ~0
    }; 

    final static int[] ALLY_IDX_TO_STATUS_SLOT = {
        Comms.ALLY_STATUS_IDX + 0/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 1/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 2/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 3/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 4/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 5/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 6/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 7/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 8/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 9/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 10/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 11/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 12/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 13/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 14/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 15/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 16/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 17/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 18/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 19/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 20/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 21/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 22/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 23/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 24/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 25/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 26/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 27/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 28/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 29/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 30/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 31/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 32/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 33/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 34/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 35/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 36/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 37/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 38/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 39/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 40/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 41/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 42/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 43/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 44/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 45/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 46/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 47/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 48/Comms.ALLY_STATUS_PERSLOT,
        Comms.ALLY_STATUS_IDX + 49/Comms.ALLY_STATUS_PERSLOT
    };

    final static int[] ALLY_IDX_TO_STATUS_BITSHIFT = {
        Comms.ALLY_STATUS_BITLEN * (0%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (1%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (2%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (3%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (4%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (5%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (6%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (7%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (8%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (9%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (10%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (11%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (12%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (13%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (14%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (15%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (16%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (17%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (18%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (19%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (20%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (21%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (22%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (23%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (24%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (25%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (26%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (27%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (28%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (29%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (30%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (31%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (32%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (33%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (34%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (35%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (36%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (37%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (38%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (39%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (40%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (41%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (42%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (43%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (44%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (45%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (46%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (47%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (48%Comms.ALLY_STATUS_PERSLOT),
        Comms.ALLY_STATUS_BITLEN * (49%Comms.ALLY_STATUS_PERSLOT)
    };

    // init

    static void init(RobotController r) {
        rc = r;
        rng = new Random(rc.getRoundNum() * 23981 + rc.getID() * 10289);

        MAP_HEIGHT = rc.getMapHeight();
        MAP_WIDTH = rc.getMapWidth();
        MAP_AREA = MAP_HEIGHT * MAP_WIDTH;

        for (MapLocation m: rc.getAllySpawnLocations()) {
            spawnIntLocations.add(locationToInt(m));
        }
    }


// METHODS
//

    // navigation

    public static int locationToSector (MapLocation location) {
        return ((location.x >> 2) << 4) | (location.y >> 2);
    }

    public static MapLocation sectorToLocation (int sector) {
        return new MapLocation((sector >> 4) << 2 + 1, (sector & 0x7) << 2 + 1);
    }
 
    public static int locationToInt(MapLocation location) {
        return (location.x * MAP_HEIGHT + location.y);
    }

    public static MapLocation intToLocation (int location) {
        return new MapLocation(location / MAP_HEIGHT, location % MAP_HEIGHT);
    }

    public static MapLocation locationDelta(MapLocation from, MapLocation to) {
        return new MapLocation(to.x - from.x, to.y - from.y);
    }

    public static Direction dxDyToDirection(int dx, int dy) {
        return SHIFTED_DX_DY_TO_DIRECTION[dx+1][dy+1];
    }

    public static int dxDyToInt(int dx, int dy) {
        return SHIFTED_DX_DY_TO_INT[dx+1][dy+1];
    }
    public static int dxDyToInt(Direction dir) {
        return SHIFTED_DX_DY_TO_INT[dir.dx+1][dir.dy+1];
    }

    public static boolean isInMap(MapLocation loc) {
        return (loc.x >= 0 && loc.x < MAP_WIDTH && loc.y >= 0 && loc.y < MAP_HEIGHT);
    }

    // comms

    public static int encodeRound() throws GameActionException {
        return (rc.getRoundNum()%80)/5;
    }

    public static FlagInfo getCarryingFlag() throws GameActionException {
        if (rc.hasFlag()) return rc.senseNearbyFlags(0, rc.getTeam())[0];
        return null;
    }
    
    // misc
    public static boolean isBitOne(int value, int LSBpos) {
        return (((value >> LSBpos) & 1) == 1);
    }
}
