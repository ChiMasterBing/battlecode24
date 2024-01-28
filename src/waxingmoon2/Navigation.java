package waxingmoon2;
import java.util.Arrays;

import battlecode.common.*;
import waxingmoon2.fast.*;

public class Navigation {
    static RobotController rc;

    static final int UNKNOWN = 0;
    static final int EMPTY = 1;
    static final int WATER = 2;
    static final int WALL = 3;
    public static final int H_SYM = 0;
    public static final int V_SYM = 1;
    public static final int R_SYM = 2;
    static final int NO_WATER_ROUND = 0; 
    static final int SYMM_BYTECODE = 5000; // do not run this if < 5000 bc
    
    static int[] spawnScores = {0, 0, 0};
    private static FastQueue<MapLocation> symmQueue = new FastQueue<MapLocation>(500); 

    

    static int WIDTH, HEIGHT;
    static int[][] map;
    static short[][] dam;

    //* NEW
    static MapInfo[] tiles;
    static MapLocation location;
    static long[] wallBitmap;
    static long[] damBitmap;
    
    public static void init(RobotController r) {
        rc = r;
        WIDTH = rc.getMapWidth();
        HEIGHT = rc.getMapHeight();
        dam = new short[WIDTH][HEIGHT];
        map = new int[WIDTH][HEIGHT];

        //* NEW
        tiles = null;
        location = new MapLocation(0, 0);
        wallBitmap = fullBitmap();
        damBitmap = emptyBitmap();
    }

    public static void scout() { //Scouts surrounding squares and updates symmetry
        tiles = rc.senseNearbyMapInfos();
        for (MapInfo tile:tiles) {
            updateTile(tile);
        }
    }
    
    public static void updateTile(MapInfo m) { //updates each new tiles seen, pushes unseen tiles to queue to process symmetry
        int type = 3;
        MapLocation pos = m.getMapLocation();
        if (m.isWall()) {
            type = WALL;
            //* NEW
            wallBitmap[pos.y - 1] |= 1 << (pos.x - 1);
        }
        else if (m.isWater()) {
            type = WATER;
        }
        else if (m.isDam()) {
            //* NEW
            damBitmap[pos.y - 1] |= 1 << (pos.x - 1);
        }
        else {
            type = EMPTY;
        }
        //dams are separate
        boolean toAdd = false;
        if (rc.getRoundNum() < 200) {
            if (dam[pos.x][pos.y] == 0) {
                toAdd = true;
                if (m.isDam()) {
                    dam[pos.x][pos.y] = 2;
                }
                else {
                    dam[pos.x][pos.y] = 1;
                }
            }
        }
        if (type != map[pos.x][pos.y]) {
            toAdd = true;
            map[pos.x][pos.y] = type;                 
        }   
        if (toAdd) symmQueue.add(pos);
    }

    static MapLocation getHSym(MapLocation loc){ return new MapLocation (WIDTH - loc.x - 1, loc.y); }
    static MapLocation getVSym(MapLocation loc){ return new MapLocation (loc.x, HEIGHT - loc.y - 1); }
    static MapLocation getRSym(MapLocation loc){ return new MapLocation (WIDTH - loc.x - 1, HEIGHT - loc.y - 1); }
    
    public static void updateSymm() throws GameActionException {
        boolean isHSYM = Comms.isSymmetry(H_SYM);
        boolean isVSYM = Comms.isSymmetry(V_SYM);
        boolean isRSYM = Comms.isSymmetry(R_SYM);

        while (!symmQueue.isEmpty()) {
            if (Clock.getBytecodesLeft() < SYMM_BYTECODE) return;
            MapLocation pos = symmQueue.poll();
            int curType = map[pos.x][pos.y];
            if (rc.getRoundNum() < 200) { //check dam symmetry
                if (isHSYM) {
                    MapLocation nxt = getHSym(pos);
                    if (rc.onTheMap(nxt) && (dam[nxt.x][nxt.y] ^ dam[pos.x][pos.y]) == 3) { //checks if its 1,2
                        isHSYM = false;
                        Comms.invalidateSymmetry(H_SYM);
                    }
                }
                if (isVSYM) {
                    MapLocation nxt = getVSym(pos);
                    if (rc.onTheMap(nxt) && (dam[nxt.x][nxt.y] ^ dam[pos.x][pos.y]) == 3) {
                        isVSYM = false;
                        Comms.invalidateSymmetry(V_SYM);
                    }
                }
                if (isRSYM) {
                    MapLocation nxt = getRSym(pos);
                    if (rc.onTheMap(nxt) && (dam[nxt.x][nxt.y] ^ dam[pos.x][pos.y]) == 3) {
                        isRSYM = false;
                        Comms.invalidateSymmetry(R_SYM);
                    }
                }
            }
            switch (curType) {
                case WATER:
                    if (rc.getRoundNum() > NO_WATER_ROUND) break;
                    if (isHSYM) {
                        MapLocation nxt = getHSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL || map[nxt.x][nxt.y] == EMPTY)) {
                            isHSYM = false;
                            Comms.invalidateSymmetry(H_SYM);
                        }
                    }
                    if (isVSYM) {
                        MapLocation nxt = getVSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL || map[nxt.x][nxt.y] == EMPTY)) {
                            isVSYM = false;
                            Comms.invalidateSymmetry(V_SYM);
                        }
                    }
                    if (isRSYM) {
                        MapLocation nxt = getRSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL || map[nxt.x][nxt.y] == EMPTY)) {
                            isRSYM = false;
                            Comms.invalidateSymmetry(R_SYM);
                        }
                    }
                    break;
                case WALL:
                    if (isHSYM) {
                        MapLocation nxt = getHSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WATER || map[nxt.x][nxt.y] == EMPTY)) {
                            isHSYM = false;
                            Comms.invalidateSymmetry(H_SYM);
                        }
                    }
                    if (isVSYM) {
                        MapLocation nxt = getVSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WATER || map[nxt.x][nxt.y] == EMPTY)) {
                            isVSYM = false;
                            Comms.invalidateSymmetry(V_SYM);
                        }
                    }
                    if (isRSYM) {
                        MapLocation nxt = getRSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WATER || map[nxt.x][nxt.y] == EMPTY)) {
                            isRSYM = false;
                            Comms.invalidateSymmetry(R_SYM);
                        }
                    }
                    break;
                case EMPTY:
                    if (isHSYM) {
                        MapLocation nxt = getHSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL) || (map[nxt.x][nxt.y] == WATER && rc.getRoundNum() <= NO_WATER_ROUND)) {
                            isHSYM = false;
                            Comms.invalidateSymmetry(H_SYM);
                        }
                    }
                    if (isVSYM) {
                        MapLocation nxt = getVSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL) || (map[nxt.x][nxt.y] == WATER && rc.getRoundNum() <= NO_WATER_ROUND)) {
                            isVSYM = false;
                            Comms.invalidateSymmetry(V_SYM);
                        }
                    }
                    if (isRSYM) {
                        MapLocation nxt = getRSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL) || (map[nxt.x][nxt.y] == WATER && rc.getRoundNum() <= NO_WATER_ROUND)) {
                            isRSYM = false;
                            Comms.invalidateSymmetry(R_SYM);
                        }
                    }
                    break;
            }
        }  
    }

    //returns the minimum of distances of a to (b, c, d).
    public static int getTripleMinDist(MapLocation a, MapLocation b, MapLocation c, MapLocation d) {
        return Math.min(a.distanceSquaredTo(b), Math.min(a.distanceSquaredTo(c), a.distanceSquaredTo(d)));
    }

    //returns the index of minimum of distances of a to (b, c, d)
    public static int getClosestSpawnNumber(MapLocation a, MapLocation b, MapLocation c, MapLocation d) {
        int d1 = a.distanceSquaredTo(b);
        int d2 = a.distanceSquaredTo(c);
        int d3 = a.distanceSquaredTo(d);
        int d4 = Math.min(d1, Math.min(d2, d3));
        if (d4 == d1) return 0;
        else if (d4 == d2) return 1;
        else return 2;
    }

    public static void eliminateSpawnSymmetries(MapLocation s1, MapLocation s2, MapLocation s3) throws GameActionException {
        MapLocation o1, o2, o3;
        //H_SYM
        o1 = getHSym(s1);
        o2 = getHSym(s2);
        o3 = getHSym(s3);
        if (getTripleMinDist(o1, s1, s2, s3) < 36 || getTripleMinDist(o2, s1, s2, s3) < 36 || getTripleMinDist(o3, s1, s2, s3) < 36) {
            Comms.invalidateSymmetry(H_SYM);
        }

        //V_SYM
        o1 = getVSym(s1);
        o2 = getVSym(s2);
        o3 = getVSym(s3);
        if (getTripleMinDist(o1, s1, s2, s3) < 36 || getTripleMinDist(o2, s1, s2, s3) < 36 || getTripleMinDist(o3, s1, s2, s3) < 36) {
            Comms.invalidateSymmetry(V_SYM);
        }

        //R_SYM
        o1 = getRSym(s1);
        o2 = getRSym(s2);
        o3 = getRSym(s3);
        if (getTripleMinDist(o1, s1, s2, s3) < 36 || getTripleMinDist(o2, s1, s2, s3) < 36 || getTripleMinDist(o3, s1, s2, s3) < 36) {
            Comms.invalidateSymmetry(R_SYM);
        }
    }

    public static void spawnHeuristic(MapLocation o1, MapLocation s1, MapLocation s2, MapLocation s3) {
        //going to o1, starting at s1
        int d1 = (int) Math.sqrt(s1.distanceSquaredTo(o1));
        int d2 = (int) Math.sqrt(s2.distanceSquaredTo(o1));
        int d3 = (int) Math.sqrt(s3.distanceSquaredTo(o1));

        int d12 = (int) Math.sqrt(s1.distanceSquaredTo(s2));
        int d13 = (int) Math.sqrt(s1.distanceSquaredTo(s3));
        int d23 = (int) Math.sqrt(s2.distanceSquaredTo(s3));

        spawnScores[0] += Math.min(d12 + d2, d13 + d3) - d1;
        spawnScores[1] += Math.min(d12 + d1, d23 + d3) - d2;
        spawnScores[2] += Math.min(d13 + d1, d23 + d2) - d3;
    }

    public static void calculateSpawnDistribution(MapLocation s1, MapLocation s2, MapLocation s3) {
        MapLocation o1, o2, o3;
        if (Comms.isSymmetry(H_SYM)) {
            o1 = getHSym(s1);
            o2 = getHSym(s2);
            o3 = getHSym(s3);

            spawnHeuristic(o1, s1, s2, s3);
            spawnHeuristic(o2, s1, s2, s3);
            spawnHeuristic(o3, s1, s2, s3);

        }
        if (Comms.isSymmetry(V_SYM)) {
            o1 = getVSym(s1);
            o2 = getVSym(s2);
            o3 = getVSym(s3);

            spawnHeuristic(o1, s1, s2, s3);
            spawnHeuristic(o2, s1, s2, s3);
            spawnHeuristic(o3, s1, s2, s3);

        }
        if (Comms.isSymmetry(R_SYM)) {
            o1 = getRSym(s1);
            o2 = getRSym(s2);
            o3 = getRSym(s3);

            spawnHeuristic(o1, s1, s2, s3);
            spawnHeuristic(o2, s1, s2, s3);
            spawnHeuristic(o3, s1, s2, s3);
        }
    }
    
    // public static void calculateSpawnDistribution(MapLocation s1, MapLocation s2, MapLocation s3) {
    //     MapLocation o1, o2, o3;
    //     if (Comms.isSymmetry(H_SYM)) {
    //         o1 = getHSym(s1);
    //         o2 = getHSym(s2);
    //         o3 = getHSym(s3);
    //         spawnScores[getClosestSpawnNumber(o1, s1, s2, s3)]++;
    //         spawnScores[getClosestSpawnNumber(o2, s1, s2, s3)]++;
    //         spawnScores[getClosestSpawnNumber(o3, s1, s2, s3)]++;
    //     }
    //     if (Comms.isSymmetry(V_SYM)) {
    //         o1 = getVSym(s1);
    //         o2 = getVSym(s2);
    //         o3 = getVSym(s3);
    //         spawnScores[getClosestSpawnNumber(o1, s1, s2, s3)]++;
    //         spawnScores[getClosestSpawnNumber(o2, s1, s2, s3)]++;
    //         spawnScores[getClosestSpawnNumber(o3, s1, s2, s3)]++;
    //     }
    //     if (Comms.isSymmetry(R_SYM)) {
    //         o1 = getRSym(s1);
    //         o2 = getRSym(s2);
    //         o3 = getRSym(s3);
    //         spawnScores[getClosestSpawnNumber(o1, s1, s2, s3)]++;
    //         spawnScores[getClosestSpawnNumber(o2, s1, s2, s3)]++;
    //         spawnScores[getClosestSpawnNumber(o3, s1, s2, s3)]++;
    //     }
    // }

    //* NEW METHODS
    public static long[] emptyBitmap() {
        long[] ret = new long[Utils.MAP_HEIGHT];
        for (int i = Utils.MAP_HEIGHT; --i > 0;) {
            ret[i] = Utils.BASIC_MASKS[64] ^ Utils.BASIC_MASKS[Utils.MAP_WIDTH];
        }
        return ret;
    }
    public static long[] fullBitmap() {
        long[] ret = new long[Utils.MAP_HEIGHT];
        for (int i = Utils.MAP_HEIGHT; --i > 0;) {
            ret[i] = Utils.BASIC_MASKS[64];
        }
        return ret;
    }
}

class BitBFS {
    final static int MAX_BFS_TURNS = 50;
    final static long SWAR_K1 = 0x5555555555555555L;
    final static long SWAR_K2 = 0x3333333333333333L;
    final static long SWAR_K4 = 0x0f0f0f0f0f0f0f0fL;
    final static long SWAR_KF = 0x0101010101010101L;

    static long[] inverseWallMask = null;

    //! ---------------------------------------------- //
    //! --------------- PUBLIC METHODS --------------- //
    //! ---------------------------------------------- //

    public static long[] getSubArray(int xL, int xR, int yL, int yR) {
        //! placeholder
        long[] ret = null;
        return ret;
    }

    public static int swarPopcount(long[] mask) {
        //! placeholder
        int sum = 0;
        for (long x: mask) {
            x &= Utils.BASIC_MASKS[Utils.MAP_WIDTH];
            x = x - ((x >> 1) & SWAR_K1);
            x = (x & SWAR_K2) + ((x >> 2) & SWAR_K2);
            x = (x + (x >> 4)) & SWAR_K4;
            x = (x * SWAR_KF) >> 56;
            sum += x;
        }
        return sum;
    }

    public static long[][] bfs() {
        MapLocation location = Navigation.rc.getLocation();
        return bfs(location.x, location.y);
    }
    public static long[][] bfs(int xOrigin, int yOrigin) {
        long[] bitmap = Navigation.emptyBitmap();
        bitmap[yOrigin - 1] |= 1 << (xOrigin - 1);
        return bfs(bitmap);
    }
    public static long[][] bfs(long[] bitmapOrigin) {
        inverseWallMask = invertMask(Navigation.wallBitmap);
        long[][] ret = new long[MAX_BFS_TURNS][9];
        int i = 0;

        long[] initialMask = copyMask(bitmapOrigin);
        andMask(initialMask, inverseWallMask);
        ret[0] = initialMask;
        long[] passedMask = invertMask(initialMask);

        while (i < MAX_BFS_TURNS && !isZero(ret[i])) {
            long[] newMask = shiftDirections(ret[i]);

            andMask(newMask, inverseWallMask);
            andMask(newMask, passedMask);
            andMask(passedMask, invertMask(newMask));

            i += 1;
            ret[i] = newMask;
        }

        return ret;
    }

    public static long[][] floodfill() {
        MapLocation location = Navigation.rc.getLocation();
        return bfs(location.x, location.y);
    }
    public static long[][] floodfill(int xOrigin, int yOrigin) {
        long[] bitmap = Navigation.emptyBitmap();
        bitmap[yOrigin - 1] |= 1 << (xOrigin - 1);
        return bfs(bitmap);
    }
    public long[] floodfill(long[] bitmapOrigin) {
        long[] passed = Navigation.emptyBitmap();
        long[] turn = copyMask(bitmapOrigin);
        andMask(turn, inverseWallMask);

        while (areDifferent(turn, passed)) {
            orMask(passed, turn);
            turn = shiftDirectionsCenter(passed);
            andMask(turn, inverseWallMask);
        }

        return passed;
    }

    //! ---------------------------------------------- //
    //! --------------- PRIVATE METHODS -------------- //
    //! ---------------------------------------------- //

    //! gets all possible next locations given either 8 directions or 8+CENTER
    private static long[] shiftDirections(long[] mask) {
        long[] ret = Navigation.emptyBitmap();

        ret[0] = (mask[0] << 1) | (mask[0] >> 1) | (mask[1] << 1) | mask[1] | (mask[1] >> 1);
        ret[1] = (mask[0] << 1) | mask[0] | (mask[0] >> 1) | (mask[1] << 1) | (mask[1] >> 1) | (mask[2] << 1) | mask[2] | (mask[2] >> 1);
        ret[2] = (mask[1] << 1) | mask[1] | (mask[1] >> 1) | (mask[2] << 1) | (mask[2] >> 1) | (mask[3] << 1) | mask[3] | (mask[3] >> 1);
        ret[3] = (mask[2] << 1) | mask[2] | (mask[2] >> 1) | (mask[3] << 1) | (mask[3] >> 1) | (mask[4] << 1) | mask[4] | (mask[4] >> 1);
        ret[4] = (mask[3] << 1) | mask[3] | (mask[3] >> 1) | (mask[4] << 1) | (mask[4] >> 1) | (mask[5] << 1) | mask[5] | (mask[5] >> 1);
        ret[5] = (mask[4] << 1) | mask[4] | (mask[4] >> 1) | (mask[5] << 1) | (mask[5] >> 1) | (mask[6] << 1) | mask[6] | (mask[6] >> 1);
        ret[6] = (mask[5] << 1) | mask[5] | (mask[5] >> 1) | (mask[6] << 1) | (mask[6] >> 1) | (mask[7] << 1) | mask[7] | (mask[7] >> 1);
        ret[7] = (mask[6] << 1) | mask[6] | (mask[6] >> 1) | (mask[7] << 1) | (mask[7] >> 1) | (mask[8] << 1) | mask[8] | (mask[8] >> 1);
        ret[8] = (mask[7] << 1) | mask[7] | (mask[7] >> 1) | (mask[8] << 1) | (mask[8] >> 1);

        ret[0] &= Utils.BASIC_MASKS[31];
        ret[1] &= Utils.BASIC_MASKS[31];
        ret[2] &= Utils.BASIC_MASKS[31];
        ret[3] &= Utils.BASIC_MASKS[31];
        ret[4] &= Utils.BASIC_MASKS[31];
        ret[5] &= Utils.BASIC_MASKS[31];
        ret[6] &= Utils.BASIC_MASKS[31];
        ret[7] &= Utils.BASIC_MASKS[31];
        ret[8] &= Utils.BASIC_MASKS[31];

        return ret;
    }
    private static long[] shiftDirectionsCenter(long[] mask) {
        long[] ret = Navigation.emptyBitmap();

        ret[0] = (mask[0] << 1) | mask[0] | (mask[0] >> 1) | (mask[1] << 1) | mask[1] | (mask[1] >> 1);
        ret[1] = (mask[0] << 1) | mask[0] | (mask[0] >> 1) | (mask[1] << 1) | mask[1] | (mask[1] >> 1) | (mask[2] << 1) | mask[2] | (mask[2] >> 1);
        ret[2] = (mask[1] << 1) | mask[1] | (mask[1] >> 1) | (mask[2] << 1) | mask[2] | (mask[2] >> 1) | (mask[3] << 1) | mask[3] | (mask[3] >> 1);
        ret[3] = (mask[2] << 1) | mask[2] | (mask[2] >> 1) | (mask[3] << 1) | mask[3] | (mask[3] >> 1) | (mask[4] << 1) | mask[4] | (mask[4] >> 1);
        ret[4] = (mask[3] << 1) | mask[3] | (mask[3] >> 1) | (mask[4] << 1) | mask[4] | (mask[4] >> 1) | (mask[5] << 1) | mask[5] | (mask[5] >> 1);
        ret[5] = (mask[4] << 1) | mask[4] | (mask[4] >> 1) | (mask[5] << 1) | mask[5] | (mask[5] >> 1) | (mask[6] << 1) | mask[6] | (mask[6] >> 1);
        ret[6] = (mask[5] << 1) | mask[5] | (mask[5] >> 1) | (mask[6] << 1) | mask[6] | (mask[6] >> 1) | (mask[7] << 1) | mask[7] | (mask[7] >> 1);
        ret[7] = (mask[6] << 1) | mask[6] | (mask[6] >> 1) | (mask[7] << 1) | mask[7] | (mask[7] >> 1) | (mask[8] << 1) | mask[8] | (mask[8] >> 1);
        ret[8] = (mask[7] << 1) | mask[7] | (mask[7] >> 1) | (mask[8] << 1) | mask[8] | (mask[8] >> 1);

        ret[0] &= Utils.BASIC_MASKS[31];
        ret[1] &= Utils.BASIC_MASKS[31];
        ret[2] &= Utils.BASIC_MASKS[31];
        ret[3] &= Utils.BASIC_MASKS[31];
        ret[4] &= Utils.BASIC_MASKS[31];
        ret[5] &= Utils.BASIC_MASKS[31];
        ret[6] &= Utils.BASIC_MASKS[31];
        ret[7] &= Utils.BASIC_MASKS[31];
        ret[8] &= Utils.BASIC_MASKS[31];

        return ret;
    }

    //! if mask is zero
    private static boolean isZero(long[] mask) {
        return (mask[0]|mask[1]|mask[2]|mask[3]|mask[4]|mask[5]|mask[6]|mask[7]|mask[8]) != 0;
    }
    //! if masks are different
    private static boolean areDifferent(long[] mask1, long[] mask2) {
        return 
        (mask1[0]!=mask2[0]) || 
        (mask1[1]!=mask2[1]) || 
        (mask1[2]!=mask2[2]) || 
        (mask1[3]!=mask2[3]) || 
        (mask1[4]!=mask2[4]) ||
        (mask1[5]!=mask2[5]) || 
        (mask1[6]!=mask2[6]) || 
        (mask1[7]!=mask2[7]) || 
        (mask1[8]!=mask2[8]);
    }
    //! ands entire mask
    private static void andMask(long[] target, long[] mask) {
        target[0] &= mask[0];
        target[1] &= mask[1];
        target[2] &= mask[2];
        target[3] &= mask[3];
        target[4] &= mask[4];
        target[5] &= mask[5];
        target[6] &= mask[6];
        target[7] &= mask[7];
        target[8] &= mask[8]; 
    }
    //! ors entire mask
    private static void orMask(long[] target, long[] mask) {
        target[0] |= mask[0];
        target[1] |= mask[1];
        target[2] |= mask[2];
        target[3] |= mask[3];
        target[4] |= mask[4];
        target[5] |= mask[5];
        target[6] |= mask[6];
        target[7] |= mask[7];
        target[8] |= mask[8]; 
    }
    //! inverts entire mask
    private static long[] invertMask(long[] mask) {
        long[] ret = new long[mask.length];
        for (int i = mask.length; i++ > 0;) {
            ret[i] = ~mask[i];
        }
        return ret;
    }
    //! copies entire mask
    private static long[] copyMask(long[] mask) {
        long[] ret = new long[mask.length];
        for (int i = mask.length; i++ > 0;) {
            ret[i] = mask[i];
        }
        return ret;
    }
    
    // //! mask with all 0
    // private static long[] empty() {
    //     long[] ret = {0,0,0,0,0,0,0,0,0};
    //     return ret;
    // }
    // //! mask with walls where vision ends
    // private static long[] vision() {
    //     long[] ret = {
    //         0b1111111111111111111111110000011,
    //         0b1111111111111111111111100000001,
    //         0b1111111111111111111111000000000,
    //         0b1111111111111111111111000000000,
    //         0b1111111111111111111111000000000,
    //         0b1111111111111111111111000000000,
    //         0b1111111111111111111111000000000,
    //         0b1111111111111111111111100000001,
    //         0b1111111111111111111111110000011
    //     };
    //     return ret;
    // }
    // //! mask with all 1
    // private static long[] filled() {
    //     return new long[] {~0, ~0, 0b111111111, 0b111111111, 0b111111111, 0b111111111, 0b111111111, 0b111111111, 0b111111111};
    // }

}