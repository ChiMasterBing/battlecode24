package sexylinebling;

import battlecode.common.*;
import java.util.*;

public strictfp class RobotPlayer {
    static int turnCount = 0;
    static final Random rng = new Random(6147);

    public static void run(RobotController rc) throws GameActionException {
        Robot robot = new Attacker(rc); //~6000 bytecode

        int turn = rc.getRoundNum();

        while(true){
            if(robot.myMoveNumber==24||robot.myMoveNumber==49||robot.myMoveNumber==1&&rc.getRoundNum()<2){
                robot = new Builder(rc);
                System.out.println("WOW");
            }
            robot.play();
            
            if (turn != rc.getRoundNum()) {
                Debug.println("Bytecode limit reached last turn?");
                turn = rc.getRoundNum();
            }
            turn++;
            Clock.yield();
        }
        // Your code should never reach here (unless it's intentional)! Self-destruction imminent...
    }
}
