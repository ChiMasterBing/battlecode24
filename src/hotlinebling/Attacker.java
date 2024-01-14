package hotlinebling;
import battlecode.common.*;
import hotlinebling.fast.FastLocSet;

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
        for(MapLocation spawn : spawnLocs){
            spawnSet.add(spawn);
        }
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
                        if(super.roundNumber%2==0) {
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
        if (crummy.length > 0) {
            bugNav.move(crummy[0]);
            return true;
        }
        else if(super.roundNumber<200-Math.max(rc.getMapHeight(), rc.getMapWidth())/2){
            //randomly explore, with bias towards center
            MapLocation choice;
            choice = myLoc.add(Direction.allDirections()[random.nextInt(8)]);
            bugNav.move(choice);
            return true;
        }
        return false;
    }
    public Boolean leaderMovementLogic() throws GameActionException {//always returns true
        if(currentTarget!=null) {
            bugNav.move(currentTarget);
            return true;
        }
        return false;
    }
    public void movement() throws GameActionException {
        if(super.roundNumber>200){
            if(flagMovementLogic()) return;
        }
        if(super.roundNumber < 250 && crumbMovementLogic()) return;
        attackMicro();
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
            if(i.getBuildLevel()==6){
                bestheal = i.getLocation();
                lowestHealth = -100000;
            }
            int cval = Math.max(i.getHealth(), 150)*100-(i.getHealLevel()+i.getAttackLevel()+i.getBuildLevel()+Math.max(i.getHealLevel(), Math.max(i.getAttackLevel(), i.getBuildLevel())));
            if(lowestHealth>cval){
                bestheal = i.getLocation();
                lowestHealth = cval;
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
            if (rc.canPickupFlag(nxt) && friendlyRobots.length- enemyRobots.length>3 && super.roundNumber > 200) {//check if this change is good
                rc.pickupFlag(nxt);
                break;
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
        if (super.roundNumber>190&&enemyRobots.length > 4 && rc.canBuild(TrapType.EXPLOSIVE, myLoc)) {
            if (traps < 2) {
                rc.build(TrapType.EXPLOSIVE, myLoc);
            }
        }
    }

    public void updateCurrentTarget() throws GameActionException {
        MapLocation[] arr = rc.senseBroadcastFlagLocations();
        if (arr.length>0&&arr[0] != null) {
            currentTarget = arr[0];
        }
        if (currentTarget == null) {
            int symm = macroPath.getSymmType();
            switch (symm) {
                case -1:
                    break;
                case macroPath.H_SYM:
                    currentTarget = macroPath.getHSym(super.myFlags[0]);
                    break;
                case macroPath.V_SYM:
                    currentTarget = macroPath.getVSym(super.myFlags[0]);
                    break;
                case macroPath.R_SYM:
                    currentTarget = macroPath.getRSym(super.myFlags[0]);
                    break;
            }
        }
    }
    public void attackLogic() throws GameActionException {
        if (!rc.isActionReady()) return;
        MapLocation attackLoc = findBestAttackLocation();
        if(attackLoc!=null&&rc.canAttack(attackLoc)){
            rc.attack(attackLoc);
        }
    }
    public void attackerLogic() throws GameActionException {
        checkPickupFlag();
        if(super.roundNumber<=210){
            checkBuildTraps();
        }
        updateCurrentTarget();
        attackLogic();

        if(enemyRobots.length==0) {
            tryHeal();
        }

        movement();
        setGlobals(); // after we moved, globals are different
        attackLogic();
        tryHeal();
        if(super.roundNumber<=210) {
            checkBuildTraps();
        }
//        tryFill();
    }
    public void turn() throws GameActionException{
        setGlobals();
        attackerLogic();
    }

    public void tryFill() throws GameActionException {
        if (!rc.isActionReady()) return;
        if (super.roundNumber > 0) {
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
    public void attackMicro() {
        if (!rc.isMovementReady()) return;
        int cooldown = rc.getActionCooldownTurns();
        MapLocation closest = null; int minEnemyDist = 10000;
        for (RobotInfo ri:enemyRobots) {
            if (ri.location.distanceSquaredTo(myLoc) < minEnemyDist) {
                minEnemyDist = ri.location.distanceSquaredTo(myLoc);
                closest = ri.location;
            }
        }
        if (closest != null) {
            MapLocation opposite = myLoc.add(myLoc.directionTo(closest).opposite());
            if (cooldown < 10) { //WE WANT TO ATTACK / HEAL
                if (minEnemyDist > 4) {
                    bugNav.move(closest);
                }else if(minEnemyDist<2){
                    bugNav.move(opposite);
                }
            }
            else if (cooldown < 20) {
                if (minEnemyDist > 9) {
                    bugNav.move(closest);
                }
                if (minEnemyDist <=4) {
                    bugNav.move(opposite);
                }
            }
            else if(cooldown<30){
                if (minEnemyDist < 12) {
                    bugNav.move(opposite);
                }else{
                    bugNav.move(closest);
                }
            }else{
                bugNav.move(opposite);
            }
        }
        rc.setIndicatorDot(myLoc, 0, 0, 0);
    }

    public int onOpponentSide(MapLocation closetSpawn, MapLocation currentLocation) {//if closer to enemy, then negative
        MapLocation oppSide = null;
        int symm = macroPath.getSymmType();
        switch (symm) { //TODO: can be precomputed
            case -1:
                return 10;
            case macroPath.H_SYM:
                oppSide = macroPath.getHSym(super.myFlags[1]);
                break;
            case macroPath.V_SYM:
                oppSide = macroPath.getVSym(super.myFlags[1]);
                break;
            case macroPath.R_SYM:
                oppSide = macroPath.getRSym(super.myFlags[1]);
                break;
        }
        return currentLocation.distanceSquaredTo(oppSide) - currentLocation.distanceSquaredTo(super.myFlags[1]);
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