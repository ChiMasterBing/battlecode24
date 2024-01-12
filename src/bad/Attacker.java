package bad;

import battlecode.common.*;

import java.util.Map;
import java.util.Random;

public class Attacker extends Robot{
    static final Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.SOUTHWEST,
            Direction.WEST,
            Direction.NORTHWEST,
    };
    static final Random rng = new Random(6147);

    public Attacker(RobotController rc) throws GameActionException {
        super(rc);
    }
    public void turn() throws GameActionException{
        super.turn();
    }
    public static void updateEnemyRobots() throws GameActionException{
        // Sensing methods can be passed in a radius of -1 to automatically
        // use the largest possible value.
        RobotInfo[] enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        if (enemyRobots.length != 0){
            rc.setIndicatorString("There are nearby enemy robots! Scary!");
            // Save an array of locations with enemy robots in them for future use.
            MapLocation[] enemyLocations = new MapLocation[enemyRobots.length];
            for (int i = 0; i < enemyRobots.length; i++){
                enemyLocations[i] = enemyRobots[i].getLocation();
            }
            // Let the rest of our team know how many enemy robots we see!
            if (rc.canWriteSharedArray(0, enemyRobots.length)){
                rc.writeSharedArray(0, enemyRobots.length);
                int numEnemies = rc.readSharedArray(0);
            }

        }
    }
    public static MapLocation findLeader() throws GameActionException {
        RobotInfo[] friendlyRobots = rc.senseNearbyRobots(-1, rc.getTeam());
        int maxH = rc.getHealth()*1000000+rc.getID();
        MapLocation ret = null;
        for(RobotInfo i: friendlyRobots){
            if(maxH<i.getHealth()){
                maxH = i.getHealth()*1000000+rc.getID();
                ret = i.getLocation();
            }
        }
        return ret;
    }
    public static MapLocation findBestAttackLocation() throws GameActionException{
        RobotInfo[] enemyRobots = rc.senseNearbyRobots(4, rc.getTeam().opponent());
        RobotInfo[] friendlyRobots = rc.senseNearbyRobots(4, rc.getTeam());
        int totalHeal = 0;
        for(RobotInfo i: enemyRobots){
            if(i.healLevel>i.attackLevel){
                totalHeal+=80;
            }
        }
        double rounds = 0;
        MapLocation ret = null;
        for(RobotInfo i : enemyRobots){
            MapLocation enemyLoc = i.getLocation();
            int totalAttack = 0;
            for(RobotInfo j: friendlyRobots){
                if(j.attackLevel>j.healLevel&&j.getLocation().isWithinDistanceSquared(enemyLoc, 4)){
                    totalAttack+=180;
                }
            }
            double rnds = 0;
            int roundstokill = 100000;
            if(i.hasFlag){
                rnds+=100000000;
            }
            if(totalAttack-totalHeal+1!=0) {
                roundstokill = i.getHealth() / (totalAttack - totalHeal + 1);
            }
            rnds +=1/((double)roundstokill+1.0);
            rnds*=1000;
            if(i.attackLevel>=i.healLevel){
                rnds+=(i.attackLevel+1);
            }else{
                rnds+=i.healLevel;
            }
            rnds*=1000;
            rnds+=20-rc.getLocation().distanceSquaredTo(enemyLoc);
            if(rnds>rounds){
                rounds = rnds;
                ret = i.getLocation();
            }

        }
        return ret;
    }
}
