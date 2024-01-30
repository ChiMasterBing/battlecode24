package waxingmoon;

import java.util.Map;
import java.util.Random;

// battlecode package
import battlecode.common.*;

// custom package
import waxingmoon.fast.*;

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
        (1 << 60) - 1, (1 << 61) - 1, (1 << 62) - 1, (1 << 63) - 1, ~0, 
    };

    final static int[] ALLY_IDX_TO_STATUS_SLOT = {49, 49, 49, 49, 49, 50, 50, 50, 50, 50, 51, 51, 51, 51, 51, 52, 52, 52, 52, 52, 53, 53, 53, 53, 53, 54, 54, 54, 54, 54, 55, 55, 55, 55, 55, 56, 56, 56, 56, 56, 57, 57, 57, 57, 57, 58, 58, 58, 58, 58};

    final static int[] ALLY_IDX_TO_STATUS_BITSHIFT = {0, 3, 6, 9, 12, 0, 3, 6, 9, 12, 0, 3, 6, 9, 12, 0, 3, 6, 9, 12, 0, 3, 6, 9, 12, 0, 3, 6, 9, 12, 0, 3, 6, 9, 12, 0, 3, 6, 9, 12, 0, 3, 6, 9, 12, 0, 3, 6, 9, 12};
    // init

    final static Direction[][] ENEMY_MOVE_ATTACK =
    {{Direction.SOUTHEAST}, {Direction.SOUTH}, {Direction.SOUTHWEST},
    {Direction.NORTHEAST}, {Direction.NORTH}, {Direction.NORTHWEST},
    {Direction.EAST, Direction.SOUTHEAST, Direction.SOUTH}, {Direction.WEST, Direction.SOUTHWEST, Direction.SOUTH},
    {Direction.EAST, Direction.NORTHEAST, Direction.NORTH}, {Direction.WEST, Direction.NORTHWEST, Direction.NORTH},
    {Direction.EAST}, {Direction.WEST}
    };
    final static int EMA_SE = 0;
    final static int EMA_S = 1;
    final static int EMA_SW = 2;
    final static int EMA_NE = 3;
    final static int EMA_N = 4;
    final static int EMA_NW = 5;
    final static int EMA_E_SE_S = 6;
    final static int EMA_W_SW_S = 7;
    final static int EMA_E_NE_N = 8;
    final static int EMA_W_NW_N = 9;
    final static int EMA_E = 10;
    final static int EMA_W = 11;

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
    public static Direction[] enemyMoveAttacks(MapLocation delta) {
        switch (delta.y) {
            case 3:
                switch (delta.x) {
                    case -1:
                        return ENEMY_MOVE_ATTACK[EMA_SE];
                    case 0:
                        return ENEMY_MOVE_ATTACK[EMA_S];
                    case 1:
                        return ENEMY_MOVE_ATTACK[EMA_SW];
                    default: 
                        return null;
                }
            case -3:
                switch (delta.x) {
                    case -1:
                        return ENEMY_MOVE_ATTACK[EMA_NE];
                    case 0:
                        return ENEMY_MOVE_ATTACK[EMA_N];
                    case 1:
                        return ENEMY_MOVE_ATTACK[EMA_NW];
                    default: 
                        return null;
                }
            case 2:
                switch (delta.x) {
                    case -2:
                        return ENEMY_MOVE_ATTACK[EMA_SE];
                    case -1:
                        return ENEMY_MOVE_ATTACK[EMA_E_SE_S];
                    case 1:
                        return ENEMY_MOVE_ATTACK[EMA_W_SW_S];
                    case 2:
                        return ENEMY_MOVE_ATTACK[EMA_SW];
                    default:
                        return null;
                }
            case -2:
                switch (delta.x) {
                    case -2:
                        return ENEMY_MOVE_ATTACK[EMA_NE];
                    case -1:
                        return ENEMY_MOVE_ATTACK[EMA_E_NE_N];
                    case 1:
                        return ENEMY_MOVE_ATTACK[EMA_W_NW_N];
                    case 2:
                        return ENEMY_MOVE_ATTACK[EMA_NW];
                    default:
                        return null;
                }
            case 1:
                switch (delta.x) {
                    case -3:
                        return ENEMY_MOVE_ATTACK[EMA_SE];
                    case -2:
                        return ENEMY_MOVE_ATTACK[EMA_E_SE_S];
                    case 2:
                        return ENEMY_MOVE_ATTACK[EMA_W_SW_S];
                    case 3:
                        return ENEMY_MOVE_ATTACK[EMA_SW];
                    default:
                        return null;
                }
            case -1:
                switch (delta.x) {
                    case -3:
                        return ENEMY_MOVE_ATTACK[EMA_NE];
                    case -2:
                        return ENEMY_MOVE_ATTACK[EMA_E_NE_N];
                    case 2:
                        return ENEMY_MOVE_ATTACK[EMA_W_NW_N];
                    case 3:
                        return ENEMY_MOVE_ATTACK[EMA_NW];
                    default:
                        return null;
                }
            case 0:
                switch (delta.x) {
                    case -3:
                        return ENEMY_MOVE_ATTACK[EMA_E];
                    case 3:
                        return ENEMY_MOVE_ATTACK[EMA_W];
                    default:
                        return null;
                }
            default: 
                return null;
        }
    }
    // navigation

    // public static int locationToSector (MapLocation location) {
    //     return ((location.x >> 2) << 4) | (location.y >> 2);
    // }

    // public static MapLocation sectorToLocation (int sector) {
    //     return new MapLocation((sector >> 4) << 2 + 1, (sector & 0x7) << 2 + 1);
    // }

    public static int locationToSector (MapLocation loc) {
        return (loc.x / 4) | ((loc.y/4) << 4);
    }

    public static MapLocation sectorToLocation (int sector) {
        return new MapLocation(4 * (sector & 15), 4 * ((sector >> 4) & 15));
    }
 
    public static int locationToInt(MapLocation location) {
        return (location.x * MAP_WIDTH + location.y);
    }

    public static MapLocation intToLocation (int location) {
        return new MapLocation(location / MAP_WIDTH, location % MAP_WIDTH);
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