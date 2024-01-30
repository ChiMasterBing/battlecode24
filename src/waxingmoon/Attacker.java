package waxingmoon;
import battlecode.common.*;
import waxingmoon.fast.FastLocSet;

import java.util.Arrays;
import java.util.Random;

//no ducks in jail
//stop spawning on overwhelmed

public class Attacker extends Robot {
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
    boolean tooCloseToSpawn = false;
    MapLocation lastCombatLoc = null;
    int prevEnemies = 0;
    int numberOfEnemies, numberOfFriendlies, numberOfCloseEnemies, numberOfCloseFriends;
    MapLocation lastEnemyLocation; //I was going to do something with this but forgot
    boolean isSwiper = false; //SWIPER NO SWIPING
    int chickenLevel = 450;

    MapLocation centerOfExploration = null;

    public Attacker(RobotController rc) throws GameActionException {
        super(rc);
        for(MapLocation spawn : spawnLocs){
            spawnSet.add(spawn);
        }
        AttackerMicro.init(rc);
        HealerMicro.init(rc);
        ChickenMicro.init(rc);
    }

    public void turn() throws GameActionException{
        premoveSetGlobals();
        callFriends();
        checkPickupFlag();

        checkBuildTraps();

        updateCurrentTarget();
        prevEnemies--;
        if(numberOfEnemies>0){
            prevEnemies = 1;
        }
        if(rc.getLevel(SkillType.HEAL)>3){
            tryHeal();
        }
        if(rc.getLevel(SkillType.HEAL)<=3){
            attackLogic();
            attackLogic();
        }
        if(rc.getID()%3<2&&rc.getLevel(SkillType.HEAL)>2) {
            if (rc.getHealth() <= chickenLevel || ((rc.getLevel(SkillType.HEAL) != 3 || rc.getLevel(SkillType.ATTACK) > 3 || myMoveNumber % 3 < 2) && numberOfEnemies == 0)) {
                tryHeal();
            }
        }

        movement();
        postmoveSetGlobals();
        callFriends();

        checkBuildTraps();
        if(rc.getLevel(SkillType.HEAL)>3){
            tryHeal();
        }
        attackLogic();
        attackLogic();
        if(Math.max(rc.getLevel(SkillType.ATTACK), rc.getLevel(SkillType.HEAL))>3){
            chickenLevel = 450+150*(Math.max(rc.getLevel(SkillType.ATTACK), rc.getLevel(SkillType.HEAL))-3);
        }
        if(rc.getLevel(SkillType.ATTACK)>3||(rc.getLevel(SkillType.HEAL)<=3&&rc.getID()%3==0)) {
            if (rc.getHealth()<=chickenLevel||closestEnemy == null || enemyRobots.length==0) {
                tryHeal();
            }
        }else{
            if (closestEnemy == null || myLoc.distanceSquaredTo(closestEnemy) > 12 || rc.getHealth() <= chickenLevel) {
                tryHeal();
            }
        }
        tryFill();
//        if(Clock.getBytecodesLeft()<5000){
//            System.out.println(Clock.getBytecodesLeft());
//        }
        buildSpawnTraps();
    }

    public void deadFunctions() throws GameActionException {

    }

    boolean previouslyDistressFlag = false;
    public void callFriends() {
        boolean isFlagPickedUp = false; 
        for (FlagInfo f:flags) {
            if (f.getTeam() == rc.getTeam()) {
                if (numberOfEnemies >= numberOfFriendlies + 1) {
                    isFlagPickedUp = true;
                    previouslyDistressFlag = true;
                }
            } 
        }

        if (previouslyDistressFlag && numberOfEnemies > 0) {
            isFlagPickedUp = true;
            previouslyDistressFlag = true;
        }
        else if (!isFlagPickedUp) {
            previouslyDistressFlag = false;
        }

        if (isSwiper && !isFlagPickedUp) return;

        if (numberOfCloseEnemies > numberOfFriendlies) return; //GIVE UP

        ///CLOSEST ENEMY =  NULL
        MapLocation callTarget = closestEnemy;
        if (callTarget == null) callTarget = myLoc;

        if(isFlagPickedUp || (friendlyRobots.length>5 && enemyRobots.length>7 && friendlyRobots.length < 15)) {
            if(Comms.squadronMessages.size()>100) {
                System.out.println("comms squadron size wayy too big");
            }

            boolean alreadyExists = false;
            for(int i = 0; i<Comms.squadronMessages.size(); i++) {
                if(Comms.squadronMessages==null){
                    System.out.println("comms squadron messages null??");
                }
                Integer message = Comms.squadronMessages.get(i);

                int cury = (message>>6)&Utils.BASIC_MASKS[5];
                int curx = (message>>11)&Utils.BASIC_MASKS[5];
                if(curx==callTarget.x/2&&cury==callTarget.y/2){
                    alreadyExists = true;
                    break;
                }
//                System.out.println(message);
            }
            if(!alreadyExists) {
                if (!isFlagPickedUp)
                    Comms.summonSquadron(callTarget, enemyRobots.length);
                else 
                    Comms.summonSquadron(callTarget, 31); //BIG DANGER
            }
        }
    }
    public MapLocation findCombatLocation(){
        if(friendlyRobots.length<=7||enemyRobots.length<=7){
            MapLocation ret = currentTarget;
            int rval = 0;
            if(Comms.squadronMessages.size()>100){
                System.out.println(Comms.squadronMessages.size());
            }
            for(int i = 0; i<Comms.squadronMessages.size(); i++){
                Integer message = Comms.squadronMessages.get(i);
                int cury = 2*((message>>6)&Utils.BASIC_MASKS[5]);
                int curx = 2*((message>>11)&Utils.BASIC_MASKS[5]);
                int cval=message&Utils.BASIC_MASKS[6];
                MapLocation cur = new MapLocation(curx, cury);
                //rc.setIndicatorLine(myLoc, cur, 255, 0, 0);
                if(ret==null || cur.distanceSquaredTo(myLoc)-2*cval < ret.distanceSquaredTo(myLoc)-2*rval){
                    rval = cval;
                    ret = cur;
                }
            }
            return ret;
        }else{
            return null;
        }
    }

    public Boolean flagMovementLogic() throws GameActionException {
        if (roundNumber <= 200) return false;
        heldFlagLastTurn = null;
        //THIS IS NULL WHAT?
        //follow not close
        if (rc.hasFlag()){
            for (FlagInfo f:flags) {
                if (f.getLocation().equals(myLoc) && f.isPickedUp()) {
                    heldFlagLastTurn = f; break;
                }
            }

            if (numberOfFriendlies == 0 && numberOfCloseEnemies > 0 && myLoc.distanceSquaredTo(closestSpawn) > 100) {
                rc.dropFlag(myLoc);
                Comms.writeEnemyFlagStatus(heldFlagLastTurn, 1);
            }

            for (Direction d:allDirections) {
                if (spawnSet.contains(myLoc.add(d)) && rc.canMove(d)) {
                    rc.move(d);
                    if (spawnSet.contains(rc.getLocation())) {
                        Debug.println("I DEPOSITED FLAG WOO!");
                        Comms.depositFlag(heldFlagLastTurn.getID());
                    }
                    return true;
                }
            }

            MapLocation tempObstacle = bugNav.lastObstacleFound;
            if(myLoc.distanceSquaredTo(closestSpawn) > 9 && (tempObstacle == null || (rc.getHealth()<=chickenLevel||rc.canSenseRobotAtLocation(tempObstacle)&& rc.senseRobotAtLocation(tempObstacle) != null&&rc.senseRobotAtLocation(tempObstacle).getTeam()==rc.getTeam()))) {
                int dist = myLoc.distanceSquaredTo(closestSpawn);
                RobotInfo best = null;
                int flagValue = rc.senseNearbyFlags(0)[0].getID();
                Comms.updateFlagID(flagValue);
                for(RobotInfo ri : rc.senseNearbyRobots(8)) {
                    int value = ri.getLocation().distanceSquaredTo(closestSpawn);
                    if(value < dist) {
                        dist = value;
                        Comms.updateRobotID(flagValue, ri.ID);
                        best = ri;
                    }
                }
                if(best != null && (!isSwiper || numberOfFriendlies > 15)) {
                    MapLocation temp = myLoc.add(myLoc.directionTo(best.getLocation()));
                    if(rc.canDropFlag(temp)) {
                        rc.dropFlag(temp);
                        Comms.writeEnemyFlagStatus(heldFlagLastTurn, 1);
                    }
                }
            }

            if (!isSwiper) bugNav.move(closestSpawn);
            else {
                if (closestSpawn.distanceSquaredTo(myLoc) < 225) bugNav.move(closestSpawn);
                else bugNav.move(myFlags[mySpawn]); //try and flank;
            }
            
            if (spawnSet.contains(rc.getLocation())) { //this should never happen?
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
                        Comms.writeEnemyFlagStatus(i, 2);
                        heldFlagLastTurn = i;
                        bugNav.move(closestSpawn);
                        return true;
                    }
                }
            }
        }

        int dist = 10000000;
        MapLocation targ = null;

        for(FlagInfo i : flags) {
            if(i.getTeam()==rc.getTeam()){ //if opp has our flag
                if(i.isPickedUp()){
                    int cdist = i.getLocation().distanceSquaredTo(myLoc)-100000000;
                    if(cdist<dist){
                        dist = cdist;
                        targ = i.getLocation();
                    }
                } else if (!i.getLocation().equals(myFlags[0]) && !i.getLocation().equals(myFlags[1]) && !i.getLocation().equals(myFlags[2])) {
                    targ = i.getLocation();
                }
            }else {
                if(!i.isPickedUp()){ //if their flag aint picked up
                    int threshold;
                    if (roundNumber > 1500) threshold = 0;
                    else if (roundNumber > 1000) threshold = 1;
                    else threshold = 3;     

                    if((friendlyRobots.length - enemyRobots.length >= threshold) || (isSwiper && enemyRobots.length == 0)){
                        int cdist = i.getLocation().distanceSquaredTo(myLoc);
                        if(cdist<dist){
                            dist = cdist;
                            targ = i.getLocation();
                        }
                    }
                }else{ //if we picked up their flag
                    if(Comms.countFlagsCaptured()!=2) {
                        if (friendlyRobots.length >= 12) return false;
                    }
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
        if(targ!=null && rc.isMovementReady()){
            bugNav.move(targ);
            return true;
        }
        return false;
    }
    
    public void checkPickupFlag() throws GameActionException {
        if(rc.getRoundNum()<=200) return;

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
                    if ((friendlyRobots.length + 1) - enemyRobots.length > 0 && rc.getRoundNum() > 200) {
                        rc.pickupFlag(nxt);
                        Comms.writeEnemyFlagStatus(i, 2);
                    }
                }
            }
        }
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
        if (crummy.length > 0 && roundNumber < 250 && (crummy.length > (numberOfFriendlies+1) || roundNumber > 200)) {
            if (rc.isMovementReady()) bugNav.move(crum);
            return true;
        }
        else if(roundNumber < 200-Math.max(rc.getMapHeight(), rc.getMapWidth())/1.5){
            MapLocation choice;
            choice = myLoc.add(allDirections[random.nextInt(8)]);
            bugNav.move(choice);
            return true;
        }
        else if (roundNumber < 200) { //minimize dam distance
            MapInfo[] dams = rc.senseNearbyMapInfos(2);
            for (MapInfo m:dams) {
                if (m.isDam() && m.getMapLocation().distanceSquaredTo(currentTarget) <= myLoc.distanceSquaredTo(currentTarget)) return true;
            }

            if (rc.isMovementReady()) bugNav.move(currentTarget);
        }
        return false;
    }

    public void movement() throws GameActionException {
        if(flagMovementLogic()) {
            explorePtr = 0;
            centerOfExploration = null;
            return;
        }
 
        if(crumbMovementLogic()) return;

        if (attackMicro()) {
            explorePtr = 0;
            centerOfExploration = null;
            return;
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

        if(lowestHealthLoc!=null&&(rc.getLevel(SkillType.HEAL)>3||rc.getID()%3<2)){
            bugNav.move(lowestHealthLoc);//I cant bfs cuz osmeimtes it moves perpenduclarly into enemy base
        }
        if(enemyRobots.length==0 && !isSwiper) {
            MapLocation combatLoc = findCombatLocation();
            if (combatLoc != null) {
                lastCombatLoc = combatLoc;
            }
        }
        if(lastCombatLoc!=null&&myLoc.distanceSquaredTo(lastCombatLoc)<20||enemyRobots.length>0){
            lastCombatLoc = null;
        }
        if(lastCombatLoc!=null){
            bugNav.move(lastCombatLoc);
        }else if(currentTarget!=null) {
            MapInfo myInfo = rc.senseMapInfo(myLoc);
            if (myInfo.getTeamTerritory() == rc.getTeam().opponent() && rc.getHealth() < 900 && friendlyRobots.length > 2) return;
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
            int score = (1000 - hp);
//            if (hp + myHeal > 750) {
//                score += 250;
//            }
            if (hp + myHeal > chickenLevel) {
               score += 100;
            }
//            score+=100*Math.max(i.getHealLevel(), Math.max(2*i.getAttackLevel(), i.getBuildLevel()));
//            if(rc.getID()%3==2){
//                score+=100;
//            }
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

    int prevTurnCrumbs = 0;
    public void checkBuildTraps() throws GameActionException{
        if(!rc.isActionReady()) return;

        if(roundNumber>200) {
            MapLocation nxt;
            int threshold = Math.max(2, 5 - rc.getCrumbs()/1000);
            int threshold2 = Math.max(2, 5-rc.getCrumbs()/1000);
            Direction d1 = null, d2 = null, d3 = null;
            if (closestEnemy != null) {
                d1 = closestEnemy.directionTo(myFlags[0]);
                d2 = closestEnemy.directionTo(myFlags[1]);
                d3 = closestEnemy.directionTo(myFlags[2]);
                Direction toMyLoc = closestEnemy.directionTo(myLoc);
                if (!(toMyLoc == d1 || toMyLoc == d2 || toMyLoc == d3)) {
                    threshold += 5;
                    threshold2 += 5;
                }
            }


            for (Direction d:allDirections) {
                nxt = myLoc.add(d);
                // if (nxt.equals(myLoc) && nxt.equals(mirrorFlags[0]) || nxt.equals(mirrorFlags[1]) || nxt.equals(mirrorFlags[2])) {
                //     if (rc.canBuild(TrapType.WATER, nxt) && rc.senseNearbyFlags(-1, rc.getTeam().opponent()).length == 0) {
                //         rc.build(TrapType.WATER, nxt);
                //     }
                // }

                int t2 = threshold2;
                if(rc.senseMapInfo(myLoc).getTeamTerritory()!=rc.getTeam()){
                    t2= threshold;
                }
                int num = rc.senseNearbyRobots(nxt, 8, rc.getTeam().opponent()).length;
                if(num>=t2){
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
        if (roundNumber > 202)
            prevTurnCrumbs = rc.getCrumbs();
    }
    public void buildSpawnTraps() throws GameActionException {
        Direction[] diagonal = {Direction.NORTHEAST, Direction.NORTHWEST, Direction.SOUTHEAST, Direction.SOUTHWEST};
        if(myLoc.distanceSquaredTo(myFlags[0])<3){
            if(flags.length==0){
                return;
            }

            MapLocation centerLoc = myFlags[0];
            // if(myLoc.distanceSquaredTo(centerLoc)>0){
            //     bugNav.move(myLoc);
            // }
            // if(myLoc.distanceSquaredTo(centerLoc)>0&&rc.senseMapInfo(centerLoc).getTrapType()==TrapType.NONE){
            //     bugNav.move(myLoc);
            //     return;
            // }
            // for(Direction d: diagonal){
            //     if(rc.canBuild(TrapType.STUN, centerLoc.add(d))){
            //         rc.build(TrapType.STUN, centerLoc.add(d));
            //     }
            // }
            if(rc.canBuild(TrapType.STUN, centerLoc)){
                rc.build(TrapType.STUN, centerLoc);
            }

        }else if(myLoc.distanceSquaredTo(myFlags[1])<3){
            //System.out.println("trying to build spawn traps");

            if(flags.length==0){
                return;
            }

            MapLocation centerLoc = myFlags[1];
            // if(myLoc.distanceSquaredTo(centerLoc)>0&&rc.senseMapInfo(centerLoc).getTrapType()==TrapType.NONE){
            //     bugNav.move(myLoc);
            //     return;
            // }

            // for(Direction d: diagonal){
            //     if(rc.canBuild(TrapType.STUN, centerLoc.add(d))){
            //         rc.build(TrapType.STUN, centerLoc.add(d));
            //     }
            // }
            if(rc.canBuild(TrapType.STUN, centerLoc)){
                rc.build(TrapType.STUN, centerLoc);
            }
        }else if(myLoc.distanceSquaredTo(myFlags[2])<3){
            //System.out.println("trying to build spawn traps");

            if(flags.length==0){
                return;
            }
            MapLocation centerLoc = myFlags[2];
            // if(myLoc.distanceSquaredTo(centerLoc)>0&&rc.senseMapInfo(centerLoc).getTrapType()==TrapType.NONE){
            //     bugNav.move(myLoc);
            //     return;
            // }
            // for(Direction d: diagonal){
            //     if(rc.canBuild(TrapType.STUN, centerLoc.add(d))){
            //         rc.build(TrapType.STUN, centerLoc.add(d));
            //     }
            // }
            if(rc.canBuild(TrapType.STUN, centerLoc)){
                rc.build(TrapType.STUN, centerLoc);
            }
        }
    }

    int[] exploreDX = {3, 0, -3, 0, 6, 0, -6, 0, 9, 0, -9, 0};
    int[] exploreDY = {0, 3, 0, -3, 0, 6, 0, -6, 0, 9, 0, -9};
    int explorePtr = 0;
    public MapLocation exploreTarget() throws GameActionException {
        //we've reached our target but see no flag... now what?
        if (explorePtr >= exploreDX.length) return centerOfExploration;
        MapLocation newTarget = centerOfExploration.translate(exploreDX[explorePtr], exploreDY[explorePtr]);
        if (myLoc.equals(newTarget)) {
            explorePtr++;
            return exploreTarget();
        }

        rc.setIndicatorString("exploring " + centerOfExploration + " " + newTarget);
        rc.setIndicatorLine(myLoc, newTarget, 0, 0, 255);
        MapLocation res = newTarget;
        if (!rc.onTheMap(res)) {
            explorePtr++;
            return exploreTarget();
        }
        return res;
    }

    public void updateCurrentTarget() throws GameActionException {
        if (roundNumber < 200) {
            int idx = Navigation.getClosestSpawnNumber(myLoc, mirrorFlags[0], mirrorFlags[1], mirrorFlags[2]);
            currentTarget = mirrorFlags[idx]; //by symmetry
        } else {
            if (myMoveNumber < 3) {
                isSwiper = true;
                int choice = -1;
                if (broadcastLocations.length == 3) {
                    choice = myMoveNumber%3;
                } else if (broadcastLocations.length == 2) {
                    choice = myMoveNumber%2;
                } else if (broadcastLocations.length == 1) {
                    choice = 0;
                }

                if (choice == -1) {
                    isSwiper = false;
                } else {
                    if (Comms.getEnemyFlagLocation(choice).x < 70 && Comms.getEnemyFlagStatus(choice) == 1) {
                        currentTarget = Comms.getEnemyFlagLocation(choice);
                    } else {
                        currentTarget = broadcastLocations[choice];
                    }
                }

                if (myLoc.equals(currentTarget) || explorePtr != 0) {
                    if (centerOfExploration == null) {
                        centerOfExploration = currentTarget;
                    }
                    currentTarget = exploreTarget();
                }
            } else {
                if (myLoc.distanceSquaredTo(Comms.getEnemyFlagLocation(0)) == 0 && flags.length == 0) {
                    if (Comms.readFlagStatus(0) == 1)
                        Comms.writeEnemyFlagStatusByNumber(0, 2);
                }
                if (myLoc.distanceSquaredTo(Comms.getEnemyFlagLocation(1)) == 0 && flags.length == 0) {
                    if (Comms.readFlagStatus(1) == 1)
                        Comms.writeEnemyFlagStatusByNumber(1, 2);
                }
                if (myLoc.distanceSquaredTo(Comms.getEnemyFlagLocation(2)) == 0 && flags.length == 0) {
                    if (Comms.readFlagStatus(2) == 1)
                        Comms.writeEnemyFlagStatusByNumber(2, 2);
                }

                if (Comms.getEnemyFlagLocation(0).x < 70 && Comms.getEnemyFlagStatus(0) == 1) {
                    currentTarget = Comms.getEnemyFlagLocation(0);
                }
                else if (Comms.getEnemyFlagLocation(1).x < 70 && Comms.getEnemyFlagStatus(1) == 1) {
                    currentTarget = Comms.getEnemyFlagLocation(1);
                }
                else if (Comms.getEnemyFlagLocation(2).x < 70 && Comms.getEnemyFlagStatus(2) == 1) {
                    currentTarget = Comms.getEnemyFlagLocation(2);
                } else if (broadcastLocations.length > 0 && broadcastLocations[0] != null) {
                    currentTarget = broadcastLocations[0];
                }
            }
            // if (Comms.countFlagsCaptured() == 2 && broadcastLocations.length == 0) {
            //     currentTarget = mirrorFlags[myMoveNumber % 3];
            // }
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
    }

    public void tryFill() throws GameActionException {
        if (!rc.isActionReady()) return;
        if (roundNumber > 0 && numberOfCloseEnemies == 0) { //FIX THIS LATER
            MapInfo[] water = rc.senseNearbyMapInfos(2);
            for (MapInfo w:water) {
                MapLocation fillLoc = w.getMapLocation();
                if (w.isWater() && rc.canFill(fillLoc))  {
                    // if ((fillLoc.x+fillLoc.y)%2==1 ) {
                    //     rc.fill(fillLoc);
                    //     return;
                    // }
                    if (w.getCrumbs() > 30) {
                        rc.fill(fillLoc);
                        return;
                    }

                    int NORTHSOUTH = 0, EASTWEST = 0;
                    if (rc.canSenseLocation(fillLoc.add(Direction.NORTH)) && fillLoc.add(Direction.NORTH).isAdjacentTo(myLoc)) {
                        if (!rc.senseMapInfo(fillLoc.add(Direction.NORTH)).isPassable()) {
                            NORTHSOUTH++;
                        }
                    }

                    if (rc.canSenseLocation(fillLoc.add(Direction.SOUTH)) && fillLoc.add(Direction.SOUTH).isAdjacentTo(myLoc)) {
                        if (!rc.senseMapInfo(fillLoc.add(Direction.SOUTH)).isPassable() || !rc.onTheMap(fillLoc.add(Direction.SOUTH))) {
                            NORTHSOUTH++;
                        }
                    }

                    if (rc.canSenseLocation(fillLoc.add(Direction.EAST)) && fillLoc.add(Direction.EAST).isAdjacentTo(myLoc)) {
                        if (!rc.senseMapInfo(fillLoc.add(Direction.EAST)).isPassable() || !rc.onTheMap(fillLoc.add(Direction.EAST))) {
                            EASTWEST++;
                        }
                    }
                    if (rc.canSenseLocation(fillLoc.add(Direction.WEST)) && fillLoc.add(Direction.WEST).isAdjacentTo(myLoc)) {
                        if (!rc.senseMapInfo(fillLoc.add(Direction.WEST)).isPassable() || !rc.onTheMap(fillLoc.add(Direction.WEST))) {
                            EASTWEST++;
                        }
                    }

                    if (!rc.onTheMap(fillLoc.add(Direction.NORTH)) && fillLoc.add(Direction.NORTH).isAdjacentTo(myLoc)) {
                        NORTHSOUTH++;
                    }
                    if (!rc.onTheMap(fillLoc.add(Direction.SOUTH)) && fillLoc.add(Direction.SOUTH).isAdjacentTo(myLoc)) {
                        NORTHSOUTH++;
                    }
                    if (!rc.onTheMap(fillLoc.add(Direction.EAST)) && fillLoc.add(Direction.EAST).isAdjacentTo(myLoc)) {
                        EASTWEST++;
                    }
                    if (!rc.onTheMap(fillLoc.add(Direction.WEST)) && fillLoc.add(Direction.WEST).isAdjacentTo(myLoc)) {
                        EASTWEST++;
                    }

                    if (NORTHSOUTH + EASTWEST >= 2) {
                        rc.fill(fillLoc);
                    }
                }
            }
        }
    }
    
    public boolean attackMicro() throws GameActionException {
        if (!rc.isMovementReady()) return true;

        int realNumberOfEnemies = numberOfEnemies;
        

        if (realNumberOfEnemies != numberOfEnemies && roundNumber > 210) {
            rc.resign();
        } 

        if (numberOfEnemies == 0) return false;
        
        if(rc.getHealth()<=chickenLevel){
            ChickenMicro.processTurn(enemyRobots, friendlyRobots, closeEnemyRobots, closeFriendlyRobots);
            return true;
        }

        int cooldown = rc.getActionCooldownTurns();

        int mosthealth = 0;
        for(RobotInfo ri : closeFriendlyRobots){
            mosthealth = Math.max(mosthealth, ri.getHealth());
        }
        Direction oppdir = myLoc.directionTo(closestEnemy).opposite();
        MapLocation opposite = myLoc.add(oppdir).add(oppdir).add(oppdir);

        if (cooldown >= 10 && rc.getHealth() < mosthealth && !tooCloseToSpawn) {
            BFSController.move(rc, opposite);
            //            BFSController.move(rc, opposite);
            if (!rc.isMovementReady()) return true;
            // if (rc.isMovementReady()) return false;
            // else return true;
        }

        // if(rc.senseMapInfo(myLoc).getTeamTerritory()==rc.getTeam().opponent()&&numberOfFriendlies<5&&numberOfEnemies+2<numberOfFriendlies){
        //     weakestEnemy = myLoc;
        //     if(!rc.isMovementReady()) return true;
        // }

        // if(cooldown>=10&&enemyRobots.length>friendlyRobots.length&&rc.senseMapInfo(myLoc).getTeamTerritory()==rc.getTeam().opponent()){
        //     bugNav.move(closestSpawn);
        // }
        if(rc.getLevel(SkillType.HEAL)>3&&rc.getHealth()<1000){
            return HealerMicro.processTurn(enemyRobots, friendlyRobots, closeEnemyRobots, closeFriendlyRobots);
        }else {
            return AttackerMicro.processTurn(enemyRobots, friendlyRobots, closeEnemyRobots, closeFriendlyRobots);
        }
    }


    public MapLocation findBestAttackLocation() {
        int minHP = 1000000000;
        MapLocation ret = null;
        int myAtk = rc.getAttackDamage();

        boolean canKill = false;

        for(RobotInfo i : closeEnemyRobots){
            MapLocation enemyLoc = i.getLocation();

            if (i.hasFlag) {
                ret = enemyLoc; break;
            }

            int cval = Math.max(i.getHealth(), myAtk);
            if (cval <= myAtk) {
                if (i.getBuildLevel() >= 5) cval -= 15; //prioritize pro builders
                else cval -= Math.max(3 * i.getAttackLevel(), 2 * i.getHealLevel());

                if (!canKill) { //this is the first one where I can kill
                    minHP = cval; ret = enemyLoc;
                } else if (cval < minHP) { //i can already kill someone: see if this is better
                    minHP = cval; ret = enemyLoc;
                }
                canKill = true;
            }
            if (cval > myAtk && !canKill) { 
                if (i.getBuildLevel() >= 5) cval -= 15; //prioritize pro builders
                else cval -= Math.max(3 * i.getAttackLevel(), 2 * i.getHealLevel());
                
                if (cval < minHP) {
                    minHP = cval; ret = enemyLoc;
                }
            }
        }

        return ret;
    }
}