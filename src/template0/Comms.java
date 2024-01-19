package template0;

import com.google.flatbuffers.FlexBuffers.Map;

// battlecode package
import battlecode.common.*;

// custom package
import template0.java_utils.*;
import template0.Comms.*;
import template0.Debug.*;
import template0.Utils.*;

/* ALLOCATIONS
MAIN    0
S0      symmetry (3), flags captured (2)

FLAGS   1 -> 6
S1-3    ally flags
S4-6    enemy flags

SQUAD   7 -> 27
Squadron updates (move, attack, retreat, protect, build, stuck, summons, etc.)
S5      queue usage idx, left (5), length (5)
S6-27   queue for updates

SECTOR  28 -> 48
Sector updates (danger level, pathing blockage, etc.)
first two bits: 00 --> flag stolen
consider round info as recent --> will only be tens and ones digit of current rounds
100 turns --> 5 turn buckets --> 20 --> 4 bits

INDIV   49 -> 58
S49-58  Bot status

UNUSED  59

BUILD   60 -> 62
Info for builders

BOT     63
S4 --> bot id for | bot # = turn mod 50
*/

public class Comms {
    private static RobotController rc;
    
// constants
    static int MASKS[];

    // currently uses 10 ints to store 3 bits per, each int holds 5 bots
    final static int ALLY_STATUS_IDX = 49;
    final static int ALLY_STATUS_PERSLOT = 5;
    final static int ALLY_STATUS_BITLEN = 3;

// stored data
    static int intToID[];
    static FastIntIntMap IDToInt;
    static BotInfo allyBotInfo[];

    static AllyFlagInfo allyFlagInfo[];
    static EnemyFlagInfo enemyFlagInfo[];

    static SectorInfo sectorInfo[];
    static int sectorMessagesSent;
    static SquadronInfo squadronInfo[];
    static int squadronMessagesSent;

// messaging
    static FastQueue<Integer> messageQueue;
    static FastQueue<Integer> priorityMessageQueue;
    
    private static int[] bufferPool;
    private static boolean[] dirtyFlags;

// init
    public void init(RobotController r) throws GameActionException {
        rc = r;
        
        intToID = new int[50];
        IDToInt = new FastIntIntMap();

        allyBotInfo = new BotInfo[50];
        allyFlagInfo = new AllyFlagInfo[3];
        enemyFlagInfo = new EnemyFlagInfo[3];
    
        sectorInfo = new SectorInfo[225];
        sectorMessagesSent = 0;
        squadronInfo = new SquadronInfo[10];
        squadronMessagesSent = 0;

        bufferPool = new int[64];
        dirtyFlags = new boolean[64];

        messageQueue = new FastQueue<Integer>(64);
        priorityMessageQueue = new FastQueue<Integer>(64);
    }

// public methods
    public static void commsStartTurn() throws GameActionException {
        initBufferPool();

        // read in all pertinent messages to data
        readAllyStatus();
        if (Robot.turn_num > 50) {
            readSectorMessages();
            readSquadronMessages();
        }
    }

    public static void commsEndTurn() throws GameActionException {
        writeRegularUpdate();
        flushQueue(priorityMessageQueue);
        flushQueue(messageQueue);

        // sends all messages at once
        flushBufferPool();
    }


// private methods
    // primary methods
    private static void writeToBufferPool(int idx, int message) throws GameActionException {
        bufferPool[idx] = message;
        dirtyFlags[idx] = true;
    }

    private static void initBufferPool() throws GameActionException {
        dirtyFlags = new boolean[64];

        bufferPool[0] = rc.readSharedArray(0);
        bufferPool[1] = rc.readSharedArray(1);
        bufferPool[2] = rc.readSharedArray(2);
        bufferPool[3] = rc.readSharedArray(3);
        bufferPool[4] = rc.readSharedArray(4);
        bufferPool[5] = rc.readSharedArray(5);
        bufferPool[6] = rc.readSharedArray(6);
        bufferPool[7] = rc.readSharedArray(7);
        bufferPool[8] = rc.readSharedArray(8);
        bufferPool[9] = rc.readSharedArray(9);
        bufferPool[10] = rc.readSharedArray(10);
        bufferPool[11] = rc.readSharedArray(11);
        bufferPool[12] = rc.readSharedArray(12);
        bufferPool[13] = rc.readSharedArray(13);
        bufferPool[14] = rc.readSharedArray(14);
        bufferPool[15] = rc.readSharedArray(15);
        bufferPool[16] = rc.readSharedArray(16);
        bufferPool[17] = rc.readSharedArray(17);
        bufferPool[18] = rc.readSharedArray(18);
        bufferPool[19] = rc.readSharedArray(19);
        bufferPool[20] = rc.readSharedArray(20);
        bufferPool[21] = rc.readSharedArray(21);
        bufferPool[22] = rc.readSharedArray(22);
        bufferPool[23] = rc.readSharedArray(23);
        bufferPool[24] = rc.readSharedArray(24);
        bufferPool[25] = rc.readSharedArray(25);
        bufferPool[26] = rc.readSharedArray(26);
        bufferPool[27] = rc.readSharedArray(27);
        bufferPool[28] = rc.readSharedArray(28);
        bufferPool[29] = rc.readSharedArray(29);
        bufferPool[30] = rc.readSharedArray(30);
        bufferPool[31] = rc.readSharedArray(31);
        bufferPool[32] = rc.readSharedArray(32);
        bufferPool[33] = rc.readSharedArray(33);
        bufferPool[34] = rc.readSharedArray(34);
        bufferPool[35] = rc.readSharedArray(35);
        bufferPool[36] = rc.readSharedArray(36);
        bufferPool[37] = rc.readSharedArray(37);
        bufferPool[38] = rc.readSharedArray(38);
        bufferPool[39] = rc.readSharedArray(39);
        bufferPool[40] = rc.readSharedArray(40);
        bufferPool[41] = rc.readSharedArray(41);
        bufferPool[42] = rc.readSharedArray(42);
        bufferPool[43] = rc.readSharedArray(43);
        bufferPool[44] = rc.readSharedArray(44);
        bufferPool[45] = rc.readSharedArray(45);
        bufferPool[46] = rc.readSharedArray(46);
        bufferPool[47] = rc.readSharedArray(47);
        bufferPool[48] = rc.readSharedArray(48);
        bufferPool[49] = rc.readSharedArray(49);
        bufferPool[50] = rc.readSharedArray(50);
        bufferPool[51] = rc.readSharedArray(51);
        bufferPool[52] = rc.readSharedArray(52);
        bufferPool[53] = rc.readSharedArray(53);
        bufferPool[54] = rc.readSharedArray(54);
        bufferPool[55] = rc.readSharedArray(55);
        bufferPool[56] = rc.readSharedArray(56);
        bufferPool[57] = rc.readSharedArray(57);
        bufferPool[58] = rc.readSharedArray(58);
        bufferPool[59] = rc.readSharedArray(59);
        bufferPool[60] = rc.readSharedArray(60);
        bufferPool[61] = rc.readSharedArray(61);
        bufferPool[62] = rc.readSharedArray(62);
        bufferPool[63] = rc.readSharedArray(63);
    }

    private static void flushBufferPool() throws GameActionException {
        if (dirtyFlags[0])
            rc.writeSharedArray(0, bufferPool[0]);
        if (dirtyFlags[1])
            rc.writeSharedArray(1, bufferPool[1]);
        if (dirtyFlags[2])
            rc.writeSharedArray(2, bufferPool[2]);
        if (dirtyFlags[3])
            rc.writeSharedArray(3, bufferPool[3]);
        if (dirtyFlags[4])
            rc.writeSharedArray(4, bufferPool[4]);
        if (dirtyFlags[5])
            rc.writeSharedArray(5, bufferPool[5]);
        if (dirtyFlags[6])
            rc.writeSharedArray(6, bufferPool[6]);
        if (dirtyFlags[7])
            rc.writeSharedArray(7, bufferPool[7]);
        if (dirtyFlags[8])
            rc.writeSharedArray(8, bufferPool[8]);
        if (dirtyFlags[9])
            rc.writeSharedArray(9, bufferPool[9]);
        if (dirtyFlags[10])
            rc.writeSharedArray(10, bufferPool[10]);
        if (dirtyFlags[11])
            rc.writeSharedArray(11, bufferPool[11]);
        if (dirtyFlags[12])
            rc.writeSharedArray(12, bufferPool[12]);
        if (dirtyFlags[13])
            rc.writeSharedArray(13, bufferPool[13]);
        if (dirtyFlags[14])
            rc.writeSharedArray(14, bufferPool[14]);
        if (dirtyFlags[15])
            rc.writeSharedArray(15, bufferPool[15]);
        if (dirtyFlags[16])
            rc.writeSharedArray(16, bufferPool[16]);
        if (dirtyFlags[17])
            rc.writeSharedArray(17, bufferPool[17]);
        if (dirtyFlags[18])
            rc.writeSharedArray(18, bufferPool[18]);
        if (dirtyFlags[19])
            rc.writeSharedArray(19, bufferPool[19]);
        if (dirtyFlags[20])
            rc.writeSharedArray(20, bufferPool[20]);
        if (dirtyFlags[21])
            rc.writeSharedArray(21, bufferPool[21]);
        if (dirtyFlags[22])
            rc.writeSharedArray(22, bufferPool[22]);
        if (dirtyFlags[23])
            rc.writeSharedArray(23, bufferPool[23]);
        if (dirtyFlags[24])
            rc.writeSharedArray(24, bufferPool[24]);
        if (dirtyFlags[25])
            rc.writeSharedArray(25, bufferPool[25]);
        if (dirtyFlags[26])
            rc.writeSharedArray(26, bufferPool[26]);
        if (dirtyFlags[27])
            rc.writeSharedArray(27, bufferPool[27]);
        if (dirtyFlags[28])
            rc.writeSharedArray(28, bufferPool[28]);
        if (dirtyFlags[29])
            rc.writeSharedArray(29, bufferPool[29]);
        if (dirtyFlags[30])
            rc.writeSharedArray(30, bufferPool[30]);
        if (dirtyFlags[31])
            rc.writeSharedArray(31, bufferPool[31]);
        if (dirtyFlags[32])
            rc.writeSharedArray(32, bufferPool[32]);
        if (dirtyFlags[33])
            rc.writeSharedArray(33, bufferPool[33]);
        if (dirtyFlags[34])
            rc.writeSharedArray(34, bufferPool[34]);
        if (dirtyFlags[35])
            rc.writeSharedArray(35, bufferPool[35]);
        if (dirtyFlags[36])
            rc.writeSharedArray(36, bufferPool[36]);
        if (dirtyFlags[37])
            rc.writeSharedArray(37, bufferPool[37]);
        if (dirtyFlags[38])
            rc.writeSharedArray(38, bufferPool[38]);
        if (dirtyFlags[39])
            rc.writeSharedArray(39, bufferPool[39]);
        if (dirtyFlags[40])
            rc.writeSharedArray(40, bufferPool[40]);
        if (dirtyFlags[41])
            rc.writeSharedArray(41, bufferPool[41]);
        if (dirtyFlags[42])
            rc.writeSharedArray(42, bufferPool[42]);
        if (dirtyFlags[43])
            rc.writeSharedArray(43, bufferPool[43]);
        if (dirtyFlags[44])
            rc.writeSharedArray(44, bufferPool[44]);
        if (dirtyFlags[45])
            rc.writeSharedArray(45, bufferPool[45]);
        if (dirtyFlags[46])
            rc.writeSharedArray(46, bufferPool[46]);
        if (dirtyFlags[47])
            rc.writeSharedArray(47, bufferPool[47]);
        if (dirtyFlags[48])
            rc.writeSharedArray(48, bufferPool[48]);
        if (dirtyFlags[49])
            rc.writeSharedArray(49, bufferPool[49]);
        if (dirtyFlags[50])
            rc.writeSharedArray(50, bufferPool[50]);
        if (dirtyFlags[51])
            rc.writeSharedArray(51, bufferPool[51]);
        if (dirtyFlags[52])
            rc.writeSharedArray(52, bufferPool[52]);
        if (dirtyFlags[53])
            rc.writeSharedArray(53, bufferPool[53]);
        if (dirtyFlags[54])
            rc.writeSharedArray(54, bufferPool[54]);
        if (dirtyFlags[55])
            rc.writeSharedArray(55, bufferPool[55]);
        if (dirtyFlags[56])
            rc.writeSharedArray(56, bufferPool[56]);
        if (dirtyFlags[57])
            rc.writeSharedArray(57, bufferPool[57]);
        if (dirtyFlags[58])
            rc.writeSharedArray(58, bufferPool[58]);
        if (dirtyFlags[59])
            rc.writeSharedArray(59, bufferPool[59]);
        if (dirtyFlags[60])
            rc.writeSharedArray(60, bufferPool[60]);
        if (dirtyFlags[61])
            rc.writeSharedArray(61, bufferPool[61]);
        if (dirtyFlags[62])
            rc.writeSharedArray(62, bufferPool[62]);
        if (dirtyFlags[63])
            rc.writeSharedArray(63, bufferPool[63]);
    }

    // write methods: write various messages to buffer
    private static void writeRegularUpdate() throws GameActionException {

    }

    private static void flushQueue(FastQueue<Integer> queue) throws GameActionException {
        while (!queue.isEmpty()) {
            if (Clock.getBytecodesLeft() < 2000) break;

            int encodedMessage = queue.poll();
            int index = encodedMessage & 0b111111;
            int message = encodedMessage >> 6;

            writeToBufferPool(index, message);
        }
    }
    
    // read methods: read buffer ints for data
    private static void readAllyStatus() {
        // 5 statuses per int
        // status length 3 bits
        allyBotInfo[0].status = (bufferPool[_rAS0(0)] >> _rAS1(0)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[1].status = (bufferPool[_rAS0(1)] >> _rAS1(1)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[2].status = (bufferPool[_rAS0(2)] >> _rAS1(2)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[3].status = (bufferPool[_rAS0(3)] >> _rAS1(3)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[4].status = (bufferPool[_rAS0(4)] >> _rAS1(4)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[5].status = (bufferPool[_rAS0(5)] >> _rAS1(5)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[6].status = (bufferPool[_rAS0(6)] >> _rAS1(6)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[7].status = (bufferPool[_rAS0(7)] >> _rAS1(7)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[8].status = (bufferPool[_rAS0(8)] >> _rAS1(8)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[9].status = (bufferPool[_rAS0(9)] >> _rAS1(9)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[10].status = (bufferPool[_rAS0(10)] >> _rAS1(10)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[11].status = (bufferPool[_rAS0(11)] >> _rAS1(11)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[12].status = (bufferPool[_rAS0(12)] >> _rAS1(12)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[13].status = (bufferPool[_rAS0(13)] >> _rAS1(13)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[14].status = (bufferPool[_rAS0(14)] >> _rAS1(14)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[15].status = (bufferPool[_rAS0(15)] >> _rAS1(15)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[16].status = (bufferPool[_rAS0(16)] >> _rAS1(16)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[17].status = (bufferPool[_rAS0(17)] >> _rAS1(17)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[18].status = (bufferPool[_rAS0(18)] >> _rAS1(18)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[19].status = (bufferPool[_rAS0(19)] >> _rAS1(19)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[20].status = (bufferPool[_rAS0(20)] >> _rAS1(20)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[21].status = (bufferPool[_rAS0(21)] >> _rAS1(21)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[22].status = (bufferPool[_rAS0(22)] >> _rAS1(22)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[23].status = (bufferPool[_rAS0(23)] >> _rAS1(23)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[24].status = (bufferPool[_rAS0(24)] >> _rAS1(24)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[25].status = (bufferPool[_rAS0(25)] >> _rAS1(25)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[26].status = (bufferPool[_rAS0(26)] >> _rAS1(26)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[27].status = (bufferPool[_rAS0(27)] >> _rAS1(27)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[28].status = (bufferPool[_rAS0(28)] >> _rAS1(28)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[29].status = (bufferPool[_rAS0(29)] >> _rAS1(29)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[30].status = (bufferPool[_rAS0(30)] >> _rAS1(30)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[31].status = (bufferPool[_rAS0(31)] >> _rAS1(31)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[32].status = (bufferPool[_rAS0(32)] >> _rAS1(32)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[33].status = (bufferPool[_rAS0(33)] >> _rAS1(33)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[34].status = (bufferPool[_rAS0(34)] >> _rAS1(34)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[35].status = (bufferPool[_rAS0(35)] >> _rAS1(35)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[36].status = (bufferPool[_rAS0(36)] >> _rAS1(36)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[37].status = (bufferPool[_rAS0(37)] >> _rAS1(37)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[38].status = (bufferPool[_rAS0(38)] >> _rAS1(38)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[39].status = (bufferPool[_rAS0(39)] >> _rAS1(39)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[40].status = (bufferPool[_rAS0(40)] >> _rAS1(40)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[41].status = (bufferPool[_rAS0(41)] >> _rAS1(41)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[42].status = (bufferPool[_rAS0(42)] >> _rAS1(42)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[43].status = (bufferPool[_rAS0(43)] >> _rAS1(43)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[44].status = (bufferPool[_rAS0(44)] >> _rAS1(44)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[45].status = (bufferPool[_rAS0(45)] >> _rAS1(45)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[46].status = (bufferPool[_rAS0(46)] >> _rAS1(46)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[47].status = (bufferPool[_rAS0(47)] >> _rAS1(47)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[48].status = (bufferPool[_rAS0(48)] >> _rAS1(48)) & MASKS[ALLY_STATUS_BITLEN];
        allyBotInfo[49].status = (bufferPool[_rAS0(49)] >> _rAS1(49)) & MASKS[ALLY_STATUS_BITLEN];
    }
        // helper: converts bot index (0-49) to index for buffer pool
        private static int _rAS0(int idx) {
            return ALLY_STATUS_IDX + idx/ALLY_STATUS_PERSLOT;
        }
        // helper: converts bot index to shift used
        private static int _rAS1(int idx) {
            return ALLY_STATUS_BITLEN * (idx%ALLY_STATUS_PERSLOT);
        }

    private static void readSectorMessages() throws GameActionException {

    }

    private static void readSquadronMessages() throws GameActionException {

    }
}

class BotInfo {
    final int NEUTRAL = 0;
    final int ATTACK = 1;
    final int DEFEND = 2;
    final int STUCK = 3;
    
    int status;
    int priority;
    int ID;

    public BotInfo() {
        status = NEUTRAL;
        ID = 50;
    }
}

class SquadronInfo {

}


class SectorInfo {

}

class AllyFlagInfo {

}

class EnemyFlagInfo {

}