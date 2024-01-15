package template0;

// battlecode package
import battlecode.common.*;

// custom package
import template0.java_utils.*;
import template0.Debug.*;
import template0.Utils.*;

public strictfp class RobotPlayer {
    // static constants are not shared between bots
    static Robot bot;
    static int turn_num = 1;

    // use "throws GameActionException" on any method that uses the battlecode package
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {
        bot = new Robot(rc);

        // init all supporting files
        FastMath.initRand(rc);
        Utils.init(rc);

        while (true) {

            try {
                if (turn_num == 1 && !rc.isSpawned()) bot.attempt_spawn();
                if (rc.isSpawned()) bot.turn();

                if (turn_num > 1) rc.resign();
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
