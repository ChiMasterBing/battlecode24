package template0;

// battlecode package
import battlecode.common.*;

// custom package
import template0.java_utils.*;
import template0.Comms.*;
import template0.Debug.*;
import template0.Utils.*;

/* ALLOCATIONS
Slots 1-4:
S1 --> symmetry (3), flags captured (2) 

Slots 5-24:
Squadron updates (move, attack, retreat, protect, build, stuck, etc.)

Slots 25-44:
Sector updates (danger level, pathing blockage, etc.)
first two bits: 00 --> flag stolen
consider round info as recent --> will only be tens and ones digit of current rounds
100 turns --> 5 turn buckets --> 20 --> 4 bits

*/

public class Comms {
    RobotController rc;
    
    // constants


    // messaging objects
    static FastQueue<Integer> messageQueue = new FastQueue<Integer>(64);
    static FastQueue<Integer> prioMessageQueue = new FastQueue<Integer>(64);


    public Comms(RobotController r) throws GameActionException {
        rc = r;
    }

    static boolean readAll() throws GameActionException {
        boolean success = true;
        int remainingByteCode = Clock.getBytecodesLeft();


        return success;
    }

}

