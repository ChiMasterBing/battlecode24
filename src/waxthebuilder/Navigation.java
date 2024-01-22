package waxthebuilder;
import java.util.Arrays;

import battlecode.common.*;
import waxthebuilder.fast.FastQueue;

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

    static boolean[] symmetries = {true, true, true};
    static int[] spawnScores = {0, 0, 0};
    private static FastQueue<MapLocation> symmQueue = new FastQueue<MapLocation>(500); 

    static int WIDTH, HEIGHT;
    static int[][] map;
    static short[][] dam;
    public static void init(RobotController r) {
        rc = r;
        WIDTH = rc.getMapWidth();
        HEIGHT = rc.getMapHeight();
        dam = new short[WIDTH][HEIGHT];
        map = new int[WIDTH][HEIGHT];
    }

    public static void fetchSymmetries() { //Fetches symmetries from communication
        if (symmetries[V_SYM]) {
            if (!Comms.isSymmetry(V_SYM)) symmetries[V_SYM] = false;
        }
        if (symmetries[H_SYM]) {
            if (!Comms.isSymmetry(H_SYM)) symmetries[H_SYM] = false;
        }
        if (symmetries[R_SYM]) {
            if (!Comms.isSymmetry(R_SYM)) symmetries[R_SYM] = false;
        }
    }

    public static void scout() { //Scouts surrounding squares and updates symmetry
        fetchSymmetries();
        MapInfo[] tiles = rc.senseNearbyMapInfos();
        for (MapInfo tile:tiles) {
            updateTile(tile);
        }
    }
    
    public static void updateTile(MapInfo m) { //updates each new tiles seen, pushes unseen tiles to queue to process symmetry
        int type = 3;
        MapLocation pos = m.getMapLocation();
        if (m.isWall()) {
            type = WALL;
        }
        else if (m.isWater()) {
            type = WATER;
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
        while (!symmQueue.isEmpty()) {
            if (Clock.getBytecodesLeft() < SYMM_BYTECODE) return;
            MapLocation pos = symmQueue.poll();
            int curType = map[pos.x][pos.y];
            if (rc.getRoundNum() < 200) { //check dam symmetry
                if (symmetries[H_SYM]) {
                    MapLocation nxt = getHSym(pos);
                    if (rc.onTheMap(nxt) && (dam[nxt.x][nxt.y] ^ dam[pos.x][pos.y]) == 3) { //checks if its 1,2
                        symmetries[H_SYM] = false;
                        Comms.invalidateSymmetry(H_SYM);
                    }
                }
                if (symmetries[V_SYM]) {
                    MapLocation nxt = getVSym(pos);
                    if (rc.onTheMap(nxt) && (dam[nxt.x][nxt.y] ^ dam[pos.x][pos.y]) == 3) {
                        symmetries[V_SYM] = false;
                        Comms.invalidateSymmetry(V_SYM);
                    }
                }
                if (symmetries[R_SYM]) {
                    MapLocation nxt = getRSym(pos);
                    if (rc.onTheMap(nxt) && (dam[nxt.x][nxt.y] ^ dam[pos.x][pos.y]) == 3) {
                        symmetries[R_SYM] = false;
                        Comms.invalidateSymmetry(R_SYM);
                    }
                }
            }
            switch (curType) {
                case WATER:
                    if (rc.getRoundNum() > NO_WATER_ROUND) break;
                    if (symmetries[H_SYM]) {
                        MapLocation nxt = getHSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL || map[nxt.x][nxt.y] == EMPTY)) {
                            symmetries[H_SYM] = false;
                            Comms.invalidateSymmetry(H_SYM);
                        }
                    }
                    if (symmetries[V_SYM]) {
                        MapLocation nxt = getVSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL || map[nxt.x][nxt.y] == EMPTY)) {
                            symmetries[V_SYM] = false;
                            Comms.invalidateSymmetry(V_SYM);
                        }
                    }
                    if (symmetries[R_SYM]) {
                        MapLocation nxt = getRSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL || map[nxt.x][nxt.y] == EMPTY)) {
                            symmetries[R_SYM] = false;
                            Comms.invalidateSymmetry(R_SYM);
                        }
                    }
                    break;
                case WALL:
                    if (symmetries[H_SYM]) {
                        MapLocation nxt = getHSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WATER || map[nxt.x][nxt.y] == EMPTY)) {
                            symmetries[H_SYM] = false;
                            Comms.invalidateSymmetry(H_SYM);
                        }
                    }
                    if (symmetries[V_SYM]) {
                        MapLocation nxt = getVSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WATER || map[nxt.x][nxt.y] == EMPTY)) {
                            symmetries[V_SYM] = false;
                            Comms.invalidateSymmetry(V_SYM);
                        }
                    }
                    if (symmetries[R_SYM]) {
                        MapLocation nxt = getRSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WATER || map[nxt.x][nxt.y] == EMPTY)) {
                            symmetries[R_SYM] = false;
                            Comms.invalidateSymmetry(R_SYM);
                        }
                    }
                    break;
                case EMPTY:
                    if (symmetries[H_SYM]) {
                        MapLocation nxt = getHSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL) || (map[nxt.x][nxt.y] == WATER && rc.getRoundNum() <= NO_WATER_ROUND)) {
                            symmetries[H_SYM] = false;
                            Comms.invalidateSymmetry(H_SYM);
                        }
                    }
                    if (symmetries[V_SYM]) {
                        MapLocation nxt = getVSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL) || (map[nxt.x][nxt.y] == WATER && rc.getRoundNum() <= NO_WATER_ROUND)) {
                            symmetries[V_SYM] = false;
                            Comms.invalidateSymmetry(V_SYM);
                        }
                    }
                    if (symmetries[R_SYM]) {
                        MapLocation nxt = getRSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL) || (map[nxt.x][nxt.y] == WATER && rc.getRoundNum() <= NO_WATER_ROUND)) {
                            symmetries[R_SYM] = false;
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
}
