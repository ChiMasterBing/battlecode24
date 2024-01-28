package template1;

//! battlecode package
import battlecode.common.*;

//! custom package
import template1.java_utils.*;
import template1.Comms.*;
import template1.Debug.*;
import template1.Utils.*;

//! needed
import java.util.Arrays;

public class VisionBitMap {
    int[] inverseWallMask;

    //! ---------------------------------------------- //
    //! --------------- INITIALIZATION --------------- //
    //! ---------------------------------------------- //

    //! impassable: bitwise passability
    public VisionBitMap(int[] impassable) {
        inverseWallMask = invertMask(impassable);
        andMask(inverseWallMask, filled());
    }
    //! intImpassible: boolean (with int 0/1) passability
    public VisionBitMap(int[][] intImpassable) {
        this(int2DTo1D(intImpassable));
    }
    public VisionBitMap(MapLocation myLoc, MapInfo[] mapInfos) {
        this(mapDataTo1d(myLoc, mapInfos));
    }

    //! helper converts passability to bitwise
    public static int[] int2DTo1D(int[][] impassable) {
        int[] ret = vision();

        ret[0] &= (impassable[0][0] << 0);
        ret[0] &= (impassable[0][1] << 1);
        ret[0] &= (impassable[0][2] << 2);
        ret[0] &= (impassable[0][3] << 3);
        ret[0] &= (impassable[0][4] << 4);
        ret[0] &= (impassable[0][5] << 5);
        ret[0] &= (impassable[0][6] << 6);
        ret[0] &= (impassable[0][7] << 7);
        ret[0] &= (impassable[0][8] << 8);
        ret[1] &= (impassable[1][0] << 0);
        ret[1] &= (impassable[1][1] << 1);
        ret[1] &= (impassable[1][2] << 2);
        ret[1] &= (impassable[1][3] << 3);
        ret[1] &= (impassable[1][4] << 4);
        ret[1] &= (impassable[1][5] << 5);
        ret[1] &= (impassable[1][6] << 6);
        ret[1] &= (impassable[1][7] << 7);
        ret[1] &= (impassable[1][8] << 8);
        ret[2] &= (impassable[2][0] << 0);
        ret[2] &= (impassable[2][1] << 1);
        ret[2] &= (impassable[2][2] << 2);
        ret[2] &= (impassable[2][3] << 3);
        ret[2] &= (impassable[2][4] << 4);
        ret[2] &= (impassable[2][5] << 5);
        ret[2] &= (impassable[2][6] << 6);
        ret[2] &= (impassable[2][7] << 7);
        ret[2] &= (impassable[2][8] << 8);
        ret[3] &= (impassable[3][0] << 0);
        ret[3] &= (impassable[3][1] << 1);
        ret[3] &= (impassable[3][2] << 2);
        ret[3] &= (impassable[3][3] << 3);
        ret[3] &= (impassable[3][4] << 4);
        ret[3] &= (impassable[3][5] << 5);
        ret[3] &= (impassable[3][6] << 6);
        ret[3] &= (impassable[3][7] << 7);
        ret[3] &= (impassable[3][8] << 8);
        ret[4] &= (impassable[4][0] << 0);
        ret[4] &= (impassable[4][1] << 1);
        ret[4] &= (impassable[4][2] << 2);
        ret[4] &= (impassable[4][3] << 3);
        ret[4] &= (impassable[4][4] << 4);
        ret[4] &= (impassable[4][5] << 5);
        ret[4] &= (impassable[4][6] << 6);
        ret[4] &= (impassable[4][7] << 7);
        ret[4] &= (impassable[4][8] << 8);
        ret[5] &= (impassable[5][0] << 0);
        ret[5] &= (impassable[5][1] << 1);
        ret[5] &= (impassable[5][2] << 2);
        ret[5] &= (impassable[5][3] << 3);
        ret[5] &= (impassable[5][4] << 4);
        ret[5] &= (impassable[5][5] << 5);
        ret[5] &= (impassable[5][6] << 6);
        ret[5] &= (impassable[5][7] << 7);
        ret[5] &= (impassable[5][8] << 8);
        ret[6] &= (impassable[6][0] << 0);
        ret[6] &= (impassable[6][1] << 1);
        ret[6] &= (impassable[6][2] << 2);
        ret[6] &= (impassable[6][3] << 3);
        ret[6] &= (impassable[6][4] << 4);
        ret[6] &= (impassable[6][5] << 5);
        ret[6] &= (impassable[6][6] << 6);
        ret[6] &= (impassable[6][7] << 7);
        ret[6] &= (impassable[6][8] << 8);
        ret[7] &= (impassable[7][0] << 0);
        ret[7] &= (impassable[7][1] << 1);
        ret[7] &= (impassable[7][2] << 2);
        ret[7] &= (impassable[7][3] << 3);
        ret[7] &= (impassable[7][4] << 4);
        ret[7] &= (impassable[7][5] << 5);
        ret[7] &= (impassable[7][6] << 6);
        ret[7] &= (impassable[7][7] << 7);
        ret[7] &= (impassable[7][8] << 8);
        ret[8] &= (impassable[8][0] << 0);
        ret[8] &= (impassable[8][1] << 1);
        ret[8] &= (impassable[8][2] << 2);
        ret[8] &= (impassable[8][3] << 3);
        ret[8] &= (impassable[8][4] << 4);
        ret[8] &= (impassable[8][5] << 5);
        ret[8] &= (impassable[8][6] << 6);
        ret[8] &= (impassable[8][7] << 7);
        ret[8] &= (impassable[8][8] << 8);

        return ret;
    }

    public static int[] mapDataTo1d(MapLocation myLoc, MapInfo[] mapInfo){
        int[] impassable = filled();
        for (MapInfo m: mapInfo) {
            if (m.isPassable()) {
                impassable[(4 + myLoc.y - m.getMapLocation().y)] ^= 1 << (4 + myLoc.x - m.getMapLocation().x);
            }
        }
        return impassable;
    }

    //! ---------------------------------------------- //
    //! --------------- PUBLIC METHODS --------------- //
    //! ---------------------------------------------- //

    //! bfs returns int[][] for which int[n] has reachability integers for n moves from now
    public int[][] bfs() {
        return bfs(4, 4);
    }
    //! rowOrigin: as input, colOrigin: LSB first
    public int[][] bfs(int rowOrigin, int colOrigin) {
        int[][] ret = new int[50][9];
        int idx = 0;

        int[] initialMask = empty();
        initialMask[rowOrigin] |= (1 << colOrigin);
        andMask(initialMask, inverseWallMask);

        ret[0] = initialMask;
        int[] passedMask = invertMask(initialMask);

        while (!isZero(ret[idx]) && idx < 50) {
            int[] newMask = shiftDirections(ret[idx]);

            andMask(newMask, inverseWallMask);
            andMask(newMask, passedMask);
            andMask(passedMask, invertMask(newMask));

            idx += 1;
            ret[idx] = newMask;
        }

        return ret;
    }

    //! returns all accessible squares given vision
    public int[] floodfill() {
        return floodfill(4, 4);
    }
    
    //! rowOrigin: as input, colOrigin: LSB first
    public int[] floodfill(int rowOrigin, int colOrigin) {
        int[] passed = empty();

        int[] turn = empty();
        turn[rowOrigin] |= (1 << colOrigin);
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
    private static int[] shiftDirections(int[] mask) {
        int[] ret = empty();

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
    private static int[] shiftDirectionsCenter(int[] mask) {
        int[] ret = empty();

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
    private static boolean isZero(int[] mask) {
        return (mask[0]|mask[1]|mask[2]|mask[3]|mask[4]|mask[5]|mask[6]|mask[7]|mask[8]) != 0;
    }
    //! if masks are different
    private static boolean areDifferent(int[] mask1, int[] mask2) {
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
    private static void andMask(int[] target, int[] mask) {
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
    private static void orMask(int[] target, int[] mask) {
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
    private static int[] invertMask(int[] mask) {
        return new int[] {
            ~mask[0] , ~mask[1], ~mask[2], ~mask[3], ~mask[4], ~mask[5], ~mask[6], ~mask[7], ~mask[8]
        };
    }

    //! mask with all 0
    private static int[] empty() {
        int[] ret = {0,0,0,0,0,0,0,0,0};
        return ret;
    }
    //! mask with walls where vision ends
    private static int[] vision() {
        int[] ret = {
            0b1111111111111111111111110000011,
            0b1111111111111111111111100000001,
            0b1111111111111111111111000000000,
            0b1111111111111111111111000000000,
            0b1111111111111111111111000000000,
            0b1111111111111111111111000000000,
            0b1111111111111111111111000000000,
            0b1111111111111111111111100000001,
            0b1111111111111111111111110000011
        };
        return ret;
    }
    //! mask with all 1
    private static int[] filled() {
        return new int[] {0b111111111, 0b111111111, 0b111111111, 0b111111111, 0b111111111, 0b111111111, 0b111111111, 0b111111111, 0b111111111};
    }
}
