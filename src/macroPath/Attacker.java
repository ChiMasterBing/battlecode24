package macroPath;

import battlecode.common.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class Attacker extends Robot{
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
    static final Random rng = new Random(6147);
    MapLocation currentTarget = null;

    RobotInfo[] enemyRobots;
    RobotInfo[] friendlyRobots;

    RobotInfo[] closeFriendlyRobots;
    RobotInfo[] closeEnemyRobots;
    MapLocation closestSpawn;
    int onOpponentSide = 0;
    int prevNoBomb = 0;
    public Attacker(RobotController rc){
        super(rc);
    }
    public MapLocation findTooClose() throws GameActionException {

        for(RobotInfo i : friendlyRobots){
            if(rc.getLocation().distanceSquaredTo(i.getLocation())<5){
                return i.getLocation();
            }
        }
        return null;
    }
    public int onOpponentSide(MapLocation closetSpawn, MapLocation currentLocation) {//if closer to enemy, hten negative
        if (currentTarget!=null) {
            return currentLocation.distanceSquaredTo(currentTarget) - rc.getLocation().distanceSquaredTo(closetSpawn);
        }
        return 10;
    }
    public void movement() throws GameActionException {


        //flag logic, if i have flag

        if (rc.hasFlag()){
            bugNav.move(closestSpawn);
            return;

        }

        //flag logic, if i sense flag
        int dist = Integer.MAX_VALUE;
        MapLocation targ = null;
        for(FlagInfo i : rc.senseNearbyFlags(-1)){
            if(rc.getRoundNum()<200){
                continue;
            }
            if(i.getTeam()==rc.getTeam()&&i.isPickedUp()){
                int cdist = i.getLocation().distanceSquaredTo(rc.getLocation())-100000000;
                if(cdist<dist){
                    dist = cdist;
                    targ = i.getLocation();
                }
            }
            if(i.getTeam()==rc.getTeam().opponent()&&!i.isPickedUp()){
                int cdist = i.getLocation().distanceSquaredTo(rc.getLocation());

                if(cdist<dist){

                    if(cdist<9) {
                        if (rc.canBuild(TrapType.STUN, rc.getLocation())) {
                            rc.build(TrapType.STUN, rc.getLocation());
                        }

                        if (rc.canBuild(TrapType.EXPLOSIVE, rc.getLocation())) {
                            rc.build(TrapType.EXPLOSIVE, rc.getLocation());
                        }
                    }
                    dist = cdist;
                    targ = i.getLocation();
                }
            }
            if(i.getTeam()==rc.getTeam().opponent()&&i.isPickedUp()){
                targ = null;
                break;
            }


        }

        if(targ!=null){
            rc.setIndicatorString("opponent has flag. bad");
            bugNav.move(targ);
            return;
        }

        //if too close to ally (explodable), then dont
        MapLocation tooClose = findTooClose();
        if(findTooClose()!=null){
            Direction dir = rc.getLocation().directionTo(tooClose).opposite();
            if(rc.canMove(dir)){
                rc.setIndicatorString("go away ");
                rc.move(dir);
            }
        }

        // move towards crumbss.
        MapLocation[] crummy = rc.senseNearbyCrumbs(-1);
        if(crummy.length>0&&rc.getRoundNum()<170){
            bugNav.move(crummy[0]);
        }

        //find leader to attack
        //all of the above sat:
        //   - round number>170 or no crummies in sight
        //   - no flag sensed
        MapLocation leaderloc = findLeader();
        if(leaderloc!=null){
            if(rc.getLocation().distanceSquaredTo(leaderloc)>20){
                bugNav.move(leaderloc);
            }
            Direction dir = rc.getLocation().directionTo(leaderloc);
            if(rc.getLocation().distanceSquaredTo(leaderloc)<5){
                dir = dir.opposite();
                rc.setIndicatorString(dir.toString());
            }
            if(rc.canMove(dir)){
                rc.setIndicatorString("yessir leader");
                rc.move(dir);
            }
        }else{
            leaderloc = currentTarget;

            for(RobotInfo i: enemyRobots){
                if(onOpponentSide(closestSpawn,i.getLocation())>onOpponentSide){
                    leaderloc = i.getLocation();
                    break;
                }
            }
            if(leaderloc==null){
                return;
            }

            bugNav.move(leaderloc);


        }
    }
    public void turn() throws GameActionException{
        rc.setIndicatorString("i dont have leader rn");

        //cacllulates who's side you're on
        MapLocation[] spawnLocs = rc.getAllySpawnLocations();
        int dist = Integer.MAX_VALUE;
        for(MapLocation spawn : spawnLocs){
            int cdist = rc.getLocation().distanceSquaredTo(spawn);
            if(cdist<dist){
                dist = cdist;
                closestSpawn = spawn;
            }
        }
        System.out.println(closestSpawn);
        onOpponentSide = onOpponentSide(closestSpawn, rc.getLocation());

        closeEnemyRobots = rc.senseNearbyRobots(4, rc.getTeam().opponent());
        closeFriendlyRobots = rc.senseNearbyRobots(4, rc.getTeam());


        enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        friendlyRobots = rc.senseNearbyRobots(-1, rc.getTeam());
        if (rc.canPickupFlag(rc.getLocation())){
            rc.pickupFlag(rc.getLocation());

            rc.setIndicatorString("Holding a flag!");
        }
        prevNoBomb--;
        if (rc.getRoundNum() > 250) {
            if(onOpponentSide<0) {

                if (closeEnemyRobots.length - closeFriendlyRobots.length > 6 - rc.getCrumbs() / 200) {
                    if (rc.canBuild(TrapType.EXPLOSIVE, rc.getLocation())) {
                        rc.build(TrapType.EXPLOSIVE, rc.getLocation());
                    } else {
                        prevNoBomb = 10;
                    }

//                System.out.println(rc.getLocation(), );
                } else if (prevNoBomb > 0 && rc.canBuild(TrapType.EXPLOSIVE, rc.getLocation())) {
                    prevNoBomb = 0;
                    rc.build(TrapType.EXPLOSIVE, rc.getLocation());
                }
            }
        } else {
            if (enemyRobots.length > 1 && rc.canBuild(TrapType.EXPLOSIVE, rc.getLocation())) {
                rc.build(TrapType.EXPLOSIVE, rc.getLocation());
            }
        }
        MapLocation[] arr = rc.senseBroadcastFlagLocations();
        if (arr.length>0&&arr[0] != null) {
            currentTarget = arr[0];
        }
//        MapLocation attackLoc = findBestAttackLocation();
//        if(attackLoc!=null&&rc.canAttack(attackLoc)){
//            rc.attack(attackLoc);
////            System.out.println("YAYYYY");
//        }

        movement();

        // If we are holding an enemy flag, singularly focus on moving towards
        // an ally spawn zone to capture it! We use the check roundNum >= SETUP_ROUNDS
        // to make sure setup phase has ended.



        MapLocation attackLoc = findBestAttackLocation();
        if(attackLoc!=null&&rc.canAttack(attackLoc)){
            rc.attack(attackLoc);
//            System.out.println("YAYYYY");
        }

        // Rarely attempt placing traps behind the robot.
//        MapLocation prevLoc = rc.getLocation().subtract(dir);
//        if (rc.canBuild(TrapType.EXPLOSIVE, prevLoc) && rng.nextInt() % 37 == 1)
//            rc.build(TrapType.EXPLOSIVE, prevLoc);
        // We can also move our code into different methods or classes to better organize it!
        updateEnemyRobots();

    }
    public void updateEnemyRobots() throws GameActionException{
        // Sensing methods can be passed in a radius of -1 to automatically
        // use the largest possible value.

        if (enemyRobots.length != 0){
//            rc.setIndicatorString("There are nearby enemy robots! Scary!");
            // Save an array of locations with enemy robots in them for future use.
            MapLocation[] enemyLocations = new MapLocation[enemyRobots.length];
            for (int i = 0; i < enemyRobots.length; i++){
                enemyLocations[i] = enemyRobots[i].getLocation();
            }
            // Let the rest of our team know how many enemy robots we see!
            if (rc.canWriteSharedArray(0, enemyRobots.length)){
                rc.writeSharedArray(0, enemyRobots.length);
                int numEnemies = rc.readSharedArray(0);
            }

        }
    }
    public MapLocation findLeader() throws GameActionException {
        int maxH = rc.getHealth()*1000000+rc.getID();
        MapLocation ret = null;
        if(currentTarget!=null) {
            if (onOpponentSide<-2&&rc.getRoundNum()%8<2) {
                rc.setIndicatorString("agro to " + String.valueOf(currentTarget));
                return currentTarget;
            }
        }
        for(RobotInfo i: friendlyRobots){
//            if(i.hasFlag){
//                rc.setIndicatorString("i have leader rn");
//
//                maxH+=1000000000;
//                ret = i.getLocation();
//            }
            if(maxH<i.getHealth()){
                maxH = i.getHealth()*1000000+rc.getID();
                ret = i.getLocation();
            }
        }
        return ret;
    }
    public MapLocation findBestAttackLocation() throws GameActionException{
        int totalHeal = 0;
        for(RobotInfo i: closeEnemyRobots){
            if(i.healLevel>i.attackLevel){
                totalHeal+=80;
            }
        }
        double rounds = 0;
        MapLocation ret = null;
        for(RobotInfo i : closeEnemyRobots){
            MapLocation enemyLoc = i.getLocation();
            int totalAttack = 0;
            for(RobotInfo j: closeFriendlyRobots){
                if(j.attackLevel>j.healLevel&&j.getLocation().isWithinDistanceSquared(enemyLoc, 4)){
                    totalAttack+=180;
                }
            }
            double rnds = 0;
            int roundstokill = 100000;
            if(i.hasFlag){
                rnds+=100000000;
            }
            if(totalAttack-totalHeal+1!=0) {
                roundstokill = i.getHealth() / (totalAttack - totalHeal + 1);
            }
            rnds +=1/((double)roundstokill+1.0);
            rnds*=1000;
            if(i.attackLevel>=i.healLevel){
                rnds+=(i.attackLevel+1);
            }else{
                rnds+=i.healLevel;
            }
            rnds*=1000;
            rnds+=20-rc.getLocation().distanceSquaredTo(enemyLoc);
            rnds*=10;
            rnds+=rc.getID()%10;

            if(rnds>rounds){
                rounds = rnds;
                ret = i.getLocation();
            }

        }
        return ret;
    }
}
