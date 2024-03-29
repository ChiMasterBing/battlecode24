package bester;

import battlecode.common.*;
//import macroPath.Attacker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public strictfp class RobotPlayer {

    /**
     * We will use this variable to count the number of turns this robot has been alive.
     * You can use static variables like this to save any information you want. Keep in mind that even though
     * these variables are static, in Battlecode they aren't actually shared between your robots.
     */
    static int turnCount = 0;
    //static macroPath MP;

    static final Random rng = new Random(6147);
    static macroPath MP;
//    static Attacker penis;

    static boolean mainDuck = false;

    /** Array containing all the possible movement directions. */
    static final Direction[] directions = {
        Direction.NORTH,
        Direction.NORTHEAST,
        Direction.EAST,
        Direction.SOUTHEAST,
        Direction.SOUTH,
        Direction.SOUTHWEST,
        Direction.WEST,
        Direction.NORTHWEST,
    };

    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {
        // You can also use indicators to save debug notes in replays.
        Robot robot;
        robot = new Attacker(rc);
        while(true){
            rc.setIndicatorString("Hello world!");
//            if(rc.getRoundNum()>500){
//                rc.resign();
//            }
            if (!rc.isSpawned()){
                if(rc.getRoundNum() == 1) {
                    if (rc.readSharedArray(0) == 0) {
                        mainDuck = true;
                        rc.writeSharedArray(0, 1);
                    }
                }
                MapLocation[] spawnLocs = rc.getAllySpawnLocations();
                // Pick a random spawn location to attempt spawning in.
                for(MapLocation randomLoc : spawnLocs){
                    if(rc.canSpawn(randomLoc)){
                        rc.spawn(randomLoc);
                    }
                }

            }else {

// //                System.out.println(Attacker.directions);
                robot.turn();


            }
            Clock.yield();
        }
        // Your code should never reach here (unless it's intentional)! Self-destruction imminent...
    }
}
