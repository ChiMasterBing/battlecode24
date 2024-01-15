package bling3;
import battlecode.common.*;
import bling3.fast.FastLocSet;

import java.util.Random;

public class Attacker extends Robot{
    MapLocation closestSpawn, myLoc = null, currentTarget = null;
    MapLocation[] broadcastLocations = {};
    MapLocation[] spawnLocs;
    RobotInfo[] enemyRobots, friendlyRobots, closeFriendlyRobots, closeEnemyRobots;
    FastLocSet spawnSet = new FastLocSet();
    int onOpponentSide = 0;
    Random random = new Random();

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
    // public void updateCurrentTarget() throws GameActionException {
    //     if (currentTarget == null) {
    //         currentTarget = mirrorFlags[0]; //by symmetry
    //     } 
    //     //from superclass
    //     if (roundNumber > 200 && (roundNumber - stolenFlagRounds[0]  < 10 || roundNumber - stolenFlagRounds[1] < 10 || roundNumber - stolenFlagRounds[2] < 10)) {
    //         //System.out.println("Intercepting!!");
    //         int minDist = 10000;
    //         if (roundNumber - stolenFlagRounds[0] < 10) {
    //             int d = myLoc.distanceSquaredTo(stolenFlags[0]);
    //             if (d < minDist) {
    //                 minDist = d;
    //                 currentTarget = stolenFlags[0];
    //             }
    //             // int d1 = mirrorFlags[0].distanceSquaredTo(stolenFlags[0]);
    //             // int d2 = mirrorFlags[1].distanceSquaredTo(stolenFlags[0]);
    //             // int d3 = mirrorFlags[2].distanceSquaredTo(stolenFlags[0]);
    //             // if (d1 < minDist) {
    //             //     currentTarget = mirrorFlags[0];
    //             //     minDist = d1;
    //             // }
    //             // if (d2 < minDist) {
    //             //     currentTarget = mirrorFlags[1];
    //             //     minDist = d2;
    //             // }
    //             // if (d3 < minDist) {
    //             //     currentTarget = mirrorFlags[2];
    //             //     minDist = d3;
    //             // }
    //         }
    //         if (roundNumber - stolenFlagRounds[1]  < 10) {
    //             int d = myLoc.distanceSquaredTo(stolenFlags[1]);
    //             if (d < minDist) {
    //                 minDist = d;
    //                 currentTarget = stolenFlags[1];
    //             }
    //             // int d1 = mirrorFlags[0].distanceSquaredTo(stolenFlags[1]);
    //             // int d2 = mirrorFlags[1].distanceSquaredTo(stolenFlags[1]);
    //             // int d3 = mirrorFlags[2].distanceSquaredTo(stolenFlags[1]);
    //             // if (d1 < minDist) {
    //             //     currentTarget = mirrorFlags[0];
    //             //     minDist = d1;
    //             // }
    //             // if (d2 < minDist) {
    //             //     currentTarget = mirrorFlags[1];
    //             //     minDist = d2;
    //             // }
    //             // if (d3 < minDist) {
    //             //     currentTarget = mirrorFlags[2];
    //             //     minDist = d3;
    //             // }
    //         }
    //         if (roundNumber - stolenFlagRounds[2] < 10) {
    //             int d = myLoc.distanceSquaredTo(stolenFlags[2]);
    //             if (d < minDist) {
    //                 minDist = d;
    //                 currentTarget = stolenFlags[2];
    //             }
    //             // int d1 = mirrorFlags[0].distanceSquaredTo(stolenFlags[2]);
    //             // int d2 = mirrorFlags[1].distanceSquaredTo(stolenFlags[2]);
    //             // int d3 = mirrorFlags[2].distanceSquaredTo(stolenFlags[2]);
    //             // if (d1 < minDist) {
    //             //     currentTarget = mirrorFlags[0];
    //             //     minDist = d1;
    //             // }
    //             // if (d2 < minDist) {
    //             //     currentTarget = mirrorFlags[1];
    //             //     minDist = d2;
    //             // }
    //             // if (d3 < minDist) {
    //             //     currentTarget = mirrorFlags[2];
    //             //     minDist = d3;
    //             // }
    //         }
    //     }
    //     else {
    //         if (broadcastLocations.length>0 && broadcastLocations[0] != null) {
    //             currentTarget = broadcastLocations[0];
    //         }
    //     }
    //     MapLocation[] arr = rc.senseBroadcastFlagLocations();
    //     if (arr.length>0&&arr[0] != null) {
    //         broadcastLocations = arr;
    //     }
    // }

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
        MapLocation closest = null; int minEnemyDist = 10000;
        for (RobotInfo ri:enemyRobots) {
            if (ri.location.distanceSquaredTo(myLoc) < minEnemyDist) {
                minEnemyDist = ri.location.distanceSquaredTo(myLoc);
                closest = ri.location;
            }
        }

        int mosthealth = 0;
        for(RobotInfo ri : closeFriendlyRobots){
            mosthealth = Math.max(mosthealth, ri.getHealth());
        }
        MapLocation opposite = myLoc.add(myLoc.directionTo(closest).opposite());
        if(rc.getHealth()<mosthealth){
            bugNav.move(opposite);
        }

        if (closest != null) {
            if (cooldown < 10) { //WE WANT TO ATTACK / HEAL
                if (minEnemyDist > 4) {
                    bugNav.move(closest);
                    return true;
                }else if(minEnemyDist<2){
                    bugNav.move(opposite);
                    return true;
                }
            }
            else if (cooldown < 20) {
                if (minEnemyDist > 9) {
                    bugNav.move(closest);
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
                    bugNav.move(closest);
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