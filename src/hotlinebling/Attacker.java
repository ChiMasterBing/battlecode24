package hotlinebling;
import battlecode.common.*;
import evennewerbad.fast.FastLocSet;

import java.util.Random;

public class Attacker extends Robot{
    MapLocation closestSpawn, myLoc = null, currentTarget = null;
    MapLocation[] spawnLocs;
    RobotInfo[] enemyRobots, friendlyRobots, closeFriendlyRobots, closeEnemyRobots;
    FastLocSet spawnSet = new FastLocSet();
    int onOpponentSide = 0;
    Random random = new Random();

    public Attacker(RobotController rc) throws GameActionException {
        super(rc);
        spawnLocs = rc.getAllySpawnLocations();
    }
    public Boolean flagMovementLogic() throws GameActionException {
        if (rc.hasFlag()){
            for (Direction d:Direction.allDirections()) {
                if (spawnSet.contains(myLoc.add(d)) && rc.canMove(d)) {
                    rc.move(d);
                    if (spawnSet.contains(rc.getLocation())) {
                        Debug.println("I DEPOSITED FLAG WOO!");
                        Comms.depositFlag();
                    }
                    return true;
                }
            }
            bugNav.move(closestSpawn);
            
            if (spawnSet.contains(rc.getLocation())) {
                Debug.println("I DEPOSITED FLAG WOO!");
                Comms.depositFlag();
            }
            return true;
        }
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
        else if(rc.getRoundNum()<200-Math.max(rc.getMapHeight(), rc.getMapWidth())/2){
            //randomly explore, with bias towards center
            MapLocation choice;
            choice = myLoc.add(Direction.allDirections()[random.nextInt(8)]);
            bugNav.move(choice);
            return true;
        }
        return false;
    }
    public Boolean leaderMovementLogic() throws GameActionException {//always returns true
        MapLocation leaderloc = findLeader();
        if(leaderloc!=null){
            Direction dir = myLoc.directionTo(leaderloc);
            if(myLoc.distanceSquaredTo(leaderloc)<8){
                dir = dir.opposite();
            }
            if(rc.canMove(dir)){
                rc.move(dir);
                return true;
            }

        }else{
            if(currentTarget!=null) {
                bugNav.move(currentTarget);
                return true;
            }
        }
        return false;
    }
    public void movement() throws GameActionException {
        if(rc.getRoundNum()>200){ 
            if(flagMovementLogic()) return; 
        }
        if(crumbMovementLogic()) return;
        leaderMovementLogic();
        attackMicro();
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

    public void tryHeal() throws GameActionException {
        if (!rc.isActionReady()) return;
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
            rc.setIndicatorString("I healed " + bestheal);
        }
    }
    public void checkPickupFlag() throws GameActionException {
        for (Direction d:Direction.allDirections()) {
            MapLocation nxt = myLoc.add(d);
            if (rc.canPickupFlag(nxt) && friendlyRobots.length- enemyRobots.length>3 && rc.getRoundNum() > 200) {//check if this change is good
                rc.pickupFlag(nxt);
                
            }
        }
    }
    public void checkBuildTraps() throws GameActionException{
        MapInfo[] nearbyInfo = rc.senseNearbyMapInfos(6);
        int traps = 0;
        for (MapInfo mi:nearbyInfo) {
            if (mi.getTrapType() != TrapType.NONE) {
                traps++;
            }
        }
        if(rc.getRoundNum()>210) {
            if (enemyRobots.length > (6 - rc.getCrumbs()/3500) && closeEnemyRobots.length >= (2 - rc.getCrumbs()/1500)) {
                if (traps <= 1 && rc.canBuild(TrapType.EXPLOSIVE, myLoc))
                    rc.build(TrapType.EXPLOSIVE, myLoc);
            }
            if (myLoc.distanceSquaredTo(currentTarget) < 5) {
                if (traps <= 2) {
                    if (friendlyRobots.length >= 4 && rc.canBuild(TrapType.STUN, myLoc))
                        rc.build(TrapType.STUN, myLoc);
                    if (rc.canBuild(TrapType.EXPLOSIVE, myLoc))
                        rc.build(TrapType.EXPLOSIVE, myLoc);
                }
            }
        }else{
            if (rc.getRoundNum()>190&&enemyRobots.length > 3 && rc.canBuild(TrapType.EXPLOSIVE, myLoc)) {
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

    public MapLocation findLeader() {
        int maxH = rc.getHealth()*1000000+rc.getID();
        MapLocation ret = null;
        for(RobotInfo i: friendlyRobots){
            if(i.hasFlag){
                rc.setIndicatorString("i have leader rn");
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
    public void attackMicro() {
        if (!rc.isMovementReady()) return;
        int cooldown = rc.getActionCooldownTurns();
        MapLocation closest = null; int minEnemyDist = 10000;
        for (RobotInfo ri:closeEnemyRobots) {
            if (ri.location.distanceSquaredTo(myLoc) < minEnemyDist) {
                minEnemyDist = ri.location.distanceSquaredTo(myLoc);
                closest = ri.location;
            }
        }
        if (closest != null) {
            if (cooldown < 10) { //WE WANT TO ATTACK / HEAL
                if (minEnemyDist > 4) {
                    bugNav.move(closest);
                }
            }
            else if (cooldown < 20) {
                if (minEnemyDist > 8) {
                    bugNav.move(closest);
                }
                if (minEnemyDist <= 4) {
                    bugNav.move(myLoc.add(myLoc.directionTo(closest).opposite()));
                }
            }
            else {
                if (minEnemyDist <= 6) {
                    bugNav.move(myLoc.add(myLoc.directionTo(closest).opposite()));
                }
            }
        }
        rc.setIndicatorDot(myLoc, 0, 0, 0);
    }

    public int onOpponentSide(MapLocation closetSpawn, MapLocation currentLocation) {//if closer to enemy, then negative
        if (currentTarget!=null) {
            return currentLocation.distanceSquaredTo(currentTarget) - currentLocation.distanceSquaredTo(closetSpawn);
        }
        return 10;
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
}
