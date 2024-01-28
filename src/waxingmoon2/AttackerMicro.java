package waxingmoon2;
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

    static int[] attackerDamages = {150, 158, 161, 165, 195, 203, 240};
    static int[] attackerCooldowns = {20, 19, 19, 18, 16, 13, 8};
    static int[] attackerDPS = {75, 83, 85, 92, 122, 156, 300};
    static int[] uAttackerDPS = {113, 124, 127, 138, 183, 234, 450};

    public static void init(RobotController r) {
        rc = r;
        stunTracker = new int[rc.getMapWidth() + 1][rc.getMapHeight() + 1];
        nullX = rc.getMapHeight();
        nullY = rc.getMapWidth();
    }

    public static boolean processTurn(RobotInfo[] er, RobotInfo[] fr, RobotInfo[] ecr, RobotInfo[] fcr) throws GameActionException {
        enemyRobots = er;
        friendlyRobots = fr;
        closeFriendlyRobots = ecr;
        closeEnemyRobots = fcr;
        roundNumber = rc.getRoundNum();
        myLoc = rc.getLocation();

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

        // for (int i=0; i<9; i++) {
        //     // . . 4 5 8
        //     // . . 1 . .
        //     // . . x . .
        //     // . * * * .
        //     // * * * * *
        //     // SO MANY SQUARES --> doing this is too bytecode heavy; thus we estimate only for NEXT TURN
        //     int x = aroundMeX[i], y = aroundMeY[i];
        //     for (RobotInfo r:enemyRobots) {
        //         MapLocation m = r.getLocation();
        //         int dist = (m.x - x) * (m.x - x) + (m.y - y) * (m.y - y);
        //         if (dist <= 8) {
        //             stunTracker[aroundMeX[i]][aroundMeY[i]] = 2;
        //         }
        //     }
        // }

        for (RobotInfo r:friendlyRobots) {

        }

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
    static class MicroInfo{
        Direction dir;
        MapLocation location;
        int minDistanceToEnemy = 1000000;
        double DPSreceived = 0;
        double enemiesTargeting = 0;
        double alliesTargeting = 0;
        double lowestHealth = 1000000;
        boolean canMove = true;
        int stunLikely = 0;
        boolean enemyTerritory;

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
            if (dist <= 12){
                DPSreceived += dps; //MAKE THIS MORE PRECISE
                lowestHealth = Math.min(lowestHealth, Math.max(unit.getHealth(), uAttackerDPS[rc.getLevel(SkillType.ATTACK)]));
            }
            if (dist <= 20) enemiesTargeting += dps;
//            if (dist <= 2) stunLikely++;
        }

        void updateAlly(RobotInfo unit){
            if (!canMove) return;
            int dist = unit.getLocation().distanceSquaredTo(location);
            if (dist <= 2)
                stunLikely--;
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
                    return lowestHealth <= M.lowestHealth;
//                    return minDistanceToEnemy >= M.minDistanceToEnemy;
                }
                else {
                    if (enemiesTargeting < M.enemiesTargeting) return true;
                    else if (enemiesTargeting > M.enemiesTargeting) return false;

                    return minDistanceToEnemy <= M.minDistanceToEnemy;
                }
            }
        }
    }
}
