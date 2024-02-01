package bitmaptest;

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
        return (location.x * MAP_WIDTH + location.y);
    }

    public static MapLocation intToLocation (int location) {
        return new MapLocation(location / MAP_WIDTH, location % MAP_WIDTH);
    }

    public static MapLocation locationDelta(MapLocation from, MapLocation to) {
        return new MapLocation(to.x - from.x, to.y - from.y);
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