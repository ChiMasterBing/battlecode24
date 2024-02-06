package moon2;
import battlecode.common.*;
import moon2.fast.FastLocSet;

import java.util.Random;

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
    }

    public void turn() throws GameActionException{
        premoveSetGlobals();
        callFriends();
        checkPickupFlag();

//        checkBuildTraps();

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
        if(rc.getHealth()<=chickenLevel||((rc.getLevel(SkillType.HEAL)!=3||rc.getLevel(SkillType.ATTACK)>3||myMoveNumber%3<2)&&numberOfEnemies==0)) {
            tryHeal();
        }

        movement();
        postmoveSetGlobals();
        callFriends();

//        checkBuildTraps();
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
        callDefense();
//        if(Clock.getBytecodesLeft()<5000){
//            System.out.println(Clock.getBytecodesLeft());
//        }
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
    public void callFriends() {
        if (isSwiper) return;
        if(friendlyRobots.length>7&&enemyRobots.length>7) {
            if(Comms.squadronMessages.size()>100){
                System.out.println("comms squadron size wayy too big");
//                rc.resign();
            }

            boolean alreadyExists = false;
            for(int i = 0; i< Comms.squadronMessages.size(); i++) {
                if(Comms.squadronMessages==null){
                    System.out.println("comms squadron messages null??");
//                    rc.resign();;
                }
                Integer message = Comms.squadronMessages.get(i);

                int cury = (message>>6)& Utils.BASIC_MASKS[5];
                int curx = (message>>11)&Utils.BASIC_MASKS[5];
                if(curx==myLoc.x/2&&cury==myLoc.y/2){
                    alreadyExists = true;
                    break;
                }
//                System.out.println(message);
            }
            if(!alreadyExists) {
                Comms.summonSquadron(myLoc, enemyRobots.length);
            }
        }
    }
    public MapLocation findCombatLocation(){
        if(friendlyRobots.length<=7||enemyRobots.length<=7){
            MapLocation ret = null;
            int rval = 0;
            if(Comms.squadronMessages.size()>100){
                System.out.println(Comms.squadronMessages.size());
            }
            for(int i = 0; i< Comms.squadronMessages.size(); i++){
                Integer message = Comms.squadronMessages.get(i);
                int cury = 2*((message>>6)&Utils.BASIC_MASKS[5]);
                int curx = 2*((message>>11)&Utils.BASIC_MASKS[5]);
                int cval=message&Utils.BASIC_MASKS[6];
                MapLocation cur = new MapLocation(curx, cury);
                if(ret==null||cur.distanceSquaredTo(myLoc)-cval<ret.distanceSquaredTo(myLoc)-rval){
                    rval = cval;
                    ret = cur;
                }
            }
            return ret;
        }else{
            return null;
        }
    }
    int[] myInfoUsed = {-1, -1, -1};
    public void callDefense() throws GameActionException {
        if(rc.getRoundNum()<200){
            return;
        }
        int score = Math.min(Math.max(numberOfFriendlies - 2 * numberOfEnemies + 8, 0), 15);
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

    public Boolean flagMovementLogic() throws GameActionException {
        if (rc.hasFlag()){
            for (Direction d:Direction.allDirections()) {
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
            if(myLoc.distanceSquaredTo(closestSpawn) > 9 && (tempObstacle == null || (rc.getHealth()<=chickenLevel||rc.canSenseRobotAtLocation(tempObstacle)&& rc.senseRobotAtLocation(tempObstacle) != null&&rc.senseRobotAtLocation(tempObstacle).getTeam()==rc.getTeam()))) {
                int dist = myLoc.distanceSquaredTo(closestSpawn);
                int bestHealth = rc.getHealth();
                RobotInfo best = null;
                RobotInfo[] superCloseFriendlyRobots = rc.senseNearbyRobots(8);
                int flagValue = rc.senseNearbyFlags(0)[0].getID();
                Comms.updateFlagID(flagValue);
                //int adjDist = -1;
                //int enemies = closeEnemyRobots.length;
                for(RobotInfo ri : superCloseFriendlyRobots) {
                    // int count = 0;
                    // for(RobotInfo bad : enemyRobots)
                    //     if(ri.getLocation().distanceSquaredTo(bad.getLocation()) > 4)
                    //         count++;
                    //if(ri.getLocation().distanceSquaredTo(myLoc) > adjDist)
                    //int robotHealth = ri.health;
                    int value = ri.getLocation().distanceSquaredTo(closestSpawn);
                    //if(robotHealth > bestHealth) {
                    if(value < dist) {
                        //if(count < enemies) {
                        //bestHealth = robotHealth;
                        dist = value;
                        //enemies = count;
                        Comms.updateRobotID(flagValue, ri.ID);
                        best = ri;
                    }
                }

                if(best != null) {
//                    System.out.println(best.ID);
                    MapLocation temp = myLoc.add(myLoc.directionTo(best.getLocation()));
                    if(rc.canDropFlag(temp)) {
                        rc.dropFlag(temp);
                    }
                }
            }

            bugNav.move(closestSpawn);

            if (spawnSet.contains(rc.getLocation())) {
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
                    if((enemyRobots.length < friendlyRobots.length-1) || (isSwiper && enemyRobots.length == 0)){
                        int cdist = i.getLocation().distanceSquaredTo(myLoc);
                        if(cdist<dist){
                            dist = cdist;
                            targ = i.getLocation();
                        }
                    }
                }else{ //if we picked up their flag
                    if(Comms.countFlagsCaptured()!=2) {
                        if (friendlyRobots.length >= 10) return false;
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
        if(targ!=null){
            bugNav.move(targ);
            return true;
        }
        return false;
    }
    public void checkPickupFlag() throws GameActionException {
        if(rc.getRoundNum()<200){
            if (myMoveNumber < 3 && roundNumber < 20) {
                for (FlagInfo f:flags) {
                    if (rc.canPickupFlag(f.getLocation()) && (f.getLocation().equals(myFlags[0]) || f.getLocation().equals(myFlags[1]) || f.getLocation().equals(myFlags[2]))) {
                        rc.pickupFlag(f.getLocation());
                    }
                }
            }
            return;
        }
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
                    if ((friendlyRobots.length + 1) - enemyRobots.length>0 && rc.getRoundNum() > 200) {
                        rc.pickupFlag(nxt);
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

    boolean seenTarget = false;
    public void hideFlag() throws GameActionException {
        MapLocation targ = Navigation.getFurthestSpawnFromEnemy(myFlags[0], myFlags[1], myFlags[2]);
        MapLocation closest = Navigation.getTripleMinLocation(targ, mirrorFlags[0], mirrorFlags[1], mirrorFlags[2]);
        Direction push = closest.directionTo(targ);
        targ = targ.add(push).add(push).add(push);

        if (roundNumber > 130 && !seenTarget) {
            //couldnt find a good location
            targ = myFlags[mySpawn];
        }
        
        if (myLoc.distanceSquaredTo(targ) <= 25 && roundNumber < 130) {
            seenTarget = true;
            Comms.commSeenFlagTarget(mySpawn);
        }

        if (roundNumber > 75) {
            MapLocation[] curs = Comms.getHiddenFlagLocations();
            //To do: deal with isolated spawns
            //check if current config is valid
            boolean allSeenTarget = Comms.hasSeenTarget(0) && Comms.hasSeenTarget(1) && Comms.hasSeenTarget(2);
            rc.setIndicatorString(String.valueOf(allSeenTarget));
            if (allSeenTarget && curs[0].distanceSquaredTo(curs[1]) >= 36 && curs[0].distanceSquaredTo(curs[2]) >= 36 && curs[2].distanceSquaredTo(curs[1]) >= 36) {
                //We gucci
                rc.setIndicatorDot(curs[0], 255, 0, 0);
                rc.setIndicatorDot(curs[1], 255, 0, 0);
                rc.setIndicatorDot(curs[2], 255, 0, 0);
                if (mySpawn == 0) {
                    if (rc.canDropFlag(curs[0]))
                        rc.dropFlag(curs[0]);
                } else if (mySpawn == 1) {
                    if (rc.canDropFlag(curs[1]))
                        rc.dropFlag(curs[1]);
                } else if (mySpawn == 2) {
                    if (rc.canDropFlag(curs[2]))
                        rc.dropFlag(curs[2]);
                }
            } else if (allSeenTarget) {
                MapLocation m1, m2;
                if (mySpawn == 0) {
                    m1 = curs[1]; m2 = curs[2];
                } else if (mySpawn == 1) {
                    m1 = curs[0]; m2 = curs[2];
                } else {
                    m1 = curs[0]; m2 = curs[1];
                }
                Direction bestDirection = null;
                int bestScore = 1000000;
                for (Direction d:allDirections) {
                    if (rc.canMove(d)) {
                        MapLocation nxt = myLoc.add(d);
                        int score = 36 - Math.min(nxt.distanceSquaredTo(m1), 36) + 36-Math.min(nxt.distanceSquaredTo(m2), 36);
                        if (score < bestScore) {
                            bestScore = score;
                            bestDirection = d;
                        }
                    }
                }
                if (bestDirection != null) {
                    rc.move(bestDirection);
                }
                Comms.dropFlagAtNewLocation(myLoc, mySpawn);
            } else {
                bugNav.move(targ);
            }     
        } else {
            bugNav.move(targ);
            Comms.dropFlagAtNewLocation(myLoc, mySpawn);
            rc.setIndicatorLine(myLoc, targ, 255, 0, 0);
        }
    }

    public void movement() throws GameActionException {
        if(roundNumber>200){
//            rc.resign();
            if(flagMovementLogic()) {
                explorePtr = 0;
                centerOfExploration = null;
                return;
            }
        } else {
            if (rc.hasFlag()) {
                hideFlag();
            }
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
            rc.setIndicatorLine(myLoc, lastCombatLoc, 255, 0, 0);
            bugNav.move(lastCombatLoc);
        }else if(currentTarget!=null)
            bugNav.move(currentTarget);
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
//            MapLocation dirToMe = cval.add(cval.directionTo(myLoc));
//            if(rc.canSenseLocation(dirToMe)&&rc.senseMapInfo(dirToMe).getTrapType()!=TrapType.NONE){
//                deadMeat++;
//            }
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
            int score = (1000 - hp);
//            if (hp + myHeal > 750) {
//                score += 250;
//            }
            if (hp + myHeal > 150) {
                score += 100;
            }
            score+=100*Math.max(i.getHealLevel(), Math.max(2*i.getAttackLevel(), i.getBuildLevel()));
            if(rc.getID()%3==2){
                score+=100;
            }
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

            // if (rc.senseMapInfo(myLoc).getTeamTerritory() != rc.getTeam()) {

            // }
            // if (closestEnemy != null && rc.canSenseLocation(closestEnemy) && rc.senseMapInfo(myLoc).getTeamTerritory() != rc.senseMapInfo(closestEnemy).getTeamTerritory()) {
            //     threshold2 = 0;
            // }
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
            if (broadcastLocations.length>0 && broadcastLocations[0] != null) {
                if (myMoveNumber >= 3) {
                    currentTarget = broadcastLocations[0];
                } else { //try to snipe some flags :>
                    isSwiper = true;
                    if (broadcastLocations.length == 3) {
                        currentTarget = broadcastLocations[myMoveNumber%2 + 1];
                    } else if (broadcastLocations.length == 2) {
                        currentTarget = broadcastLocations[1];
                    } else if (broadcastLocations.length == 1) {
                        currentTarget = broadcastLocations[0];
                    } else {
                        isSwiper = false;
                    }
                    if (myLoc.equals(currentTarget) || explorePtr != 0) {
                        if (centerOfExploration == null) {
                            centerOfExploration = currentTarget;
                        }
                        currentTarget = exploreTarget();
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
    public void chickenBehavior() throws GameActionException{
        int bestdirVal = -100000000;
        Direction bestDirection = Direction.CENTER;
        for(Direction d: allDirections){
            if(rc.canMove(d)){
                MapLocation nxt = myLoc.add(d);
                int enemies = 0;
                int numcloseenemies = 0;
                for(RobotInfo ri: enemyRobots){
                    if(ri.getLocation().distanceSquaredTo(nxt)<=4) {
                        numcloseenemies++;
                    }else if(ri.getLocation().distanceSquaredTo(nxt)<11){//or 12
                        enemies++;
                    }
                }
                int friendlies = 0;
                for(RobotInfo ri: friendlyRobots){
                    if(ri.getLocation().distanceSquaredTo(nxt)<=4){
                        friendlies+=8;
                    }else if(ri.getLocation().distanceSquaredTo(nxt)<12){
                        friendlies+=6;
                    }else{
//                        friendlies+=4;
                    }
                }

                int curDirVal = friendlies-20*enemies-numcloseenemies*32;
                if(numcloseenemies==0){
                    curDirVal+=50;
                }

                if(bestdirVal<curDirVal) {
                    bestdirVal = curDirVal;
                    bestDirection = d;
                }
            }
        }
        if(rc.canMove(bestDirection)){
            rc.move(bestDirection);
        }
    }
    public boolean attackMicro() throws GameActionException {
        if (!rc.isMovementReady()) return true;
        if (numberOfEnemies == 0) return false;
        if(rc.getHealth()<=chickenLevel){
            chickenBehavior();
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
        if(rc.getLevel(SkillType.HEAL)>3){
            return AttackerMicro.processTurn(enemyRobots, friendlyRobots, closeEnemyRobots, closeFriendlyRobots);
        }else {
            return AttackerMicro.processTurn(enemyRobots, friendlyRobots, closeEnemyRobots, closeFriendlyRobots);
        }
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