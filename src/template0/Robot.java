package template0;

// battlecode package
import battlecode.common.*;

// custom package
import template0.java_utils.*;
import template0.Debug.*;
import template0.Utils.*;

public class Robot {
    // self data
    static RobotController rc;
    static int roundNumber = 0;

    static int moveOrder = -1;
    static int botType;
    static int status;
    static MapLocation location;

    // map data
    static MapLocation[] spawn_zones;
    static MapLocation[] friendly_flags;
    static MapLocation[] enemy_flags;
    static MapLocation closestSpawn;

    // bot data
    static RobotInfo[] friendly_units;
    static RobotInfo[] enemy_units;
    static RobotInfo[] enemy_units_atk;
    
    static Team team;
    static Team enemy;

    // constants
    final static int ATTACKER = 0;
    final static int BUILDER = 1;

    public Robot(RobotController r) throws GameActionException {
        rc = r;
        team = rc.getTeam();
        enemy = team.opponent();
    }

    public void attempt_spawn() throws GameActionException {
        if (moveOrder == -1) {
            moveOrder = rc.readSharedArray(0);
            rc.writeSharedArray(0, moveOrder+1);
            System.out.println("my id " + String.valueOf(moveOrder));
        }

        MapLocation[] spawn_locations = rc.getAllySpawnLocations();
        // choose first location and attempt to spawn
        MapLocation spawn_attempt = spawn_locations[7];
        if (rc.canSpawn(spawn_attempt)) {
            rc.spawn(spawn_attempt);
            System.out.println("i spone " + String.valueOf(moveOrder));
        }
        
        return;
    }

    public void turn() throws GameActionException {
        roundNumber += 1;
        System.out.println("am " + String.valueOf(moveOrder));
        /*if (id == 0 && turnNum == 1) {
            rc.move(Utils.DIRECTIONS[0]);
            System.out.println("i mov "  + String.valueOf(id));
        } */
        
        return;
    }

    public void communicate() throws GameActionException {
        return;
    }

    public boolean move_off_spawn() throws GameActionException {
        for (Direction dir: Utils.DIRECTIONS) {
            MapLocation target = rc.getLocation().translate(dir.dx, dir.dy);
            if (!Utils.spawnIntLocations.contains(Utils.locationToInt(target))) {
                rc.move(dir);
                return true;
            }
        }
        return false;
    }

    public void get_closest_spawn() throws GameActionException {
        MapLocation[] spawnLocs = rc.getAllySpawnLocations();
        int dist = Integer.MAX_VALUE;
        for(MapLocation spawn : spawnLocs){
            int cdist = rc.getLocation().distanceSquaredTo(spawn);
            if(cdist<dist){
                dist = cdist;
                closestSpawn = spawn;
            }
        }
    }
}
