package bling3;
import java.util.Arrays;
import battlecode.common.*;
import bling3.fast.FastQueue;

public class macroPath {
    
    static RobotController rc;

    static final int UNKNOWN = 0;
    static final int EMPTY = 1;
    static final int WATER = 2;
    static final int WALL = 3;

    static int WIDTH, HEIGHT;
    static int[][] map;
    static int[][] obstacleID;
    static short[][] dam;
    static DSU dsu;
    static int maxObstacleID = 1;
    public static void init(RobotController r) {
        rc = r;
        WIDTH = rc.getMapWidth();
        HEIGHT = rc.getMapHeight();
        dam = new short[WIDTH][HEIGHT];
        map = new int[WIDTH][HEIGHT];
        //obstacleID = new int[WIDTH][HEIGHT];
        //dsu = new DSU(100); //can change later
    }

    public static void scout() {
        if (symmetries[V_SYM]) {
            if (!Comms.isSymmetry(V_SYM)) symmetries[V_SYM] = false;
        }
        if (symmetries[H_SYM]) {
            if (!Comms.isSymmetry(H_SYM)) symmetries[H_SYM] = false;
        }
        if (symmetries[R_SYM]) {
            if (!Comms.isSymmetry(R_SYM)) symmetries[R_SYM] = false;
        }

        MapInfo[] tiles = rc.senseNearbyMapInfos();
        for (MapInfo tile:tiles) {
            updateTile(tile);
        }
    }
    
    public static void updateTile(MapInfo m) {
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

    //-----------------------------------------
    //SYMMETRY MANAGEMENT
    public static final int H_SYM = 0;
    public static final int V_SYM = 1;
    public static final int R_SYM = 2;
    static final int NO_WATER_ROUND = 0; //anything before this our robots will not dig
    static boolean[] symmetries = {true, true, true};
    private static FastQueue<MapLocation> symmQueue = new FastQueue<MapLocation>(500); //the squares to check symmetry

    static MapLocation getHSym(MapLocation loc){ return new MapLocation (WIDTH - loc.x - 1, loc.y); }
    static MapLocation getVSym(MapLocation loc){ return new MapLocation (loc.x, HEIGHT - loc.y - 1); }
    static MapLocation getRSym(MapLocation loc){ return new MapLocation (WIDTH - loc.x - 1, HEIGHT - loc.y - 1); }

    static final int SYMM_BYTECODE = 5000; // do not run this if < 5000 bc
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
}

//My Codeforces impl of DSU, is not good
//can replace later
class DSU {
	int[] rank, parent, size;
	int n;
	public DSU(int n) {
		this.n = n;
		rank = new int[n];
		parent = new int[n];
		size = new int[n];
		for (int i=0; i<n; i++) {
			parent[i] = i;
			size[i]++;
		}
	}
	public DSU(int n, int[] p, int[] r, int[] s) {
		rank = new int[n];
		parent = new int[n];
		size = new int[n];
		for (int i=0; i<n; i++) {
			rank[i] = r[i];
			parent[i] = p[i];
			size[i] = s[i];
		}
	}
	int get(int x) {
		if (parent[x] != x) {
			parent[x] = get(parent[x]); //path compression
			return parent[x];
		}
		else {
			return x;
		}
	}
	void merge(int x, int y) {
		int a = get(x), b = get(y);
		if (a == b) {
			return;
		}
		if (rank[a] < rank[b]) {
			parent[a] = b;
			size[b] += size[a];
			size[a] = 0;
		}
		else if (rank[b] > rank[a]) {
			parent[b] = a;
			size[a] += size[b];
			size[b] = 0;
		}
		else {
			parent[a] = b;
			rank[b]++;
			size[b] += size[a];
			size[a] = 0;
		}
	}
}