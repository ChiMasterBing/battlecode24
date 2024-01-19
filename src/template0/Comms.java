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
S28     queue usage idx, left (5), length (5)
S29-48  queue for updates

INDIV   49 -> 58
S49-58  Bot status


BUILD   59 -> 61
Info for builders

UNUSED  62, 63

*/

public class Comms {
// VARIABLES
//

    private static RobotController rc;
    
    // constants

    static int MASKS[];

    final static int MAIN_IDX = 0;
    final static int ALLY_FLAG_IDX = 1;
    final static int ENEMY_FLAG_IDX = 4;

    final static int SQUADRON_QUEUE_HEADER = 7;
    final static int SQUADRON_QUEUE_IDX = 8;
    final static int SQUADRON_QUEUE_LEN = 20;

    final static int SECTOR_QUEUE_HEADER = 28;
    final static int SECTOR_QUEUE_IDX = 29;
    final static int SECTOR_QUEUE_LEN = 20;

    // currently uses 10 ints to store 3 bits per, each int holds 5 bots
    final static int ALLY_STATUS_IDX = 49;
    final static int ALLY_STATUS_PERSLOT = 5;
    final static int ALLY_STATUS_BITLEN = 3;

    // stored comms data

    static int intToID[];
    static FastIntIntMap IDToInt;
    static BotInfo allyBotInfo[];

    static AllyFlagInfo allyFlagInfo[];
    static EnemyFlagInfo enemyFlagInfo[];

    static SectorInfo sectorInfo[];
    static SquadronInfo squadronInfo[];

    // messaging variables

    static FastQueue<Integer> messageQueue;
    static FastQueue<Integer> priorityMessageQueue;
    static int sectorMessagesSent;
    static int squadronMessagesSent;
    
    private static int[] bufferPool;
    private static boolean[] dirtyFlags;

// INIT
//

    public static void init(RobotController r) throws GameActionException {
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

// PUBLIC METHODS
//

    public static void commsStartTurn() throws GameActionException {
        initBufferPool();

        if (Robot.turn_num == 1) {
            writeBotID();
        } else if (Robot.turn_num == 2) {
            readBotID();
        }

        // read in all pertinent messages to data
        readMainInfo();
        readAllyFlags();
        readEnemyFlags();
        readAllyStatus();

        if (Robot.turn_num > 50) {
            readSectorMessages();
            readSquadronMessages();
        }

        readBuilderMessages();
    }

    public static void commsEndTurn() throws GameActionException {
        if (Robot.turn_num > 2) writeRegularUpdate();
        flushQueue(priorityMessageQueue);
        flushQueue(messageQueue);

        // sends all messages at once
        flushBufferPool();
    }


// PRIVATE METHODS
//

    // primary methods
    //

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
    //

    private static void writeBotID()

    private static void writeRegularUpdate() throws GameActionException {
        // regular update contains:
        // status update

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
    
    // read methods: analyzes buffer ints for data
    //
    
    private static void readAllyFlags() {

    }

    private static void readEnemyFlags() {

    }

    private static void readSectorMessages() throws GameActionException {

    }

    private static void readSquadronMessages() throws GameActionException {

    }

    // may be a lot of bytecode? idk
    private static void readAllyStatus() {
        // 5 statuses per int, status length 3 bits
        allyBotInfo[0].status = getAllyStatus(0);
        allyBotInfo[1].status = getAllyStatus(1);
        allyBotInfo[2].status = getAllyStatus(2);
        allyBotInfo[3].status = getAllyStatus(3);
        allyBotInfo[4].status = getAllyStatus(4);
        allyBotInfo[5].status = getAllyStatus(5);
        allyBotInfo[6].status = getAllyStatus(6);
        allyBotInfo[7].status = getAllyStatus(7);
        allyBotInfo[8].status = getAllyStatus(8);
        allyBotInfo[9].status = getAllyStatus(9);
        allyBotInfo[10].status = getAllyStatus(10);
        allyBotInfo[11].status = getAllyStatus(11);
        allyBotInfo[12].status = getAllyStatus(12);
        allyBotInfo[13].status = getAllyStatus(13);
        allyBotInfo[14].status = getAllyStatus(14);
        allyBotInfo[15].status = getAllyStatus(15);
        allyBotInfo[16].status = getAllyStatus(16);
        allyBotInfo[17].status = getAllyStatus(17);
        allyBotInfo[18].status = getAllyStatus(18);
        allyBotInfo[19].status = getAllyStatus(19);
        allyBotInfo[20].status = getAllyStatus(20);
        allyBotInfo[21].status = getAllyStatus(21);
        allyBotInfo[22].status = getAllyStatus(22);
        allyBotInfo[23].status = getAllyStatus(23);
        allyBotInfo[24].status = getAllyStatus(24);
        allyBotInfo[25].status = getAllyStatus(25);
        allyBotInfo[26].status = getAllyStatus(26);
        allyBotInfo[27].status = getAllyStatus(27);
        allyBotInfo[28].status = getAllyStatus(28);
        allyBotInfo[29].status = getAllyStatus(29);
        allyBotInfo[30].status = getAllyStatus(30);
        allyBotInfo[31].status = getAllyStatus(31);
        allyBotInfo[32].status = getAllyStatus(32);
        allyBotInfo[33].status = getAllyStatus(33);
        allyBotInfo[34].status = getAllyStatus(34);
        allyBotInfo[35].status = getAllyStatus(35);
        allyBotInfo[36].status = getAllyStatus(36);
        allyBotInfo[37].status = getAllyStatus(37);
        allyBotInfo[38].status = getAllyStatus(38);
        allyBotInfo[39].status = getAllyStatus(39);
        allyBotInfo[40].status = getAllyStatus(40);
        allyBotInfo[41].status = getAllyStatus(41);
        allyBotInfo[42].status = getAllyStatus(42);
        allyBotInfo[43].status = getAllyStatus(43);
        allyBotInfo[44].status = getAllyStatus(44);
        allyBotInfo[45].status = getAllyStatus(45);
        allyBotInfo[46].status = getAllyStatus(46);
        allyBotInfo[47].status = getAllyStatus(47);
        allyBotInfo[48].status = getAllyStatus(48);
        allyBotInfo[49].status = getAllyStatus(49);
    }
    
    // can use individually
    private static int getAllyStatus(int idx) {
        return (bufferPool[allyIdxToStatusBufferSlot(idx)] >> allyIdxToStatusBitShift(idx)) & MASKS[ALLY_STATUS_BITLEN];
    }
        // helper: converts bot index (0-49) to index for buffer pool
        private static int allyIdxToStatusBufferSlot(int idx) {
            return ALLY_STATUS_IDX + idx/ALLY_STATUS_PERSLOT;
        }
        // helper: converts bot index to shift used
        private static int allyIdxToStatusBitShift(int idx) {
            return ALLY_STATUS_BITLEN * (idx%ALLY_STATUS_PERSLOT);
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
    Sector sectorLocation;

    public SquadronInfo() {

    }
}


class SectorInfo {

    public SectorInfo() {

    }
}

class AllyFlagInfo {
    boolean captured;

    MapLocation spawnLocation;
    boolean stolen;
    MapLocation currentLocation;

    public AllyFlagInfo(MapLocation spawn) {
        captured = false;
        spawnLocation = spawn;
        stolen = true;
    }
}

class EnemyFlagInfo {
    boolean captured;

    boolean spawnKnown;
    MapLocation spawnLocation;
    boolean stealing;
    MapLocation currentLocation;

    public EnemyFlagInfo() {
        captured = false;
        spawnKnown = false;
        stealing = false;
    }
}