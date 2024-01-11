package macroPath;

import battlecode.common.*;

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
    public void movement() throws GameActionException {
        MapLocation[] spawnLocs = rc.getAllySpawnLocations();
        int dist = Integer.MAX_VALUE;
        MapLocation closestSpawn = null;
        for(MapLocation spawn : spawnLocs){
            int cdist = rc.getLocation().distanceSquaredTo(spawn);
            if(cdist<dist){
                dist = cdist;
                closestSpawn = spawn;
            }
        }

        if (rc.hasFlag()){
            bugNav.move(closestSpawn);
            return;

        }


        dist = Integer.MAX_VALUE;
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
        MapLocation tooClose = findTooClose();
        if(findTooClose()!=null){
            Direction dir = rc.getLocation().directionTo(tooClose).opposite();
            if(rc.canMove(dir)){
                rc.setIndicatorString("go away ");
                rc.move(dir);
            }
        }
        // Move and attack randomly if no objective.
        MapLocation[] crummy = rc.senseNearbyCrumbs(-1);
        if(crummy.length>0&&rc.getRoundNum()<170){
            bugNav.move(crummy[0]);
        }
        MapLocation leaderloc = findLeader(closestSpawn);
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
            if(currentTarget==null){
                return;
            }

            bugNav.move(currentTarget);


        }
    }
    public void turn() throws GameActionException{
        rc.setIndicatorString("i dont have leader rn");

        enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        friendlyRobots = rc.senseNearbyRobots(-1, rc.getTeam());
        if (rc.canPickupFlag(rc.getLocation())){
            rc.pickupFlag(rc.getLocation());

            rc.setIndicatorString("Holding a flag!");
        }


        if(rc.getRoundNum()>250) {
            if (enemyRobots.length- friendlyRobots.length > 4 && rc.canBuild(TrapType.EXPLOSIVE, rc.getLocation())) {
                rc.build(TrapType.EXPLOSIVE, rc.getLocation());
            }
        }else{
            if (enemyRobots.length > 1 && rc.canBuild(TrapType.EXPLOSIVE, rc.getLocation())) {
                rc.build(TrapType.EXPLOSIVE, rc.getLocation());
            }
        }
        MapLocation[] arr = rc.senseBroadcastFlagLocations();
        if (arr.length>0&&arr[0] != null) {
            currentTarget = arr[0];
        }
        MapLocation attackLoc = findBestAttackLocation();
        if(attackLoc!=null&&rc.canAttack(attackLoc)){
            rc.attack(attackLoc);
//            System.out.println("YAYYYY");
        }

        movement();

        // If we are holding an enemy flag, singularly focus on moving towards
        // an ally spawn zone to capture it! We use the check roundNum >= SETUP_ROUNDS
        // to make sure setup phase has ended.



         attackLoc = findBestAttackLocation();
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
    public MapLocation findLeader(MapLocation closestSpawn) throws GameActionException {
        int maxH = rc.getHealth()*1000000+rc.getID();
        MapLocation ret = null;
        if(currentTarget!=null) {
            if (rc.getLocation().distanceSquaredTo(currentTarget) > rc.getLocation().distanceSquaredTo(closestSpawn)&&rc.getRoundNum()%8<2) {
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
    public static MapLocation findBestAttackLocation() throws GameActionException{
        RobotInfo[] badBots = rc.senseNearbyRobots(4, rc.getTeam().opponent());
        RobotInfo[] goodBots = rc.senseNearbyRobots(4, rc.getTeam());
        int totalHeal = 0;
        for(RobotInfo i: badBots){
            if(i.healLevel>i.attackLevel){
                totalHeal+=80;
            }
        }
        double rounds = 0;
        MapLocation ret = null;
        for(RobotInfo i : badBots){
            MapLocation enemyLoc = i.getLocation();
            int totalAttack = 0;
            for(RobotInfo j: goodBots){
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
