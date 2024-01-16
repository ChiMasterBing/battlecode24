package bobnoheal;
import battlecode.common.*;
import bling3.fast.FastLocSet;

import java.util.Random;

public class Attacker extends Robot {
    MapLocation closestSpawn, myLoc = null, currentTarget = null;
    MapLocation[] broadcastLocations = {};
    MapLocation[] spawnLocs;
    RobotInfo[] enemyRobots, friendlyRobots, closeFriendlyRobots, closeEnemyRobots;
    FastLocSet spawnSet = new FastLocSet();
    int onOpponentSide = 0;
    Random random = new Random();
    int deadMeat = 0;
    MapLocation closestEnemy;
    int minEnemyDist;

    public Attacker(RobotController rc) throws GameActionException {
        super(rc);
        spawnLocs = rc.getAllySpawnLocations();
    }

    public void turn() throws GameActionException{
        setGlobals();
        checkPickupFlag();
        checkBuildTraps();
        updateCurrentTarget();
        attackLogic();
        if(enemyRobots.length==0) {
            tryHeal();
        }

        // if (rc.getRoundNum() > 400 && enemyRobots.length == 0) {
        //     buildSpawnTraps();
        // }

        movement();
        setGlobals(); //after we moved, globals are different
        attackLogic();
        tryHeal();
        checkBuildTraps();
        tryFill();
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
                    if (friendlyRobots.length >= 10) return false;
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

    public void movement() throws GameActionException {
        if(rc.getRoundNum()>200){
            if(flagMovementLogic()) return;
        }
        if(crumbMovementLogic()) return;

        if (enemyRobots.length > friendlyRobots.length)
            bugNav.move(closestSpawn);

        if (attackMicro())
            return;

        // if(friendlyRobots.length>=2){
        //     free = true;
        // }
        // if(myLoc.distanceSquaredTo(closestSpawn)>9&&myLoc.distanceSquaredTo(closestSpawn)<20&&friendlyRobots.length<2&&!free){// i do this cuz bad pathfind
        //     rc.setIndicatorString("stuck");
        //     return;
        // }

        if(currentTarget!=null)
            bugNav.move(currentTarget);
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

        deadMeat = 0;
        closestEnemy = null;
        minEnemyDist = 100000;
        for (RobotInfo ri:enemyRobots) {
            MapLocation cval = ri.location;
            if (cval.distanceSquaredTo(myLoc) < minEnemyDist) {
                minEnemyDist = cval.distanceSquaredTo(myLoc);
                closestEnemy = cval;
            }
            MapLocation dirToMe = cval.add(cval.directionTo(myLoc));
            if(rc.canSenseLocation(dirToMe)&&rc.senseMapInfo(dirToMe).getTrapType()!=TrapType.NONE){
                deadMeat++;
            }
        }

    }


    public void tryHeal() throws GameActionException {
        if (!rc.isActionReady()) return;
        MapLocation bestHeal = null;
        int bestScore = 0;
        int myHeal = SkillType.HEAL.getSkillEffect(rc.getLevel(SkillType.HEAL));
        for(RobotInfo i: closeFriendlyRobots){
            int hp = i.getHealth();
            if (hp == 1000) continue;
            int score = (1000 - hp);
            if (hp + myHeal > 750) {
                score += 250;
            }
            else if (hp + myHeal > 150) {
                score += 500;
            }
            score += (i.getHealLevel() + i.getAttackLevel()) * 50;
            if (score > bestScore) {
                bestScore = score;
                bestHeal = i.getLocation();
            }
        }
        //try healing yourself
        int hp = rc.getHealth();
        if (hp < 1000) {
            int score = (1000 - hp);
            if (hp + myHeal > 750) {
                score += 250;
            }
            else if (hp + myHeal > 150) {
                score += 500;
            }
            score += (rc.getLevel(SkillType.HEAL) + rc.getLevel(SkillType.ATTACK)) * 50;
            if (score > bestScore) {
                bestScore = score;
                bestHeal = myLoc;
            }
        }

        if(bestHeal != null && rc.canHeal(bestHeal)) { //bytecode issues
            rc.heal(bestHeal);
            rc.setIndicatorString("I healed " + bestHeal);
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
        MapInfo[] nearbyInfo = rc.senseNearbyMapInfos(4);
        int expl = 0, stun = 0;
        for (MapInfo mi:nearbyInfo) {
            if (mi.getTrapType() == TrapType.EXPLOSIVE) {
                expl++;
            }
            else if (mi.getTrapType() == TrapType.STUN) {
                stun++;
            }
        }
//        int traps = expl + stun;
        if(rc.getRoundNum()>200 && expl<1) {
            MapLocation nxt;

            int threshold = Math.max(1, 7- rc.getCrumbs()/1000);
            for (Direction d:Direction.allDirections()) {
                nxt = myLoc.add(d);
                if (rc.senseNearbyRobots(nxt, 8, rc.getTeam().opponent()).length >= threshold) {
//                    if (friendlyRobots.length > enemyRobots.)

                    if((closeEnemyRobots.length > 4 || rc.getCrumbs()<250)) { //friendlyRobots.length > 10 &&
                        if(rc.canBuild(TrapType.STUN, nxt)) {
                            //System.out.println("DANIEL SO PRO");
                            rc.build(TrapType.STUN, nxt);
                        }
                    }
                    if(rc.canBuild(TrapType.EXPLOSIVE, nxt)){
                        rc.build(TrapType.EXPLOSIVE, nxt);
                    }
//                    if (rc.canBuild(TrapType.EXPLOSIVE, nxt)) {
//                        rc.build(TrapType.EXPLOSIVE, nxt);
//                    }

                }
            }

        } else{
            // if (rc.getRoundNum()>190&&enemyRobots.length > 3 && rc.canBuild(TrapType.EXPLOSIVE, myLoc)) {
            //     if (traps < 2) {
            //         rc.build(TrapType.EXPLOSIVE, myLoc);
            //     }
            // }
        }
    }

    public void buildSpawnTraps() throws GameActionException {
        Direction[] diagonal = {Direction.NORTHEAST, Direction.NORTHWEST, Direction.SOUTHEAST, Direction.SOUTHWEST};
        if(myLoc.distanceSquaredTo(myFlags[0])<3){
            MapLocation centerLoc = myFlags[0];
            if(myLoc.distanceSquaredTo(centerLoc)>0){
                bugNav.move(myLoc);
            }
            if(myLoc.distanceSquaredTo(centerLoc)>0&&rc.senseMapInfo(centerLoc).getTrapType()==TrapType.NONE){
                bugNav.move(myLoc);
                return;
            }
            for(Direction d: diagonal){
                if(rc.canBuild(TrapType.STUN, centerLoc.add(d))){
                    rc.build(TrapType.STUN, centerLoc.add(d));
                }
            }
            // if(rc.canBuild(TrapType.EXPLOSIVE, centerLoc)){
            //     rc.build(TrapType.EXPLOSIVE, centerLoc);
            // }

        }else if(myLoc.distanceSquaredTo(myFlags[1])<3){

            if(rc.senseNearbyFlags(-1,rc.getTeam()).length==0){
                return;
            }

            MapLocation centerLoc = myFlags[1];
            if(myLoc.distanceSquaredTo(centerLoc)>0&&rc.senseMapInfo(centerLoc).getTrapType()==TrapType.NONE){
                bugNav.move(myLoc);
                return;
            }

            for(Direction d: diagonal){
                if(rc.canBuild(TrapType.STUN, centerLoc.add(d))){
                    rc.build(TrapType.STUN, centerLoc.add(d));
                }
            }
            // if(rc.canBuild(TrapType.EXPLOSIVE, centerLoc)){
            //     rc.build(TrapType.EXPLOSIVE, centerLoc);
            // }

        }else if(myLoc.distanceSquaredTo(myFlags[2])<3){
            if(rc.senseNearbyFlags(-1,rc.getTeam()).length==0){
                return;
            }
            MapLocation centerLoc = myFlags[2];
            if(myLoc.distanceSquaredTo(centerLoc)>0&&rc.senseMapInfo(centerLoc).getTrapType()==TrapType.NONE){
                bugNav.move(myLoc);
                return;
            }
            for(Direction d: diagonal){
                if(rc.canBuild(TrapType.STUN, centerLoc.add(d))){
                    rc.build(TrapType.STUN, centerLoc.add(d));
                }
            }
            // if(rc.canBuild(TrapType.EXPLOSIVE, centerLoc)){
            //     rc.build(TrapType.EXPLOSIVE, centerLoc);
            // }
        }
    }

    public void updateCurrentTarget() throws GameActionException {
        if (currentTarget == null) {
            currentTarget = mirrorFlags[0]; //by symmetry
        }

        if (broadcastLocations.length>0 && broadcastLocations[0] != null) {
            currentTarget = broadcastLocations[0];
        }

        MapLocation[] arr = rc.senseBroadcastFlagLocations();
        if (arr.length>0&&arr[0] != null) {
            broadcastLocations = arr;
        }
    }

    public int onOpponentSide(MapLocation closetSpawn, MapLocation currentLocation) {//if closer to enemy, then negative
        return currentLocation.distanceSquaredTo(mirrorFlags[0]) - currentLocation.distanceSquaredTo(super.myFlags[0]) + currentLocation.distanceSquaredTo(mirrorFlags[1]) - currentLocation.distanceSquaredTo(super.myFlags[1]) + currentLocation.distanceSquaredTo(mirrorFlags[2]) - currentLocation.distanceSquaredTo(super.myFlags[2]);
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

    public boolean attackMicro() {
        if (!rc.isMovementReady()) return true;
        if (enemyRobots.length == 0) return false;

        int cooldown = rc.getActionCooldownTurns();


        int mosthealth = 0;
        for(RobotInfo ri : closeFriendlyRobots){
            mosthealth = Math.max(mosthealth, ri.getHealth());
        }
        MapLocation opposite = myLoc.add(myLoc.directionTo(closestEnemy).opposite());
        if(rc.getHealth()<mosthealth){
            bugNav.move(opposite);
        }

        if(deadMeat>0){
            bugNav.move(opposite);
        }

        if (closestEnemy != null) {
            if (cooldown < 10) { //WE WANT TO ATTACK / HEAL
                if (minEnemyDist > 4) {
                    bugNav.move(closestEnemy);
                    return true;
                }else if(minEnemyDist<2){
                    bugNav.move(opposite);
                    return true;
                }
            }
            else if (cooldown < 20) {
                if (minEnemyDist > 9) {
                    bugNav.move(closestEnemy);
                    return true;
                }
                if (minEnemyDist <=4) {
                    bugNav.move(opposite);
                    return true;
                }
            }
            else if(cooldown<30){
                if (minEnemyDist < 12) {
                    bugNav.move(opposite);
                    return true;
                }else{
                    bugNav.move(closestEnemy);
                    return true;
                }
            }else{
                bugNav.move(opposite);
                return true;
            }
        }
        rc.setIndicatorDot(myLoc, 255, 0, 0);
        return false;
    }

    public MapLocation findBestAttackLocation() {
        int minHP = 1000000000;
        MapLocation ret = null;
        for(RobotInfo i : closeEnemyRobots){
            MapLocation enemyLoc = i.getLocation();
            int cval = Math.max(i.getHealth(), 150)*100-(i.getHealLevel()+i.getAttackLevel()+i.getBuildLevel()+Math.max(i.getHealLevel(), Math.max(i.getAttackLevel(), i.getBuildLevel())));
            if (cval < minHP) {
                minHP = cval;
                ret = enemyLoc;
            }
            if (cval > 150) { //not always good to hit flagholder
                if (i.hasFlag) {
                    ret = enemyLoc;
                    break;
                }
            }
        }

        return ret;
    }
}