package tester;

import java.util.Arrays;

import battlecode.common.*;

public class macroPath {
    
    static RobotController rc;

    static final int UNKNOWN = 0;
    static final int EMPTY = 1;
    static final int WATER = 2;
    static final int WALL = 3;


    static int WIDTH, HEIGHT;
    static int[][] map;
    static int[][] obstacleID;
    static DSU dsu;
    static int maxObstacleID = 1;
    public static void init(RobotController r) {
        rc = r;
        WIDTH = rc.getMapWidth();
        HEIGHT = rc.getMapHeight();
        map = new int[WIDTH][HEIGHT];
        obstacleID = new int[WIDTH][HEIGHT];
        symmQueue = new FastQueue<MapLocation>();
        dsu = new DSU(100); //can change later
        System.out.println("macroPath: finished init");
    }

    public static void scout() {
        if (rc.getRoundNum() % 10 == 0) {
            System.out.println("scouting " + Arrays.toString(symmetries));
        }
        MapInfo[] tiles = rc.senseNearbyMapInfos();
        for (MapInfo tile:tiles) {
            updateTile(tile);
        }
    }
    
    public static void updateTile(MapInfo m) {
        int type = 3;
        MapLocation pos = m.getMapLocation();
        boolean isObstacle = false;
        if (!m.isPassable()) {
            if (m.isWater()) {
                type = 2;
            }
            isObstacle = true;
        }
        else {
            type = 1;
        }
        if (type != map[pos.x][pos.y]) {
            symmQueue.add(pos);
            map[pos.x][pos.y] = type;     
            if (isObstacle) {
                assert (obstacleID[pos.x][pos.y] == 0);
                int id, cnt = 0;
                int one = 0, two = 0, three = 0, four = 0;

                
                if (pos.x + 1 < WIDTH && (id = obstacleID[pos.x + 1][pos.y]) != 0) {
                    cnt++;
                    if (one == 0) one = id;
                    else if (two == 0) two = id;
                    else if (three == 0) three = id;
                    else four = id;
                }
                if (pos.x - 1 >= 0 && (id = obstacleID[pos.x - 1][pos.y]) != 0) {
                    cnt++;
                    if (one == 0) one = id;
                    else if (two == 0) two = id;
                    else if (three == 0) three = id;
                    else four = id;
                }
                if (pos.y + 1 < HEIGHT && (id = obstacleID[pos.x][pos.y + 1]) != 0) {
                    cnt++;   
                    if (one == 0) one = id;
                    else if (two == 0) two = id;
                    else if (three == 0) three = id;
                    else four = id;
                }
                if (pos.y - 1 >= 0 && (id = obstacleID[pos.x][pos.y - 1]) != 0) {
                    cnt++;
                    if (one == 0) one = id;
                    else if (two == 0) two = id;
                    else if (three == 0) three = id;
                    else four = id;
                }
                
                switch (cnt) {
                    case 0:
                        obstacleID[pos.x][pos.y] = maxObstacleID;
                        maxObstacleID++;     
                        System.out.println("New obstacle");
                        System.out.println(maxObstacleID);
                        // rc.setIndicatorDot(pos, 255, 0, 0);
                        // System.out.println(obstacleID[2][6]);
                        break;
                    case 1:
                        obstacleID[pos.x][pos.y] = one;
                        break;
                    default:
                        //System.out.println("WOAH");
                        obstacleID[pos.x][pos.y] = one;
                        dsu.merge(one, two);
                        if (three != 0) {
                            dsu.merge(one, three);
                        }
                        if (four != 0) {
                            dsu.merge(one, four);
                        }
                }

                // System.out.println("------");
                // System.out.println(obstacleID[2][6]);
                // System.out.println(obstacleID[3][6]);
                // System.out.println(obstacleID[4][6]);
                // System.out.println("------");
            }
        }   
    }

    //-----------------------------------------
    //SYMMETRY MANAGEMENT
    public static int H_SYM = 0;
    public static int V_SYM = 1;
    public static int R_SYM = 2;
    static final int NO_WATER_ROUND = 100; //anything before this our robots will not dig
    static boolean[] symmetries = {true, true, true};
    static FastQueue<MapLocation> symmQueue; //the squares to check symmetry

    public static int getSymmType() {
        //Improvement: switch to switch statement
        if ((symmetries[H_SYM] && symmetries[V_SYM]) ||
            (symmetries[R_SYM] && symmetries[H_SYM]) ||
            (symmetries[R_SYM] && symmetries[V_SYM])) {
            return -1;
        }
        else if (symmetries[H_SYM]) return H_SYM;
        else if (symmetries[V_SYM]) return V_SYM;
        else return R_SYM;
    }

    static MapLocation getHSym(MapLocation loc){ return new MapLocation (WIDTH - loc.x - 1, loc.y); }
    static MapLocation getVSym(MapLocation loc){ return new MapLocation (loc.x, HEIGHT - loc.y - 1); }
    static MapLocation getRSym(MapLocation loc){ return new MapLocation (WIDTH - loc.x - 1, HEIGHT - loc.y - 1); }

    static final int SYMM_BYTECODE = 5000; // do not run this if < 5000 bc
    public static void updateSymm() {
        if (getSymmType() != -1) return;
        while (!symmQueue.isEmpty()) {
            if (Clock.getBytecodesLeft() < SYMM_BYTECODE) {
                return;
            }
            MapLocation pos = symmQueue.poll();
            int curType = map[pos.x][pos.y];
            switch (curType) {
                case WATER:
                    if (rc.getRoundNum() > NO_WATER_ROUND) break;
                    if (symmetries[H_SYM]) {
                        MapLocation nxt = getHSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL || map[nxt.x][nxt.y] == EMPTY)) {
                            symmetries[H_SYM] = false;
                        }
                    }
                    if (symmetries[V_SYM]) {
                        MapLocation nxt = getVSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL || map[nxt.x][nxt.y] == EMPTY)) {
                            symmetries[V_SYM] = false;
                        }
                    }
                    if (symmetries[R_SYM]) {
                        MapLocation nxt = getRSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL || map[nxt.x][nxt.y] == EMPTY)) {
                            symmetries[R_SYM] = false;
                        }
                    }
                    break;
                case WALL:
                    if (symmetries[H_SYM]) {
                        MapLocation nxt = getHSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WATER || map[nxt.x][nxt.y] == EMPTY)) {
                            symmetries[H_SYM] = false;
                        }
                    }
                    if (symmetries[V_SYM]) {
                        MapLocation nxt = getVSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WATER || map[nxt.x][nxt.y] == EMPTY)) {
                            symmetries[V_SYM] = false;
                        }
                    }
                    if (symmetries[R_SYM]) {
                        MapLocation nxt = getRSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WATER || map[nxt.x][nxt.y] == EMPTY)) {
                            symmetries[R_SYM] = false;
                        }
                    }
                    break;
                case EMPTY:
                    if (symmetries[H_SYM]) {
                        MapLocation nxt = getHSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL) || (map[nxt.x][nxt.y] == WATER && rc.getRoundNum() <= NO_WATER_ROUND)) {
                            symmetries[H_SYM] = false;
                        }
                    }
                    if (symmetries[V_SYM]) {
                        MapLocation nxt = getVSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL) || (map[nxt.x][nxt.y] == WATER && rc.getRoundNum() <= NO_WATER_ROUND)) {
                            symmetries[V_SYM] = false;
                        }
                    }
                    if (symmetries[R_SYM]) {
                        MapLocation nxt = getRSym(pos);
                        if (rc.onTheMap(nxt) && (map[nxt.x][nxt.y] == WALL) || (map[nxt.x][nxt.y] == WATER && rc.getRoundNum() <= NO_WATER_ROUND)) {
                            symmetries[R_SYM] = false;
                        }
                    }
                    break;
            }
        }  
    }

    //MACRO OBSTACLES -- inspired by 4 muskets
    //RIGHT NOW:
    //water is getting counted the same as wall
    public static class Obstacle {
        static int id;
        

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