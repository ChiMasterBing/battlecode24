package bobthebuilder;
import battlecode.common.*;
import bobthebuilder.fast.FastLocSet;

import java.util.Random;

public class Attacker extends Robot{
    MapLocation closestSpawn, myLoc = null, currentTarget = null;
    MapLocation[] broadcastLocations = {};
    RobotInfo[] enemyRobots, friendlyRobots, closeFriendlyRobots, closeEnemyRobots;
    FastLocSet spawnSet = new FastLocSet();
    int onOpponentSide = 0;
    Random random = new Random();
    int deadMeat = 0;
    MapLocation lowestHealthLoc;
    MapLocation weakestEnemy;
    MapLocation closestEnemy;
    int minEnemyDist;
    int[] attackUpgrade = {150, 158, 165,  173, 180, 195, 225};
    int[] healUpgrade = {80, 82, 84, 86, 88, 92, 100};
    boolean tooCloseToSpawn = false;
    public Attacker(RobotController rc) throws GameActionException {
        super(rc);
    }

    public void turn() throws GameActionException{
        updateCurrentTarget();
        premoveSetGlobals();
        checkPickupFlag();
        checkBuildTraps();
        
        attackLogic();
        if(enemyRobots.length==0||rc.getLevel(SkillType.HEAL)>3) {
            tryHeal();
        }

        movement();
        postmoveSetGlobals();
        checkBuildTraps();
        attackLogic();
        if(enemyRobots.length==0||rc.getLevel(SkillType.HEAL)>3){
            tryHeal();
        }
        tryFill();
    }
    public void checkPickupFlag() throws GameActionException {
        for (Direction d:allDirections) {
            MapLocation nxt = myLoc.add(d);
            if (rc.canPickupFlag(nxt) && friendlyRobots.length- enemyRobots.length>3 && roundNumber > 200) {
                rc.pickupFlag(nxt);
            }
        }
    }
    public Boolean flagMovementLogic() throws GameActionException {
        if (rc.hasFlag()){
//            for (Direction d:allDirections) {
//                if (spawnSet.contains(myLoc.add(d)) && rc.canMove(d)) {
//                    rc.move(d);
//                    Comms.depositFlag();
//                    Debug.println("I DEPOSITED FLAG WOO!");
//                    return true;
//                }
//            }
            bugNav.move(closestSpawn);
            if (spawnSet.contains(rc.getLocation())) {
//                rc.resign();
                Debug.println("I DEPOSITED FLAG WOO!");
                Comms.depositFlag();
            }
            return true;
        }

        int dist = Integer.MAX_VALUE;
        MapLocation targ = null;

        for(FlagInfo i : flags){
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
                        if(roundNumber%2==0) {
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
        if (crummy.length > 0 && roundNumber < 250) {
            BFSController.move(rc, crummy[0]);
            return true;
        }
        else if(roundNumber < 200-Math.max(rc.getMapHeight(), rc.getMapWidth())/2){
            //randomly explore, with bias towards center
            MapLocation choice;
            choice = myLoc.add(allDirections[random.nextInt(8)]);
            bugNav.move(choice);
            return true;
        }
        return false;
    }
    public void movement() throws GameActionException {
        if(roundNumber>200){
            if(flagMovementLogic()) return;
        }
        if(crumbMovementLogic()) return;


        if (attackMicro())
            return;
//        if(rc.getHealth()<=750&&closeFriendlyRobots.length>0){
//            return;
//        }
//        for(RobotInfo i: closeFriendlyRobots){
//            if(i.getHealth()<=300){
//                return;
//            }
//        }

        if(lowestHealthLoc!=null){
            bugNav.move(lowestHealthLoc);//I cant bfs cuz osmeimtes it moves perpenduclarly into enemy base
        }

        if(currentTarget!=null)
            bugNav.move(currentTarget);
    }
    public void premoveSetGlobals() throws GameActionException {
        myLoc = rc.getLocation();
        closeEnemyRobots = rc.senseNearbyRobots(4, rc.getTeam().opponent());
        closeFriendlyRobots = rc.senseNearbyRobots(4, rc.getTeam());
        enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        friendlyRobots = rc.senseNearbyRobots(-1, rc.getTeam());
        tooCloseToSpawn= false;
        if(roundNumber>200&&currentTarget!=null&&myLoc.distanceSquaredTo(currentTarget)>=closestSpawn.distanceSquaredTo(currentTarget)){
            tooCloseToSpawn = true;
        }
        int lowestHealth = 1000;
        lowestHealthLoc = null;
        for(RobotInfo ri : friendlyRobots){
            if(ri.getHealth()<lowestHealth){
                lowestHealth = ri.getHealth();
                lowestHealthLoc = ri.getLocation();
            }
        }

        int dist = Integer.MAX_VALUE;
        for(MapLocation spawn : spawnLocs){
            int cdist = myLoc.distanceSquaredTo(spawn);
            spawnSet.add(spawn);
            if(cdist<dist){
                dist = cdist;
                closestSpawn = spawn;
            }
        }

        if (currentTarget == null) {
            Debug.println(mirrorFlags[0] + " ");
            rc.resign();
        }

        if(roundNumber>200&&myLoc.distanceSquaredTo(currentTarget)>=closestSpawn.distanceSquaredTo(currentTarget)){
            tooCloseToSpawn = true;
        }



        int lowestHealth = 1000;
        lowestHealthLoc = null;
        for(RobotInfo ri : friendlyRobots){
            if(ri.getHealth()<lowestHealth){
                lowestHealth = ri.getHealth();
                lowestHealthLoc = ri.getLocation();
            }
        }

        deadMeat = 0;
        closestEnemy = null;
        minEnemyDist = 100000;
        for (RobotInfo ri:enemyRobots) {
            MapLocation cval = ri.location;
            int ridist = cval.distanceSquaredTo(myLoc);
            if (ridist < minEnemyDist) {
                minEnemyDist = ridist;
                closestEnemy = cval;
            }
            MapLocation dirToMe = cval.add(cval.directionTo(myLoc));
            if(rc.canSenseLocation(dirToMe)&&rc.senseMapInfo(dirToMe).getTrapType()!=TrapType.NONE){
                deadMeat++;
            }
        }






        weakestEnemy = null;
        int weakestEnemyHealth = 1000000000;
        for(RobotInfo ri: enemyRobots){
            MapLocation cval = ri.location;
            int ridist = cval.distanceSquaredTo(myLoc);
            if(minEnemyDist<12&&ridist>=12){
                continue;
            }
            if(minEnemyDist<10&&ridist>=10){
                continue;
            }
            if(minEnemyDist<5&&ridist>=5){
                continue;
            }
            if(minEnemyDist<2&&ridist>=2){
                continue;
            }
            int weakness = (ri.getHealth()/150)*1500+ridist;
            if(weakestEnemyHealth>weakness){
                weakestEnemyHealth = weakness;
                weakestEnemy = cval;
                minEnemyDist = ridist;
            }

        }

        onOpponentSide = onOpponentSide(closestSpawn, myLoc); //NOTE: this is never used
    }
    public void postmoveSetGlobals() throws GameActionException { //a lot of the setGlobals are only used for movement, hence, diff function
        myLoc = rc.getLocation();
        closeEnemyRobots = rc.senseNearbyRobots(4, rc.getTeam().opponent());
        closeFriendlyRobots = rc.senseNearbyRobots(4, rc.getTeam());
        enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        friendlyRobots = rc.senseNearbyRobots(-1, rc.getTeam());
        onOpponentSide = onOpponentSide(closestSpawn, myLoc); //NOTE: this is never used
    }

    public void tryHeal() throws GameActionException {
        if (!rc.isActionReady()) return;
        MapLocation bestHeal = null;
        int bestScore = 0;
        int myHeal = healUpgrade[rc.getLevel(SkillType.HEAL)];
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
            score += (i.getHealLevel() + i.getAttackLevel()) * 10;
            if (score > bestScore) {
                bestScore = score;
                bestHeal = i.getLocation();
            }
        }

        if(bestHeal != null && rc.canHeal(bestHeal)) {
            rc.heal(bestHeal);
            rc.setIndicatorString("I healed " + bestHeal);
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
        if(roundNumber>200) {
            MapLocation nxt;
            int threshold = Math.max(1, 7- rc.getCrumbs()/500);
            for (Direction d:allDirections) {
                nxt = myLoc.add(d);
                if (rc.senseNearbyRobots(nxt, 8, rc.getTeam().opponent()).length >= threshold) {
//                    if((closeFriendlyRobots.length-enemyRobots.length > 2 || rc.getCrumbs()<250)) {

                    if((friendlyRobots.length > 6 || rc.getCrumbs()<250)) {
                        if(stun>0){
                            return;
                        }
                        if(rc.canBuild(TrapType.STUN, nxt)) {
                            rc.build(TrapType.STUN, nxt);
                        }
                    }else if(expl<2&&rc.canBuild(TrapType.EXPLOSIVE, nxt)){
                        rc.build(TrapType.EXPLOSIVE, nxt);
                    }
                }
            }
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
        if(attackLoc!=null&&rc.canAttack(attackLoc)){
            rc.attack(attackLoc);
        }
    }
    public void tryFill() throws GameActionException {
        if (!rc.isActionReady()) return;
        if (roundNumber > 0 && enemyRobots.length == 0) {
            MapInfo[] water = rc.senseNearbyMapInfos(3);
            for (MapInfo w:water) {
                if (w.isWater() && rc.canFill(w.getMapLocation()))  {
                    rc.fill(w.getMapLocation());
                    return;
                }
            }
        }
    }

    public boolean attackMicro() throws GameActionException {
        if (!rc.isMovementReady()) return true;
        if (enemyRobots.length == 0) return false;

        int cooldown = rc.getActionCooldownTurns();

        int mosthealth = 0;
        for(RobotInfo ri : closeFriendlyRobots){
            mosthealth = Math.max(mosthealth, ri.getHealth());
        }
        Direction oppdir = myLoc.directionTo(closestEnemy).opposite();
        MapLocation opposite = myLoc.add(oppdir).add(oppdir);

        if(rc.getHealth()<mosthealth&&!tooCloseToSpawn){
            BFSController.move(rc, opposite);
//            BFSController.move(rc, opposite);
            if (!rc.isMovementReady()) return true;
            // if (rc.isMovementReady()) return false;
            // else return true;
        }

        if(cooldown>=10&&deadMeat>0){
//            bugNav.move(opposite);
            BFSController.move(rc, opposite);
            if (!rc.isMovementReady()) return true;
            // if (rc.isMovementReady()) return false;
            // else return true;
        }

        if (weakestEnemy != null) {
            if (cooldown < 10) { //WE WANT TO ATTACK / HEAL
                if (minEnemyDist > 4) {
                    if(rc.getLevel(SkillType.HEAL)>3){
                        return false;
                    }
                    BFSController.move(rc, weakestEnemy); return true;
                }else if(minEnemyDist<2&&!tooCloseToSpawn){
                    bugNav.move(opposite);
                    return true;
//                    BFSController.move(rc,opposite); return true;
                }
            }
            else if (cooldown < 20) {
                if (minEnemyDist > 9) {
                    if(rc.getLevel(SkillType.HEAL)>3){
                        return false;
                    }
                    BFSController.move(rc, weakestEnemy); return true;
                }
                if (minEnemyDist <=4&&!tooCloseToSpawn) {
                    bugNav.move(opposite);
                    return true;
//                    BFSController.move(rc,opposite); return true;
                }
            }
            else if(cooldown<30){
                if (minEnemyDist < 12&&!tooCloseToSpawn) {
                    bugNav.move(opposite);
                    return true;
//                    BFSController.move(rc,opposite); return true;
                }else{
                    if(rc.getLevel(SkillType.HEAL)>3){
                        return false;
                    }
                    BFSController.move(rc, weakestEnemy); return true;
                }
            }else{
                bugNav.move(opposite);
                return true;

//                BFSController.move(rc, opposite); return true;
            }
        }
        return false;
    }


    int[] healPen = {-1, -2, -2, -5, -5, -10, -10};
    int[] atkPen = {-1, -5, -5, -10, -10, -15, -15};
    int[] buildPen = {-1, -2, -2, -5, -5, -10, -10};

    public MapLocation findBestAttackLocation() {
        int minHP = 1000000000;
        MapLocation ret = null;
        int myAtk = attackUpgrade[rc.getLevel(SkillType.ATTACK)];
        for(RobotInfo i : closeEnemyRobots){
            MapLocation enemyLoc = i.getLocation();

            int cval = Math.max(i.getHealth(), myAtk)*100-(i.getHealLevel()+i.getAttackLevel()+i.getBuildLevel()+Math.max(i.getHealLevel(), Math.max(i.getAttackLevel(), i.getBuildLevel())));
            if(i.getHealth()<myAtk){
                cval+=(myAtk-i.getHealth())*5;
            }
            if (cval < minHP) {
                minHP = cval;
                ret = enemyLoc;
            }
//            if (cval > myAtk) { //not always good to hit flagholder
            if (i.hasFlag) {
                ret = enemyLoc;
                break;
            }
//            }
        }
        return ret;
    }
}