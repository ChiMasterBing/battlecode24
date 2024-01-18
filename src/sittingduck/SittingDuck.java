package sittingduck;

import sittingduck.fast.FastLocSet;
import battlecode.common.*;

public class SittingDuck extends Robot {
    int myFlagNum = 0;

    MapLocation myLoc;
    FastLocSet spawnSet = new FastLocSet();

    public SittingDuck(RobotController rc) throws GameActionException {
        super(rc);
        MYTYPE = 1;
        if (myMoveNumber == 2) {
            myFlagNum = 0;
        }
        else if (myMoveNumber == 12) {
            myFlagNum = 1;
        }
        else if (myMoveNumber == 22) {
            myFlagNum = 2;
        }

        for(MapLocation spawn : spawnLocs){
            spawnSet.add(spawn);
        }
    }

    RobotInfo[] enemyRobots, friendlyRobots, closeFriendlyRobots, closeEnemyRobots;

    int turnsSpentAway = 0;
    public void turn() throws GameActionException {
        if (roundNumber < 10) return;

        myLoc = rc.getLocation();
        closeEnemyRobots = rc.senseNearbyRobots(4, rc.getTeam().opponent());
        closeFriendlyRobots = rc.senseNearbyRobots(4, rc.getTeam());
        enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        friendlyRobots = rc.senseNearbyRobots(-1, rc.getTeam());
        if (rc.canSenseLocation(myFlags[myFlagNum])) { //im in distance of my flag
            boolean isFlagPresent = false;
            for (FlagInfo f:flags) {
                if (f.getID() == flagIDs[myFlagNum]) {
                    isFlagPresent = true; break;
                }
            }

            int score = Math.min(Math.max(friendlyRobots.length - enemyRobots.length + 8, 0), 15);
            if (enemyRobots.length > 0) {
                score = Math.max(score-1, 0);
            }
            if (isFlagPresent) { 
                Comms.writeFlagStatus(myFlagNum, score);
            }
            else {
                Debug.println("OUR FLAG IS GONE --> DONT WRITE DISTRESS");
                rc.resign();
                Comms.writeFlagStatus(myFlagNum, 15); //we dont care about defending if flag aint there
            }

            
            rc.setIndicatorString(score + " ");

            turnsSpentAway = 0;
        }
        else {
            turnsSpentAway++;
            if (turnsSpentAway > 26) { //if we've been away for too long, dont want to keep panicking
                Comms.writeFlagStatus(myFlagNum, 0 + 8);
            }
        }
        movement();
        tryHeal();
    }

    int[] healUpgrade = {80, 82, 84, 86, 88, 92, 100};

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

    public void buildTrap() {




    }

    public boolean visionInRange(MapLocation a, MapLocation b) {
        if (a.distanceSquaredTo(b) <= 10) return true;
        return false;
    }



    public void movement() throws GameActionException {
        //keep in range of friendly duck so we can heal
        //keep in range of flag
        //keep out of range of enemy fire

        if (enemyRobots.length > 0) {
            int minDist = 1000000;
            MapLocation closestEnemyRobot = null;
            for (RobotInfo r:enemyRobots) {
                if (r.getLocation().distanceSquaredTo(myLoc) < minDist) {
                    minDist = r.getLocation().distanceSquaredTo(myLoc);
                    closestEnemyRobot = r.getLocation();
                }
            }

            int stun = 0;
            MapInfo[] nearbyTraps = rc.senseNearbyMapInfos(4);
            for (MapInfo t:nearbyTraps) {
                if (t.getTrapType() == TrapType.STUN) {
                    stun++;
                }
            }

            if (stun < 1 && spawnSet.contains(myLoc)) {
                if (rc.canBuild(TrapType.STUN, myLoc.add(myLoc.directionTo(closestEnemyRobot)))) {
                    rc.build(TrapType.STUN, myLoc.add(myLoc.directionTo(closestEnemyRobot)));
                }
                
                if (rc.canBuild(TrapType.STUN, myLoc.add(myLoc.directionTo(closestEnemyRobot).rotateLeft()))) {
                    rc.build(TrapType.STUN, myLoc.add(myLoc.directionTo(closestEnemyRobot).rotateLeft()));
                }

                if (rc.canBuild(TrapType.STUN, myLoc.add(myLoc.directionTo(closestEnemyRobot).rotateRight()))) {
                    rc.build(TrapType.STUN, myLoc.add(myLoc.directionTo(closestEnemyRobot).rotateRight()));
                }
            }

            if (minDist < 10) {
                Direction bestDir = null;
                int maxDist = minDist;
                for (Direction d:allDirections) {
                    MapLocation nxt = myLoc.add(d);
                    if (nxt.distanceSquaredTo(closestEnemyRobot) > maxDist && visionInRange(nxt, myFlags[myFlagNum]) && rc.canMove(d)) {
                        maxDist = nxt.distanceSquaredTo(closestEnemyRobot);
                        bestDir = d;
                    }
                }
                if (bestDir != null) {
                    rc.move(bestDir);
                }
            }
        } 
        else {
            bugNav.move(myFlags[myFlagNum]);
        }
    }
}
