package waxingmoon2;

import battlecode.common.*;
import waxingmoon.fast.*;

public class bugNav2 {
    static RobotController rc;
    static MapLocation target;
    static boolean[] impassable = null;
    static final int INF = 1000000;
    static final int MAX_MAP_SIZE = GameConstants.MAP_MAX_HEIGHT;
    static boolean shouldGuessRotation = true; // if I should guess which rotation is the best
    static boolean rotateRight = true; // if I should rotate right or left
    static MapLocation lastObstacleFound = null; // latest obstacle I've found in my way
    static int minDistToTarget = INF; // minimum distance I've been to the enemy while going around an obstacle
    static MapLocation prevTarget = null; // previous target
    static FastIntSet visited = new FastIntSet();
    static int id = 12620;
    static FastLocSet dontMove;

    public static void init(RobotController r) {
        rc = r;
    }

    static final Direction[] directions = {
        Direction.NORTH,
        Direction.NORTHEAST,
        Direction.EAST,
        Direction.SOUTHEAST,
        Direction.SOUTH,
        Direction.SOUTHWEST,
        Direction.WEST,
        Direction.NORTHWEST,
        Direction.CENTER
    };

    static void initTurn() {
        impassable = new boolean[directions.length];
    }

    static public Direction move(MapLocation loc) {
        initTurn();
//        Debug.setIndicatorLine(Debug.INDICATORS, rc.getLocation(), loc, 0, 0, 255);
        if (!rc.isMovementReady()) return null;
        target = loc;
        return nav();
    }

    static boolean canMove(Direction dir) {
        if (!rc.canMove(dir))
            return false;
        if (impassable[dir.ordinal()])
            return false;
        return true;
    }

    static int getBaseMovementCooldown() {
        //IF THIS IS FLAG
        return 10;
    }

    static int distance(MapLocation A, MapLocation B) {
        return Math.max(Math.abs(A.x - B.x), Math.abs(A.y - B.y));
    }

    static Direction nav() {
        try {
            // different target? ==> previous data does not help!
            if (prevTarget == null || target.distanceSquaredTo(prevTarget) > 0) {
                // Debug.println("New target: " + target, id);
                resetPathfinding();
            }

            // If I'm at a minimum distance to the target, I'm free!
            MapLocation myLoc = rc.getLocation();
            int d = distance(myLoc, target);
            if (d < minDistToTarget) {
                resetPathfinding();
                minDistToTarget = d;
            }

            int code = getCode();

            if (visited.contains(code)) {
                // Debug.println("Contains code", id);
                resetPathfinding();
            }
            visited.add(code);

            // Update data
            prevTarget = target;

            // If there's an obstacle I try to go around it [until I'm free] instead of
            // going to the target directly
            Direction dir = myLoc.directionTo(target);
            if (lastObstacleFound != null) {
                // Debug.println("Last obstacle found: " + lastObstacleFound, id);
                dir = myLoc.directionTo(lastObstacleFound);
            }

            if (canMove(dir) || rc.canFill(myLoc.add(dir))) {
                // Debug.println("can move: " + dir, id);
                resetPathfinding();
            }
            
            if (!rc.isSpawned()) {
                System.out.println("WHATT");
            }
            
            // I rotate clockwise or counterclockwise (depends on 'rotateRight'). If I try
            // to go out of the map I change the orientation
            // Note that we have to try at most 16 times since we can switch orientation in
            // the middle of the loop. (It can be done more efficiently)
            for (int i = 8; i-- > 0;) {
                MapLocation newLoc = myLoc.add(dir);
                if (rc.canSenseLocation(newLoc)) {
                    if (canMove(dir) || rc.canFill(newLoc)) {
                        return dir;
                    }
                }
                RobotInfo ri; 
                if (!rc.onTheMap(newLoc)) {
                    rotateRight = !rotateRight;
                } else if ((ri = rc.senseRobotAtLocation(newLoc)) != null) {
                    //Debug.println("itsa mario");

                    if (ri.team == rc.getTeam()) {
                        //WHAT
                    }
                } else if (!rc.sensePassability(newLoc) && !rc.canFill(newLoc)) {
                    

                    // This is the latest obstacle found if
                    // - I can't move there
                    // - It's on the map
                    // - It's not passable
                    lastObstacleFound = newLoc;
                    if (shouldGuessRotation) {
                        shouldGuessRotation = false;
                        // Debug.println("Guessing rot dir", id);
                        // Rotate left and right and find the first dir that you can move in
                        Direction dirL = dir;
                        for (int j = 8; j-- > 0;) {
                            if (canMove(dirL) || rc.canFill(myLoc.add(dirL)))
                                break;
                            dirL = dirL.rotateLeft();
                        }

                        Direction dirR = dir;
                        for (int j = 8; j-- > 0;) {
                            if (canMove(dirR) || rc.canFill(myLoc.add(dirR)))
                                break;
                            dirR = dirR.rotateRight();
                        }

                        // Check which results in a location closer to the target
                        MapLocation locL = myLoc.add(dirL);
                        MapLocation locR = myLoc.add(dirR);

                        int lDist = distance(target, locL);
                        int rDist = distance(target, locR);
                        int lDistSq = target.distanceSquaredTo(locL);
                        int rDistSq = target.distanceSquaredTo(locR);

                        if (lDist < rDist) {
                            rotateRight = false;
                        } else if (rDist < lDist) {
                            rotateRight = true;
                        } else {
                            rotateRight = rDistSq < lDistSq;
                        }
                    }
                    // Debug.println("Guessed: " + rotateRight, id);
                }

                if (rotateRight) dir = dir.rotateRight();
                else dir = dir.rotateLeft();
            }

            if (canMove(dir) || rc.canFill(myLoc.add(dir))) return dir;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Debug.println("Last exit", id);
        return null;
    }

    // clear some of the previous data
    static void resetPathfinding() {
        // Debug.println("Resetting pathfinding", id);
        lastObstacleFound = null;
        minDistToTarget = INF;
        visited.clear();
        shouldGuessRotation = true;
    }

    static int getCode() {
        int x = rc.getLocation().x;
        int y = rc.getLocation().y;
        Direction obstacleDir = rc.getLocation().directionTo(target);
        if (lastObstacleFound != null)
            obstacleDir = rc.getLocation().directionTo(lastObstacleFound);
        int bit = rotateRight ? 1 : 0;
        return (((((x << 6) | y) << 4) | obstacleDir.ordinal()) << 1) | bit;
    }
}
