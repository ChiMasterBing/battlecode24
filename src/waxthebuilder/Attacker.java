package waxthebuilder;
import battlecode.common.*;
import attackMicroTestB.fast.FastLocSet;

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
    int prevEnemies = 0;
    MapLocation lastEnemyLocation; //I was going to do something with this but forgot
    public Attacker(RobotController rc) throws GameActionException {
        super(rc);
    }

    public void turn() throws GameActionException{
        premoveSetGlobals();
        checkPickupFlag();
        checkBuildTraps();
        updateCurrentTarget();
        prevEnemies--;
        if(enemyRobots.length>0){
            prevEnemies = 1;
        }
        attackLogic();
        attackLogic();
        if((rc.getLevel(SkillType.HEAL)!=3||rc.getLevel(SkillType.ATTACK)>3)&&prevEnemies<=0) {
            tryHeal();
        }

        movement();
        postmoveSetGlobals();
        checkBuildTraps();
        attackLogic();
        attackLogic();
        if(prevEnemies<=0){
            rc.setIndicatorString("my heal levl"+rc.getExperience(SkillType.HEAL));
            tryHeal();
        }
        tryFill();
        callDefense();
    }

    public void deadFunctions() throws GameActionException {
        if (myInfoUsed[0] == Comms.readFlagStatus(0)) {
            Comms.writeFlagStatus(0, 8);
            myInfoUsed[0] = -1;
        }
        if (myInfoUsed[1] == Comms.readFlagStatus(1)) {
            Comms.writeFlagStatus(1, 8);
            myInfoUsed[1] = -1;
        }
        if (myInfoUsed[2] == Comms.readFlagStatus(2)) {
            Comms.writeFlagStatus(2, 8);
            myInfoUsed[2] = -1;
        }
    }

    int[] myInfoUsed = {-1, -1, -1};
    public void callDefense() throws GameActionException {
        int score = Math.min(Math.max(friendlyRobots.length - 2 * enemyRobots.length + 8, 0), 15);
//        rc.setIndicatorString(score + " <--");
        if (score < 8) {
            int d1 = 10000, d2 = d1, d3 = d1;
            if (Comms.myFlagExists(0))
                d1 = myLoc.distanceSquaredTo(myFlags[0]);
            if (Comms.myFlagExists(1))
                d2 = myLoc.distanceSquaredTo(myFlags[1]);
            if (Comms.myFlagExists(2))
                d3 = myLoc.distanceSquaredTo(myFlags[2]);
            int d4 = Math.min(d1, Math.min(d2, d3));

            if (d4 == 10000) {
                Debug.println("Whoops, seems like the sitting duck has died?");
                return;
            }

            if (d4 <= 10) {
                if (enemyRobots.length > 5 && friendlyRobots.length <= 2) {
                    if (d4 == d1) {
                        Comms.writeFlagStatus(0, 15);
                    } else if (d4 == d2) {
                        Comms.writeFlagStatus(1, 15);
                    } else if (d4 == d3) {
                        Comms.writeFlagStatus(2, 15);
                    }
                    return; //its hopeless
                }
            }

            if (d4 == d1) {
                int cur = Comms.readFlagStatus(0);
                if (score < cur) {
                    //Debug.println("CALLING IN DEFENSE @ " + myLoc + " " + score);
                    Comms.writeFlagStatus(0, score);
                    myInfoUsed[0] = score;
                }
            } else if (d4 == d2) {
                int cur = Comms.readFlagStatus(1);
                if (score < cur) {
                    //Debug.println("CALLING IN DEFENSE @ " + myLoc);
                    Comms.writeFlagStatus(1, score);
                    myInfoUsed[1] = score;
                }
            } else if (d4 == d3) {
                int cur = Comms.readFlagStatus(2);
                if (score < cur) {
                    //Debug.println("CALLING IN DEFENSE @ " + myLoc);
                    Comms.writeFlagStatus(2, score);
                    myInfoUsed[2] = score;
                }
            }

        } else {
            if (myInfoUsed[0] == Comms.readFlagStatus(0)) {
                Comms.writeFlagStatus(0, 8);
                myInfoUsed[0] = -1;
            } else if (myInfoUsed[1] == Comms.readFlagStatus(1)) {
                Comms.writeFlagStatus(1, 8);
                myInfoUsed[1] = -1;
            } else if (myInfoUsed[2] == Comms.readFlagStatus(2)) {
                Comms.writeFlagStatus(2, 8);
                myInfoUsed[2] = -1;
            }
        }
    }

    public void checkPickupFlag() throws GameActionException {
        for (Direction d:allDirections) {
            MapLocation nxt = myLoc.add(d);
            if (rc.canPickupFlag(nxt) && (friendlyRobots.length + 1) - enemyRobots.length > 0 && roundNumber > 200) {
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
                Comms.depositFlag(0);
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
                    if(3 * enemyRobots.length < 2 * (friendlyRobots.length + 1)){//check if this is hleful
                        int cdist = i.getLocation().distanceSquaredTo(myLoc);
                        if(cdist<dist){
                            dist = cdist;
                            targ = i.getLocation();
                        }
                    }
                }else{ //if we picked up their flag
                    if (Comms.countFlagsCaptured() < 2) return false; //CHECK IF IT HAS BEEN PICKED I{}
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
        int closestCrum = 10000;
        MapLocation crum = null;
        for(MapLocation mi : crummy){
            if(closestCrum>mi.distanceSquaredTo(myLoc)) {
                crum = mi;
                closestCrum = mi.distanceSquaredTo(myLoc);
            }
        }
        if (crummy.length > 0 && roundNumber < 250 && crummy.length > (friendlyRobots.length+1)) {
//            BFSController.move(rc, crum);
            if (rc.isMovementReady()) bugNav.move(crum);
            return true;
        }
        else if(roundNumber < 200-Math.max(rc.getMapHeight(), rc.getMapWidth())/2){
            //randomly explore, with bias towards center
            MapLocation choice;
            choice = myLoc.add(allDirections[random.nextInt(8)]);
            bugNav.move(choice);
            return true;
        }
        else if (roundNumber < 200) {
            //minimize dam distance
            //
            MapInfo[] dams = rc.senseNearbyMapInfos(2);
            for (MapInfo m:dams) {
                if (m.isDam() && m.getMapLocation().distanceSquaredTo(currentTarget) <= myLoc.distanceSquaredTo(currentTarget)) return true;
            }

            if (rc.isMovementReady()) {
//                BFSController.move(rc, currentTarget);
                bugNav.move(currentTarget);
            }
        }
        return false;
    }
    public void movement() throws GameActionException {
        if(roundNumber>200){
            if(flagMovementLogic()) {
                explorePtr = 0;
                return;
            }
        }
        if(crumbMovementLogic()) return;


        if (attackMicro()) {
            explorePtr = 0;
            return;
        }
        if(rc.getHealth()<240){
            bugNav.move(closestSpawn);
        }
        int ret = 0;
        for(RobotInfo i: closeFriendlyRobots){
            if(i.getLocation().distanceSquaredTo(myLoc)<2){
                ret++;
                if(ret>2) {
                    Direction oppDir = i.getLocation().directionTo(myLoc);
                    bugNav.move(myLoc.add(oppDir).add(oppDir));
                    return;
                }
            }
        }

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
            if(ri.getHealth()<lowestHealth && myLoc.distanceSquaredTo(ri.getLocation()) > 2){ //> 2 or else will bugfind interfere ||
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
            score += (i.getHealLevel() + 2*i.getAttackLevel()) * 7;
            if (score > bestScore) {
                bestScore = score;
                bestHeal = i.getLocation();
            }
        }

        if(bestHeal != null && rc.canHeal(bestHeal)) {
            rc.heal(bestHeal);
//            rc.setIndicatorString("I healed " + bestHeal);
        }
    }

    public void checkBuildTraps() throws GameActionException{
        if(roundNumber>200) {
            MapLocation nxt;
            int threshold = Math.max(1, 7- rc.getCrumbs()/1000);
            int threshold2 = Math.max(0, 7- rc.getCrumbs()/500);
            for (Direction d:allDirections) {
                nxt = myLoc.add(d);
                if (nxt.equals(myLoc) && nxt.equals(mirrorFlags[0]) || nxt.equals(mirrorFlags[1]) || nxt.equals(mirrorFlags[2])) {
                    if (rc.canBuild(TrapType.WATER, nxt) && rc.senseNearbyFlags(-1, rc.getTeam().opponent()).length == 0) {
                        rc.build(TrapType.WATER, nxt);
                    }
                } //CHECK THIS
                int num = rc.senseNearbyRobots(nxt, 8, rc.getTeam().opponent()).length;
                if(num>=threshold2){
                    MapInfo[] mp = rc.senseNearbyMapInfos(nxt, 2);
                    boolean stun = false;
                    for(MapInfo i: mp){
                        if(i.getTrapType()==TrapType.STUN){
                            stun = true;
                            break;
                        }
                    }
                    if(!stun) {
                        if(rc.canBuild(TrapType.STUN, nxt)) {
                            rc.build(TrapType.STUN, nxt);
                        }
                    }
                }
                if (rc.canSenseLocation(nxt)&&!rc.senseMapInfo(nxt).isWater()&&rc.senseNearbyRobots(nxt, 8, rc.getTeam().opponent()).length >= threshold) {
                    if(rc.canBuild(TrapType.EXPLOSIVE, nxt)){
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

    int[] exploreDX = {3, 0, -3, 0, 6, 0, -6, 0, 9, 0, -9, 0};
    int[] exploreDY = {0, 3, 0, -3, 0, 6, 0, -6, 0, 9, 0, -9};
    int explorePtr = 0;
    public MapLocation exploreTarget(MapLocation target) throws GameActionException {
        //we've reached our target but see no flag... now what?
        if (explorePtr >= exploreDX.length) return target;
        MapLocation res = target.translate(exploreDX[explorePtr], exploreDY[explorePtr]);
        if (!rc.onTheMap(res)) {
            explorePtr++;
            return exploreTarget(target);
        }
        return res;
    }

    public void updateCurrentTarget() throws GameActionException {
        if (roundNumber < 200) {
            int idx = Navigation.getClosestSpawnNumber(myLoc, mirrorFlags[0], mirrorFlags[1], mirrorFlags[2]);
            currentTarget = mirrorFlags[idx]; //by symmetry
        } else {
            if (broadcastLocations.length>0 && broadcastLocations[0] != null) {
                if (myMoveNumber >= 3) {
                    currentTarget = broadcastLocations[0];
                } else { //try to snipe some flags :>
                    if (broadcastLocations.length == 3) {
                        currentTarget = broadcastLocations[myMoveNumber%2 + 1];
                    } else if (broadcastLocations.length == 2) {
                        currentTarget = broadcastLocations[1];
                    } else if (broadcastLocations.length == 1) {
                        currentTarget = broadcastLocations[0];
                    }
                    if (myLoc.equals(currentTarget)) {
                        currentTarget = exploreTarget(myLoc);
                    }
                }
            }
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
            MapInfo[] water = rc.senseNearbyMapInfos(2);
            for (MapInfo w:water) {
                MapLocation fillLoc = w.getMapLocation();
                if (w.isWater()&&(fillLoc.x+fillLoc.y)%2==1 && rc.canFill(fillLoc))  {
                    rc.fill(fillLoc);
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

         if(cooldown>=10&&rc.getHealth()<mosthealth&&!tooCloseToSpawn){
             BFSController.move(rc, opposite);
 //            BFSController.move(rc, opposite);
             if (!rc.isMovementReady()) return true;
             // if (rc.isMovementReady()) return false;
             // else return true;
         }
        if((rc.getHealth() < 240 || friendlyRobots.length < enemyRobots.length) && !tooCloseToSpawn){
//            BFSController.move(rc, closestSpawn);
            BFSController.move(rc, opposite);
//            BFSController.move(rc, opposite);
            if (!rc.isMovementReady()) return true;
            // if (rc.isMovementReady()) return false;
            // else return true;
        }

        if(cooldown>=10&&deadMeat>0){
            bugNav.move(opposite);
//            BFSController.move(rc, opposite);
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
                    BFSController.move(rc, weakestEnemy);
                    if (rc.isMovementReady() && friendlyRobots.length - enemyRobots.length > 4) {
                        bugNav.move(currentTarget);
                        return true;
                    }
                    else { //dont want to get stuck
                        return true;
                    }
                }else if(minEnemyDist<2&&!tooCloseToSpawn){
                    BFSController.move(rc, opposite);
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
                    BFSController.move(rc, opposite);
                    return true;
//                    BFSController.move(rc,opposite); return true;
                }
            }
            else if(cooldown<30){
                if (minEnemyDist < 12&&!tooCloseToSpawn) {
                    BFSController.move(rc, opposite);
                    return true;
//                    BFSController.move(rc,opposite); return true;
                }else{
                    if(rc.getLevel(SkillType.HEAL)>3){
                        return false;
                    }
                    BFSController.move(rc, weakestEnemy); return true;
                }
            }else{
                BFSController.move(rc, opposite);
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