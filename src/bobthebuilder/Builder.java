package bobthebuilder;
import battlecode.common.*;
import bling3.fast.FastLocSet;
import java.util.Random;
import java.util.Arrays;
import java.util.Map;


public class Builder extends Robot{
    MapLocation[] broadcastLocations = {};
    MapLocation[] spawnLocs;
    MapInfo[] nearbyInfo;
    MapLocation myLoc, closestSpawn, currentTarget;
    FastLocSet spawnSet = new FastLocSet();
    Random random = new Random();
    int onOpponentSide;
    RobotInfo[] enemyRobots, friendlyRobots, closeEnemyRobots;
    Boolean closeToWater = false;
    public Builder(RobotController rc) throws GameActionException {
        super(rc);
        spawnLocs = rc.getAllySpawnLocations();
    }
    
    public void setGlobals() throws GameActionException{
        nearbyInfo = rc.senseNearbyMapInfos(-1);

        myLoc = rc.getLocation();
        enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        closeEnemyRobots = rc.senseNearbyRobots(4, rc.getTeam().opponent());
        friendlyRobots = rc.senseNearbyRobots(-1, rc.getTeam());
        int dist = Integer.MAX_VALUE;
        for(MapLocation spawn : spawnLocs){
            int cdist = myLoc.distanceSquaredTo(spawn);
            spawnSet.add(spawn);
            if(cdist<dist){
                dist = cdist;
                closestSpawn = spawn;
            }
        }
        onOpponentSide = onOpponentSide(closestSpawn, myLoc);
    }

    public int onOpponentSide(MapLocation closetSpawn, MapLocation currentLocation) {//if closer to enemy, then negative
        return currentLocation.distanceSquaredTo(mirrorFlags[0]) - currentLocation.distanceSquaredTo(super.myFlags[0]) + currentLocation.distanceSquaredTo(mirrorFlags[1]) - currentLocation.distanceSquaredTo(super.myFlags[1]) + currentLocation.distanceSquaredTo(mirrorFlags[2]) - currentLocation.distanceSquaredTo(super.myFlags[2]);
    }
    public void updateCurrentTarget() throws GameActionException {
        if (currentTarget == null) {
            currentTarget = mirrorFlags[0]; //by symmetry
        }
        
        // if (!leader && cPtr > 0) {
        //     int minDist = 10000;
        //     for (int i=0; i<cPtr; i++) {
        //         int d = combatFronts[i].distanceSquaredTo(myLoc);
        //         if (d < minDist) {
        //             if (d < 25 && enemyRobots.length == 0) {
        //                 combatFronts[i] = new MapLocation(10000, 10000);
        //                 continue;
        //             }
        //             minDist = d;
        //             currentTarget = combatFronts[i];
        //         }
        //     }
        //     return;
        // }
        
        if (broadcastLocations.length>0 && broadcastLocations[0] != null) {
            currentTarget = broadcastLocations[0];
        }

        MapLocation[] arr = rc.senseBroadcastFlagLocations();
        if (arr.length>0&&arr[0] != null) {
            broadcastLocations = arr;
        }
    }
    public void buildBest(int explosiveTraps, int stunTraps, int friendlyNearBomb, int enemyNearBomb, MapLocation trapLocation) throws GameActionException {
        if(stunTraps+explosiveTraps>4+rc.getCrumbs()/1500){
            return;
        }

        if(explosiveTraps>3*stunTraps&&(friendlyNearBomb>enemyNearBomb||rc.getCrumbs()<125)){
            if(rc.canBuild(TrapType.STUN, trapLocation)) {
                rc.build(TrapType.STUN, trapLocation);
            }
        }else{
            if(rc.canBuild(TrapType.EXPLOSIVE, trapLocation)){
                rc.build(TrapType.EXPLOSIVE, trapLocation);
            }
        }
    }
    public MapLocation closestAttacker(){
        MapLocation closest = null; int minEnemyDist = 10000;
        for (RobotInfo ri:enemyRobots) {
            if (ri.location.distanceSquaredTo(myLoc) < minEnemyDist) {
                minEnemyDist = ri.location.distanceSquaredTo(myLoc);
                closest = ri.location;
            }
        }
        return closest;
    }
    public void attemptBuildTraps() throws GameActionException {
        if(enemyRobots.length <= 1 && !(rc.getCrumbs() > 4000 && roundNumber > 250)) return;

        if (enemyRobots.length == 0) {
            //well we have a lot of crums so why not
            for (Direction d:Direction.allDirections()) {
                if (rc.getCrumbs() < 3000) return;
                if (rc.canBuild(TrapType.EXPLOSIVE, myLoc.add(d))) {
                    rc.build(TrapType.EXPLOSIVE, myLoc.add(d));
                }
            }
            return;
        }

        int explosiveTraps = 0;
        int stunTraps = 0;
        MapLocation closestAttacker = closestAttacker();
        MapLocation close = myLoc.add(myLoc.directionTo(closestAttacker));

        MapInfo[] nearbyInfo = rc.senseNearbyMapInfos(close, 13);
        for (MapInfo mi:nearbyInfo) {
            if (mi.getTrapType() == TrapType.EXPLOSIVE) {
                explosiveTraps++;
            }else if(mi.getTrapType()==TrapType.STUN){
                stunTraps++;
            }
        }

        int nearBomb = 0;
        for(RobotInfo i: friendlyRobots){
            if(close.distanceSquaredTo(i.getLocation())<=20){
                nearBomb++;
            }
        }
        int enemyNearBomb = 0;
        for(RobotInfo i: enemyRobots){
            if(close.distanceSquaredTo(i.getLocation())<=13){
                enemyNearBomb++;
            }
        }
        buildBest(explosiveTraps, stunTraps, nearBomb, enemyNearBomb, close);
    }
    public Boolean flagMovementLogic() throws GameActionException {
        int dist = Integer.MAX_VALUE;
        MapLocation targ = null;

        for(FlagInfo i : rc.senseNearbyFlags(-1)){
            if(i.getTeam()==rc.getTeam()){ //if opp has our flag
                if(i.isPickedUp()){
                    int cdist = i.getLocation().distanceSquaredTo(myLoc)-100000000;
                    if(cdist<dist){
                        dist = cdist;
                        targ = i.getLocation();
                    }
                }
            }else {
                if(!i.isPickedUp()){ //if their flag aint picked up
                    if(enemyRobots.length < friendlyRobots.length-1){//check if this is hleful
                        int cdist = i.getLocation().distanceSquaredTo(myLoc);
                        if(cdist<dist){
                            dist = cdist;
                            targ = i.getLocation();
                        }
                    }
                }else{ //if we picked up their flag
                    if (i.getLocation().distanceSquaredTo(myLoc) > 9) {
                        targ = i.getLocation();
                        break;
                    } else {
                        if(rc.getRoundNum()%2==0) {
                            targ = closestSpawn;
                            break;
                        }
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
    public void dodgeEnemies(){
        if (!rc.isMovementReady()) return;
        MapLocation closest = closestAttacker();
        if (closest != null) {
            MapLocation opposite = myLoc.add(myLoc.directionTo(closest).opposite());
//            if(onOpponentSide>0&&friendlyRobots.length>enemyRobots.length&&enemyRobots.length*150<rc.getHealth()){
//                bugNav.move(closest);
//            }
            if(myLoc.distanceSquaredTo(closest)<16){
                rc.setIndicatorString("running away bc too close");
                bugNav.move(opposite);
            }else{
                rc.setIndicatorString("moving towards enemy at "+closest);
                bugNav.move(closest);
            }

        }
    }
    public Boolean fillMovementLogic() throws GameActionException {
        for(MapInfo i : nearbyInfo){
            if(i.isWater()){
                rc.setIndicatorLine(myLoc, i.getMapLocation(), 0, 0, 0);
                closeToWater = true;
                bugNav.move(i.getMapLocation());
                return true;
            }
        }
        return false;
    }
    public Boolean crumbMovementLogic() throws GameActionException {
        MapLocation[] crummy = rc.senseNearbyCrumbs(-1);
        if (crummy.length > 0 && rc.getRoundNum() < 250) {
            if(rc.senseMapInfo(crummy[0]).isWater()){
                if(rc.canFill(crummy[0])){
                    rc.fill(crummy[0]);
                }
            }
            bugNav.move(crummy[0]);
            return true;
        }
        else if(rc.getRoundNum()<160-Math.max(rc.getMapHeight(), rc.getMapWidth())/2){
            MapLocation choice;
            choice = myLoc.add(Direction.allDirections()[random.nextInt(8)]);
            bugNav.move(choice);
            return true;
        }
        return false;
    }
    public Boolean avoidOtherBuilders() {//always returns true
        MapLocation[] buddies = Comms.getBuilderLocations();
        //System.out.println(Arrays.toString(buddies));
        if (buddies[0] != null) {
            rc.setIndicatorLine(myLoc, buddies[0], 255, 0, 0);
        }
        if (buddies[1] != null) {
            rc.setIndicatorLine(myLoc, buddies[1], 255, 0, 0);
        }
        if (buddies[2] != null) {
            rc.setIndicatorLine(myLoc, buddies[2], 255, 0, 0);
        }

        for(RobotInfo i: friendlyRobots){
            if(i.getBuildLevel()==6&&i.getLocation().distanceSquaredTo(currentTarget)>myLoc.distanceSquaredTo(currentTarget)){
                rc.setIndicatorString("too close to freindly bot");
                bugNav.move(myLoc.add(myLoc.directionTo(i.getLocation()).opposite()));
            }
        }
        return false;
    }
    public void followFriendly(){
        MapLocation closestFriend =null;
        int mindist = 100000;
        for(RobotInfo i: friendlyRobots){
            int cval = i.getLocation().distanceSquaredTo(myLoc);
            if(i.getBuildLevel()<4&&cval<mindist){
                mindist = cval;
                closestFriend = i.getLocation();
            }
        }
        if(mindist>12) {
            bugNav.move(closestFriend);
        }
    }
    public void movement() throws GameActionException {
//        if(rc.getRoundNum()>400){
//            rc.resign();
//        }
        if(rc.getRoundNum()>200){
            dodgeEnemies();
            // if(flagMovementLogic()) return;
            if(friendlyRobots.length>2) {
                followFriendly();
            }
//            else{
//                bugNav.move(closestSpawn);
//            }
//            if(friendlyRobots.length<2){
//                if(onOpponentSide<0){
//                    rc.setIndicatorString("im lonely, and going home");
//                    bugNav.move(closestSpawn);
//                }
//            }
        }
        closeToWater = false;
        if(crumbMovementLogic())
            return;

        if(rc.getLevel(SkillType.BUILD)<6&&rc.getRoundNum()<160-Math.max(rc.getMapHeight(), rc.getMapWidth())/2) {
            if (fillMovementLogic()) return;
        }

        if(avoidOtherBuilders())
            return;
        if(currentTarget!=null) {
            if(rc.getMovementCooldownTurns()==0) {
                rc.setIndicatorString("attacking towards " + currentTarget);
            }
            bugNav.move(currentTarget);
        }
    }
    public MapLocation findBestAttackLocation() {
        int minHP = 1000000000;
        MapLocation ret = null;
        for(RobotInfo i : closeEnemyRobots){
            MapLocation enemyLoc = i.getLocation();
            int cval = Math.max(i.getHealth(), 150);
            if (cval< minHP) {
                minHP = cval;
                ret = enemyLoc;
            }
        }
        return ret;
    }
    public void tryAttack() throws GameActionException {
        if (!rc.isActionReady()) return;
        MapLocation attackLoc = findBestAttackLocation();
        if(attackLoc!=null&&rc.canAttack(attackLoc)){
            rc.setIndicatorString("I attacked " + attackLoc);
            rc.attack(attackLoc);
        }
    }
    public void selfPreservation() throws GameActionException {
        if(enemyRobots.length<2) {
            tryAttack();
        }
        if((rc.getHealth()<=750||friendlyRobots.length<2)&&rc.canHeal(myLoc)){
            rc.heal(myLoc);
        }
    }
    public void tryFill() throws GameActionException {
        for(Direction d: Direction.allDirections()){
            MapLocation tmp = myLoc.add(d);
            if(rc.canFill(tmp)){
                rc.fill(tmp);
            }
        }
    }
    public void funnyDig() throws GameActionException {
        for(Direction d: Direction.allDirections()){
            MapLocation tmp = myLoc.add(d);
            if(rc.canDig(tmp)){
                rc.dig(tmp);
            }
        }
    }
    public void pathFind() throws GameActionException {
        if(enemyRobots.length<friendlyRobots.length-1||rc.getMovementCooldownTurns()==0||enemyRobots.length==0){
            tryFill();
        }
    }
    public void increaseLevel() throws GameActionException {
        if(rc.getRoundNum()<160-Math.max(rc.getMapHeight(), rc.getMapWidth())/2) {
            tryFill();
        }
        if(!closeToWater){
            funnyDig();
        }

    }


    @Override
    public void turn() throws GameActionException {
        rc.setIndicatorString("");
        setGlobals();
        updateCurrentTarget();
//        attemptBuildTraps();
        movement();
        setGlobals();
        attemptBuildTraps();
        selfPreservation();
        pathFind();
        if(rc.getLevel(SkillType.BUILD)<6) {
            increaseLevel();
        }
    }
}