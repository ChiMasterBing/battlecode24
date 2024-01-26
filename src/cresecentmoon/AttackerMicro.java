package cresecentmoon;

//sometimes, full moons turn bad dreams into horrible nightmares.
//--waxing moon
import battlecode.common.*;
//XSquare style micro
//weight squares to move in, one for each direction
//keep track of bombs
//stun has radius of adjacent. keeps track of where might PROC stuns, not where stuns are.
//compute likelihood for stuns ONLY on adjacent tiles I can next walk to. 

public class AttackerMicro {
    static int[][] stunTracker; // 0 --> probably no bomb, 1 IDK, 2 perhaps bomb
    static RobotController rc;
    static RobotInfo[] enemyRobots, friendlyRobots, closeFriendlyRobots, closeEnemyRobots;
    static int roundNumber;
    static MapLocation myLoc;
    static int[] aroundMeX = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    static int[] aroundMeY = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    static int nullX, nullY;
    static int myMoveNumber;

    static int[] attackerDamages = {150, 158, 161, 165, 195, 203, 240};
    static int[] attackerCooldowns = {20, 19, 19, 18, 16, 13, 8};
    static int[] attackerDPS = {75, 83, 85, 92, 122, 156, 300};
    static int[] uAttackerDPS = {105, 116, 121, 128, 170, 218, 420};

    public static void init(RobotController r) {
        rc = r;
        stunTracker = new int[rc.getMapWidth() + 1][rc.getMapHeight() + 1];
        nullX = rc.getMapHeight();
        nullY = rc.getMapWidth();
    }

    public static void setGlobals(RobotInfo[] er, RobotInfo[] fr, RobotInfo[] ecr, RobotInfo[] fcr, int moveNumber) {
        myMoveNumber = moveNumber;
        enemyRobots = er;
        friendlyRobots = fr;
        closeFriendlyRobots = fcr;
        closeEnemyRobots = ecr;
        roundNumber = rc.getRoundNum();
        myLoc = rc.getLocation();
    }

    //STEP BACK AFTER GETTING STUNNED

    public static boolean movementMicro() throws GameActionException {
        MicroInfo[] microInfo = new MicroInfo[9];
        for (Direction d:Direction.allDirections()) {
            MapLocation nxt = myLoc.add(d);
            if (rc.onTheMap(nxt)) {
                aroundMeX[d.ordinal()] = nxt.x;
                aroundMeY[d.ordinal()] = nxt.y;
            } else {
                aroundMeX[d.ordinal()] = nullX;
                aroundMeY[d.ordinal()] = nullY;
            }
            //stunTracker[nxt.x][nxt.y] = 1;
            microInfo[d.ordinal()] = new MicroInfo(d);
        }

        // MapInfo[] mapInfos = rc.senseNearbyMapInfos();
        // for (MapInfo m:mapInfos) {
        //     if (m.getTrapType() == TrapType.STUN) {
        //         MapLocation loc = m.getMapLocation();
        //         microInfo[0].updateTrap(loc);
        //         microInfo[1].updateTrap(loc);
        //         microInfo[2].updateTrap(loc);
        //         microInfo[3].updateTrap(loc);
        //         microInfo[4].updateTrap(loc);
        //         microInfo[5].updateTrap(loc);
        //         microInfo[6].updateTrap(loc);
        //         microInfo[7].updateTrap(loc);
        //         microInfo[8].updateTrap(loc);
        //     }
        // }

        //HOW TO AVOID CHAINSTUN

        // for (RobotInfo r:closeFriendlyRobots) {
        //     microInfo[0].updateAlly(r);
        //     microInfo[1].updateAlly(r);
        //     microInfo[2].updateAlly(r);
        //     microInfo[3].updateAlly(r);
        //     microInfo[4].updateAlly(r);
        //     microInfo[5].updateAlly(r);
        //     microInfo[6].updateAlly(r);
        //     microInfo[7].updateAlly(r);
        //     microInfo[8].updateAlly(r);
        // }

        for (RobotInfo r:enemyRobots) {
            int dps;
            if (roundNumber >= 600) {
                dps = uAttackerDPS[r.getAttackLevel()];
            } else {
                dps = attackerDPS[r.getAttackLevel()];
            }
            microInfo[0].updateEnemy(r, dps);
            microInfo[1].updateEnemy(r, dps);
            microInfo[2].updateEnemy(r, dps);
            microInfo[3].updateEnemy(r, dps);
            microInfo[4].updateEnemy(r, dps);
            microInfo[5].updateEnemy(r, dps);
            microInfo[6].updateEnemy(r, dps);
            microInfo[7].updateEnemy(r, dps);
            microInfo[8].updateEnemy(r, dps);
        }

        MicroInfo bestMicro = microInfo[8];
        for (int i = 0; i < 8; ++i) {
            if (microInfo[i].isBetter(bestMicro, rc.getActionCooldownTurns())) bestMicro = microInfo[i];
        }

        if (bestMicro.dir == Direction.CENTER) return true;
        if (rc.canMove(bestMicro.dir)) {
            rc.setIndicatorString("best micro dir is " + bestMicro.dir);
            rc.move(bestMicro.dir); return true;
        }
        return false;
    }

    public static MapLocation findBestAttackLocation() throws GameActionException {
        if (closeEnemyRobots.length == 0) return null;
        int startBC = Clock.getBytecodesLeft();
        TargetInfo[] targetInfo = new TargetInfo[closeEnemyRobots.length];
        int ptr = 0;
        for (RobotInfo r:closeEnemyRobots) { 
            targetInfo[ptr++] = new TargetInfo(r);
        }

        for (RobotInfo r:friendlyRobots) {
            for (TargetInfo i:targetInfo) {
                i.updateAlly(r);
            }
        }

        TargetInfo bestTarget = targetInfo[0];
        for (int i = 1; i < targetInfo.length; ++i) {
            if (targetInfo[i].isBetter(bestTarget)) bestTarget = targetInfo[i];
        }

        rc.setIndicatorString("BYTECODES " + (startBC - Clock.getBytecodesLeft()) + " " + targetInfo.length);

        return bestTarget.location;
    }

    static class TargetInfo {
        MapLocation location;
        boolean canAttack = false;
        int DPSreceived = 0;
        int DPSpotential = 0;
        int health = 0, atkLevel, healLevel, buildLevel;
        boolean hasFlag = false;
        public TargetInfo (RobotInfo r) throws GameActionException {
            this.location = r.getLocation();
            if (rc.canAttack(location)) {
                canAttack = true;
                atkLevel = r.getAttackLevel();
                healLevel = r.getHealLevel();
                buildLevel = r.getBuildLevel();
                health = r.getHealth();
                hasFlag = r.hasFlag;
                DPSreceived += rc.getAttackDamage();
                //DPSpotential +=
            }
        }
        public void updateAlly(RobotInfo r) {
            int dist = r.getLocation().distanceSquaredTo(location);
            if (dist > 4) return;
            int friendMoveOrder = Comms.IDToMoveOrder.getVal(r.ID);
            // if (roundNumber <= 600) DPSpotential += attackerDPS[r.getAttackLevel()];
            // else DPSpotential += uAttackerDPS[r.getAttackLevel()];
            if (friendMoveOrder > myMoveNumber) { //they might be able to attack after
                if (roundNumber <= 600) DPSreceived += attackerDamages[r.getAttackLevel()];
                else DPSreceived += attackerDamages[r.getAttackLevel()] + 60;
            } 
        }

        boolean isBetter(TargetInfo t) {
            if (hasFlag) return true;
            if (t.hasFlag) return false;

            if (DPSreceived >= health && t.DPSreceived < t.health) return true;
            if (DPSreceived < health && t.DPSreceived >= t.health) return false;

            //modify
            if ((health - DPSreceived) < (t.health - t.DPSreceived)) return true;
            // if (DPSreceived < health && t.DPSreceived >= t.health) return false;
            // ---- 
            // if (buildLevel >= 5 && t.buildLevel < 5) return true; 
            // if (buildLevel < 5 && t.buildLevel >= 5) return false; 

            // if (atkLevel > t.atkLevel && atkLevel > 3) return true;
            // if (t.atkLevel > atkLevel && t.atkLevel > 3) return false;

            // if (healLevel > t.healLevel && healLevel > 3) return true;
            // if (t.healLevel > healLevel && t.healLevel > 3) return false;
        
            // if (atkLevel > t.atkLevel) return true;
            // if (t.atkLevel > atkLevel) return false;

            // if (healLevel > t.healLevel) return true;
            return false;
        }
    }

    static class MicroInfo{
        Direction dir;
        MapLocation location;
        int minDistanceToEnemy = 1000000;
        double DPSreceived = 0;
        double enemiesTargeting = 0;
        double alliesTargeting = 0;
        boolean canMove = true;
        int stunLikely = 0;
        boolean enemyTerritory;
        int heals = 0;
        public MicroInfo(Direction dir) throws GameActionException {
            this.dir = dir;
            this.location = rc.getLocation().add(dir);
            if (dir != Direction.CENTER && !rc.canMove(dir)) canMove = false;
            else{
                if (rc.senseMapInfo(location).getTeamTerritory() == rc.getTeam()) {
                    enemyTerritory = false;
                } else {
                    enemyTerritory = true;
                }
            }
        }

        void updateEnemy(RobotInfo unit, int dps){
            if (!canMove) return;
            int dist = unit.getLocation().distanceSquaredTo(location);
            if (dist < minDistanceToEnemy)  minDistanceToEnemy = dist;

            if (dist <= 4) DPSreceived += dps; 
            //else if (dist <= 10) DPSreceived += dps; //can move and hit us

            if (dist <= 20) enemiesTargeting += dps;
            if (dist <= 4) stunLikely++;
        }

        void updateTrap(MapLocation m) {
            if (!canMove) return;
            int dist = m.distanceSquaredTo(location);
            if (dist <= 0) {
                DPSreceived -= rc.getAttackDamage();
            }
        }

        void updateAlly(RobotInfo unit){
            if (!canMove) return;
            int dist = unit.getLocation().distanceSquaredTo(location);
            if (dist <= 4) {
                //heals += 20;
                DPSreceived += 10; //try avoiding getting chainstunned
                // stunLikely--;
            }
        }

        int safe(){ 
            if (!canMove) return -1;
            if (DPSreceived > 0) return 0;
            if (enemiesTargeting > alliesTargeting) return 1;
            return 2;
        }

        boolean inRange(){
            // if (alwaysInRange) return true;
            return minDistanceToEnemy <= 4;
        }

        //equal => true
        boolean isBetter(MicroInfo M, int cooldown){
            if (!canMove) return false;
            if (!M.canMove) return true;
            if (rc.getHealth() < DPSreceived) return false;
            if (cooldown < 10) { //WE WANT TO BE BIG AND STEAMY
                if (inRange() && !M.inRange()) return true;
                if (!inRange() && M.inRange()) return false;
                
                if (inRange()) { //both squares are in range
                    if (DPSreceived < M.DPSreceived) return true;
                    if (M.DPSreceived < DPSreceived) return false;

                    if (enemiesTargeting < M.enemiesTargeting) return true;
                    else if (enemiesTargeting > M.enemiesTargeting) return false;

                    if (stunLikely < M.stunLikely) return true;
                    if (stunLikely > M.stunLikely) return false;

                    return minDistanceToEnemy >= M.minDistanceToEnemy;
                }
                else return minDistanceToEnemy <= M.minDistanceToEnemy;
            } else { //dont wanna proc anything rn
                if (!inRange() && M.inRange()) return true;
                if (inRange() && !M.inRange()) return false;

                if (inRange()) { //both squares are in range
                    if (DPSreceived < M.DPSreceived) return true;
                    if (M.DPSreceived < DPSreceived) return false;
                    
                    if (enemiesTargeting < M.enemiesTargeting) return true;
                    else if (enemiesTargeting > M.enemiesTargeting) return false;
                    
                    return minDistanceToEnemy >= M.minDistanceToEnemy;
                }
                else {
                    // if (heals > M.heals) return true;
                    // if (M.heals > heals) return false;

                    if (enemiesTargeting - heals < M.enemiesTargeting - M.heals) return true;
                    else if (enemiesTargeting - heals > M.enemiesTargeting - M.heals) return false;

                    return minDistanceToEnemy <= M.minDistanceToEnemy;
                }
            }
        }
    }
}
