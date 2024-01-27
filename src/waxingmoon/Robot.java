package waxingmoon;

import java.util.Arrays;

import battlecode.common.*;
import waxingmoon.fast.*;

public abstract class Robot {
    RobotController rc;

    int myMoveNumber, roundNumber, mySpawn = -1;
    int MYTYPE, myFlagNum;
    MapLocation[] spawnLocs;
    MapLocation[] myFlags = {null, null, null};
    MapLocation[] hideLocations = {null, null, null};
    MapLocation[] mirrorFlags = {null, null, null};
    MapLocation[] stolenFlags = {null, null, null};
    MapLocation[] closestSpawnToFlag = {null, null, null};
    FlagInfo[] flags = null;
    int[] stolenFlagRounds = {0, 0, 0};
    int[] flagIDs = {0, 0, 0};

    int assumedSymmetry = Navigation.R_SYM;
    Direction[] allDirections = Direction.allDirections();

    public Robot(RobotController rc) throws GameActionException {
        this.rc = rc;
        bugNav.init(rc);
        Navigation.init(rc);
        Debug.init(rc);
        Comms.init(rc);
        spawnLocs = rc.getAllySpawnLocations();
        initFlagStatus(); //~4000 bytecode
        updateSymmetryComputations();
    }
    
    public void senseGlobals() throws GameActionException {
        flags = rc.senseNearbyFlags(-1); 
        if (roundNumber > 190) { //If we are in range of a spawn zone, check if our flag is still there.
            boolean[] tmp = {false, false, false};
            for (FlagInfo f:flags) {
                if (f.getTeam() == rc.getTeam()) {
                    if (f.getID() == flagIDs[0]) tmp[0] = true;
                    else if (f.getID() == flagIDs[1]) tmp[1] = true;
                    else if (f.getID() == flagIDs[2]) tmp[2] = true;
                }
            }
            if (rc.canSenseLocation(hideLocations[0])) {
                if (tmp[0]) {
                    Comms.writeMyFlag(0, 1);
                }
                else {
                    Comms.writeMyFlag(0, 0);
                    Comms.writeFlagStatus(0, 15);
                }
            }
            if (rc.canSenseLocation(hideLocations[1])) {
                if (tmp[1]) {
                    Comms.writeMyFlag(1, 1);
                }
                else {
                    Comms.writeMyFlag(1, 0);
                    Comms.writeFlagStatus(1, 15);
                }
            }
            if (rc.canSenseLocation(hideLocations[2])) {
                if (tmp[2]) {
                    Comms.writeMyFlag(2, 1);
                }
                else {
                    Comms.writeMyFlag(2, 0);
                    Comms.writeFlagStatus(2, 15);
                }
            }
        }
    }

    private void initFlagStatus() {
        FastLocSet locs = new FastLocSet();
        for (MapLocation m:spawnLocs) {
            locs.add(m);
        }
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
        }
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

    public void spawnedTurn() throws GameActionException {
        senseGlobals();
        if (roundNumber == 3) {
            commFlagID();
        }

        turn();

        if (roundNumber < 200) {
            if (Clock.getBytecodesLeft() > 6000 && roundNumber > 10) {
                Navigation.scout();
                Navigation.updateSymm();
            }
        }
        else {
            if (Clock.getBytecodesLeft() > 12000 && roundNumber > 10) {
                Navigation.scout();
                Navigation.updateSymm();
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
            case 190:
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
            // case 210:
            //     rc.resign();
            //     break;
        }
    }

    
    private boolean trySpawn() throws GameActionException {
        // if (myMoveNumber >= 6) return false;
        if (roundNumber < 200) {
            if (roundNumber == 1) {
                if (myMoveNumber < 3) {
                    mySpawn = myMoveNumber;
                    rc.spawn(myFlags[mySpawn]);
                    //find closest you can get to your opp via dam
                }
            }
            else if (roundNumber > 5) {
                int tot = Navigation.spawnScores[0] + Navigation.spawnScores[1] + Navigation.spawnScores[2];
                //System.out.println(macroPath.spawnScores[0] + " " + macroPath.spawnScores[1] + " " + macroPath.spawnScores[2]);
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

            int a = Comms.readFlagStatus(0);
            int b = Comms.readFlagStatus(1);
            int c = Comms.readFlagStatus(2);

            int min = Math.min(a, Math.min(b, c));

            if (min == a) {
                center = closestSpawnToFlag[0];
                mySpawn = 0;
            }
            else if (min == b) {
                center = closestSpawnToFlag[1];
                mySpawn = 1;
            }
            else if (min == c) {
                center = closestSpawnToFlag[2];
                mySpawn = 2;
            }

            if (myMoveNumber < 6) {
                center = myFlags[myMoveNumber%3]; //always try spawning stuff at other spawns to lessen getting sniped
                mySpawn = myMoveNumber%3;
            }

            if ((min < 8 || roundNumber % 3 == 0) && (center != null)) {
                for (Direction d:allDirections) {
                    if (rc.canSpawn(center.add(d))) {
                        rc.spawn(center.add(d));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void play() throws GameActionException {
        roundNumber = rc.getRoundNum();

        if (myMoveNumber < 1 && roundNumber % 100 == 0) { //Debug Messages
            Debug.println(Comms.readSymmetry() + " <-- Symm");
            Debug.println(Comms.countFlagsCaptured() + "<-- flags captured");
        }

        buyUpgrades();

        Comms.commsStartTurn(roundNumber);

        doPreRoundTasks();

        if (!rc.isSpawned()){
            mySpawn = -1;
            if (trySpawn()) spawnedTurn();
            else deadFunctions();
        }else {
            spawnedTurn();
        }

        Comms.commsEndTurn(); 

        if (Clock.getBytecodesLeft() > 1500) {
            int symm = Comms.readSymmetry();
            if (symm != assumedSymmetry) {
                assumedSymmetry = symm;
                updateSymmetryComputations();
            }
        }

//        rc.setIndicatorString(Comms.myFlagExists(myMoveNumber%3) + " " + mySpawn + ": " + Comms.readFlagStatus(0) + " " + Comms.readFlagStatus(1) + " " + Comms.readFlagStatus(2));
    }
}