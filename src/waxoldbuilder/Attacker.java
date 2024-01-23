package waxoldbuilder;
import battlecode.common.*;
import waxthebuilder.fast.FastLocSet;

import java.util.Random;

public class Attacker extends Robot {
    //GENERAL CONSTANTS
    FastLocSet spawnSet = new FastLocSet();
    Random random = new Random();
    //TARGETTING
    MapLocation closestSpawn, myLoc = null, currentTarget = null;
    MapLocation[] broadcastLocations = {};
    //ATTACK MICRO
    MapLocation weakestFriend, weakestEnemy, closestEnemy;
    int minEnemyDist;
    boolean tooCloseToSpawn = false;
    RobotInfo[] enemyRobots, friendlyRobots, closeFriendlyRobots, closeEnemyRobots;
    int numberOfEnemies, numberOfFriendlies, numberOfCloseEnemies, numberOfCloseFriends;

    public Attacker(RobotController rc) throws GameActionException {
        super(rc);
        for(MapLocation spawn : spawnLocs){
            spawnSet.add(spawn);
        }
    }

    public void turn() throws GameActionException{
        premoveSetGlobals();
        updateCurrentTarget();
        checkPickupFlag();
        checkBuildTraps();
        
        attackLogic();

        if((rc.getLevel(SkillType.HEAL)!=3||rc.getLevel(SkillType.ATTACK)>3)&&numberOfEnemies==0) { //CHECK THIS
            tryHeal();
        }

        movement();
        postmoveSetGlobals();
        checkBuildTraps();
        attackLogic();

        if(closestEnemy == null || myLoc.distanceSquaredTo(closestEnemy) > 9){
            tryHeal();
        }

        tryFill();
        callDefense();
    }

    int[] myInfoUsed = {-1, -1, -1};
    public void deadFunctions() throws GameActionException { //this runs if bot is dead
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

    public void callDefense() throws GameActionException {
        int score = Math.min(Math.max(numberOfFriendlies - 2 * numberOfEnemies + 8, 0), 15);
        if (score < 8) {
            int d1 = 10000, d2 = d1, d3 = d1;
            if (Comms.myFlagExists(0))
                d1 = myLoc.distanceSquaredTo(myFlags[0]);
            if (Comms.myFlagExists(1))
                d2 = myLoc.distanceSquaredTo(myFlags[1]);
            if (Comms.myFlagExists(2))
                d3 = myLoc.distanceSquaredTo(myFlags[2]);
            int d4 = Math.min(d1, Math.min(d2, d3));
            if (d4 == 10000) 
                return;
            if (d4 <= 10) {
                if (numberOfEnemies > 5 && numberOfFriendlies <= 2) {
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

    public boolean flagMovementLogic() throws GameActionException {
        if (rc.hasFlag()){
            for (Direction d:allDirections) {
                if (spawnSet.contains(myLoc.add(d)) && rc.canMove(d)) {
                    rc.move(d);
                    if (spawnSet.contains(rc.getLocation())) {
                        Debug.println("I DEPOSITED FLAG WOO!");
                        Comms.depositFlag(0);
                    }
                    return true;
                }
            }

            MapLocation tempObstacle = bugNav.lastObstacleFound;
            if(myLoc.distanceSquaredTo(closestSpawn) > 9 && (tempObstacle == null || (tempObstacle != null && rc.senseRobotAtLocation(tempObstacle) != null))) {
                int dist = myLoc.distanceSquaredTo(closestSpawn);
                RobotInfo best = null;
                int flagValue = rc.senseNearbyFlags(0)[0].getID();
                Comms.updateFlagID(flagValue);
                for(RobotInfo ri : closeFriendlyRobots) {
                    int value = ri.getLocation().distanceSquaredTo(closestSpawn);
                    if(value < dist) {
                        dist = value;
                        Comms.updateRobotID(flagValue, ri.ID);
                        best = ri;
                    }
                }
                if(best != null) {
                    MapLocation temp = myLoc.add(myLoc.directionTo(best.getLocation()));
                    if(rc.canDropFlag(temp)) {
                        rc.dropFlag(temp);
                    }
                }
            }
            bugNav.move(closestSpawn);
            if (spawnSet.contains(rc.getLocation())) {
                rc.resign();
                Debug.println("I DEPOSITED FLAG WOO!");
                Comms.depositFlag(0);
            }
            return true;
        }

        else {
            FlagInfo[] closeFlags = rc.senseNearbyFlags(8);
            if(closeFlags.length > 0) {
                FlagInfo i = closeFlags[0];
                if(Comms.closeToFlag(i.getID(), rc.getID())) {
                    if (rc.canPickupFlag(i.getLocation())) {
                        rc.pickupFlag(i.getLocation());
                        bugNav.move(closestSpawn);
                        return true;
                    }
                }
            }
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
                    if (Comms.closeToFlag(i.getID(), rc.getID())) {
                        targ = i.getLocation();
                        break;
                    }
                    else if (i.getLocation().distanceSquaredTo(myLoc) > 6) {
                        targ = closestSpawn;
                        break;
                    }
                    else {
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

    public void checkPickupFlag() throws GameActionException {
        for (Direction d:allDirections) {
            MapLocation nxt = myLoc.add(d);
            if(rc.canPickupFlag(nxt)) {
                boolean isDead = true;
                FlagInfo i = rc.senseNearbyFlags(2)[0];
                for(RobotInfo ri : friendlyRobots) {
                    if(Comms.closeToFlag(i.getID(), ri.getID())) {
                        isDead = false;
                    }
                }
                if (Comms.assignedToOther(i.getID(), rc.getID())||isDead) {
                    if (friendlyRobots.length- enemyRobots.length>0 && roundNumber > 200) {
                        rc.pickupFlag(nxt);
                    }
                }
            }
        }
    }

    public Boolean crumbMovementLogic() throws GameActionException {
        MapLocation[] crummy = rc.senseNearbyCrumbs(-1);
        if (crummy.length > 0 && roundNumber < 250 && crummy.length > (numberOfFriendlies+1)) {
            int closestCrum = 10000;
            MapLocation crum = null;
            for(MapLocation mi : crummy){
                if(closestCrum>mi.distanceSquaredTo(myLoc)) {
                    crum = mi;
                    closestCrum = mi.distanceSquaredTo(myLoc);
                }
            }
            if (rc.isMovementReady()) bugNav.move(crum);
            return true;
        }
        else if (roundNumber < 200-Math.max(rc.getMapHeight(), rc.getMapWidth())/2){
            MapLocation choice;
            choice = myLoc.add(allDirections[random.nextInt(8)]);
            bugNav.move(choice);
            return true;
        }
        else if (roundNumber < 200) {
            //minimize dam distance
            MapInfo[] dams = rc.senseNearbyMapInfos(2);
            for (MapInfo m:dams) {
                if (m.isDam() && m.getMapLocation().distanceSquaredTo(currentTarget) <= myLoc.distanceSquaredTo(currentTarget)) return true;
            }
            if (rc.isMovementReady()) {
                bugNav.move(currentTarget);
            }
        }
        return false;
    }
    public void movement() throws GameActionException {
        //if (roundNumber == 1000) rc.resign();
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

        int ret = 0; //IS THIS GOOD?
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

        if(weakestFriend!=null) {
            bugNav.move(weakestFriend);//I cant bfs cuz osmeimtes it moves perpenduclarly into enemy base
        }

        if(currentTarget!=null) {
            bugNav.move(currentTarget);
        }
    }
    public void premoveSetGlobals() throws GameActionException {
        myLoc = rc.getLocation();
        closeEnemyRobots = rc.senseNearbyRobots(4, rc.getTeam().opponent());
        closeFriendlyRobots = rc.senseNearbyRobots(4, rc.getTeam());
        enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        friendlyRobots = rc.senseNearbyRobots(-1, rc.getTeam());
        numberOfEnemies = enemyRobots.length;
        numberOfFriendlies = friendlyRobots.length;
        numberOfCloseEnemies = closeEnemyRobots.length;
        numberOfCloseFriends = closeFriendlyRobots.length;

        tooCloseToSpawn = false;
        if(roundNumber>200&&currentTarget!=null&&myLoc.distanceSquaredTo(currentTarget)>=closestSpawn.distanceSquaredTo(currentTarget)){
            tooCloseToSpawn = true;
        }

        int lowestHealth = 1000;
        weakestFriend = null;
        for(RobotInfo ri : friendlyRobots){
            if(ri.getHealth()<lowestHealth && myLoc.distanceSquaredTo(ri.getLocation()) > 2){ //> 2 or else will bugfind interfere ||
                lowestHealth = ri.getHealth();
                weakestFriend = ri.getLocation();
            }
        }

        int dist = Integer.MAX_VALUE;
        for(MapLocation spawn : spawnLocs){
            int cdist = myLoc.distanceSquaredTo(spawn);
            if(cdist<dist){
                dist = cdist;
                closestSpawn = spawn;
            }
        }

        closestEnemy = null;
        minEnemyDist = 100000;
        for (RobotInfo ri:enemyRobots) {
            MapLocation cval = ri.location;
            int ridist = cval.distanceSquaredTo(myLoc);
            if (ridist < minEnemyDist) {
                minEnemyDist = ridist;
                closestEnemy = cval;
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

    }
    public void postmoveSetGlobals() throws GameActionException { //a lot of the setGlobals are only used for movement, hence, diff function
        myLoc = rc.getLocation();
        closeEnemyRobots = rc.senseNearbyRobots(4, rc.getTeam().opponent());
        closeFriendlyRobots = rc.senseNearbyRobots(4, rc.getTeam());
        enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        friendlyRobots = rc.senseNearbyRobots(-1, rc.getTeam());
        numberOfEnemies = enemyRobots.length;
        numberOfFriendlies = friendlyRobots.length;
        numberOfCloseEnemies = closeEnemyRobots.length;
        numberOfCloseFriends = closeFriendlyRobots.length;
    }

    public void tryHeal() throws GameActionException {
        if (!rc.isActionReady()) return;
        MapLocation bestHeal = null;
        int bestScore = 0;
        int myHeal = rc.getHealAmount();
        for(RobotInfo i: closeFriendlyRobots){
            int hp = i.getHealth();
            if (hp == 1000) continue;
            int score = (1000 - hp);
            if (hp + myHeal > 750) {
                score += 250;
            }
            else if (hp + myHeal > 150) { //this number can be tuned
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
        }
    }
    
    public void buildSpawnTraps() throws GameActionException { //NOT USED RIGHT NOW
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

    int[] exploreDX = {3, 0, -3, 0, 6, 0, -6, 0, 9, 0, -9, 0}; //CHECK IF THIS ACTUALLY WORKS
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

    public void attackLogic() throws GameActionException {
        if (!rc.isActionReady()) return;
        MapLocation attackLoc = findBestAttackLocation();
        if(attackLoc!=null&&rc.canAttack(attackLoc)){
            rc.attack(attackLoc);
        }
        if (!(rc.getLevel(SkillType.ATTACK) == 6) || !rc.isActionReady()) return;
        attackLoc = findBestAttackLocation();
        if(attackLoc!=null&&rc.canAttack(attackLoc)){
            rc.attack(attackLoc);
        }
    }

    public void tryFill() throws GameActionException {
        if (!rc.isActionReady()) return;
        if (roundNumber > 0 && numberOfEnemies == 0) {
            MapInfo[] water = rc.senseNearbyMapInfos(2);
            for (MapInfo w:water) {
                MapLocation fillLoc = w.getMapLocation();
                if (w.isWater() && rc.canFill(fillLoc))  {
                    if ((fillLoc.x+fillLoc.y)%2==1 ) {
                        rc.fill(fillLoc);
                        return;
                    }

                    if (w.getCrumbs() > 30) {
                        rc.fill(fillLoc);
                        return;
                    }

                    if (rc.canSenseLocation(fillLoc.add(Direction.NORTH))) {
                        if (rc.senseMapInfo(fillLoc.add(Direction.NORTH)).isWall()) {
                            rc.fill(fillLoc);
                            return;
                        }   
                    }
                    if (rc.canSenseLocation(fillLoc.add(Direction.SOUTH))) {
                        if (rc.senseMapInfo(fillLoc.add(Direction.SOUTH)).isWall()) {
                            rc.fill(fillLoc);
                            return;
                        }   
                    }
                    if (rc.canSenseLocation(fillLoc.add(Direction.EAST))) {
                        if (rc.senseMapInfo(fillLoc.add(Direction.EAST)).isWall()) {
                            rc.fill(fillLoc);
                            return;
                        }   
                    }
                    if (rc.canSenseLocation(fillLoc.add(Direction.WEST))) {
                        if (rc.senseMapInfo(fillLoc.add(Direction.WEST)).isWall()) {
                            rc.fill(fillLoc);
                            return;
                        }   
                    }
                }
            }
        }
    }

    public boolean attackMicro() throws GameActionException {
        if (!rc.isMovementReady()) return true;
        if (numberOfEnemies == 0) return false;

        int cooldown = rc.getActionCooldownTurns();

        int mosthealth = 0;
        for(RobotInfo ri : closeFriendlyRobots){
            mosthealth = Math.max(mosthealth, ri.getHealth());
        }
        Direction oppdir = myLoc.directionTo(closestEnemy).opposite();
        MapLocation opposite = myLoc.add(oppdir).add(oppdir).add(oppdir);

        if(cooldown>=10&&rc.getHealth()<mosthealth&&!tooCloseToSpawn){
            BFSController.move(rc, opposite);
            //            BFSController.move(rc, opposite);
            if (!rc.isMovementReady()) return true;
            // if (rc.isMovementReady()) return false;
            // else return true;
        }

        int oneShot = 150 + roundNumber / 20;
        if (roundNumber >= 600) oneShot += 80;
        if((rc.getHealth() < oneShot || numberOfFriendlies < numberOfEnemies) && !tooCloseToSpawn){
            BFSController.move(rc, closestSpawn);
            if (!rc.isMovementReady()) return true;
        }

        //---------------------

        if (weakestEnemy != null) {
            if (cooldown < 10) { //WE WANT TO ATTACK / HEAL
                if (minEnemyDist > 4) {
                    if(rc.getLevel(SkillType.HEAL)>3){
                        return false;
                    }
                    BFSController.move(rc, weakestEnemy);
                    if (rc.isMovementReady() && numberOfFriendlies - numberOfEnemies > 4) {
                        bugNav.move(currentTarget);
                        return true;
                    }
                    else { //dont want to get stuck
                        return true;
                    }
                }else if(minEnemyDist<2&&!tooCloseToSpawn){
                    BFSController.move(rc, opposite);
                    return true;
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
                }
            }
            else if(cooldown<30){
                if (minEnemyDist < 12&&!tooCloseToSpawn) {
                    BFSController.move(rc, opposite);
                    return true;
                }else{
                    if(rc.getLevel(SkillType.HEAL)>3){
                        return false;
                    }
                    BFSController.move(rc, weakestEnemy); return true;
                }
            }else{
                BFSController.move(rc, opposite);
                return true;
            }
        }
        return false;
    }

    public MapLocation findBestAttackLocation() {
        int minHP = 1000000000;
        MapLocation ret = null;
        int myAtk = rc.getAttackDamage();

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
            if (i.hasFlag) {
                ret = enemyLoc;
                break;
            }
        }
        return ret;
    }


    public void checkBuildTraps() throws GameActionException{
        if(!rc.isActionReady()) return;
        if(roundNumber>200) {
            MapLocation nxt;
            int threshold2 = Math.max(2, 5 - rc.getCrumbs()/1000);

            Direction d1 = null, d2 = null, d3 = null;
            if (closestEnemy != null) {
                d1 = closestEnemy.directionTo(myFlags[0]);
                d2 = closestEnemy.directionTo(myFlags[1]);
                d3 = closestEnemy.directionTo(myFlags[2]);
                Direction toMyLoc = closestEnemy.directionTo(myLoc);
                if (!(toMyLoc == d1 || toMyLoc == d2 || toMyLoc == d3)) {
                    threshold2 += 5;
                } 
            }
            
            for (Direction d:allDirections) {
                nxt = myLoc.add(d);
                if (nxt.equals(myLoc) && nxt.equals(mirrorFlags[0]) || nxt.equals(mirrorFlags[1]) || nxt.equals(mirrorFlags[2])) {
                    if (rc.canBuild(TrapType.WATER, nxt) && rc.senseNearbyFlags(-1, rc.getTeam().opponent()).length == 0) {
                        rc.build(TrapType.WATER, nxt);
                    }
                }
                int num = rc.senseNearbyRobots(nxt, 8, rc.getTeam().opponent()).length;
                if(num>=threshold2){
                    MapInfo[] mp = rc.senseNearbyMapInfos(nxt, 4);
                    boolean stun = false;
                    for(MapInfo i: mp){
                        if (i.getTrapType()==TrapType.STUN) {
                            int dist = i.getMapLocation().distanceSquaredTo(nxt);
                            if (dist == 1 || dist == 4) {
                                stun = true;
                            }
                        }
                    }
                    if(!stun) {
                        if(rc.canBuild(TrapType.STUN, nxt)) {
                            rc.build(TrapType.STUN, nxt);
                        }
                    }
                }
            }
        }
    }
}