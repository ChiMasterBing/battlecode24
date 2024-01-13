package evennewerbad;

import battlecode.common.*;
import java.util.*;

public strictfp class RobotPlayer {
    static int turnCount = 0;
    static final Random rng = new Random(6147);

    public static void run(RobotController rc) throws GameActionException {
        Robot robot;
        robot = new Attacker(rc);
        while(true){
            Comms.initBufferPool();
            if (rc.getRoundNum() == 2) {
                robot.populateTeamIDS();
            }
            if (rc.getRoundNum() == 3 && robot.myMoveNumber < 1) { 
                for (int i=0; i<64; i++) {
                    rc.writeSharedArray(i, 0);
                }
            }
            if (!rc.isSpawned()){
                MapLocation[] spawnLocs = rc.getAllySpawnLocations();
                if (rc.getRoundNum() < 200) {
                    if (rc.canSpawn(spawnLocs[robot.myMoveNumber % spawnLocs.length])) {
                        rc.spawn(spawnLocs[robot.myMoveNumber % spawnLocs.length]);
                    }
                }
                else {
                    MapLocation center = null;
                    if (rc.getRoundNum() % 9 == 0) center = robot.myFlags[0]; // && Comms.isBitSet(1, 0)
                    if (rc.getRoundNum() % 9 == 3) center = robot.myFlags[1]; //  && Comms.isBitSet(1, 1)
                    if (rc.getRoundNum() % 9 == 6) center = robot.myFlags[2]; //  && Comms.isBitSet(1, 2)
                    for (Direction d:Direction.allDirections()) {
                        if (rc.getRoundNum() % 3 == 0 && rc.canSpawn(center.add(d))) {
                            rc.spawn(center.add(d));
                            robot.turn();
                            break;
                        }
                    }
                }
            }else {
                robot.turn();   
            }
            Clock.yield();
        }
        // Your code should never reach here (unless it's intentional)! Self-destruction imminent...
    }
}
