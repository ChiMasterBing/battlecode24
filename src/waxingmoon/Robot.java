package waxingmoon;

import java.util.Arrays;
import java.util.Random;

import battlecode.common.*;
import waxingmoon.fast.*;

public abstract class Robot {
    RobotController rc;

    Random random = new Random();
    int myMoveNumber, roundNumber, mySpawn = -1;
    int MYTYPE, myFlagNum;
    MapLocation[] spawnLocs;
    MapLocation[] myFlags = {null, null, null};
    MapLocation[] hideLocations = {null, null, null};
    MapLocation[] mirrorFlags = {null, null, null};
    boolean[] stolenFlags = {false, false, false};
    MapLocation[] closestSpawnToFlag = {null, null, null};
    FlagInfo[] flags = null;
    int[] stolenFlagRounds = {0, 0, 0};
    int[] flagIDs = {0, 0, 0};

    int assumedSymmetry = Navigation.R_SYM;
    Direction[] allDirections = Direction.allDirections();
    FastLocIntMap spawnLocToIndex =  new FastLocIntMap();

    public Robot(RobotController rc) throws GameActionException {
        this.rc = rc;
        bugNav.init(rc);
        bugNav2.init(rc);
        Navigation.init(rc);
        Debug.init(rc);
        Comms.init(rc);
        Utils.init(rc);
        spawnLocs = rc.getAllySpawnLocations();
        initFlagStatus(); //~4000 bytecode
        updateSymmetryComputations();
    }
    
    public void senseGlobals() throws GameActionException {
        flags = rc.senseNearbyFlags(-1); 

        // if (roundNumber > 500) rc.resign();

        if (roundNumber > 190) { //If we are in range of a spawn zone, check if our flag is still there.
            boolean[] tmp = {false, false, false};

            for (FlagInfo f:flags) {
                if (f.getTeam() == rc.getTeam()) {
                    if (f.getID() == flagIDs[0]) tmp[0] = true;
                    else if (f.getID() == flagIDs[1]) tmp[1] = true;
                    else if (f.getID() == flagIDs[2]) tmp[2] = true;
                } else {
                    int f1 = Comms.getEnemyFlagID(0);
                    int f2 = Comms.getEnemyFlagID(1);
                    int f3 = Comms.getEnemyFlagID(2);
                    
                    if (f.getID() % 61 == f1 || f.getID() % 61 == f2 || f.getID() % 61 == f3) {
                        continue;
                    }
                    // System.out.println(f1 + " " + f2 + " " + f3 + " " + f.getID() + " " + (f.getID() % 61));
                    
                    if (Comms.getEnemyFlagStatus(0) == 0) {
                        Comms.writeEnemyFlagLocation(0, f);
                    } else if (Comms.getEnemyFlagStatus(1) == 0) {
                        Comms.writeEnemyFlagLocation(1, f);
                    } else if (Comms.getEnemyFlagStatus(2) == 0) {
                        Comms.writeEnemyFlagLocation(2, f);
                    }
                }
            }
            if (rc.canSenseLocation(myFlags[0])) {
                if (!tmp[0]) {
                    Comms.writeMyFlag(0, 0);
                    Comms.distressFlag(0, 0);
                } else {
                    Comms.writeMyFlag(0, 1);
                }

                if (rc.senseNearbyRobots(-1, rc.getTeam().opponent()).length == 0 && (rc.senseMapInfo(myFlags[0]).getTrapType() == TrapType.STUN || rc.getCrumbs() < 100)) {
                    Comms.distressFlag(0, 0);
                }
            }
            if (rc.canSenseLocation(myFlags[1])) {
                if (!tmp[1]) {
                    Comms.writeMyFlag(1, 0);
                    Comms.distressFlag(1, 0);
                } else {
                    Comms.writeMyFlag(1, 1);
                }


                if (rc.senseNearbyRobots(-1, rc.getTeam().opponent()).length == 0 && (rc.senseMapInfo(myFlags[1]).getTrapType() == TrapType.STUN || rc.getCrumbs() < 100)) {
                    Comms.distressFlag(1, 0);
                }
            }
            if (rc.canSenseLocation(myFlags[2])) {
                if (!tmp[2]) {
                    Comms.writeMyFlag(2, 0);
                    Comms.distressFlag(2, 0);
                } else {
                    Comms.writeMyFlag(2, 1);
                }

                if (rc.senseNearbyRobots(-1, rc.getTeam().opponent()).length == 0 && (rc.senseMapInfo(myFlags[2]).getTrapType() == TrapType.STUN || rc.getCrumbs() < 100)) {
                    Comms.distressFlag(2, 0);
                }
            }
        }
    }

    private void initFlagStatus() {
        FastLocSet locs = new FastLocSet();
        for (MapLocation m:spawnLocs) {
            locs.add(m);
        }
        int index = 0;
        for (MapLocation m:spawnLocs) {
            boolean isCenter = true;
            for (Direction d:allDirections) {
                if (!locs.contains(m.add(d))) {
                    isCenter = false;
                    break;
                }
            }
            if (isCenter) {
                if (myFlags[0] == null) myFlags[0] = m;
                else if (myFlags[1] == null) myFlags[1] = m;
                else myFlags[2] = m;
            }
            spawnLocToIndex.add(m, index);
            //System.out.println(index + " " + m);
            index++;
        }
        //rc.resign();
    }

    private void commFlagID() throws GameActionException {
        for (FlagInfo f:flags) {
            MapLocation floc = f.getLocation();
            if (floc.equals(myFlags[0])) {
                Comms.init_WriteAllyFlags(1, f);
            }
            else if (floc.equals(myFlags[1])) {
                Comms.init_WriteAllyFlags(2, f);
            }
            else {
                Comms.init_WriteAllyFlags(3, f);
            }
        }
    }

    private void readCommFlagID() {
        Comms.init_ReadAllyFlags();
        flagIDs[0] = Comms.allyFlagData[0].flagID;
        flagIDs[1] = Comms.allyFlagData[1].flagID;
        flagIDs[2] = Comms.allyFlagData[2].flagID;
    }

    public void updateSymmetryComputations() throws GameActionException {
        switch (assumedSymmetry) {
            case Navigation.R_SYM:
                mirrorFlags[0] = Navigation.getRSym(myFlags[0]);
                mirrorFlags[1] = Navigation.getRSym(myFlags[1]);
                mirrorFlags[2] = Navigation.getRSym(myFlags[2]);
                break;
            case Navigation.H_SYM:
                mirrorFlags[0] = Navigation.getHSym(myFlags[0]);
                mirrorFlags[1] = Navigation.getHSym(myFlags[1]);
                mirrorFlags[2] = Navigation.getHSym(myFlags[2]);
                break;
            case Navigation.V_SYM:
                mirrorFlags[0] = Navigation.getVSym(myFlags[0]);
                mirrorFlags[1] = Navigation.getVSym(myFlags[1]);
                mirrorFlags[2] = Navigation.getVSym(myFlags[2]);
                break;
        }
    }

    public abstract void turn() throws GameActionException;
    public abstract void deadFunctions() throws GameActionException;

    MapLocation lastLocation = new MapLocation(80, 80);
    public void spawnedTurn() throws GameActionException {
        int currentRound = roundNumber;

        senseGlobals();
        if (roundNumber == 3) {
            commFlagID();
        }

        MapLocation m1 = Comms.getEnemyFlagLocation(0);
        MapLocation m2 = Comms.getEnemyFlagLocation(1);
        MapLocation m3 = Comms.getEnemyFlagLocation(2);
        int s1 = Comms.getEnemyFlagStatus(0);
        int s2 = Comms.getEnemyFlagStatus(1);
        int s3 = Comms.getEnemyFlagStatus(2);

        if (s1 == 1) {
            rc.setIndicatorDot(m1, 255, 0, 0);
        } else if (s1 == 3) {
            rc.setIndicatorDot(m1, 0, 0, 255);
        } else if (s1 == 2) {
            rc.setIndicatorDot(m1, 0, 255, 0);
        }
        if (s2 == 1) {
            rc.setIndicatorDot(m2, 255, 0, 0);
        } else if (s2 == 3) {
            rc.setIndicatorDot(m2, 0, 0, 255);
        } else if (s2 == 2) {
            rc.setIndicatorDot(m2, 0, 255, 0);
        }
        if (s3 == 1) {
            rc.setIndicatorDot(m3, 255, 0, 0);
        } else if (s3 == 3) {
            rc.setIndicatorDot(m3, 0, 0, 255);
        } else if (s3 == 2) {
            rc.setIndicatorDot(m3, 0, 255, 0);
        }
        
        // rc.setIndicatorString(m1 + " " + m2 + " " + m3);

        lastLocation = rc.getLocation();

        turn();

        if (spawnLocToIndex.contains(lastLocation)) {
            Comms.writeOccupy(spawnLocToIndex.getVal(lastLocation), 0);
        }

        if (spawnLocToIndex.contains(rc.getLocation())) { //we're stepping on spawn right now
            Comms.writeOccupy(spawnLocToIndex.getVal(rc.getLocation()), 1);
        }
        lastLocation = rc.getLocation();

        if (rc.getRoundNum() == currentRound) {
            if (roundNumber < 200) {
                if (Clock.getBytecodesLeft() > 6000 && roundNumber > 10) {
                    Navigation.scout();
                    Navigation.updateSymm();
                }
            }
            else {
                if (Clock.getBytecodesLeft() > 6000 && roundNumber > 10) {
                    Navigation.scout();
                    Navigation.updateSymm();
                }
            }
        }
    }

    // todo for me:
    // choose correct place
    // dont overcrowd crums

    private void buyUpgrades() throws GameActionException {
        if (roundNumber == 600 && rc.canBuyGlobal(GlobalUpgrade.ATTACK)) rc.buyGlobal(GlobalUpgrade.ATTACK);
        else if (roundNumber == 1200 && rc.canBuyGlobal(GlobalUpgrade.HEALING)) rc.buyGlobal(GlobalUpgrade.HEALING);
        else if (roundNumber == 1800 && rc.canBuyGlobal(GlobalUpgrade.CAPTURING)) rc.buyGlobal(GlobalUpgrade.CAPTURING);
    }

    private void doPreRoundTasks() throws GameActionException {
        switch (roundNumber) {
            case 1:
                myMoveNumber = Comms.myMoveOrder;
                break;
            case 2: 
                break;
            case 3: 
                if (!rc.isSpawned()) {
                    Navigation.eliminateSpawnSymmetries(myFlags[0], myFlags[1], myFlags[2]);
                    Navigation.calculateSpawnDistribution(myFlags[0], myFlags[1], myFlags[2]);
                }
                break;
            case 4:
                readCommFlagID();
                break;
            case 5:
                Comms.writeSniperStatus(0, 1);
                Comms.writeSniperStatus(1, 1);
                Comms.writeSniperStatus(2, 1);
                Comms.writeFlagStatus(myMoveNumber%3, 8);
                break;
            case 176:
                // for (FlagInfo f:flags) {
                //     int flagnum = 999999999; 
                //     if (f.getID() == flagIDs[0]) flagnum = 0;
                //     else if (f.getID() == flagIDs[1]) flagnum = 1;
                //     else if (f.getID() == flagIDs[2]) flagnum = 2;
                //     Comms.dropFlagAtNewLocation(f.getLocation(), myFlagNum);
                // }
                break;
            case 188:
                hideLocations = Comms.getHiddenFlagLocations();
                for(int i = 0; i<3; i++){
                    int curDist = 1000000000;
                    for(int j = 0; j<3; j++) {
                        int dist = hideLocations[i].distanceSquaredTo(myFlags[j]);
                        if (curDist>dist){
                            curDist = dist;
                            closestSpawnToFlag[i] = myFlags[j];
                        }
                    }
                }
                Navigation.calculateDefenderSpawn(myFlags[0], myFlags[1], myFlags[2]);
                // Debug.println(Arrays.toString(hideLocations));
//                rc.resign();
                break;
        }
    }

    boolean isSwiper = false;
    private boolean trySpawn() throws GameActionException {
        if (myMoveNumber == 49) {
            if (roundNumber > 20) {
                for (int i=0; i<spawnLocs.length; i++) {
                    boolean status = Comms.readOccupy(i);
                    if (!rc.canSpawn(spawnLocs[i]) && !status) { //square should be open but theres cant spawn there
                        rc.setIndicatorDot(spawnLocs[i], 255, 0, 0); //dots dont work when no spawned :(
                        
                        int spawn = Navigation.getClosestSpawnNumber(spawnLocs[i], myFlags[0], myFlags[1], myFlags[2]);
                        if (Comms.myFlagExists(spawn))
                            Comms.distressFlag(spawn, 1);
                    } else if (!status) {
                        rc.setIndicatorDot(spawnLocs[i], 0, 0, 255); 
                    } else {
                        rc.setIndicatorDot(spawnLocs[i], 0, 255, 0);
                    }
                }
            }
            return false;
        }


        if (roundNumber < 200) {
            if (roundNumber == 1) { //find closest you can get to your opp via dam
                if (myMoveNumber < 3) {
                    mySpawn = myMoveNumber;
                    rc.spawn(myFlags[mySpawn]);
                }
            }
            else if (roundNumber > 5) {
                int tot = Navigation.spawnScores[0] + Navigation.spawnScores[1] + Navigation.spawnScores[2];
                int r1 = (Navigation.spawnScores[0] * 47) / tot;
                int r2 = (Navigation.spawnScores[1] * 47) / tot;
                MapLocation center = null;
                if (myMoveNumber < r1 + 3) {
                    mySpawn = 0;
                    center = myFlags[0];
                }
                else if (myMoveNumber < r1 + r2 + 3 && myMoveNumber >= r1 + 3) {
                    mySpawn = 1;
                    center = myFlags[1];
                }
                else {
                    mySpawn = 2;
                    center = myFlags[2];
                }

                for (Direction d:allDirections) {
                    if (rc.canSpawn(center.add(d))) {
                        rc.spawn(center.add(d));
                        return true;
                    }
                }
            }
        }
        else if (roundNumber > 200) {
            MapLocation center = null;
            isSwiper = false;

            if (Comms.checkDistressFlag(0)) {
                center = myFlags[0];
                mySpawn = 0;
            } else if (Comms.checkDistressFlag(1)) {
                center = myFlags[1];
                mySpawn = 1;
            } else if (Comms.checkDistressFlag(2)) {
                center = myFlags[2];
                mySpawn = 2;
            } else {
                //find the closest spawn to where combat is happening and spawn there
                int minDist = 100000;
                // if(Comms.squadronMessages.size()>100) Debug.println(Comms.squadronMessages.size() + " ");

                Navigation.spawnScores[0] = 0;
                Navigation.spawnScores[1] = 0;
                Navigation.spawnScores[2] = 0;
                

                for(int i = 0; i<Comms.squadronMessages.size(); i++){
                    Integer message = Comms.squadronMessages.get(i);
                    int cury = 2*((message>>6)&0x1f);
                    int curx = 2*((message>>11)&0x1f);
                    //int cval=message&Utils.BASIC_MASKS[6];
                    MapLocation cur = new MapLocation(curx, cury);                    
                    int d1 = myFlags[0].distanceSquaredTo(cur);
                    int d2 = myFlags[1].distanceSquaredTo(cur);
                    int d3 = myFlags[2].distanceSquaredTo(cur);
                    
                    //Navigation.spawnHeuristic(cur, myFlags[0], myFlags[1], myFlags[2]);

                    if (d1 < minDist) {
                        minDist = d1;
                        center = myFlags[0];
                        mySpawn = 0;
                    }
                    if (d2 < minDist) {
                        minDist = d2;
                        center = myFlags[1];
                        mySpawn = 1;
                    }
                    if (d3 < minDist) {
                        minDist = d3;
                        center = myFlags[2];
                        mySpawn = 2;
                    }
                }
            }

            if (center == null) { // or is swiper  || myMoveNumber < 3
                mySpawn = myMoveNumber % 3;
                center = myFlags[myMoveNumber % 3];
            }

            for (int i=8; i>=0; i--) {
                MapLocation attempt = center.add(allDirections[i]);
                if (rc.canSpawn(attempt)) {
                    rc.spawn(attempt);
                    return true;
                }
            }
        }
        return false;
    }

    boolean aliveLastTurn = false;
    FlagInfo heldFlagLastTurn = null;

    public void play() throws GameActionException {
        roundNumber = rc.getRoundNum();

        // if (myMoveNumber < 1 && roundNumber % 100 == 0) { //Debug Messages
        //     Debug.println(Comms.readSymmetry() + " <-- Symm");
        //     Debug.println(Comms.countFlagsCaptured() + "<-- flags captured");
        // }        
         buyUpgrades();

        Comms.commsStartTurn(roundNumber);

        doPreRoundTasks();

        if (!rc.isSpawned()){
            if (aliveLastTurn) { //i died
                if (spawnLocToIndex.contains(lastLocation)) {
                    Comms.writeOccupy(spawnLocToIndex.getVal(lastLocation), 0);
                    lastLocation = new MapLocation(80, 80);
                }

                if (heldFlagLastTurn != null) {
                    Comms.writeEnemyFlagStatus(heldFlagLastTurn, 1);
                    heldFlagLastTurn = null;
                }
            }
            mySpawn = -1; 
            if (trySpawn()) {
                spawnedTurn(); aliveLastTurn = true;
            } else  {
                deadFunctions(); aliveLastTurn = false;
            }
        } else {
            aliveLastTurn = true;
            spawnedTurn();
            Comms.writeAlive();
        }

        Comms.commsEndTurn(); 

        if (Clock.getBytecodesLeft() > 1500) {
            int symm = Comms.readSymmetry();
            if (symm != assumedSymmetry) {
                assumedSymmetry = symm;
                updateSymmetryComputations();
            }
        }

        //rc.setIndicatorString(Comms.getAlive() + " ");
    }
}