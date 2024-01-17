package sittingduck;

import battlecode.common.*;

public class SittingDuck extends Robot {
    int myFlagNum = 0;

    MapLocation myLoc;

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

            if (!isFlagPresent) { //WE'RE TROLLING
                //can implement this later if guaranteed our flag is gone go to someplace else and help out
            }

            Comms.writeFlagStatus(myFlagNum, Math.min(Math.max(friendlyRobots.length - enemyRobots.length + 8, 0), 15));
            rc.setIndicatorString(Math.min(Math.max(friendlyRobots.length - enemyRobots.length + 8, 0), 15) + " ");

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

    public void buildSpawnTraps() {




    }

    public boolean visionInRange(MapLocation a, MapLocation b) {
        if (a.distanceSquaredTo(b) <= 20) return true;
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
