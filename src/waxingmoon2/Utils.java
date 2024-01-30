package waxingmoon2;

import battlecode.common.*;
import waxingmoon2.fast.FastIntSet;

import java.util.Random;

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

    static final int[] BASIC_MASKS = {
        (1 << 0) - 1,
        (1 << 1) - 1,
        (1 << 2) - 1,
        (1 << 3) - 1,
        (1 << 4) - 1,
        (1 << 5) - 1,
        (1 << 6) - 1,
        (1 << 7) - 1,
        (1 << 8) - 1,
        (1 << 9) - 1,
        (1 << 10) - 1,
        (1 << 11) - 1,
        (1 << 12) - 1,
        (1 << 13) - 1,
        (1 << 14) - 1,
        (1 << 15) - 1,
        (1 << 16) - 1
    };

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

    static FastIntSet atkRadiusDeltas;

    // init

    static void init(RobotController r) {
        rc = r;
        rng = new Random(rc.getRoundNum() * 23981 + rc.getID() * 10289);

        MAP_HEIGHT = rc.getMapHeight();
        MAP_WIDTH = rc.getMapWidth();
        MAP_AREA = MAP_HEIGHT * MAP_WIDTH;

        atkRadiusDeltas.add(-2*MAP_WIDTH);
        atkRadiusDeltas.add(-1-MAP_WIDTH);
        atkRadiusDeltas.add(-MAP_WIDTH);
        atkRadiusDeltas.add(1-MAP_WIDTH);
        atkRadiusDeltas.add(-2);
        atkRadiusDeltas.add(-1);
        atkRadiusDeltas.add(1);
        atkRadiusDeltas.add(2);
        atkRadiusDeltas.add(2*MAP_WIDTH);
        atkRadiusDeltas.add(MAP_WIDTH-1);
        atkRadiusDeltas.add(MAP_WIDTH);
        atkRadiusDeltas.add(MAP_WIDTH+1);
        atkRadiusDeltas.add(2*MAP_WIDTH);

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
        return (location.x * MAP_WIDTH + location.y);
    }

    public static MapLocation intToLocation (int location) {
        return new MapLocation(location / MAP_WIDTH, location % MAP_WIDTH);
    }

    public static MapLocation locationDelta(MapLocation from, MapLocation to) {
        return new MapLocation(to.x - from.x, to.y - from.y);
    }

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