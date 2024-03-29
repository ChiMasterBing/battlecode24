package bobnoheal;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

import java.util.Random;

public strictfp class RobotPlayer {
    static int turnCount = 0;
    static final Random rng = new Random(6147);

    public static void run(RobotController rc) throws GameActionException {
        Robot robot; //= new Attacker(rc); //~6000 bytecode
        int currentMoveNumber = rc.readSharedArray(0);
        if (currentMoveNumber == 11 || currentMoveNumber == 21 || currentMoveNumber == 1) {
            robot = new Attacker(rc);
        }
        else {
            robot = new Attacker(rc); //~6000 bytecode
        }

        int totalByteCode = 0;

        int turn = rc.getRoundNum();
        while(true){
            robot.play();

            if (turn != rc.getRoundNum()) {
                Debug.println("Bytecode limit reached last turn?");
                turn = rc.getRoundNum();
            }
            turn++;
            totalByteCode += Clock.getBytecodesLeft();

//            rc.setIndicatorString(totalByteCode/turn + " <-- bytecodes");
            Clock.yield();
        }
        // Your code should never reach here (unless it's intentional)! Self-destruction imminent...
    }
}