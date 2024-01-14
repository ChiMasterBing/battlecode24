package hotlinebling;
import battlecode.common.*;
import hotlinebling.fast.FastLocSet;

import java.util.Map;
import java.util.Random;

public class Builder extends Robot{
    MapLocation closestSpawn, myLoc = null, currentTarget = null;
    MapLocation[] spawnLocs;
    RobotInfo[] enemyRobots, friendlyRobots, closeFriendlyRobots, closeEnemyRobots;
    FastLocSet spawnSet = new FastLocSet();
    int onOpponentSide = 0;
    Random random = new Random();

    public Builder(RobotController rc) throws GameActionException {
        super(rc);
        spawnLocs = rc.getAllySpawnLocations();
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
    public Boolean crumbMovementLogic() throws GameActionException {
        MapLocation[] crummy = rc.senseNearbyCrumbs(-1);
        if (crummy.length > 0 && rc.getRoundNum() < 250) {
            bugNav.move(crummy[0]);
            return true;
        }
        else if(rc.getRoundNum()<160-Math.max(rc.getMapHeight(), rc.getMapWidth())/2){
            //randomly explore, with bias towards center
            MapLocation choice;
            choice = myLoc.add(Direction.allDirections()[random.nextInt(8)]);
            bugNav.move(choice);
            return true;
        }
        return false;
    }
    public Boolean leaderMovementLogic() {//always returns true
        int maxH = 0;
        MapLocation ret = null;
        for(RobotInfo i: friendlyRobots){
//
//            if(maxH<i.getHealth()&&i.getBuildLevel()!=6){
//                maxH = i.getHealth()*1000000+rc.getID();
//                ret = i.getLocation();
//            }
            if(i.getBuildLevel()==6){
                rc.setIndicatorString("too close to freindly bot");
                bugNav.move(myLoc.add(myLoc.directionTo(i.getLocation()).opposite()));
            }
        }
        if(ret!=null){
            bugNav.move(ret);
        }
        if(currentTarget!=null) {
            rc.setIndicatorString("attacking towards "+currentTarget);
            bugNav.move(currentTarget);
            return true;
        }
        return false;
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
    public void attackerMovementLogic() throws GameActionException{
        if (!rc.isMovementReady()) return;
        MapLocation closest = closestAttacker();
        if (closest != null) {
            MapLocation opposite = myLoc.add(myLoc.directionTo(closest).opposite());
            if(myLoc.distanceSquaredTo(closest)<16){
                bugNav.move(opposite);
            }
//            else if(myLoc.distanceSquaredTo(closest)>13){
//                bugNav.move(closest);
//            }
        }

    }
    public void levelUp() throws GameActionException {
        for(Direction d: Direction.allDirections()){
            if(rc.canFill(myLoc.add(d))){
                rc.fill(myLoc.add(d));
            }
        }
        for(Direction d: Direction.allDirections()){
            if(rc.canDig(myLoc.add(d))){
                rc.dig(myLoc.add(d));
            }
        }

    }
    public void movement() throws GameActionException {
        if(rc.getRoundNum()>200){
            if(friendlyRobots.length==0){
                if(onOpponentSide<0){
                    bugNav.move(closestSpawn);
                }
            }
            attackerMovementLogic();
            if(rc.getHealth()<=500&&onOpponentSide<0){
                rc.setIndicatorString("moving towards home bc low on health");

                bugNav.move(closestSpawn);
            }
            if(flagMovementLogic()) return;
        }
        if(rc.getLevel(SkillType.BUILD)<6) {
            levelUp();
        }
        if(crumbMovementLogic()) return;
        leaderMovementLogic();
    }
    public void setGlobals() throws GameActionException{
        myLoc = rc.getLocation();
        closeEnemyRobots = rc.senseNearbyRobots(4, rc.getTeam().opponent());
        closeFriendlyRobots = rc.senseNearbyRobots(4, rc.getTeam());
        enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
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
    public void buildBest(int traps) throws GameActionException {
        if(traps>2){
            return;
        }
        MapLocation closestAttacker = closestAttacker();
        if(closestAttacker==null){
            closestAttacker = myLoc;
        }
        MapLocation close = myLoc.add(myLoc.directionTo(closestAttacker));
        if(friendlyRobots.length>5&&(rc.getCrumbs()<125||friendlyRobots.length>=4||traps>1)){

            if(rc.canBuild(TrapType.STUN, close)) {
                rc.build(TrapType.STUN, close);
                return;
            }
        }else if(traps<1+rc.getCrumbs()/2000){
            if(rc.canBuild(TrapType.EXPLOSIVE, close)) {
                rc.build(TrapType.EXPLOSIVE, close);
                return;
            }
        }

        MapInfo[] arr = rc.senseNearbyMapInfos(4);
        for(MapInfo loc: arr){
            if(friendlyRobots.length>5&&(rc.getCrumbs()<125||friendlyRobots.length>=4||traps>1)){
                if(rc.canBuild(TrapType.STUN, loc.getMapLocation())) {
                    rc.build(TrapType.STUN, loc.getMapLocation());
                    return;
                }
            }else if(traps<1+rc.getRoundNum()/2000){
                if(rc.canBuild(TrapType.EXPLOSIVE, loc.getMapLocation())) {
                    rc.build(TrapType.EXPLOSIVE, loc.getMapLocation());
                    return;
                }
            }
        }

    }
    public void checkBuildTraps() throws GameActionException{
        MapInfo[] nearbyInfo = rc.senseNearbyMapInfos(-1);
        int traps = 0;
        for (MapInfo mi:nearbyInfo) {
            if (mi.getTrapType() != TrapType.NONE) {
                traps++;
            }
        }
        if(rc.getRoundNum()>210) {

            buildBest(traps);

        }else{
            if (rc.getRoundNum()>180&&enemyRobots.length > 2 && rc.canBuild(TrapType.EXPLOSIVE, myLoc)) {
                if (traps < 2) {
                    rc.build(TrapType.EXPLOSIVE, myLoc);
                }
            }
        }
    }
    public void updateCurrentTarget() throws GameActionException {
        if (currentTarget == null) {
            int symm = macroPath.getSymmType();
            if (symm != -1) {
                if (symm == macroPath.H_SYM) {
                    currentTarget = macroPath.getHSym(super.myFlags[0]);
                }
                else if (symm == macroPath.V_SYM) {
                    currentTarget = macroPath.getVSym(super.myFlags[0]);
                }
                else {
                    currentTarget = macroPath.getRSym(super.myFlags[0]);
                }

            }
        }
        MapLocation[] arr = rc.senseBroadcastFlagLocations();
        if (arr.length>0&&arr[0] != null) {
            currentTarget = arr[0];
        }
    }
    public void attackLogic() throws GameActionException {
        if (!rc.isActionReady()) return;
        MapLocation attackLoc = findBestAttackLocation();
        rc.setIndicatorString("best attack loc "  + attackLoc);
        if(attackLoc!=null&&rc.canAttack(attackLoc)){
            rc.setIndicatorString("I attacked " + attackLoc);
            rc.attack(attackLoc);
        }
    }
    public MapLocation findBestAttackLocation() {
        int minHP = 1000000000;
        MapLocation ret = null;
        for(RobotInfo i : closeEnemyRobots){
            MapLocation enemyLoc = i.getLocation();
            int cval = Math.max(i.getHealth(), 150)*100-(i.getHealLevel()+i.getAttackLevel()+i.getBuildLevel()+Math.max(i.getHealLevel(), Math.max(i.getAttackLevel(), i.getBuildLevel())));
            if (cval< minHP) {
                minHP = cval;
                ret = enemyLoc;
            }
            if (i.hasFlag) {
                ret = enemyLoc;
                break;
            }
        }

        return ret;
    }

    public void turn() throws GameActionException{
        setGlobals();
        updateCurrentTarget();

        checkBuildTraps();

        movement();
        setGlobals(); // after we moved, globals are different
        checkBuildTraps();
        if(enemyRobots.length==0&&rc.canHeal(myLoc)){
            rc.heal(myLoc);
        }
        attackLogic();
        tryFill();
    }

    public void tryFill() throws GameActionException {
        if (!rc.isActionReady()) return;
        if (rc.getRoundNum() > 0) {
            if (enemyRobots.length == 0) {
                MapInfo[] water = rc.senseNearbyMapInfos(4);
                for (MapInfo w:water) {
                    if (w.isWater() && rc.canFill(w.getMapLocation()))  {
                        rc.fill(w.getMapLocation());
                        return;
                    }
                }
            }
        }
    }


    public int onOpponentSide(MapLocation closetSpawn, MapLocation currentLocation) {//if closer to enemy, then negative
        if (currentTarget!=null) {
            return currentLocation.distanceSquaredTo(currentTarget) - currentLocation.distanceSquaredTo(closetSpawn);
        }
        return 10;
    }
}