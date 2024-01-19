package bobthebuilder;
import battlecode.common.*;
import bobthebuilder.fast.FastLocSet;
import java.util.Random;
import java.util.Arrays;
import java.util.Map;


public class Builder extends Robot{
    MapLocation[] broadcastLocations = {};
    MapLocation[] spawnLocs;
    MapInfo[] nearbyInfo;
    MapLocation myLoc, closestSpawn, currentTarget, closestAttacker;
    FastLocSet spawnSet = new FastLocSet();
    Random random = new Random();
    RobotInfo[] enemyRobots, friendlyRobots, closeEnemyRobots, closeFriendlyRobots;
    int[] attackUpgrade = {150, 158, 165,  173, 180, 195, 225};
    public Builder(RobotController rc) throws GameActionException {
        super(rc);
        spawnLocs = rc.getAllySpawnLocations();
    }

    public void setGlobals() throws GameActionException{
        nearbyInfo = rc.senseNearbyMapInfos(-1);

        myLoc = rc.getLocation();
        enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        closeEnemyRobots = rc.senseNearbyRobots(4, rc.getTeam().opponent());
        closeFriendlyRobots = rc.senseNearbyRobots(4, rc.getTeam());
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
        closestAttacker = closestAttacker();
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
    public void buildBest(int explosiveTraps, MapLocation trapLocation) throws GameActionException {
        if(explosiveTraps>=4||rc.senseNearbyRobots(trapLocation, 8, rc.getTeam()).length<Math.max(5+rc.getCrumbs()/500, 2)){
            return;
        }
        if(rc.canBuild(TrapType.EXPLOSIVE, trapLocation)){
            rc.build(TrapType.EXPLOSIVE, trapLocation);
        }
    }
    public MapLocation closestAttacker(){
        MapLocation closest = null; int minEnemyDist = 10000;
        for (RobotInfo ri:enemyRobots) {
            if (ri.location.distanceSquaredTo(myLoc) < minEnemyDist) {
                minEnemyDist = ri.location.distanceSquaredTo(myLoc);
                closest = ri.location;
            }
        }

        return closest;
    }
    public void attemptBuildTraps(boolean explosive) throws GameActionException {
        if(roundNumber<=200) {
            return;
        }
        int explosiveTraps = 0;

        MapInfo[] nearbyInfo = rc.senseNearbyMapInfos(myLoc, 16);
        for (MapInfo mi:nearbyInfo) {
            if (mi.getTrapType() == TrapType.EXPLOSIVE) {
                explosiveTraps++;
            }
        }

        int threshold = Math.max(2, 5- rc.getCrumbs()/250);
        for(Direction d: allDirections){
            MapLocation nxt = myLoc.add(d);
            if(nxt.x%3==1&&nxt.y%3==1) {
                if (closeFriendlyRobots.length >= threshold) {
                    if (rc.canBuild(TrapType.STUN, nxt)) {
                        rc.build(TrapType.STUN, nxt);
                    }
                }
            }
            if(explosive) {
                buildBest(explosiveTraps, nxt);
            }
        }

    }
    public Boolean flagMovementLogic() throws GameActionException {
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
    public void dodgeEnemies() throws GameActionException {
        if (!rc.isMovementReady()) return;
        if (closestAttacker != null) {
            Direction oppDir = myLoc.directionTo(closestAttacker).opposite();
            MapLocation opposite = myLoc.add(oppDir).add(oppDir);
            if(myLoc.distanceSquaredTo(closestAttacker)<20){
                rc.setIndicatorString("running away bc too close");
                bugNav.move(opposite);
            }

        }
    }
    public Boolean crumbMovementLogic() throws GameActionException {
        if(roundNumber < 200-Math.max(rc.getMapHeight(), rc.getMapWidth())/2){
            //randomly explore, with bias towards center
            MapLocation choice;
            choice = myLoc.add(allDirections[random.nextInt(8)]);
            bugNav.move(choice);
            return true;
        }
        return false;
    }
    public Boolean avoidOtherBuilders() {//always returns true

        for(RobotInfo i: friendlyRobots){
            if(i.getBuildLevel()==6&&i.getLocation().distanceSquaredTo(currentTarget)>myLoc.distanceSquaredTo(currentTarget)){
                rc.setIndicatorString("too close to freindly bot");
                bugNav.move(myLoc.add(myLoc.directionTo(i.getLocation()).opposite()));
            }
        }
        return false;
    }
    public void followFriendly() throws GameActionException {
        MapLocation closestFriend =null;
        int mindist = 100000;

        if(closestAttacker==null){
            closestAttacker = currentTarget;
        }
        int myDistToAttacker = myLoc.distanceSquaredTo(closestAttacker);
        Direction oppDir = myLoc.directionTo(closestAttacker);
        MapLocation oppositeLocation = myLoc.add(oppDir).add(oppDir);

        for(RobotInfo i: friendlyRobots){
            int distToMe = i.getLocation().distanceSquaredTo(closestAttacker);
            if(distToMe<myDistToAttacker){
                bugNav.move(oppositeLocation);
            }

            int cval = i.getLocation().distanceSquaredTo(myLoc);
            if(i.getBuildLevel()<4&&cval<mindist){
                mindist = cval;
                closestFriend = i.getLocation();
            }
        }
        if(mindist!=100000) {
            bugNav.move(closestFriend);
        }
    }
    public void movement() throws GameActionException {
        if(rc.getRoundNum()>200){
            dodgeEnemies();

            if(friendlyRobots.length>1) {
                followFriendly();
            }
        }
        if(crumbMovementLogic())
            return;


        if(avoidOtherBuilders())
            return;
        if(currentTarget!=null) {
            if(rc.getMovementCooldownTurns()==0) {
                rc.setIndicatorString("attacking towards " + currentTarget);
            }
            bugNav.move(currentTarget);
        }
    }
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
            if (i.hasFlag) {
                ret = enemyLoc;
                break;
            }
        }
        return ret;
    }

    public void tryAttack() throws GameActionException {
        if (!rc.isActionReady()) return;
        MapLocation attackLoc = findBestAttackLocation();
        if(attackLoc!=null&&rc.canAttack(attackLoc)){
            rc.attack(attackLoc);
        }
    }
    public void selfPreservation() throws GameActionException {
        if(enemyRobots.length<2) {
            tryAttack();
        }
    }
    public void tryFill() throws GameActionException {
        for(Direction d: Direction.allDirections()){
            MapLocation tmp = myLoc.add(d);
            if(rc.canFill(tmp)){
                rc.fill(tmp);
            }
        }
    }
    public void funnyDig() throws GameActionException {
        for(Direction d: allDirections){
            MapLocation tmp = myLoc.add(d);
            if((tmp.x+tmp.y)%2==0&&rc.canDig(tmp)){
                rc.dig(tmp);
            }
        }
    }
    public void pathFind() throws GameActionException {
        tryFill();
    }
    public void increaseLevel() throws GameActionException {
//        if(rc.getRoundNum()<230-Math.max(rc.getMapHeight(), rc.getMapWidth())/2) {
            funnyDig();
//        }
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
            if(i.getBuildLevel()>3){
                score+=500;
            }
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
    int[] healUpgrade = {80, 82, 84, 86, 88, 92, 100};

    @Override
    public void turn() throws GameActionException {
        rc.setIndicatorString("");
        setGlobals();//needs to be updated
        updateCurrentTarget();
        attemptBuildTraps(false);
        movement();
        setGlobals();
        attemptBuildTraps(true);
        selfPreservation();
//        tryHeal();

//        pathFind();
        if(rc.getLevel(SkillType.BUILD)<6) {
            increaseLevel();
        }
    }
}