package template0;

import java.util.Random;

// battlecode package
import battlecode.common.*;

// custom package
import template0.java_utils.*;
import template0.Debug.*;

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

    static final int[] MASKS = {
        0b0, 0b1, 0b11, 0b111, 0b1111, 
        0b11111, 0b111111, 0b1111111,
        0b11111111,0b111111111,
        0b1111111111, 0b11111111111,
        0b111111111111, 0b1111111111111,
        0b11111111111111,
        0b111111111111111,
        0b1111111111111111,
    };

    // init

    static void init(RobotController r) {
        rc = r;
        rng = new Random(rc.getRoundNum() * 23981 + rc.getID() * 10289);

        MAP_HEIGHT = rc.getMapHeight();
        MAP_WIDTH = rc.getMapWidth();
        MAP_AREA = MAP_HEIGHT * MAP_WIDTH;

        for (MapLocation m: rc.getAllySpawnLocations()) {
            spawnIntLocations.add(mapLocationToInt(m));
        }
    }


    // methods
    // navigation
    public static int mapLocationToInt(MapLocation location) {
        return (location.x * MAP_WIDTH + location.y);
    }

    public static MapLocation locationDelta(MapLocation from, MapLocation to) {
        return new MapLocation(to.x - from.x, to.y - from.y);
    }

    // comms
    public static int encodeRound() throws GameActionException{
        return (rc.getRoundNum()%80)/5;
    }
    
    
}
