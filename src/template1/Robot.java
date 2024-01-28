package template1;

// battlecode package
import battlecode.common.*;

// custom package
import template1.java_utils.*;
import template1.Debug.*;
import template1.Utils.*;

public class Robot {
    // self data
    static RobotController rc;
    static int roundNumber = 0;

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

    static int w;

    public Robot(RobotController r) throws GameActionException {
        rc = r;
        team = rc.getTeam();
        enemy = team.opponent();
        w = 0;
        BugNav.setTarget(new MapLocation(Utils.rng.nextInt(Utils.MAP_WIDTH), Utils.rng.nextInt(Utils.MAP_HEIGHT)));
    }

    public void attempt_spawn() throws GameActionException {
        MapLocation[] spawn_locations = rc.getAllySpawnLocations();
        // choose first location and attempt to spawn
        for (MapLocation m : spawn_locations) {
            if (rc.canSpawn(m)) {
                rc.spawn(m);
                break;
            }
        }       
        return;
    }

    public void turn() throws GameActionException {
        roundNumber += 1;
        if (roundNumber == 1) {
            System.out.println(Comms.myMoveOrder);
        }
        int a = Clock.getBytecodeNum();
        //System.out.println("comm start " + String.valueOf(Clock.getBytecodeNum() - a));

        if (BugNav.target == null || rc.getLocation().distanceSquaredTo(BugNav.target) < 9) {
            w += 1;
            BugNav.setTarget(new MapLocation(Utils.rng.nextInt(Utils.MAP_WIDTH), Utils.rng.nextInt(Utils.MAP_HEIGHT)));
        }
        if (roundNumber == 1999) {
            System.out.println(String.valueOf(w));
        }
        
        if (roundNumber > 3) {
            a = Clock.getBytecodeNum();
            Direction toMove = BugNav.navigate();
            //
            
            if (toMove != null) {
                if (rc.canMove(toMove)) {
                    rc.move(toMove);
                    BugNav.makeMove(toMove);
                } else if (rc.canFill(rc.getLocation().add(toMove))) {
                    rc.fill(rc.getLocation().add(toMove));
                }
            }
            // BogNav.move(BugNav.target);
            // if (rc.senseMapInfo(rc.getLocation().add(rc.getLocation().directionTo(BugNav.target))).isWater()) {
            //     rc.fill(rc.getLocation().add(rc.getLocation().directionTo(BugNav.target)));
            // }
            //System.out.println("bugnav " + String.valueOf(Clock.getBytecodeNum() - a));
        }

        a = Clock.getBytecodeNum();
        //System.out.println("comm end " + String.valueOf(Clock.getBytecodeNum() - a));

        //! indicators
        switch(Comms.myStatus) {
            case Comms.STATUS_NEUTRAL:
                rc.setIndicatorString("n " + (BugNav.isBugging?String.valueOf(roundNumber - BugNav.bugNavTracker.originalTurn):".") + " " + (BugNav.isBugging&&BugNav.bugNavTracker.reverse?"r":"."));
                break;
            case Comms.STATUS_STUCK:
                rc.setIndicatorString("s " + (BugNav.isBugging?String.valueOf(roundNumber - BugNav.bugNavTracker.originalTurn):".") + " " + (BugNav.isBugging&&BugNav.bugNavTracker.reverse?"r":"."));
                break;
            default:
                rc.setIndicatorString("u " + (BugNav.isBugging?String.valueOf(roundNumber - BugNav.bugNavTracker.originalTurn):".") + " " + (BugNav.isBugging&&BugNav.bugNavTracker.reverse?"r":"."));
        }
        if (BugNav.target != null) rc.setIndicatorLine(rc.getLocation(), BugNav.target, 0, 100, 0);
        if (BugNav.isBugging) rc.setIndicatorLine(rc.getLocation(), rc.getLocation().add(Utils.DIRS_CENTER[BugNav.bugNavTracker.pivotDir]), 255, 255, 0);
        if (BugNav.isBugging) rc.setIndicatorLine(rc.getLocation(), rc.getLocation().add(Utils.DIRS_CENTER[BugNav.bugNavTracker.nextPivot()]), 255, 0, 0);
        // //* indicators

        //System.out.println("-----------------");
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
