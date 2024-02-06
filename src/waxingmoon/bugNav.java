package waxingmoon;

import battlecode.common.*;
import waxingmoon.fast.*;

public class bugNav {
    static RobotController rc;
    static MapLocation target;
    static boolean[] impassable = null;
    static final int INF = 1000000;
    static final int MAX_MAP_SIZE = GameConstants.MAP_MAX_HEIGHT;
    static boolean shouldGuessRotation = true; // if I should guess which rotation is the best
    static boolean rotateRight = true; // if I should rotate right or left
    static MapLocation lastObstacleFound = null; // latest obstacle I've found in my way
    static int minDistToEnemy = INF; // minimum distance I've been to the enemy while going around an obstacle
    static MapLocation prevTarget = null; // previous target
    static FastIntSet visited = new FastIntSet();
    static int id = 12620;
    static FastLocSet dontMove;
    static boolean isSwiper = false;
    static boolean isBugging;

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

    static public void move(MapLocation loc) {
        if (!rc.isMovementReady()) return;
        initTurn();
        target = loc;
        nav();
    }

    static boolean canMove(Direction dir) {
        if (!rc.canMove(dir))
            return false;
        if (impassable[dir.ordinal()])
            return false;
        return true;
    }

    static int getBaseMovementCooldown() {
        if (rc.hasFlag()) {
            if (rc.getRoundNum() > 1800) return 12;
            return 20;
        }
        return 10;
    }

    static int distance(MapLocation A, MapLocation B) {
        return Math.max(Math.abs(A.x - B.x), Math.abs(A.y - B.y));
    }

    static boolean nav() {
        // System.out.println("obstacle " + rc.getLocation() + "" + lastObstacleFound + " ");
        try {
            // different target? ==> previous data does not help!
            // If I'm at a minimum distance to the target, I'm free!
            MapLocation myLoc = rc.getLocation();

            if (prevTarget != null && !prevTarget.equals(target)) {
                if (myLoc.directionTo(prevTarget) != myLoc.directionTo(target)) {
                    resetPathfinding();
                }
            } else {
                int d = distance(myLoc, target);
                if (d < minDistToEnemy) {
                    resetPathfinding();
                    // System.out.println("reset B");
                    minDistToEnemy = d;
                }
            }

            int code = getCode();

            if (visited.contains(code)) {
                // Debug.println("Contains code", id);
                // System.out.println("reset C");
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

            if (lastObstacleFound != null)
                rc.setIndicatorLine(myLoc,lastObstacleFound, 255, 255, 0);

            if (canMove(dir)) {
                // Debug.println("can move: " + dir, id);
                resetPathfinding();
            }
            
            rc.setIndicatorLine(myLoc, myLoc.add(dir), 255, 0, 255);

            isBugging = (lastObstacleFound != null);

            // I rotate clockwise or counterclockwise (depends on 'rotateRight'). If I try
            // to go out of the map I change the orientation
            // Note that we have to try at most 16 times since we can switch orientation in
            // the middle of the loop. (It can be done more efficiently)
            for (int i = 8; i-- > 0;) {
                MapLocation newLoc = myLoc.add(dir);
                if (rc.canSenseLocation(newLoc)) {
                    if (canMove(dir)) {
                        rc.move(dir);
                        rc.setIndicatorString("bugging " + lastObstacleFound);
                        return true;
                    }
                }
                if (!rc.onTheMap(newLoc)) {
                    rotateRight = !rotateRight;
                } else if (!rc.sensePassability(newLoc)) {
                    // This is the latest obstacle found if
                    // - I can't move there
                    // - It's on the map
                    // - It's not passable
                    rc.setIndicatorDot(newLoc, 0, 0, 0);

                    lastObstacleFound = newLoc;
                    if (shouldGuessRotation) {
                        shouldGuessRotation = false;
                        // Debug.println("Guessing rot dir", id);
                        // Rotate left and right and find the first dir that you can move in
                        Direction dirL = dir;
                        for (int j = 8; j-- > 0;) {
                            if (canMove(dirL))
                                break;
                            dirL = dirL.rotateLeft();
                        }

                        Direction dirR = dir;
                        for (int j = 8; j-- > 0;) {
                            if (canMove(dirR))
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

            if (canMove(dir)) rc.move(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    // clear some of the previous data
    static void resetPathfinding() {
        //System.out.println("Resetting pathfinding " + id);
        lastObstacleFound = null;
        minDistToEnemy = INF;
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
