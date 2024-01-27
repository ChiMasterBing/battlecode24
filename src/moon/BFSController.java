package moon;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import moon.BFS.*;

public class BFSController {
    public static void move(RobotController rc, MapLocation target) throws GameActionException {
        if(!rc.isMovementReady()){
            return;
        }
        MapLocation myLoc = rc.getLocation();
        Direction d = myLoc.directionTo(target);

        rc.setIndicatorLine(myLoc, target, 0, 0, 0);
        if(d==Direction.NORTH){
            d= BFSNorth.findBestDirection(rc, target);
        }else if(d==Direction.SOUTH){
            d= BFSSouth.findBestDirection(rc, target);
        }else if(d==Direction.EAST){
            d= BFSEast.findBestDirection(rc, target);
        }else if(d==Direction.WEST){
            d= BFSWest.findBestDirection(rc, target);
        }else if(d==Direction.NORTHEAST){
            d= BFSNorthEast.findBestDirection(rc, target);
        }else if(d==Direction.NORTHWEST){
            d= BFSNorthWest.findBestDirection(rc, target);
        }else if(d==Direction.SOUTHEAST){
            d= BFSSouthEast.findBestDirection(rc, target);
        }else{
            d= BFSSouthWest.findBestDirection(rc, target);
        }
        if(rc.canMove(d)){//FIX THIS
            rc.move(d);
        }
    }
}
