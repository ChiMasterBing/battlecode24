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
    static int myMoveNumber;
    static long noStunMask;

    static int[] attackerDamages = {150, 158, 161, 165, 195, 203, 240};
    static int[] attackerCooldowns = {20, 19, 19, 18, 16, 13, 8};
    static int[] attackerDPS = {75, 83, 85, 92, 122, 156, 300};
    static int[] uAttackerDPS = {105, 116, 121, 128, 170, 218, 420};
    static int[] healerHPS = {27, 29, 31, 34, 35, 36, 44};
    static int[] uhealerHPS = {43, 46, 51, 55, 56, 59, 72};
    public static void init(RobotController r) {
        rc = r;
//        stunTracker = new int[rc.getMapWidth() + 1][rc.getMapHeight() + 1];
        nullX = rc.getMapHeight();
        nullY = rc.getMapWidth();
    }

    public static boolean processTurn(RobotInfo[] er, RobotInfo[] fr, RobotInfo[] ecr, RobotInfo[] fcr) throws GameActionException {
        setGlobals(er, fr, ecr, fcr, -1); //dont worry about movenumber rn, not used in this version I sent u
        return movementMicro();
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
            microInfo[d.ordinal()] = new MicroInfo(d);
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

        for (RobotInfo r:friendlyRobots) {
            int hps;
            if (roundNumber >= 1200) {
                hps = uhealerHPS[r.getHealLevel()];
            } else {
                hps = healerHPS[r.getHealLevel()];
            }
            microInfo[0].updateAlly(r, hps);
            microInfo[1].updateAlly(r, hps);
            microInfo[2].updateAlly(r, hps);
            microInfo[3].updateAlly(r, hps);
            microInfo[4].updateAlly(r, hps);
            microInfo[5].updateAlly(r, hps);
            microInfo[6].updateAlly(r, hps);
            microInfo[7].updateAlly(r, hps);
            microInfo[8].updateAlly(r, hps);
        }

        MicroInfo bestMicro = microInfo[8];
        for (int i = 0; i < 8; ++i) {
            if (microInfo[i].isBetter(bestMicro, rc.getActionCooldownTurns())) bestMicro = microInfo[i];
        }

        if (bestMicro.dir == Direction.CENTER) return true;
        if (rc.canMove(bestMicro.dir)) {
            //rc.setIndicatorString("best micro dir is " + bestMicro.dir);
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
        boolean canMove = true;
        int stunLikely = 0;
        boolean enemyTerritory;
        int heals = 0;
        int stunScrew = 0; //how many allies am i screwing if I move here and stun goes off
        int lowestHealth = 1000000;

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
            MapLocation enemyLoc = unit.getLocation();
            int dist = enemyLoc.distanceSquaredTo(location);
            if (dist < minDistanceToEnemy)  minDistanceToEnemy = dist;

            if (dist <= 4) {
                DPSreceived += dps;
            }
            else if (dist <= 12) {
                // condition
                //int a = Clock.getBytecodeNum();
                Direction[] moveAtkDirs = Utils.enemyMoveAttacks(Utils.locationDelta(location, enemyLoc));
                if (moveAtkDirs == null) {
                    DPSreceived += dps;
                } else {
                    for (Direction dir: moveAtkDirs) {
                        rc.setIndicatorLine(enemyLoc, new MapLocation(enemyLoc.x + dir.dx, enemyLoc.y + dir.dy), 255, Navigation.map[enemyLoc.x + dir.dx][enemyLoc.y + dir.dy] == 1 ? 0:255, 0);
                        if (Navigation.map[enemyLoc.x + dir.dx][enemyLoc.y + dir.dy] < 2) {
                            DPSreceived += dps;
                            break;
                        }
                    }
                    DPSreceived += dps/2;
                }
                // System.out.println(Clock.getBytecodeNum()-a);
            }
            
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

        void updateAlly(RobotInfo unit, int hps){
            if (!canMove) return;
            int dist = unit.getLocation().distanceSquaredTo(location);
            if (dist <= 2)
                stunLikely--;
            if(dist<=4)
                heals+=hps;
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

        boolean canBeHit() {
            return minDistanceToEnemy <= 12;
        }

        boolean isBetter(MicroInfo M, int cooldown){
            if (!canMove) return false;
            if (!M.canMove) return true;

            if (rc.getHealth() < DPSreceived&&rc.getHealth()>M.DPSreceived) return false;
            if (rc.getHealth() > DPSreceived&&rc.getHealth()<M.DPSreceived) return true;

            if (cooldown < 10) { //WE WANT TO BE BIG AND STEAMY
                if (inRange() && !M.inRange()) return true;
                if (!inRange() && M.inRange()) return false;

                if (inRange()) { //both squares are in range
                    if (DPSreceived < M.DPSreceived) return true;
                    if (M.DPSreceived < DPSreceived) return false;

                    if (enemiesTargeting < M.enemiesTargeting) return true;
                    else if (enemiesTargeting > M.enemiesTargeting) return false;

//                    if (stunLikely < M.stunLikely) return true;
//                    if (stunLikely > M.stunLikely) return false;

                    return minDistanceToEnemy >= M.minDistanceToEnemy;
                }
                else {
                    return minDistanceToEnemy <= M.minDistanceToEnemy;
                }
            } else { //mess with CanBeHit?
                 if (!canBeHit() && M.canBeHit()) return true;
                 if (canBeHit() && !M.canBeHit()) return false;
                 if (canBeHit()) {
                    if (DPSreceived < M.DPSreceived) return true;
                    if (M.DPSreceived < DPSreceived) return false;

                    if (enemiesTargeting < M.enemiesTargeting) return true;
                    else if (enemiesTargeting > M.enemiesTargeting) return false;

                    return minDistanceToEnemy >= M.minDistanceToEnemy;
                }
                else {
                    if (enemiesTargeting - heals < M.enemiesTargeting - M.heals) return true;
                    else if (enemiesTargeting - heals > M.enemiesTargeting - M.heals) return false;

                    return minDistanceToEnemy <= M.minDistanceToEnemy;
                }
            }
        }
    }
}