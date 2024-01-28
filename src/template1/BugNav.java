package template1;

import com.google.flatbuffers.FlexBuffers.Map;

//! battlecode package
import battlecode.common.*;

//! custom package
import template1.java_utils.*;
import template1.java_utils.FastIntIntMap;
import template1.Comms.*;
import template1.Debug.*;
import template1.Utils.*;

public class BugNav {
    static RobotController rc;

    //! constants
    //final static boolean LINE_NAV = false;
    //final static boolean DETECT_DEAD_ENDS = false;
    //final static boolean RADIUS_NAV = true;
    

    final static int OPEN = 1;
    final static int ALLY_UNSTUCK = 3;
    final static int ALLY_STUCK = 2;
    final static int WATER = 5;
    final static int WALL = 4;
    final static int BORDER = 6;

    static int ROTATE = 1;
    static boolean betterRotate = true;

    //! running info
    static MapLocation location;
    static MapLocation[] dirMapLocs;
    static MapInfo[] dirMapInfos;
    static RobotInfo[] dirRobots;
    static int[] dirStatus;

    public static MapLocation target;
    static int distSqToTarget;
    static Direction preferredDir;
    static int preferredDirInt;

    static boolean isBugging;
    static BugNavTracker bugNavTracker;
    static boolean isStuck;

    //! init
    public static void init(RobotController r) throws GameActionException {
        rc = r;

        location = rc.getLocation();
        dirMapLocs = new MapLocation[8];
        dirMapInfos = new MapInfo[8];
        dirRobots = new RobotInfo[8];
        dirStatus = new int[8];

        target = null;
        distSqToTarget = -1;

        preferredDir = null;
        preferredDirInt = -1;

        isBugging = false;
        bugNavTracker = null;
        isStuck = false;
    }

    //! returns previous target
    public static MapLocation setTarget(MapLocation newTarget) throws GameActionException {
        if (isBugging) endBugging();
        MapLocation oldTarget = target;
        target = newTarget;
        return oldTarget;
    }

    public static MapLocation unsetTarget() throws GameActionException {
        if (isBugging) endBugging();
        MapLocation oldTarget = target;
        target = null;
        return oldTarget;
    }

    //! greedy or bugnav
    public static Direction navigate() throws GameActionException {
        if (target == null) return null;

        //! initialize turn info
        location = rc.getLocation();
        preferredDir = location.directionTo(target);
        preferredDirInt = Utils.dxDyToInt(preferredDir);
        if (preferredDirInt == 8) return null;
        senseAdjacents();

        //! condition to stop bugging
        //! radius bugNav
        // if (isBugging && (location.distanceSquaredTo(target) < bugNavTracker.orginalDist || Utils.rng.nextInt(25) < 1)) {
        //     endBugging();
        // }
        //! line bugNav
        if (isBugging && location.distanceSquaredTo(target) < bugNavTracker.orginalDist && bugNavTracker.originalPos.directionTo(location).equals(bugNavTracker.originalDir))
            endBugging();

        //! try greedy moves
        if (!isBugging || bugNavTracker.pivotDir == 8) {
            if (isBugging) endBugging();

            int moveDirInt = getGreedyMove();
            //! greedy move does not exist (all walls, stuck allies, or boundaries)
            if (moveDirInt == 8) {
                startBugging(preferredDir);
            } 
            //! greedy move exists (blocked by unstuck ally)
            else if (dirRobots[moveDirInt] != null) {
                isStuck = true;
                return Direction.CENTER;
            } 
            //! greedy move exists (open or water)
            else {
                isStuck = false;
                return Utils.DIRS_CENTER[moveDirInt];
            }
        }

        //! only reaches if blocked or if bugging
        int bugMoveDir = getBugMove();
        return bugMoveDir != 8 ? Utils.DIRS_CENTER[bugMoveDir] : null;
    }

    public static void makeMove(Direction dir) {
        if (!isBugging) return;
        if (Utils.dxDyToInt(dir) != bugNavTracker.nextPivot()) {
            //System.out.println("makemove1 " + String.valueOf(Utils.dxDyToInt(dir)) + " " + String.valueOf(bugNavTracker.nextPivot()));
            endBugging();
            
        }
        else if (bugNavTracker.moveToNextPivot()) {
            //System.out.println("makemove22");
            endBugging();
             }
    }

    //! primary private methods
    private static int getGreedyMove() throws GameActionException {
        int distToTarget = location.distanceSquaredTo(target);

        int priority = 0;
        int retDir = 8;

        //! best direction
        int tryDir = preferredDirInt;
        switch (dirStatus[tryDir]) {
            case OPEN:
                return tryDir;
            case ALLY_UNSTUCK:
                if (priority < 1) {
                    retDir = tryDir;
                    priority = 1;
                }
            //case ALLY_STUCK:
            case WATER:
                if (priority < 2) {
                    retDir = tryDir;
                    priority = 2;
                }
            //case WALL:
            //case BORDER:
        }
        //! rotate preferred
        tryDir = rotateDir(tryDir, false);
        if (dirMapLocs[tryDir].distanceSquaredTo(target) < distToTarget) {
            switch (dirStatus[tryDir]) {
                case OPEN:
                    return tryDir;
                case ALLY_UNSTUCK:
                    if (priority < 1) {
                        retDir = tryDir;
                        priority = 1;
                    }
                //case ALLY_STUCK:
                case WATER:
                    if (priority < 2) {
                        retDir = tryDir;
                        priority = 2;
                    }
                //case WALL:
                //case BORDER:
            }
        }
        //! rotate preferred x2
        tryDir = rotateDir(tryDir, false);
        if (dirMapLocs[tryDir].distanceSquaredTo(target) < distToTarget) {
            switch (dirStatus[tryDir]) {
                case OPEN:
                    return tryDir;
                case ALLY_UNSTUCK:
                    if (priority < 1) {
                        retDir = tryDir;
                        priority = 1;
                    }
                //case ALLY_STUCK:
                case WATER:
                    if (priority < 2) {
                        retDir = tryDir;
                        priority = 2;
                    }
                //case WALL:
                //case BORDER:
            }
        }
        //! rotate not preferred
        tryDir = preferredDirInt;
        tryDir = rotateDir(tryDir, true);
        if (dirMapLocs[tryDir].distanceSquaredTo(target) < distToTarget) {
            switch (dirStatus[tryDir]) {
                case OPEN:
                    return tryDir;
                case ALLY_UNSTUCK:
                    if (priority < 1) {
                        retDir = tryDir;
                        priority = 1;
                    }
                //case ALLY_STUCK:
                case WATER:
                    if (priority < 2) {
                        retDir = tryDir;
                        priority = 2;
                    }
                //case WALL:
                //case BORDER:
            }
        }
        //! rotate not preferred x2
        tryDir = rotateDir(tryDir, true);
        if (dirMapLocs[tryDir].distanceSquaredTo(target) < distToTarget) {
            switch (dirStatus[tryDir]) {
                case OPEN:
                    return tryDir;
                case ALLY_UNSTUCK:
                    if (priority < 1) {
                        retDir = tryDir;
                        priority = 1;
                    }
                //case ALLY_STUCK:
                case WATER:
                    if (priority < 2) {
                        retDir = tryDir;
                        priority = 2;
                    }
                //case WALL:
                //case BORDER:
            }
        }
        
        return retDir;
    }

    private static int getBugMove() throws GameActionException {
        assert isBugging : "is not buggin";
        int dirToMove = 8;
    

        //! if no where to move anyways
        if (((dirStatus[0]|dirStatus[1]|dirStatus[2]|dirStatus[3]|dirStatus[4]|dirStatus[5]|dirStatus[6]|dirStatus[7])&1) == 0) {
            isStuck = true;
            return 8;
        }
        
        //! if last pivot is clear, move there if possible otherwise stop bugging
        if (dirStatus[bugNavTracker.pivotDir] == OPEN || dirStatus[bugNavTracker.pivotDir] == WATER) {
            dirToMove = bugNavTracker.pivotDir;
            //! if not enough pivot history, end bugging
            if (bugNavTracker.revertPivot()) {
                //System.out.println("rfeverting");
                endBugging();
                return 8;
            }
            return dirToMove;
        }
        
        //! rotate about pivot until OPEN or WATER
        int originalPivotDir = bugNavTracker.pivotDir;
        dirToMove = bugNavTracker.nextPivot();

        while (dirToMove != originalPivotDir) {
            if (bugNavTracker.passed.contains((Utils.locationToInt(location) <<3) | bugNavTracker.pivotDir)) {
                endBugging();
                return 8;
            }
            switch (dirStatus[dirToMove]) {
                //! can move here
                case OPEN:
                    return dirToMove;

                //! become stuck but cannot move
                //! do not go for water and more rotation because of bugNav pivots
                case ALLY_UNSTUCK:
                    isStuck = true;
                    return 8;

                //! do nothing, rotate again
                case ALLY_STUCK:
                    //* how to back up??
                    bugNavTracker.updatePivot(dirToMove);
                    break;

                //! can move here
                case WATER:
                    return dirToMove;

                //! do nothing, rotate again
                case WALL:
                    bugNavTracker.updatePivot(dirToMove);
                    break;

                //! border turn around
                case BORDER:
                    //! second time hitting wall
                    if (bugNavTracker.reverse == true) {
                        System.out.println("wborder2");
                        endBugging();
                        return 8;
                    } 
                    //! first time hitting wall
                    bugNavTracker.reverse = true;
                    bugNavTracker.passed = new FastIntSet();
                    dirToMove = originalPivotDir;
                    break;
            }

            //! only reaches if ALLY_STUCK, WALL, or first time BORDER
            dirToMove = rotateDir(dirToMove, bugNavTracker.reverse);
            
        }

        //! no satisfactory move: error or all blocked by stuck ducks
        isStuck = true;
        return 8;
    }

    //! bugnav util functions
    public static void startBugging(Direction pivot) throws GameActionException {
        isBugging = true;
        bugNavTracker = new BugNavTracker(pivot);
        ROTATE = 1;
        if (betterRotate) {
            int dirMain = getBugMove();
            MapLocation locMain = location.add(Utils.DIRS_CENTER[dirMain]);
            ROTATE = -1;
            int dirRev = getBugMove();
            MapLocation locRev = location.add(Utils.DIRS_CENTER[dirRev]);
            MapLocation center = new MapLocation(Utils.MAP_WIDTH/2, Utils.MAP_HEIGHT/2);
            if (locMain.distanceSquaredTo(target) < locRev.distanceSquaredTo(target))
                ROTATE = 1;
            else if (locMain.distanceSquaredTo(target) > locRev.distanceSquaredTo(target))
                ROTATE = -1;
            else if (locMain.distanceSquaredTo(center) <= locRev.distanceSquaredTo(center))
                ROTATE = 1;
        }
    }
    
    public static void endBugging() {
        //ROTATE = -ROTATE;
        isBugging = false;
        bugNavTracker = null;
        isStuck = false;
    }

    //! other util functions
    private static void senseAdjacents() throws GameActionException {
        dirMapLocs[0] = location.add(Utils.DIRECTIONS[0]);
        dirMapLocs[1] = location.add(Utils.DIRECTIONS[1]);
        dirMapLocs[2] = location.add(Utils.DIRECTIONS[2]);
        dirMapLocs[3] = location.add(Utils.DIRECTIONS[3]);
        dirMapLocs[4] = location.add(Utils.DIRECTIONS[4]);
        dirMapLocs[5] = location.add(Utils.DIRECTIONS[5]);
        dirMapLocs[6] = location.add(Utils.DIRECTIONS[6]);
        dirMapLocs[7] = location.add(Utils.DIRECTIONS[7]);
        
        dirMapInfos[0] = !Utils.isInMap(dirMapLocs[0]) ? null : rc.senseMapInfo(dirMapLocs[0]);
        dirMapInfos[1] = !Utils.isInMap(dirMapLocs[1]) ? null : rc.senseMapInfo(dirMapLocs[1]);
        dirMapInfos[2] = !Utils.isInMap(dirMapLocs[2]) ? null : rc.senseMapInfo(dirMapLocs[2]);
        dirMapInfos[3] = !Utils.isInMap(dirMapLocs[3]) ? null : rc.senseMapInfo(dirMapLocs[3]);
        dirMapInfos[4] = !Utils.isInMap(dirMapLocs[4]) ? null : rc.senseMapInfo(dirMapLocs[4]);
        dirMapInfos[5] = !Utils.isInMap(dirMapLocs[5]) ? null : rc.senseMapInfo(dirMapLocs[5]);
        dirMapInfos[6] = !Utils.isInMap(dirMapLocs[6]) ? null : rc.senseMapInfo(dirMapLocs[6]);
        dirMapInfos[7] = !Utils.isInMap(dirMapLocs[7]) ? null : rc.senseMapInfo(dirMapLocs[7]);

        dirRobots[0] = (dirMapInfos[0] != null && dirMapInfos[0].isPassable()) ? rc.senseRobotAtLocation(dirMapLocs[0]) : null;
        dirRobots[1] = (dirMapInfos[1] != null && dirMapInfos[1].isPassable()) ? rc.senseRobotAtLocation(dirMapLocs[1]) : null;
        dirRobots[2] = (dirMapInfos[2] != null && dirMapInfos[2].isPassable()) ? rc.senseRobotAtLocation(dirMapLocs[2]) : null;
        dirRobots[3] = (dirMapInfos[3] != null && dirMapInfos[3].isPassable()) ? rc.senseRobotAtLocation(dirMapLocs[3]) : null;
        dirRobots[4] = (dirMapInfos[4] != null && dirMapInfos[4].isPassable()) ? rc.senseRobotAtLocation(dirMapLocs[4]) : null;
        dirRobots[5] = (dirMapInfos[5] != null && dirMapInfos[5].isPassable()) ? rc.senseRobotAtLocation(dirMapLocs[5]) : null;
        dirRobots[6] = (dirMapInfos[6] != null && dirMapInfos[6].isPassable()) ? rc.senseRobotAtLocation(dirMapLocs[6]) : null;
        dirRobots[7] = (dirMapInfos[7] != null && dirMapInfos[7].isPassable()) ? rc.senseRobotAtLocation(dirMapLocs[7]) : null;
    
        dirStatus[0] = genDirStatus(0);
        dirStatus[1] = genDirStatus(1);
        dirStatus[2] = genDirStatus(2);
        dirStatus[3] = genDirStatus(3);
        dirStatus[4] = genDirStatus(4);
        dirStatus[5] = genDirStatus(5);
        dirStatus[6] = genDirStatus(6);
        dirStatus[7] = genDirStatus(7);
    }

    private static int genDirStatus(int dirInt) {
        if (dirMapInfos[dirInt] == null) {
            return (!isBugging || !bugNavTracker.reverse)?BORDER:WALL;
        } else if (dirMapInfos[dirInt].isWall()) {
            return WALL;
        } else if (dirMapInfos[dirInt].isDam()) {
            return ALLY_UNSTUCK;
        } else if (dirMapInfos[dirInt].isWater()) {
            return WATER;
        }
        if (dirRobots[dirInt] == null)
            return OPEN;
        return Comms.allyStatus[Comms.IDToMoveOrder.getVal(dirRobots[dirInt].getID())] == 1 ? ALLY_STUCK : ALLY_UNSTUCK;
    }

    private static int rotateDir(int dirInt, boolean reverse) {
        return reverse ? (dirInt - BugNav.ROTATE + 8) % 8 : (dirInt + BugNav.ROTATE + 8) % 8;    
    }
}

class BugNavTracker {
    int pivotDir;
    int pPivotDir;
    int ppPivotDir;
    boolean reverse;

    int orginalDist;
    MapLocation originalPos;
    Direction originalDir;
    int originalTurn;

    FastIntSet passed;

    public BugNavTracker(int pivotDirInt) {
        pivotDir = pivotDirInt;
        pPivotDir = 8;
        ppPivotDir = 8;
        reverse = false;

        orginalDist = BugNav.location.distanceSquaredTo(BugNav.target);
        originalPos = BugNav.location;
        originalDir = Utils.DIRS_CENTER[pivotDirInt];
        originalTurn = Comms.roundNumber;

        passed = new FastIntSet();
    }
    
    public BugNavTracker(Direction pivotDir) {
        this(Utils.dxDyToInt(pivotDir));
    }

    public void updatePivot(int pivotDirInt) {
        ppPivotDir = pPivotDir;
        pPivotDir = pivotDir;
        pivotDir = pivotDirInt;
    } 

    //! returns true if fails
    public boolean revertPivot() {
        if (pPivotDir == 8 || ppPivotDir == 8) 
            return true;

        int newPivotDx = (Utils.INT_TO_DX[ppPivotDir] - Utils.INT_TO_DX[pPivotDir]);
        int newPivotDy = (Utils.INT_TO_DY[ppPivotDir] - Utils.INT_TO_DY[pPivotDir]);

        if (newPivotDx > 1 || newPivotDx < -1 || newPivotDy > 1 || newPivotDy < 1)
            return true;

        pivotDir = Utils.dxDyToInt(newPivotDx, newPivotDy);
        pPivotDir = 8;
        ppPivotDir = 8;

        //! if no problems
        return false;
    }

    public int nextPivot() {
        return reverse ? (pivotDir - BugNav.ROTATE + 8) % 8 : (pivotDir + BugNav.ROTATE + 8) % 8;
    }

    //! returns true if fails
    public boolean moveToNextPivot() {
        passed.add((Utils.locationToInt(BugNav.rc.getLocation().add(Utils.DIRS_CENTER[nextPivot()].opposite())) << 3) | pivotDir);

        int nextPivot = nextPivot();
        int newPivotDx = Utils.INT_TO_DX[pivotDir] - Utils.INT_TO_DX[nextPivot];
        int newPivotDy = Utils.INT_TO_DY[pivotDir] - Utils.INT_TO_DY[nextPivot];

        // System.out.println(String.valueOf(Utils.INT_TO_DX[pivotDir]) + " " + String.valueOf(Utils.INT_TO_DY[pivotDir]));
        // System.out.println(String.valueOf(Utils.INT_TO_DX[nextPivot]) + " " + String.valueOf(Utils.INT_TO_DY[nextPivot]));
        // System.out.println(String.valueOf(newPivotDx) + " " + String.valueOf(newPivotDy));

        if (newPivotDx > 1 || newPivotDx < -1 || newPivotDy > 1 || newPivotDy < -1) {
            return true;
        }

        ppPivotDir = pPivotDir;
        pPivotDir = pivotDir;
        pivotDir = Utils.dxDyToInt(newPivotDx, newPivotDy);
        return false;
    }
}
