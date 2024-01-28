package template1;

// battlecode package
import battlecode.common.*;

// custom package
import template1.java_utils.*;
import template1.Debug.*;
import template1.Utils.*;

public strictfp class RobotPlayer {
    // static constants are not shared between bots
    static Robot bot;
    static int turn_num = 1;

    // use "throws GameActionException" on any method that uses the battlecode package
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {
        

        // init all supporting files
        FastMath.initRand(rc);
        Utils.init(rc);
        Comms.init(rc);
        BugNav.init(rc);
        BogNav.init(rc);
        Utils.init(rc);
        bot = new Robot(rc);
        Comms.writeBotID();

        while (true) {

            try {
                //if (turn_num == 205) rc.resign();
                int numBots = 50;
                Comms.commsStartTurn(turn_num);
                if (!rc.isSpawned() && turn_num > 1 && Comms.myMoveOrder < numBots) bot.attempt_spawn();
                //if (turn_num > 5) rc.resign();
                if (rc.isSpawned()) bot.turn();
                Comms.commsEndTurn();
            } 

            // illegal action within battlecode package
            catch (GameActionException e) {
                Debug.println("game exception");
                e.printStackTrace();
                // reset all bot internal vars
                // bot = new Robot(rc);
            } 

            // other exceptions
            catch (Exception e) {
                Debug.println("java exception");
                e.printStackTrace();
                // reset all bot internal vars
                // bot = new Robot(rc);
            } 

            // end of turn
            finally {
                Clock.yield();
                turn_num += 1;
            }
        }
    }
}
