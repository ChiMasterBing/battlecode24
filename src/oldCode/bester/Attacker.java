package bester;

import battlecode.common.*;


public class Attacker extends Robot{

    MapLocation currentTarget = null;

    RobotInfo[] enemyRobots;
    RobotInfo[] friendlyRobots;

    RobotInfo[] closeFriendlyRobots;
    RobotInfo[] closeEnemyRobots;
    MapLocation closestSpawn;
    int onOpponentSide = 0;


    public Attacker(RobotController rc){
        super(rc);
    }
    public Boolean flagMovementLogic() throws GameActionException {
        if (rc.hasFlag()){
            bugNav.move(closestSpawn);
            return true;
        }
        int dist = Integer.MAX_VALUE;
        MapLocation targ = null;

        for(FlagInfo i : rc.senseNearbyFlags(-1)){
            if(i.getTeam()==rc.getTeam()){
                if(i.isPickedUp()){
                    int cdist = i.getLocation().distanceSquaredTo(rc.getLocation())-100000000;
                    if(cdist<dist){
                        dist = cdist;
                        targ = i.getLocation();
                    }
                }
            }else {
                if(!i.isPickedUp()){
                    if(enemyRobots.length<friendlyRobots.length-1){//check if this is hleful
                        int cdist = i.getLocation().distanceSquaredTo(rc.getLocation());
                        if(cdist<dist){
                            dist = cdist;
                            targ = i.getLocation();
                        }
                    }
                }else{
                    if(i.getLocation().distanceSquaredTo(rc.getLocation())>9){
                        targ = i.getLocation();
                        break;
                    }
                }
            }

        }
        if(targ!=null){
            bugNav.move(targ);
            return true;
        }
        return false;

    }
    public Boolean crumbMovementLogic() throws GameActionException {
        MapLocation[] crummy = rc.senseNearbyCrumbs(-1);
        if (crummy.length > 0 && rc.getRoundNum() < 250) {
            bugNav.move(crummy[0]);
            return true;
        }
        return false;
    }
    public Boolean leaderMovementLogic() throws GameActionException {//always returns true
        MapLocation leaderloc = findLeader();

        if(leaderloc!=null){
//            rc.setIndicatorString("leader found at "+leaderloc);
            Direction dir = rc.getLocation().directionTo(leaderloc);
            if(rc.getLocation().distanceSquaredTo(leaderloc)<8){
                dir = dir.opposite();
            }
            if(rc.canMove(dir)){
                rc.move(dir);
                return true;
            }

        }else{
//            rc.setIndicatorString("moving towards"+currentTarget);
            if(currentTarget!=null) {
                bugNav.move(currentTarget);
                return true;
            }
        }
        return false;
    }
    public void movement() throws GameActionException {
        if(rc.getRoundNum()>200){
            if(flagMovementLogic()){
                return;
            }
        }
        if(rc.getRoundNum()<250) {
            if(crumbMovementLogic()) {
                return;
            }
        }
        leaderMovementLogic();
    }
    public void setGlobals() throws GameActionException{

        closeEnemyRobots = rc.senseNearbyRobots(4, rc.getTeam().opponent());
        closeFriendlyRobots = rc.senseNearbyRobots(4, rc.getTeam());
        enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        friendlyRobots = rc.senseNearbyRobots(-1, rc.getTeam());

        MapLocation[] spawnLocs = rc.getAllySpawnLocations();
        int dist = Integer.MAX_VALUE;
        for(MapLocation spawn : spawnLocs){
            int cdist = rc.getLocation().distanceSquaredTo(spawn);
            if(cdist<dist){
                dist = cdist;
                closestSpawn = spawn;
            }
        }

        onOpponentSide = onOpponentSide(closestSpawn, rc.getLocation());


    }
    public void tryHeal() throws GameActionException {
        MapLocation bestheal = null;
        int lowestHealth = Integer.MAX_VALUE;
        for(RobotInfo i: closeFriendlyRobots){
            if(lowestHealth>i.getHealth()){
                bestheal = i.getLocation();
                lowestHealth = i.getHealth();
            }
        }
        if(bestheal!=null&&rc.canHeal(bestheal)){
            rc.heal(bestheal);
        }
    }
    public void checkPickupFlag() throws GameActionException {
        if (rc.getRoundNum()>200&&rc.canPickupFlag(rc.getLocation())&&friendlyRobots.length- enemyRobots.length>3){//check if this change is good
            rc.pickupFlag(rc.getLocation());
        }
    }
    public void checkBuildTraps() throws GameActionException{
        if(rc.getRoundNum()>210) {
            if (enemyRobots.length - friendlyRobots.length > 4-rc.getCrumbs()/1500 && rc.canBuild(TrapType.EXPLOSIVE, rc.getLocation())) {
                rc.build(TrapType.EXPLOSIVE, rc.getLocation());
            }
        }else{
            if (enemyRobots.length > 4-rc.getCrumbs()/500 && rc.canBuild(TrapType.EXPLOSIVE, rc.getLocation())) {
                rc.build(TrapType.EXPLOSIVE, rc.getLocation());
            }
        }
    }

    public void updateCurrentTarget(){
        if(rc.getRoundNum()>200){
            rc.resign();
        }
        MapLocation[] arr = rc.senseBroadcastFlagLocations();
        rc.setIndicatorString("i dont have target");
        if (arr.length>0) {
            rc.setIndicatorString("target at"+currentTarget);
            currentTarget = arr[0];
        }
    }
    public void attackLogic() throws GameActionException {
        MapLocation attackLoc = findBestAttackLocation();
        if(attackLoc!=null&&rc.canAttack(attackLoc)){
            rc.attack(attackLoc);
        }
    }
    public void turn() throws GameActionException{
        setGlobals();
        checkPickupFlag();
        checkBuildTraps();
        updateCurrentTarget();
        attackLogic();

        tryHeal();

        movement();


        setGlobals(); // after we moved, globals are different

        attackLogic();

        tryHeal();
        updateEnemyRobots();

    }
    public void updateEnemyRobots() throws GameActionException{
        // Sensing methods can be passed in a radius of -1 to automatically
        // use the largest possible value.

        if (enemyRobots.length != 0){
            rc.setIndicatorString("There are nearby enemy robots! Scary!");
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
    public MapLocation findLeader() {
        int maxH = rc.getHealth()*1000000+rc.getID();
        MapLocation ret = null;
        for(RobotInfo i: friendlyRobots){
            if(i.hasFlag){
                maxH+=1000000000;
                ret = i.getLocation();
            }
            if(maxH<i.getHealth()){
                maxH = i.getHealth()*1000000+rc.getID();
                ret = i.getLocation();
            }
        }
        if(ret==null){
            if(enemyRobots.length>friendlyRobots.length&&onOpponentSide<0){
                return closestSpawn;
            }
        }
        return ret;
    }
    public int onOpponentSide(MapLocation closetSpawn, MapLocation currentLocation) {//if closer to enemy, then negative
        if (currentTarget!=null) {
            return currentLocation.distanceSquaredTo(currentTarget) - rc.getLocation().distanceSquaredTo(closetSpawn);
        }
        return 10;
    }
    public MapLocation findBestAttackLocation() {
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
