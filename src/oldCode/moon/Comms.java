package moon;

// battlecode package

import battlecode.common.*;
import moon.fast.FastIntIntMap;
import moon.fast.FastQueue;

/* ALLOCATIONS
bit allocations are LSB first

MAIN    0
S0      symmetry (3), flags captured (2)

FLAGS   1 -> 6
S1-3    ally flags : sector location (8)
S4-6    enemy flags : sector location (8)

FLAG PASSING 7-12:
7-9     Robot ID
10-12   Flag ID

SQUAD   13 -> 30
Squadron updates (move, attack, retreat, protect, build, stuck, summons, etc.)
S5      queue usage idx : offset (5), length (5)
S6-27   queue for updates


SECTOR  31 -> 48
Sector updates (danger level, pathing blockage, etc.)
first two bits: 00 --> flag stolen
consider round info as recent --> will only be tens and ones digit of current rounds
100 turns --> 5 turn buckets --> 20 --> 4 bits
S28     queue usage idx : offset (5), length (5)
S29-48  queue for updates

INDIV   49 -> 58
S49-58  Bot status


BUILD   59 -> 61
Info for builders

UNUSED  62, 63

*/

public class Comms {
    /* --------------------------------------------------------------------------------- */
    /* ----------------------------------- VARIABLES ----------------------------------- */
    /* --------------------------------------------------------------------------------- */

    private static RobotController rc;
    
    // --------- //
    // constants //
    // --------- //

    final static int MAIN_IDX = 0;
    final static int ALLY_FLAG_IDX = 1;
    final static int ENEMY_FLAG_IDX = 4;

    final static int FLAG_NEUTRAL = 0;
    final static int FLAG_TAKEN = 1;
    final static int FLAG_ID_HEADER = 7;
    final static int NUM_FLAGS = 3;
    final static int ROBOT_ID_HEADER = 10;


    final static int SQUADRON_QUEUE_HEADER = 13;
    final static int SQUADRON_QUEUE_IDX = 14;
    final static int SQUADRON_QUEUE_LEN = 17;

    final static int SECTOR_QUEUE_HEADER = 31;
    final static int SECTOR_QUEUE_IDX = 32;
    final static int SECTOR_QUEUE_LEN = 17;

    final static int QUEUE_SQUADRON = 0;
    final static int QUEUE_SECTOR = 1;

    // currently uses 10 ints to store 3 bits per, each int holds 5 bots
    final static int ALLY_STATUS_IDX = 49;
    final static int ALLY_STATUS_PERSLOT = 5;
    final static int ALLY_STATUS_BITLEN = 3;

    final static int BUILDER_IDX = 59;


    // ----------------- //
    // stored comms data //
    // ----------------- //

    static int moveOrderToID[];
    static FastIntIntMap IDToMoveOrder;

    static BotData allyBotData[];

    static FlagData allyFlagData[];
    static FlagData enemyFlagData[];
    static int flagsCaptured;

    static SectorInfo sectorInfo[];
    static SquadronInfo squadronInfo[];

    static FastQueue<Integer> sectorMessages;
    static FastQueue<Integer> squadronMessages;

    // ------------------- //
    // messaging variables //
    // ------------------- //

    private static FastQueue<Integer> messageQueue;
    //private static FastQueue<Integer> priorityMessageQueue;
    static int sectorMessagesOffset;
    static int sectorMessagesLen;
    static int sectorMessagesSent;
    static int squadronMessagesOffset;
    static int squadronMessagesLen;
    static int squadronMessagesSent;
    static int roundNumber;
    static int myMoveOrder;
    static int myStatus;
    
    private static int[] bufferPool;
    private static boolean[] dirtyFlags;

    /* --------------------------------------------------------------------------------- */
    /* ------------------------------------- INIT -------------------------------------- */
    /* --------------------------------------------------------------------------------- */

    public static void init(RobotController r) throws GameActionException {
        rc = r;
        
        moveOrderToID = new int[50];
        IDToMoveOrder = new FastIntIntMap();

        allyBotData = new BotData[50];
        allyFlagData = new FlagData[3];
        enemyFlagData = new FlagData[3];
        flagsCaptured = 0;

        sectorInfo = new SectorInfo[400];
        sectorMessagesOffset = 0;
        sectorMessagesLen = 0;
        sectorMessagesSent = 0;
        squadronInfo = new SquadronInfo[10];
        squadronMessagesSent = 0;
        squadronMessagesOffset = 0;
        squadronMessagesLen = 0;

        bufferPool = new int[64];
        dirtyFlags = new boolean[64];

        messageQueue = new FastQueue<Integer>(64);
        squadronMessages = new FastQueue<Integer>();
        //priorityMessageQueue = new FastQueue<Integer>(64);
    }

    /* --------------------------------------------------------------------------------- */
    /* -------------------------------- PUBLIC METHODS --------------------------------- */
    /* --------------------------------------------------------------------------------- */

    // ------------------------------- //
    // primary methods: run every turn //
    // ------------------------------- //

    public static void commsStartTurn(int round) throws GameActionException {
        initBufferPool();
        squadronMessages = new FastQueue<Integer>();

        roundNumber = round;

        if (roundNumber == 1) {
            writeBotID();
            return;
        } else if (roundNumber == 2) {
            readBotIDs();
            return;
        }else if(roundNumber==3){
            wipeBotID();
            return;
        }

        // read in all pertinent messages to data
        // parseMainInfo();
        // if (roundNumber > 2000) { //i disabled this for now because it does nothing and inteferes
        //     parseAllyFlags();
        //     parseEnemyFlags();
        // }
        // parseAllyStatuses();
         if (roundNumber > 150) {
        //     readSectorMessages();
             readSquadronMessages();
         }
        // if (true /*& Robot.type = BUILDER*/) parseBuilderMessages();
    }

    public static void commsEndTurn() throws GameActionException {
        if (roundNumber < 4) {
            flushBufferPool();
            return;
        }
        
        //flushQueue(priorityMessageQueue);
        flushQueue(messageQueue);
        writeRegularUpdate();


        // sends all messages at once
        flushBufferPool();
    }

    // ----------------------------------- //
    // action methods: write data to comms //
    // ----------------------------------- //

    public static void invalidateSymmetry(int symm) {
        writeToBufferPool(MAIN_IDX, bufferPool[MAIN_IDX] | (1 << symm));
    }

    public static void depositFlag(int flagID) {
        flagsCaptured += 1;
        writeToBufferPool(MAIN_IDX, overwriteBits(bufferPool[MAIN_IDX], flagsCaptured, 3, 2));
    }

    public static int countFlagsCaptured() {
        return flagsCaptured;
    }


    public static void distressFlag() {

    }
    public static void updateFlagID(int flagID){
        for(int i= FLAG_ID_HEADER; i<FLAG_ID_HEADER+NUM_FLAGS; i++){
            if(bufferPool[i]==0){
                writeToBufferPool(i, flagID);
                return;
            }else if(bufferPool[i]==flagID){
                return;
            }
        }
    }
    public static void updateRobotID(int flagID, int robotID){
        for(int i = FLAG_ID_HEADER; i<FLAG_ID_HEADER+NUM_FLAGS; i++){
            if(bufferPool[i]==flagID){
                writeToBufferPool(ROBOT_ID_HEADER+i-FLAG_ID_HEADER, robotID);
            }
        }
    }
    public static void summonSquadron(MapLocation myLoc, int danger) {//danger is equal to enemy count atm. SECTOR SIZE = 2x2
        int message = (myLoc.x/2);
        message = (message<<5)|(myLoc.y/2);
        message = (message<<6)|danger;
//        if(message>65535){
//            rc.resign();
//        }
//        System.out.println(myLoc);
//        System.out.println("MESSAGE"+message);
        writeSquadronMessageToQueue(message);
    }

    public static void writeBuilderLocation(MapLocation m, int myMoveNumber) throws GameActionException {
        // derive builder number from myMoveNumber
        // setBuilderLocation(int builderNumber, MapLocation location)
    }

    // -------------------------------------- //
    // data methods: retrieve data from comms //
    // -------------------------------------- //

    public static boolean isSymmetry(int symm) {
        return !Utils.isBitOne(bufferPool[MAIN_IDX], symm);
    }

    public static int readSymmetry() { //0 bit = symm could be valid, 1 = invalid
        if (isSymmetry(Navigation.R_SYM)) return Navigation.R_SYM;
        if (isSymmetry(Navigation.H_SYM)) return Navigation.H_SYM;
        if (isSymmetry(Navigation.V_SYM)) return Navigation.V_SYM;
        //this should never happen
        return -1;
    }
    public static boolean closeToFlag(int flagID, int robotID){
        for(int i = FLAG_ID_HEADER; i<FLAG_ID_HEADER+NUM_FLAGS; i++){
            if(bufferPool[i]==flagID){
                return (bufferPool[ROBOT_ID_HEADER+i-FLAG_ID_HEADER]==robotID);
            }
        }
        return false;
    }
    public static boolean assignedToOther(int flagID, int robotID){//probably can merge this method with top one
        for(int i = FLAG_ID_HEADER; i<FLAG_ID_HEADER+NUM_FLAGS; i++){
            if(bufferPool[i]==flagID){
                return (bufferPool[ROBOT_ID_HEADER+i-FLAG_ID_HEADER]==robotID);
            }
        }
        return false;
    }

    public static MapLocation[] getBuilderLocations() {
        MapLocation[] ret = {null, null, null};
        int sector0 = readBits(bufferPool[BUILDER_IDX + 0], 0, 8);
        int sector1 = readBits(bufferPool[BUILDER_IDX + 1], 0, 8);
        int sector2 = readBits(bufferPool[BUILDER_IDX + 2], 0, 8);
        if (sector0 != Utils.BASIC_MASKS[16]) ret[0] = Utils.sectorToLocation(sector0);
        if (sector1 != Utils.BASIC_MASKS[16]) ret[1] = Utils.sectorToLocation(sector1);
        if (sector2 != Utils.BASIC_MASKS[16]) ret[2] = Utils.sectorToLocation(sector2);
        return ret;
    }

    /* --------------------------------------------------------------------------------- */
    /* -------------------------------- PRIVATE METHODS -------------------------------- */
    /* --------------------------------------------------------------------------------- */

    // ---------------------------------------------------------------------- //
    // primary methods: utilities for buffer, only methods that should use rc //
    // ---------------------------------------------------------------------- //

    private static int wipeBits(int message, int left, int length) {
        assert (length > 0) & (left >= 0) & (left + length <= 16) : "Invalid bounds";
        int mask = ~(Utils.BASIC_MASKS[length] << left);
        return message & mask;
    }

    private static int overwriteBits(int message, int newValue, int left, int length) {
        assert (length > 0) & (left >= 0) & (left + length <= 16) : "Invalid bounds";
        assert (newValue >= 0) & (newValue < (1 << left)) : "Message too large";
        int sectionWiped = wipeBits(message, left, length);
        return sectionWiped | (newValue << left);
    }

    private static int readBits(int message, int left, int length) {
        assert (length > 0) & (left >= 0) & (left + length <= 16) : "Invalid bounds";
        return (message >> left) & Utils.BASIC_MASKS[length];
    }

    private static void writeToBufferPool(int idx, int message) {
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
        if (dirtyFlags[13]) {
//            System.out.println(bufferPool[13]);
            rc.writeSharedArray(13, bufferPool[13]);
        }
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

    // ----------------------------------------------- //
    // write methods: write various messages to buffer //
    // ----------------------------------------------- //

    // for turn 1 only
    private static void writeBotID() {
        assert roundNumber == 1 : "Attempted to write bot ID to array when not turn 1.";
        
        myMoveOrder = bufferPool[0];
        writeToBufferPool(MAIN_IDX, myMoveOrder + 1);
        writeToBufferPool(myMoveOrder + 1, rc.getID());
        if(myMoveOrder==49){
            writeToBufferPool(MAIN_IDX, 0);
        }
    }
    // for turn 2 only
    private static void readBotIDs() {
        assert roundNumber == 2 : "Attempted to read bot IDs from array when not turn 2.";
        readSingleBotID(0);
        readSingleBotID(1);
        readSingleBotID(2);
        readSingleBotID(3);
        readSingleBotID(4);
        readSingleBotID(5);
        readSingleBotID(6);
        readSingleBotID(7);
        readSingleBotID(8);
        readSingleBotID(9);
        readSingleBotID(10);
        readSingleBotID(11);
        readSingleBotID(12);
        readSingleBotID(13);
        readSingleBotID(14);
        readSingleBotID(15);
        readSingleBotID(16);
        readSingleBotID(17);
        readSingleBotID(18);
        readSingleBotID(19);
        readSingleBotID(20);
        readSingleBotID(21);
        readSingleBotID(22);
        readSingleBotID(23);
        readSingleBotID(24);
        readSingleBotID(25);
        readSingleBotID(26);
        readSingleBotID(27);
        readSingleBotID(28);
        readSingleBotID(29);
        readSingleBotID(30);
        readSingleBotID(31);
        readSingleBotID(32);
        readSingleBotID(33);
        readSingleBotID(34);
        readSingleBotID(35);
        readSingleBotID(36);
        readSingleBotID(37);
        readSingleBotID(38);
        readSingleBotID(39);
        readSingleBotID(40);
        readSingleBotID(41);
        readSingleBotID(42);
        readSingleBotID(43);
        readSingleBotID(44);
        readSingleBotID(45);
        readSingleBotID(46);
        readSingleBotID(47);
        readSingleBotID(48);
        readSingleBotID(49);
    }
        // helper for readBotIDs(), writes ID data for idx
        private static void readSingleBotID(int idx) { 
            int botID = bufferPool[idx + 1];
            allyBotData[idx] = new BotData();
            allyBotData[idx].moveOrder = botID;
            moveOrderToID[idx] = botID;
            IDToMoveOrder.add(botID, idx);
        }
    private static void wipeBotID(){
        writeToBufferPool(myMoveOrder+1, 0);
    }
    // for all turns after
    private static void writeRegularUpdate() {
        // regular update contains:

        // status update
        if (getAllyStatus(myMoveOrder) != myStatus) {
            int statusMessage = bufferPool[allyIdxToStatusSlot(myMoveOrder)] & ~(Utils.BASIC_MASKS[ALLY_STATUS_BITLEN] << allyIdxToStatusBitShift(myMoveOrder)) | (myStatus << allyIdxToStatusBitShift(myMoveOrder));
            writeToBufferPool(allyIdxToStatusSlot(myMoveOrder),  statusMessage);
        }

        // queue header update
        writeToBufferPool(SECTOR_QUEUE_HEADER, sectorMessagesOffset | (sectorMessagesLen << 6));
        int value = (squadronMessagesLen<<6) | (squadronMessagesOffset);
//        if(value<0){
//            System.out.println(squadronMessagesOffset);
//            System.out.println(squadronMessagesLen);
//            System.out.println(value);
//            rc.resign();
//        }
//        System.out.println((squadronMessagesOffset | (squadronMessagesLen << 6)));
//        writeToBufferPool(SQUADRON_QUEUE_HEADER, squadronMessagesOffset | (squadronMessagesLen << 6));

        writeToBufferPool(SQUADRON_QUEUE_HEADER, value);
    }

    private static void writeSquadronMessageToQueue(int message) {
        messageQueue.add((message << 2) | QUEUE_SQUADRON);
    }

    private static void writeSectorMessageToQueue(int message) {
        messageQueue.add((message << 2) | QUEUE_SECTOR);
    }

    private static void flushQueue(FastQueue<Integer> queue) throws GameActionException {
        while (!queue.isEmpty()) {
            if (Clock.getBytecodesLeft() < 2000) break;

            int encodedMessage = queue.poll();
            int queueType = encodedMessage & Utils.BASIC_MASKS[2];
            int index = 63;
            switch (queueType) {
                case QUEUE_SQUADRON:
                    if (squadronMessagesLen >= SQUADRON_QUEUE_LEN) break;
                    index = SQUADRON_QUEUE_IDX+(squadronMessagesOffset + squadronMessagesLen) % SQUADRON_QUEUE_LEN;
                    squadronMessagesSent += 1;
                    squadronMessagesLen += 1;
                    int message = encodedMessage >> 2;
                    writeToBufferPool(index, message);
                    break;
                case QUEUE_SECTOR:
                    if (sectorMessagesLen >= SECTOR_QUEUE_LEN) break;
                    index = SECTOR_QUEUE_IDX+(sectorMessagesOffset + sectorMessagesLen) % SECTOR_QUEUE_LEN;
                    sectorMessagesSent += 1;
                    sectorMessagesLen += 1;
                    int message2 = encodedMessage >> 2;
                    writeToBufferPool(index, message2);
                    break;
            }
        }
    }
    // ------------------------------------------- //
    // read methods: analyzes buffer ints for data //
    // ------------------------------------------- //
    
    private static void parseMainInfo() {
        // Navigation.symmetry = readSymmetry(); we're just going to use Comms.readSymmetry to get the symmetry
        flagsCaptured = readBits(bufferPool[MAIN_IDX], 3, 2);
    }

    public static void commSeenFlagTarget(int flagnum) {
        writeToBufferPool(0, bufferPool[0] | (1 << (flagnum + 10)));
    }

    public static boolean hasSeenTarget(int flagnum) {
        return Utils.isBitOne(bufferPool[0], flagnum + 10);
    }

    public static void dropFlagAtNewLocation(MapLocation m, int flagnum) {
        //12 bits conveying EXACT location
        int mask = (m.x << 6) | m.y;
        writeToBufferPool(flagnum + 1, mask);
    }

    public static MapLocation[] getHiddenFlagLocations() {
        //this should only run once around round 175
        MapLocation m1 = new MapLocation((bufferPool[1] >> 6) & 0x3f, bufferPool[1] & 0x3f);
        MapLocation m2 = new MapLocation((bufferPool[2] >> 6) & 0x3f, bufferPool[2] & 0x3f);
        MapLocation m3 = new MapLocation((bufferPool[3] >> 6) & 0x3f, bufferPool[3] & 0x3f);
        MapLocation[] res = {m1, m2, m3};
        return res;
    }

    public static void startFlagMainPhase() throws GameActionException {
        //This indicates that all flags have been hidden --> entering main phase
        writeToBufferPool(1, (1 << 5));
        writeToBufferPool(2, (1 << 5));
        writeToBufferPool(3, (1 << 5));
    }

    public static boolean myFlagExists(int flagnum) {
        //This should only be run in main phase.
        return Utils.isBitOne(bufferPool[flagnum], 5);
    }

    public static void writeMyFlag(int flagnum, int exists) throws GameActionException {
        //writes 1 in the first bit if the flag currently is safe, 0 otherwise
        //This should only be run in main phase.
        int mask = (bufferPool[flagnum] & 0xffdf) | (exists << 5);
        writeToBufferPool(flagnum, mask);
    }

    public static void writeFlagStatus(int flagnum, int counts) throws GameActionException { 
        //This should only be run in main phase.
        //bits: 4 bits each
        int basemask = 0xf; //<< (4 * flagnum);
        int mask = (bufferPool[1 + flagnum] & (0xffff ^ basemask)) | (counts); //<< (4 * flagnum)
        writeToBufferPool(1 + flagnum, mask);
    }

    public static int readFlagStatus(int flagnum) { //returns true if theres more enemy ducks at flag then friendly ducks AND that flag still exists
        //This should only be run in main phase.
        int basemask = 0xf; // << (4 * flagnum);
        int mask = (bufferPool[1 + flagnum] & basemask);
        return mask;
    }

    public static void init_WriteAllyFlags(int bufferIdx, FlagInfo f) {
        //This should be run WAY before main phase.
        //This will happen in the first few turns to get the flagIDs on all robots
        writeToBufferPool(bufferIdx, f.getID());
    }

    public static void init_ReadAllyFlags() {
        //This should be run WAY before main phase.
        allyFlagData[0] = new FlagData(); 
        allyFlagData[0].flagID = bufferPool[1];
        allyFlagData[1] = new FlagData(); 
        allyFlagData[1].flagID = bufferPool[2];
        allyFlagData[2] = new FlagData(); 
        allyFlagData[2].flagID = bufferPool[3];
    }

    private static void parseSingleFlag(int bufferIdx, FlagData[] dataArray, int dataIdx) {
        dataArray[dataIdx].currentSector = readBits(bufferPool[bufferIdx], 0, 8);
    } 
    private static void parseAllyFlags() {
        parseSingleFlag(ALLY_FLAG_IDX + 0, allyFlagData, 0);
        parseSingleFlag(ALLY_FLAG_IDX + 1, allyFlagData, 1);
        parseSingleFlag(ALLY_FLAG_IDX + 2, allyFlagData, 2);
    }
    private static void parseEnemyFlags() {
        parseSingleFlag(ENEMY_FLAG_IDX + 0, enemyFlagData, 0);
        parseSingleFlag(ENEMY_FLAG_IDX + 1, enemyFlagData, 1);
        parseSingleFlag(ENEMY_FLAG_IDX + 2, enemyFlagData, 2);
    }


    private static void readSectorMessages() {
        sectorMessagesOffset = readBits(bufferPool[SECTOR_QUEUE_HEADER], 0, 6);
        sectorMessagesOffset += sectorMessagesSent;
        sectorMessagesOffset %= SECTOR_QUEUE_LEN;
        sectorMessagesLen = readBits(bufferPool[SECTOR_QUEUE_HEADER], 6, 6);
        sectorMessagesLen -= sectorMessagesSent;
        sectorMessagesSent = 0;
        
        for (int offset = sectorMessagesOffset; offset < sectorMessagesOffset + sectorMessagesLen; offset++) {
            sectorMessages.add(bufferPool[SECTOR_QUEUE_IDX + offset % SECTOR_QUEUE_LEN]);
        }
    }
    private static void parseSectorMessage() {

    }

    public static void readSquadronMessages()  {
//        System.out.println(bufferPool[SQUADRON_QUEUE_HEADER]);
        squadronMessagesOffset = readBits(bufferPool[SQUADRON_QUEUE_HEADER], 0, 6);
        squadronMessagesOffset += squadronMessagesSent;
        squadronMessagesOffset %= SQUADRON_QUEUE_LEN;
        squadronMessagesLen = readBits(bufferPool[SQUADRON_QUEUE_HEADER], 6, 6);
//        assert (squadronMessagesLen>=squadronMessagesSent);
        if(squadronMessagesSent>squadronMessagesLen) {
            System.out.println(squadronMessagesLen + " " + squadronMessagesSent);
            squadronMessagesLen = squadronMessagesSent;//SUS
//            rc.resign();
        }
        squadronMessagesLen -= squadronMessagesSent;
        squadronMessagesSent = 0;

        for (int offset = squadronMessagesOffset; offset < squadronMessagesOffset + squadronMessagesLen; offset++) {
            squadronMessages.add(bufferPool[SQUADRON_QUEUE_IDX + offset % SQUADRON_QUEUE_LEN]);
        }
    }
    private static void parseSquadronMessage() {

    }

    // may be a lot of bytecode? idk
    private static void parseAllyStatuses() {
        // 5 statuses per int, status length 3 bits
        allyBotData[0].status = getAllyStatus(0);
        allyBotData[1].status = getAllyStatus(1);
        allyBotData[2].status = getAllyStatus(2);
        allyBotData[3].status = getAllyStatus(3);
        allyBotData[4].status = getAllyStatus(4);
        allyBotData[5].status = getAllyStatus(5);
        allyBotData[6].status = getAllyStatus(6);
        allyBotData[7].status = getAllyStatus(7);
        allyBotData[8].status = getAllyStatus(8);
        allyBotData[9].status = getAllyStatus(9);
        allyBotData[10].status = getAllyStatus(10);
        allyBotData[11].status = getAllyStatus(11);
        allyBotData[12].status = getAllyStatus(12);
        allyBotData[13].status = getAllyStatus(13);
        allyBotData[14].status = getAllyStatus(14);
        allyBotData[15].status = getAllyStatus(15);
        allyBotData[16].status = getAllyStatus(16);
        allyBotData[17].status = getAllyStatus(17);
        allyBotData[18].status = getAllyStatus(18);
        allyBotData[19].status = getAllyStatus(19);
        allyBotData[20].status = getAllyStatus(20);
        allyBotData[21].status = getAllyStatus(21);
        allyBotData[22].status = getAllyStatus(22);
        allyBotData[23].status = getAllyStatus(23);
        allyBotData[24].status = getAllyStatus(24);
        allyBotData[25].status = getAllyStatus(25);
        allyBotData[26].status = getAllyStatus(26);
        allyBotData[27].status = getAllyStatus(27);
        allyBotData[28].status = getAllyStatus(28);
        allyBotData[29].status = getAllyStatus(29);
        allyBotData[30].status = getAllyStatus(30);
        allyBotData[31].status = getAllyStatus(31);
        allyBotData[32].status = getAllyStatus(32);
        allyBotData[33].status = getAllyStatus(33);
        allyBotData[34].status = getAllyStatus(34);
        allyBotData[35].status = getAllyStatus(35);
        allyBotData[36].status = getAllyStatus(36);
        allyBotData[37].status = getAllyStatus(37);
        allyBotData[38].status = getAllyStatus(38);
        allyBotData[39].status = getAllyStatus(39);
        allyBotData[40].status = getAllyStatus(40);
        allyBotData[41].status = getAllyStatus(41);
        allyBotData[42].status = getAllyStatus(42);
        allyBotData[43].status = getAllyStatus(43);
        allyBotData[44].status = getAllyStatus(44);
        allyBotData[45].status = getAllyStatus(45);
        allyBotData[46].status = getAllyStatus(46);
        allyBotData[47].status = getAllyStatus(47);
        allyBotData[48].status = getAllyStatus(48);
        allyBotData[49].status = getAllyStatus(49);
    }
    // can use individually
    private static int getAllyStatus(int idx) {
        return readBits(bufferPool[allyIdxToStatusSlot(idx)], allyIdxToStatusBitShift(idx), ALLY_STATUS_BITLEN);
    }
        // helper: converts bot index (0-49) to index for buffer pool
        private static int allyIdxToStatusSlot(int idx) {
            return ALLY_STATUS_IDX + idx/ALLY_STATUS_PERSLOT;
        }
        // helper: converts bot index to shift used
        private static int allyIdxToStatusBitShift(int idx) {
            return ALLY_STATUS_BITLEN * (idx%ALLY_STATUS_PERSLOT);
        }

    private static void parseBuilderMessages() {
        // evaluate stuff
        // resetBuilderMessage(idx);
    }
    private static void setBuilderLocation(int builderNumber, MapLocation location) {
        int message = bufferPool[BUILDER_IDX + builderNumber];
        if (location == null) writeToBufferPool(BUILDER_IDX + builderNumber, overwriteBits(message, Utils.BASIC_MASKS[8], 0, 8));
        else writeToBufferPool(BUILDER_IDX + builderNumber, overwriteBits(message, Utils.locationToSector(location), 0, 8));
    }
}


class BotData {
    final int NEUTRAL = 0;
    final int ATTACK = 1;
    final int DEFEND = 2;
    final int STUCK = 3;

    final int SACRIFICE = 6;
    final int DEAD = 7;
    
    RobotInfo recentRobotInfo;
    int robotInfoTurn;

    int status;
    int priority;
    int moveOrder;

    public BotData() {
        status = NEUTRAL;
        priority = 0;
        moveOrder = -1;
    }
}

class SquadronInfo {
    int currentSector;

    public SquadronInfo() {

    }
}


class SectorInfo {

    public SectorInfo() {

    }
}

class FlagData {
    FlagInfo recentFlagInfo;
    int flagInfoTurn;
    int flagID;

    MapLocation spawnLocation;
    boolean taken;
    int currentSector;
    boolean captured;

    public FlagData() {
        captured = false;
        taken = true;
    }
}