package bobthebuilder;
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

        movement();
        setGlobals(); //after we moved, globals are different
        attackLogic();
        tryHeal();
        checkBuildTraps();
        tryFill();

        boolean isBuilderNear = false;
        for (RobotInfo f:friendlyRobots) {
            if (f.buildLevel >= 4) {
                isBuilderNear = true;
            }
        }

        if (Clock.getBytecodesLeft() > 10000) {
            if (roundNumber > 50 && !isBuilderNear) {
                int closestReport = 10000;
                if (friendlyRobots.length >= 8 && enemyRobots.length >= 3) {
                    for (int i=0; i<cPtr; i++) {
                        int d = combatFronts[i].distanceSquaredTo(myLoc);
                        if (d < closestReport) {
                            closestReport = d;
                        }
                    }
                    if (closestReport > 25) {
                        //System.out.println("I'M NOTIFYING " + myLoc);
                        Comms.notifyCombatFronts(myLoc);
                        combatFronts[cPtr] = myLoc;
                        cPtr++;
                    }
                }
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
                    if (friendlyRobots.length > 6) return false;
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

        closestEnemy = null; 
        minEnemyDist = 10000;
        for (RobotInfo ri:enemyRobots) {
            if (ri.location.distanceSquaredTo(myLoc) < minEnemyDist) {
                minEnemyDist = ri.location.distanceSquaredTo(myLoc);
                closestEnemy = ri.location;
            }
        }
    }

    public void tryHealBuilder() throws GameActionException {
        if (!rc.isActionReady()) return;
        int myHeal = SkillType.HEAL.getSkillEffect(rc.getLevel(SkillType.HEAL));
        for (RobotInfo i:closeFriendlyRobots) {
            if (i.getBuildLevel() >= 4 && i.getHealth() + myHeal < 1000 && rc.canHeal(i.getLocation())) {
                rc.heal(i.getLocation());
                return;
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
        if (myMoveNumber != -1) return;
        MapInfo[] nearbyInfo = rc.senseNearbyMapInfos(6);
        int traps = 0;
        for (MapInfo mi:nearbyInfo) {
            if (mi.getTrapType() != TrapType.NONE) {
                traps++;
            }
        }
        if(rc.getRoundNum()>210) {
            if (enemyRobots.length > (6 - rc.getCrumbs()/3500) && closeEnemyRobots.length >= (2 - rc.getCrumbs()/1500)) {
                if (traps <= 1) {
                    Direction dirToE = myLoc.directionTo(closestEnemy);
                    if (rc.canBuild(TrapType.EXPLOSIVE, myLoc.add(dirToE))) {
                        rc.build(TrapType.EXPLOSIVE, myLoc.add(dirToE));
                    }
                    else if (rc.canBuild(TrapType.EXPLOSIVE, myLoc)) {
                        rc.build(TrapType.EXPLOSIVE, myLoc);
                    }
                    if (friendlyRobots.length >= 7) {
                        if (rc.canBuild(TrapType.STUN, myLoc.add(dirToE))) {
                            rc.build(TrapType.STUN, myLoc.add(dirToE));
                        }
                        else if (rc.canBuild(TrapType.STUN, myLoc)) {
                            rc.build(TrapType.STUN, myLoc);
                        }
                    }
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