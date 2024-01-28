package bitmaptest;

//! battlecode package
import battlecode.common.*;

//* BYTECODE COSTS
//* instantiate overhead: 135
//* per tile sensed: 26.1
//* bfs overhead: 270
//* bfs iteration: 260
//* floodfill overhead: 270
//* floodfill iteration: 205

public class VisionBitMap {
    MapLocation center;
    int[] passableMask;

    //! ---------------------------------------------- //
    //! --------------- INITIALIZATION --------------- //
    //! ---------------------------------------------- //

    public VisionBitMap(MapLocation myLoc, MapInfo[] mapInfos) {
        // ~1460 bytecode
        System.out.println(Clock.getBytecodeNum());

        center = myLoc;
        int[] tempMask = new int[] {0x1ff, 0x1ff, 0x1ff, 0x1ff, 0x1ff, 0x1ff, 0x1ff, 0x1ff, 0x1ff};

        int x = 4 + myLoc.x;
        int y = 4 + myLoc.y;
        int i = mapInfos.length;
        System.out.println(i);
        System.out.println(Clock.getBytecodeNum());

        while (i-- > 0) {
            MapInfo m = mapInfos[i];
            if (m.isPassable()) {
                tempMask[y - m.getMapLocation().y] ^= 1 << (x - m.getMapLocation().x);
            }
        }
        System.out.println(Clock.getBytecodeNum());


        tempMask[0] ^= 0x1ff;
        tempMask[1] ^= 0x1ff;
        tempMask[2] ^= 0x1ff;
        tempMask[3] ^= 0x1ff;
        tempMask[4] ^= 0x1ff;
        tempMask[5] ^= 0x1ff;
        tempMask[6] ^= 0x1ff;
        tempMask[7] ^= 0x1ff;
        tempMask[8] ^= 0x1ff;

        passableMask = tempMask;
    }

    //! ---------------------------------------------- //
    //! --------------- PUBLIC METHODS --------------- //
    //! ---------------------------------------------- //

    //! BFS
    //! bfs returns int[][] for which int[n] has reachability integers for n moves from now
    public int[][] bfs() {
        return bfs(center, 3);
    }
    public int[][] bfs(MapLocation origin, int maxIters) {
        //* track array to return
        int[][] ret = new int[maxIters+1][9];

        //* copy to local var as unpassed locationss
        int pM0 = passableMask[0];
        int pM1 = passableMask[1];
        int pM2 = passableMask[2];
        int pM3 = passableMask[3];
        int pM4 = passableMask[4];
        int pM5 = passableMask[5];
        int pM6 = passableMask[6];
        int pM7 = passableMask[7];
        int pM8 = passableMask[8];
        
        //* place origin
        int[] turn = new int[9];
        turn[origin.x-center.x+4] |= 1 << (origin.y-center.y+4);
        
        //* remove locations in walls
        int turn0 = turn[0] & pM0;
        int turn1 = turn[1] & pM1;
        int turn2 = turn[2] & pM2;
        int turn3 = turn[3] & pM3;
        int turn4 = turn[4] & pM4;
        int turn5 = turn[5] & pM5;
        int turn6 = turn[6] & pM6;
        int turn7 = turn[7] & pM7;
        int turn8 = turn[8] & pM8;

        //* remove origin from possible moves
        pM0 ^= turn0;
        pM1 ^= turn1;
        pM2 ^= turn2;
        pM3 ^= turn3;
        pM4 ^= turn4;
        pM5 ^= turn5;
        pM6 ^= turn6;
        pM7 ^= turn7;
        pM8 ^= turn8;

        //* set ret[0] to origin
        ret[0] = new int[] {turn0, turn1, turn2, turn3, turn4, turn5, turn6, turn7, turn8};       

        //* temp vars
        int temp0, temp8, horiz0, horiz1, horiz2, horiz3, horiz4, horiz5, horiz6, horiz7, horiz8;

        //* iteration index, primarily for ret[idx]
        int idx = 0;
        //* while iteration valid and frontier tiles still exist
        while (idx++ < maxIters && (turn0 != 0 || turn1 != 0 || turn2 != 0 || turn3 != 0 || turn4 != 0 || turn5 != 0 || turn6 != 0 || turn7 != 0 || turn8 != 0)) {
            //* generate new frontier
            horiz0 = (turn0 << 1) | turn0 | (turn0 >> 1);
            horiz1 = (turn1 << 1) | turn1 | (turn1 >> 1);
            horiz2 = (turn2 << 1) | turn2 | (turn2 >> 1);
            horiz3 = (turn3 << 1) | turn3 | (turn3 >> 1);
            horiz4 = (turn4 << 1) | turn4 | (turn4 >> 1);
            horiz5 = (turn5 << 1) | turn5 | (turn5 >> 1);
            horiz6 = (turn6 << 1) | turn6 | (turn6 >> 1);
            horiz7 = (turn7 << 1) | turn7 | (turn7 >> 1);
            horiz8 = (turn8 << 1) | turn8 | (turn8 >> 1);

            temp0 = horiz0 | horiz1;
            temp8 = horiz7 | horiz8;
            
            //* remove invalid locations
            turn0 = temp0 & pM0;
            turn1 = (temp0 | horiz2) & pM1;
            turn2 = (horiz1 | horiz2 | horiz3) & pM2;
            turn3 = (horiz2 | horiz3 | horiz4) & pM3;
            turn4 = (horiz3 | horiz4 | horiz5) & pM4;
            turn5 = (horiz4 | horiz5 | horiz6) & pM5;
            turn6 = (horiz5 | horiz6 | horiz7) & pM6;
            turn7 = (horiz6 | temp8) & pM7;
            turn8 = temp8 & pM8;

            //* remove current turn from possible new moves
            pM0 ^= turn0;
            pM1 ^= turn1;
            pM2 ^= turn2;
            pM3 ^= turn3;
            pM4 ^= turn4;
            pM5 ^= turn5;
            pM6 ^= turn6;
            pM7 ^= turn7;
            pM8 ^= turn8;

            //* set output
            ret[idx] = new int[] {turn0, turn1, turn2, turn3, turn4, turn5, turn6, turn7, turn8};
        }
        return ret;
    }

    //! FLOODFILL
    //! returns all accessible squares given vision
    public int[] floodfill() {
        return floodfill(center, 6);
    }
    public int[] floodfill(MapLocation origin, int maxIters) {
        //* copy to local var as unpassed locations
        int pM0 = passableMask[0];
        int pM1 = passableMask[1];
        int pM2 = passableMask[2];
        int pM3 = passableMask[3];
        int pM4 = passableMask[4];
        int pM5 = passableMask[5];
        int pM6 = passableMask[6];
        int pM7 = passableMask[7];
        int pM8 = passableMask[8];

        //* place origin
        int[] turn = new int[9];
        turn[origin.x-center.x+4] |= 1 << (origin.y-center.y+4);

        //* remove invalid locations
        int turn0 = turn[0] & pM0;
        int turn1 = turn[1] & pM1;
        int turn2 = turn[2] & pM2;
        int turn3 = turn[3] & pM3;
        int turn4 = turn[4] & pM4;
        int turn5 = turn[5] & pM5;
        int turn6 = turn[6] & pM6;
        int turn7 = turn[7] & pM7;
        int turn8 = turn[8] & pM8;

        //* temp vars
        int temp0, temp8, horiz0, horiz1, horiz2, horiz3, horiz4, horiz5, horiz6, horiz7, horiz8;

        //! ~200 overhead
        //! ~320 for 1 iteration
        //* while iteration valid and frontier tiles still exist
        while (maxIters-- > 0 && (turn0 != 0 || turn1 != 0 || turn2 != 0 || turn3 != 0 || turn4 != 0 || turn5 != 0 || turn6 != 0 || turn7 != 0 || turn8 != 0)) {
            //* remove current turn from possible moves
            pM0 ^= turn0;
            pM1 ^= turn1;
            pM2 ^= turn2;
            pM3 ^= turn3;
            pM4 ^= turn4;
            pM5 ^= turn5;
            pM6 ^= turn6;
            pM7 ^= turn7;
            pM8 ^= turn8; 

            //* generate new positions
            horiz0 = (turn0 << 1) | turn0 | (turn0 >> 1);
            horiz1 = (turn1 << 1) | turn1 | (turn1 >> 1);
            horiz2 = (turn2 << 1) | turn2 | (turn2 >> 1);
            horiz3 = (turn3 << 1) | turn3 | (turn3 >> 1);
            horiz4 = (turn4 << 1) | turn4 | (turn4 >> 1);
            horiz5 = (turn5 << 1) | turn5 | (turn5 >> 1);
            horiz6 = (turn6 << 1) | turn6 | (turn6 >> 1);
            horiz7 = (turn7 << 1) | turn7 | (turn7 >> 1);
            horiz8 = (turn8 << 1) | turn8 | (turn8 >> 1);

            temp0 = horiz0 | horiz1;
            temp8 = horiz7 | horiz8;
            
            //* remove invalid locations
            turn0 = temp0 & pM0;
            turn1 = (temp0 | horiz2) & pM1;
            turn2 = (horiz1 | horiz2 | horiz3) & pM2;
            turn3 = (horiz2 | horiz3 | horiz4) & pM3;
            turn4 = (horiz3 | horiz4 | horiz5) & pM4;
            turn5 = (horiz4 | horiz5 | horiz6) & pM5;
            turn6 = (horiz5 | horiz6 | horiz7) & pM6;
            turn7 = (horiz6 | temp8) & pM7;
            turn8 = temp8 & pM8;
        }

        //* remove final turn from unpassed location
        pM0 ^= turn0;
        pM1 ^= turn1;
        pM2 ^= turn2;
        pM3 ^= turn3;
        pM4 ^= turn4;
        pM5 ^= turn5;
        pM6 ^= turn6;
        pM7 ^= turn7;
        pM8 ^= turn8;

        //* return (passible locations) - (unpassed locations)
        return new int[] {pM0^passableMask[0], pM1^passableMask[1],  pM2^passableMask[2],  pM3^passableMask[3],  pM4^passableMask[4],  pM5^passableMask[5],  pM6^passableMask[6],  pM7^passableMask[7],  pM8^passableMask[8]};
    }

    public boolean isBitOne(int[] mask, MapLocation location) {
        return ((mask[4 + center.y - location.y] >> (4 + center.x - location.x)) & 1) == 1;
    }
    
    //! ---------------------------------------------- //
    //! --------------- PRIVATE METHODS -------------- //
    //! ---------------------------------------------- //

    //! gets all possible next locations given either 8 directions or 8+CENTER
    private static int[] shiftDirections(int[] mask) {
        int[] ret = new int[9];

        ret[0] = (mask[0] << 1) | (mask[0] >> 1) | (mask[1] << 1) | mask[1] | (mask[1] >> 1);
        ret[1] = (mask[0] << 1) | mask[0] | (mask[0] >> 1) | (mask[1] << 1) | (mask[1] >> 1) | (mask[2] << 1) | mask[2] | (mask[2] >> 1);
        ret[2] = (mask[1] << 1) | mask[1] | (mask[1] >> 1) | (mask[2] << 1) | (mask[2] >> 1) | (mask[3] << 1) | mask[3] | (mask[3] >> 1);
        ret[3] = (mask[2] << 1) | mask[2] | (mask[2] >> 1) | (mask[3] << 1) | (mask[3] >> 1) | (mask[4] << 1) | mask[4] | (mask[4] >> 1);
        ret[4] = (mask[3] << 1) | mask[3] | (mask[3] >> 1) | (mask[4] << 1) | (mask[4] >> 1) | (mask[5] << 1) | mask[5] | (mask[5] >> 1);
        ret[5] = (mask[4] << 1) | mask[4] | (mask[4] >> 1) | (mask[5] << 1) | (mask[5] >> 1) | (mask[6] << 1) | mask[6] | (mask[6] >> 1);
        ret[6] = (mask[5] << 1) | mask[5] | (mask[5] >> 1) | (mask[6] << 1) | (mask[6] >> 1) | (mask[7] << 1) | mask[7] | (mask[7] >> 1);
        ret[7] = (mask[6] << 1) | mask[6] | (mask[6] >> 1) | (mask[7] << 1) | (mask[7] >> 1) | (mask[8] << 1) | mask[8] | (mask[8] >> 1);
        ret[8] = (mask[7] << 1) | mask[7] | (mask[7] >> 1) | (mask[8] << 1) | (mask[8] >> 1);

        return ret;
    }
    private static int[] shiftDirectionsCenter(int[] mask) {
        int[] ret = new int[9];

        ret[0] = (mask[0] << 1) | mask[0] | (mask[0] >> 1) | (mask[1] << 1) | mask[1] | (mask[1] >> 1);
        ret[1] = (mask[0] << 1) | mask[0] | (mask[0] >> 1) | (mask[1] << 1) | mask[1] | (mask[1] >> 1) | (mask[2] << 1) | mask[2] | (mask[2] >> 1);
        ret[2] = (mask[1] << 1) | mask[1] | (mask[1] >> 1) | (mask[2] << 1) | mask[2] | (mask[2] >> 1) | (mask[3] << 1) | mask[3] | (mask[3] >> 1);
        ret[3] = (mask[2] << 1) | mask[2] | (mask[2] >> 1) | (mask[3] << 1) | mask[3] | (mask[3] >> 1) | (mask[4] << 1) | mask[4] | (mask[4] >> 1);
        ret[4] = (mask[3] << 1) | mask[3] | (mask[3] >> 1) | (mask[4] << 1) | mask[4] | (mask[4] >> 1) | (mask[5] << 1) | mask[5] | (mask[5] >> 1);
        ret[5] = (mask[4] << 1) | mask[4] | (mask[4] >> 1) | (mask[5] << 1) | mask[5] | (mask[5] >> 1) | (mask[6] << 1) | mask[6] | (mask[6] >> 1);
        ret[6] = (mask[5] << 1) | mask[5] | (mask[5] >> 1) | (mask[6] << 1) | mask[6] | (mask[6] >> 1) | (mask[7] << 1) | mask[7] | (mask[7] >> 1);
        ret[7] = (mask[6] << 1) | mask[6] | (mask[6] >> 1) | (mask[7] << 1) | mask[7] | (mask[7] >> 1) | (mask[8] << 1) | mask[8] | (mask[8] >> 1);
        ret[8] = (mask[7] << 1) | mask[7] | (mask[7] >> 1) | (mask[8] << 1) | mask[8] | (mask[8] >> 1);

        return ret;
    }
    //! if mask is zero
    private static boolean isntZero(int[] mask) {
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
    //! xors entire mask
    private static void xorMask(int[] target, int[] mask) {
        target[0] ^= mask[0];
        target[1] ^= mask[1];
        target[2] ^= mask[2];
        target[3] ^= mask[3];
        target[4] ^= mask[4];
        target[5] ^= mask[5];
        target[6] ^= mask[6];
        target[7] ^= mask[7];
        target[8] ^= mask[8]; 
    }
    //! inverts entire mask
    private static int[] invertMask(int[] mask) {
        return new int[] {
            ~mask[0] , ~mask[1], ~mask[2], ~mask[3], ~mask[4], ~mask[5], ~mask[6], ~mask[7], ~mask[8]
        };
    }

}
