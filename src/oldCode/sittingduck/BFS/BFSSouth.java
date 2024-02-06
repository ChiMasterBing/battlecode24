package sittingduck.BFS;

import battlecode.common.*;

public class BFSSouth {
    public static Direction findBestDirection(RobotController rc, MapLocation target) throws GameActionException {
        MapLocation m40 = rc.getLocation();
        int ret40= 0;
        MapLocation m49= m40.add(Direction.SOUTH);
        Direction d49 = null;
        int ret49= 10000;
        boolean wet49 = false;
        MapLocation m41= m40.add(Direction.EAST);
        Direction d41 = null;
        int ret41= 10000;
        boolean wet41 = false;
        MapLocation m50= m40.add(Direction.SOUTHEAST);
        Direction d50 = null;
        int ret50= 10000;
        boolean wet50 = false;
        MapLocation m48= m40.add(Direction.SOUTHWEST);
        Direction d48 = null;
        int ret48= 10000;
        boolean wet48 = false;
        MapLocation m39= m40.add(Direction.WEST);
        Direction d39 = null;
        int ret39= 10000;
        boolean wet39 = false;
        MapLocation m58= m49.add(Direction.SOUTH);
        Direction d58 = null;
        int ret58= 10000;
        boolean wet58 = false;
        MapLocation m59= m49.add(Direction.SOUTHEAST);
        Direction d59 = null;
        int ret59= 10000;
        boolean wet59 = false;
        MapLocation m57= m49.add(Direction.SOUTHWEST);
        Direction d57 = null;
        int ret57= 10000;
        boolean wet57 = false;
        MapLocation m42= m41.add(Direction.EAST);
        Direction d42 = null;
        int ret42= 10000;
        boolean wet42 = false;
        MapLocation m51= m41.add(Direction.SOUTHEAST);
        Direction d51 = null;
        int ret51= 10000;
        boolean wet51 = false;
        MapLocation m60= m50.add(Direction.SOUTHEAST);
        Direction d60 = null;
        int ret60= 10000;
        boolean wet60 = false;
        MapLocation m56= m48.add(Direction.SOUTHWEST);
        Direction d56 = null;
        int ret56= 10000;
        boolean wet56 = false;
        MapLocation m47= m48.add(Direction.WEST);
        Direction d47 = null;
        int ret47= 10000;
        boolean wet47 = false;
        MapLocation m38= m39.add(Direction.WEST);
        Direction d38 = null;
        int ret38= 10000;
        boolean wet38 = false;
        MapLocation m67= m58.add(Direction.SOUTH);
        Direction d67 = null;
        int ret67= 10000;
        boolean wet67 = false;
        MapLocation m68= m58.add(Direction.SOUTHEAST);
        Direction d68 = null;
        int ret68= 10000;
        boolean wet68 = false;
        MapLocation m66= m58.add(Direction.SOUTHWEST);
        Direction d66 = null;
        int ret66= 10000;
        boolean wet66 = false;
        MapLocation m69= m59.add(Direction.SOUTHEAST);
        Direction d69 = null;
        int ret69= 10000;
        boolean wet69 = false;
        MapLocation m65= m57.add(Direction.SOUTHWEST);
        Direction d65 = null;
        int ret65= 10000;
        boolean wet65 = false;
        MapLocation m43= m42.add(Direction.EAST);
        Direction d43 = null;
        int ret43= 10000;
        boolean wet43 = false;
        MapLocation m52= m42.add(Direction.SOUTHEAST);
        Direction d52 = null;
        int ret52= 10000;
        boolean wet52 = false;
        MapLocation m61= m51.add(Direction.SOUTHEAST);
        Direction d61 = null;
        int ret61= 10000;
        boolean wet61 = false;
        MapLocation m70= m60.add(Direction.SOUTHEAST);
        Direction d70 = null;
        int ret70= 10000;
        boolean wet70 = false;
        MapLocation m64= m56.add(Direction.SOUTHWEST);
        Direction d64 = null;
        int ret64= 10000;
        boolean wet64 = false;
        MapLocation m55= m56.add(Direction.WEST);
        Direction d55 = null;
        int ret55= 10000;
        boolean wet55 = false;
        MapLocation m46= m47.add(Direction.WEST);
        Direction d46 = null;
        int ret46= 10000;
        boolean wet46 = false;
        MapLocation m37= m38.add(Direction.WEST);
        Direction d37 = null;
        int ret37= 10000;
        boolean wet37 = false;
        MapLocation m76= m67.add(Direction.SOUTH);
        Direction d76 = null;
        int ret76= 10000;
        boolean wet76 = false;
        MapLocation m77= m67.add(Direction.SOUTHEAST);
        Direction d77 = null;
        int ret77= 10000;
        boolean wet77 = false;
        MapLocation m75= m67.add(Direction.SOUTHWEST);
        Direction d75 = null;
        int ret75= 10000;
        boolean wet75 = false;
        MapLocation m78= m68.add(Direction.SOUTHEAST);
        Direction d78 = null;
        int ret78= 10000;
        boolean wet78 = false;
        MapLocation m74= m66.add(Direction.SOUTHWEST);
        Direction d74 = null;
        int ret74= 10000;
        boolean wet74 = false;
        MapLocation m44= m43.add(Direction.EAST);
        Direction d44 = null;
        int ret44= 10000;
        boolean wet44 = false;
        MapLocation m53= m43.add(Direction.SOUTHEAST);
        Direction d53 = null;
        int ret53= 10000;
        boolean wet53 = false;
        MapLocation m62= m52.add(Direction.SOUTHEAST);
        Direction d62 = null;
        int ret62= 10000;
        boolean wet62 = false;
        MapLocation m54= m55.add(Direction.WEST);
        Direction d54 = null;
        int ret54= 10000;
        boolean wet54 = false;
        MapLocation m45= m46.add(Direction.WEST);
        Direction d45 = null;
        int ret45= 10000;
        boolean wet45 = false;
        MapLocation m36= m37.add(Direction.WEST);
        Direction d36 = null;
        int ret36= 10000;
        boolean wet36 = false;
        MapInfo mpinfo;
        boolean movable49 = false;
        if(rc.canSenseLocation(m49)){
            mpinfo = rc.senseMapInfo(m49);
            wet49 = mpinfo.isWater();
            if(!rc.isLocationOccupied(m49)&&(mpinfo.isPassable() || wet49)){
                movable49 = true;
            }
        }

        boolean movable41 = false;
        if(rc.canSenseLocation(m41)){
            mpinfo = rc.senseMapInfo(m41);
            wet41 = mpinfo.isWater();
            if(!rc.isLocationOccupied(m41)&&(mpinfo.isPassable() || wet41)){
                movable41 = true;
            }
        }

        boolean movable50 = false;
        if(rc.canSenseLocation(m50)){
            mpinfo = rc.senseMapInfo(m50);
            wet50 = mpinfo.isWater();
            if(!rc.isLocationOccupied(m50)&&(mpinfo.isPassable() || wet50)){
                movable50 = true;
            }
        }

        boolean movable48 = false;
        if(rc.canSenseLocation(m48)){
            mpinfo = rc.senseMapInfo(m48);
            wet48 = mpinfo.isWater();
            if(!rc.isLocationOccupied(m48)&&(mpinfo.isPassable() || wet48)){
                movable48 = true;
            }
        }

        boolean movable39 = false;
        if(rc.canSenseLocation(m39)){
            mpinfo = rc.senseMapInfo(m39);
            wet39 = mpinfo.isWater();
            if(!rc.isLocationOccupied(m39)&&(mpinfo.isPassable() || wet39)){
                movable39 = true;
            }
        }

        boolean movable58 = false;
        if(rc.canSenseLocation(m58)){
            mpinfo = rc.senseMapInfo(m58);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet58 = mpinfo.isWater();
                movable58 = true;
            }
        }

        boolean movable59 = false;
        if(rc.canSenseLocation(m59)){
            mpinfo = rc.senseMapInfo(m59);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet59 = mpinfo.isWater();
                movable59 = true;
            }
        }

        boolean movable57 = false;
        if(rc.canSenseLocation(m57)){
            mpinfo = rc.senseMapInfo(m57);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet57 = mpinfo.isWater();
                movable57 = true;
            }
        }

        boolean movable42 = false;
        if(rc.canSenseLocation(m42)){
            mpinfo = rc.senseMapInfo(m42);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet42 = mpinfo.isWater();
                movable42 = true;
            }
        }

        boolean movable51 = false;
        if(rc.canSenseLocation(m51)){
            mpinfo = rc.senseMapInfo(m51);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet51 = mpinfo.isWater();
                movable51 = true;
            }
        }

        boolean movable60 = false;
        if(rc.canSenseLocation(m60)){
            mpinfo = rc.senseMapInfo(m60);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet60 = mpinfo.isWater();
                movable60 = true;
            }
        }

        boolean movable56 = false;
        if(rc.canSenseLocation(m56)){
            mpinfo = rc.senseMapInfo(m56);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet56 = mpinfo.isWater();
                movable56 = true;
            }
        }

        boolean movable47 = false;
        if(rc.canSenseLocation(m47)){
            mpinfo = rc.senseMapInfo(m47);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet47 = mpinfo.isWater();
                movable47 = true;
            }
        }

        boolean movable38 = false;
        if(rc.canSenseLocation(m38)){
            mpinfo = rc.senseMapInfo(m38);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet38 = mpinfo.isWater();
                movable38 = true;
            }
        }

        boolean movable67 = false;
        if(rc.canSenseLocation(m67)){
            mpinfo = rc.senseMapInfo(m67);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet67 = mpinfo.isWater();
                movable67 = true;
            }
        }

        boolean movable68 = false;
        if(rc.canSenseLocation(m68)){
            mpinfo = rc.senseMapInfo(m68);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet68 = mpinfo.isWater();
                movable68 = true;
            }
        }

        boolean movable66 = false;
        if(rc.canSenseLocation(m66)){
            mpinfo = rc.senseMapInfo(m66);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet66 = mpinfo.isWater();
                movable66 = true;
            }
        }

        boolean movable69 = false;
        if(rc.canSenseLocation(m69)){
            mpinfo = rc.senseMapInfo(m69);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet69 = mpinfo.isWater();
                movable69 = true;
            }
        }

        boolean movable65 = false;
        if(rc.canSenseLocation(m65)){
            mpinfo = rc.senseMapInfo(m65);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet65 = mpinfo.isWater();
                movable65 = true;
            }
        }

        boolean movable43 = false;
        if(rc.canSenseLocation(m43)){
            mpinfo = rc.senseMapInfo(m43);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet43 = mpinfo.isWater();
                movable43 = true;
            }
        }

        boolean movable52 = false;
        if(rc.canSenseLocation(m52)){
            mpinfo = rc.senseMapInfo(m52);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet52 = mpinfo.isWater();
                movable52 = true;
            }
        }

        boolean movable61 = false;
        if(rc.canSenseLocation(m61)){
            mpinfo = rc.senseMapInfo(m61);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet61 = mpinfo.isWater();
                movable61 = true;
            }
        }

        boolean movable70 = false;
        if(rc.canSenseLocation(m70)){
            mpinfo = rc.senseMapInfo(m70);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet70 = mpinfo.isWater();
                movable70 = true;
            }
        }

        boolean movable64 = false;
        if(rc.canSenseLocation(m64)){
            mpinfo = rc.senseMapInfo(m64);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet64 = mpinfo.isWater();
                movable64 = true;
            }
        }

        boolean movable55 = false;
        if(rc.canSenseLocation(m55)){
            mpinfo = rc.senseMapInfo(m55);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet55 = mpinfo.isWater();
                movable55 = true;
            }
        }

        boolean movable46 = false;
        if(rc.canSenseLocation(m46)){
            mpinfo = rc.senseMapInfo(m46);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet46 = mpinfo.isWater();
                movable46 = true;
            }
        }

        boolean movable37 = false;
        if(rc.canSenseLocation(m37)){
            mpinfo = rc.senseMapInfo(m37);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet37 = mpinfo.isWater();
                movable37 = true;
            }
        }

        boolean movable76 = false;
        if(rc.canSenseLocation(m76)){
            mpinfo = rc.senseMapInfo(m76);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet76 = mpinfo.isWater();
                movable76 = true;
            }
        }

        boolean movable77 = false;
        if(rc.canSenseLocation(m77)){
            mpinfo = rc.senseMapInfo(m77);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet77 = mpinfo.isWater();
                movable77 = true;
            }
        }

        boolean movable75 = false;
        if(rc.canSenseLocation(m75)){
            mpinfo = rc.senseMapInfo(m75);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet75 = mpinfo.isWater();
                movable75 = true;
            }
        }

        boolean movable78 = false;
        if(rc.canSenseLocation(m78)){
            mpinfo = rc.senseMapInfo(m78);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet78 = mpinfo.isWater();
                movable78 = true;
            }
        }

        boolean movable74 = false;
        if(rc.canSenseLocation(m74)){
            mpinfo = rc.senseMapInfo(m74);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet74 = mpinfo.isWater();
                movable74 = true;
            }
        }

        boolean movable44 = false;
        if(rc.canSenseLocation(m44)){
            mpinfo = rc.senseMapInfo(m44);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet44 = mpinfo.isWater();
                movable44 = true;
            }
        }

        boolean movable53 = false;
        if(rc.canSenseLocation(m53)){
            mpinfo = rc.senseMapInfo(m53);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet53 = mpinfo.isWater();
                movable53 = true;
            }
        }

        boolean movable62 = false;
        if(rc.canSenseLocation(m62)){
            mpinfo = rc.senseMapInfo(m62);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet62 = mpinfo.isWater();
                movable62 = true;
            }
        }

        boolean movable54 = false;
        if(rc.canSenseLocation(m54)){
            mpinfo = rc.senseMapInfo(m54);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet54 = mpinfo.isWater();
                movable54 = true;
            }
        }

        boolean movable45 = false;
        if(rc.canSenseLocation(m45)){
            mpinfo = rc.senseMapInfo(m45);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet45 = mpinfo.isWater();
                movable45 = true;
            }
        }

        boolean movable36 = false;
        if(rc.canSenseLocation(m36)){
            mpinfo = rc.senseMapInfo(m36);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet36 = mpinfo.isWater();
                movable36 = true;
            }
        }

        if(ret40!=10000){
            if(movable49){
                if(wet49){
                    if(ret49>2+ret40){
                        d49=Direction.SOUTH;
                        ret49 = 2+ret40;
                    }
                }else{
                    if(ret49>1+ret40){
                        d49=Direction.SOUTH;
                        ret49 = 1+ret40;
                    }
                }
            }

            if(movable41){
                if(wet41){
                    if(ret41>2+ret40){
                        d41=Direction.EAST;
                        ret41 = 2+ret40;
                    }
                }else{
                    if(ret41>1+ret40){
                        d41=Direction.EAST;
                        ret41 = 1+ret40;
                    }
                }
            }

            if(movable50){
                if(wet50){
                    if(ret50>3+ret40){
                        d50=Direction.SOUTHEAST;
                        ret50 = 3+ret40;
                    }
                }else{
                    if(ret50>2+ret40){
                        d50=Direction.SOUTHEAST;
                        ret50 = 2+ret40;
                    }
                }
            }

            if(movable48){
                if(wet48){
                    if(ret48>3+ret40){
                        d48=Direction.SOUTHWEST;
                        ret48 = 3+ret40;
                    }
                }else{
                    if(ret48>2+ret40){
                        d48=Direction.SOUTHWEST;
                        ret48 = 2+ret40;
                    }
                }
            }

            if(movable39){
                if(wet39){
                    if(ret39>2+ret40){
                        d39=Direction.WEST;
                        ret39 = 2+ret40;
                    }
                }else{
                    if(ret39>1+ret40){
                        d39=Direction.WEST;
                        ret39 = 1+ret40;
                    }
                }
            }

        }
        if(ret49!=10000){
            if(movable58){
                if(wet58){
                    if(ret58>2+ret49){
                        d58=d49;
                        ret58 = 2+ret49;
                    }
                }else{
                    if(ret58>1+ret49){
                        d58=d49;
                        ret58 = 1+ret49;
                    }
                }
            }

            if(movable50){
                if(wet50){
                    if(ret50>2+ret49){
                        d50=d49;
                        ret50 = 2+ret49;
                    }
                }else{
                    if(ret50>1+ret49){
                        d50=d49;
                        ret50 = 1+ret49;
                    }
                }
            }

            if(movable59){
                if(wet59){
                    if(ret59>2+ret49){
                        d59=d49;
                        ret59 = 2+ret49;
                    }
                }else{
                    if(ret59>1+ret49){
                        d59=d49;
                        ret59 = 1+ret49;
                    }
                }
            }

            if(movable57){
                if(wet57){
                    if(ret57>2+ret49){
                        d57=d49;
                        ret57 = 2+ret49;
                    }
                }else{
                    if(ret57>1+ret49){
                        d57=d49;
                        ret57 = 1+ret49;
                    }
                }
            }

            if(movable48){
                if(wet48){
                    if(ret48>2+ret49){
                        d48=d49;
                        ret48 = 2+ret49;
                    }
                }else{
                    if(ret48>1+ret49){
                        d48=d49;
                        ret48 = 1+ret49;
                    }
                }
            }

        }
        if(ret41!=10000){
            if(movable50){
                if(wet50){
                    if(ret50>2+ret41){
                        d50=d41;
                        ret50 = 2+ret41;
                    }
                }else{
                    if(ret50>1+ret41){
                        d50=d41;
                        ret50 = 1+ret41;
                    }
                }
            }

            if(movable42){
                if(wet42){
                    if(ret42>2+ret41){
                        d42=d41;
                        ret42 = 2+ret41;
                    }
                }else{
                    if(ret42>1+ret41){
                        d42=d41;
                        ret42 = 1+ret41;
                    }
                }
            }

            if(movable51){
                if(wet51){
                    if(ret51>2+ret41){
                        d51=d41;
                        ret51 = 2+ret41;
                    }
                }else{
                    if(ret51>1+ret41){
                        d51=d41;
                        ret51 = 1+ret41;
                    }
                }
            }

            if(movable49){
                if(wet49){
                    if(ret49>2+ret41){
                        d49=d41;
                        ret49 = 2+ret41;
                    }
                }else{
                    if(ret49>1+ret41){
                        d49=d41;
                        ret49 = 1+ret41;
                    }
                }
            }

        }
        if(ret50!=10000){
            if(movable59){
                if(wet59){
                    if(ret59>2+ret50){
                        d59=d50;
                        ret59 = 2+ret50;
                    }
                }else{
                    if(ret59>1+ret50){
                        d59=d50;
                        ret59 = 1+ret50;
                    }
                }
            }

            if(movable51){
                if(wet51){
                    if(ret51>2+ret50){
                        d51=d50;
                        ret51 = 2+ret50;
                    }
                }else{
                    if(ret51>1+ret50){
                        d51=d50;
                        ret51 = 1+ret50;
                    }
                }
            }

            if(movable60){
                if(wet60){
                    if(ret60>2+ret50){
                        d60=d50;
                        ret60 = 2+ret50;
                    }
                }else{
                    if(ret60>1+ret50){
                        d60=d50;
                        ret60 = 1+ret50;
                    }
                }
            }

            if(movable58){
                if(wet58){
                    if(ret58>2+ret50){
                        d58=d50;
                        ret58 = 2+ret50;
                    }
                }else{
                    if(ret58>1+ret50){
                        d58=d50;
                        ret58 = 1+ret50;
                    }
                }
            }

            if(movable49){
                if(wet49){
                    if(ret49>2+ret50){
                        d49=d50;
                        ret49 = 2+ret50;
                    }
                }else{
                    if(ret49>1+ret50){
                        d49=d50;
                        ret49 = 1+ret50;
                    }
                }
            }

        }
        if(ret48!=10000){
            if(movable57){
                if(wet57){
                    if(ret57>2+ret48){
                        d57=d48;
                        ret57 = 2+ret48;
                    }
                }else{
                    if(ret57>1+ret48){
                        d57=d48;
                        ret57 = 1+ret48;
                    }
                }
            }

            if(movable49){
                if(wet49){
                    if(ret49>2+ret48){
                        d49=d48;
                        ret49 = 2+ret48;
                    }
                }else{
                    if(ret49>1+ret48){
                        d49=d48;
                        ret49 = 1+ret48;
                    }
                }
            }

            if(movable58){
                if(wet58){
                    if(ret58>2+ret48){
                        d58=d48;
                        ret58 = 2+ret48;
                    }
                }else{
                    if(ret58>1+ret48){
                        d58=d48;
                        ret58 = 1+ret48;
                    }
                }
            }

            if(movable56){
                if(wet56){
                    if(ret56>2+ret48){
                        d56=d48;
                        ret56 = 2+ret48;
                    }
                }else{
                    if(ret56>1+ret48){
                        d56=d48;
                        ret56 = 1+ret48;
                    }
                }
            }

            if(movable47){
                if(wet47){
                    if(ret47>2+ret48){
                        d47=d48;
                        ret47 = 2+ret48;
                    }
                }else{
                    if(ret47>1+ret48){
                        d47=d48;
                        ret47 = 1+ret48;
                    }
                }
            }

        }
        if(ret39!=10000){
            if(movable48){
                if(wet48){
                    if(ret48>2+ret39){
                        d48=d39;
                        ret48 = 2+ret39;
                    }
                }else{
                    if(ret48>1+ret39){
                        d48=d39;
                        ret48 = 1+ret39;
                    }
                }
            }

            if(movable49){
                if(wet49){
                    if(ret49>2+ret39){
                        d49=d39;
                        ret49 = 2+ret39;
                    }
                }else{
                    if(ret49>1+ret39){
                        d49=d39;
                        ret49 = 1+ret39;
                    }
                }
            }

            if(movable47){
                if(wet47){
                    if(ret47>2+ret39){
                        d47=d39;
                        ret47 = 2+ret39;
                    }
                }else{
                    if(ret47>1+ret39){
                        d47=d39;
                        ret47 = 1+ret39;
                    }
                }
            }

            if(movable38){
                if(wet38){
                    if(ret38>2+ret39){
                        d38=d39;
                        ret38 = 2+ret39;
                    }
                }else{
                    if(ret38>1+ret39){
                        d38=d39;
                        ret38 = 1+ret39;
                    }
                }
            }

        }
        if(ret58!=10000){
            if(movable67){
                if(wet67){
                    if(ret67>2+ret58){
                        d67=d58;
                        ret67 = 2+ret58;
                    }
                }else{
                    if(ret67>1+ret58){
                        d67=d58;
                        ret67 = 1+ret58;
                    }
                }
            }

            if(movable59){
                if(wet59){
                    if(ret59>2+ret58){
                        d59=d58;
                        ret59 = 2+ret58;
                    }
                }else{
                    if(ret59>1+ret58){
                        d59=d58;
                        ret59 = 1+ret58;
                    }
                }
            }

            if(movable68){
                if(wet68){
                    if(ret68>2+ret58){
                        d68=d58;
                        ret68 = 2+ret58;
                    }
                }else{
                    if(ret68>1+ret58){
                        d68=d58;
                        ret68 = 1+ret58;
                    }
                }
            }

            if(movable66){
                if(wet66){
                    if(ret66>2+ret58){
                        d66=d58;
                        ret66 = 2+ret58;
                    }
                }else{
                    if(ret66>1+ret58){
                        d66=d58;
                        ret66 = 1+ret58;
                    }
                }
            }

            if(movable57){
                if(wet57){
                    if(ret57>2+ret58){
                        d57=d58;
                        ret57 = 2+ret58;
                    }
                }else{
                    if(ret57>1+ret58){
                        d57=d58;
                        ret57 = 1+ret58;
                    }
                }
            }

        }
        if(ret59!=10000){
            if(movable68){
                if(wet68){
                    if(ret68>2+ret59){
                        d68=d59;
                        ret68 = 2+ret59;
                    }
                }else{
                    if(ret68>1+ret59){
                        d68=d59;
                        ret68 = 1+ret59;
                    }
                }
            }

            if(movable60){
                if(wet60){
                    if(ret60>2+ret59){
                        d60=d59;
                        ret60 = 2+ret59;
                    }
                }else{
                    if(ret60>1+ret59){
                        d60=d59;
                        ret60 = 1+ret59;
                    }
                }
            }

            if(movable69){
                if(wet69){
                    if(ret69>2+ret59){
                        d69=d59;
                        ret69 = 2+ret59;
                    }
                }else{
                    if(ret69>1+ret59){
                        d69=d59;
                        ret69 = 1+ret59;
                    }
                }
            }

            if(movable67){
                if(wet67){
                    if(ret67>2+ret59){
                        d67=d59;
                        ret67 = 2+ret59;
                    }
                }else{
                    if(ret67>1+ret59){
                        d67=d59;
                        ret67 = 1+ret59;
                    }
                }
            }

            if(movable58){
                if(wet58){
                    if(ret58>2+ret59){
                        d58=d59;
                        ret58 = 2+ret59;
                    }
                }else{
                    if(ret58>1+ret59){
                        d58=d59;
                        ret58 = 1+ret59;
                    }
                }
            }

        }
        if(ret57!=10000){
            if(movable66){
                if(wet66){
                    if(ret66>2+ret57){
                        d66=d57;
                        ret66 = 2+ret57;
                    }
                }else{
                    if(ret66>1+ret57){
                        d66=d57;
                        ret66 = 1+ret57;
                    }
                }
            }

            if(movable58){
                if(wet58){
                    if(ret58>2+ret57){
                        d58=d57;
                        ret58 = 2+ret57;
                    }
                }else{
                    if(ret58>1+ret57){
                        d58=d57;
                        ret58 = 1+ret57;
                    }
                }
            }

            if(movable67){
                if(wet67){
                    if(ret67>2+ret57){
                        d67=d57;
                        ret67 = 2+ret57;
                    }
                }else{
                    if(ret67>1+ret57){
                        d67=d57;
                        ret67 = 1+ret57;
                    }
                }
            }

            if(movable65){
                if(wet65){
                    if(ret65>2+ret57){
                        d65=d57;
                        ret65 = 2+ret57;
                    }
                }else{
                    if(ret65>1+ret57){
                        d65=d57;
                        ret65 = 1+ret57;
                    }
                }
            }

            if(movable56){
                if(wet56){
                    if(ret56>2+ret57){
                        d56=d57;
                        ret56 = 2+ret57;
                    }
                }else{
                    if(ret56>1+ret57){
                        d56=d57;
                        ret56 = 1+ret57;
                    }
                }
            }

        }
        if(ret42!=10000){
            if(movable51){
                if(wet51){
                    if(ret51>2+ret42){
                        d51=d42;
                        ret51 = 2+ret42;
                    }
                }else{
                    if(ret51>1+ret42){
                        d51=d42;
                        ret51 = 1+ret42;
                    }
                }
            }

            if(movable43){
                if(wet43){
                    if(ret43>2+ret42){
                        d43=d42;
                        ret43 = 2+ret42;
                    }
                }else{
                    if(ret43>1+ret42){
                        d43=d42;
                        ret43 = 1+ret42;
                    }
                }
            }

            if(movable52){
                if(wet52){
                    if(ret52>2+ret42){
                        d52=d42;
                        ret52 = 2+ret42;
                    }
                }else{
                    if(ret52>1+ret42){
                        d52=d42;
                        ret52 = 1+ret42;
                    }
                }
            }

            if(movable50){
                if(wet50){
                    if(ret50>2+ret42){
                        d50=d42;
                        ret50 = 2+ret42;
                    }
                }else{
                    if(ret50>1+ret42){
                        d50=d42;
                        ret50 = 1+ret42;
                    }
                }
            }

            if(movable41){
                if(wet41){
                    if(ret41>2+ret42){
                        d41=d42;
                        ret41 = 2+ret42;
                    }
                }else{
                    if(ret41>1+ret42){
                        d41=d42;
                        ret41 = 1+ret42;
                    }
                }
            }

        }
        if(ret51!=10000){
            if(movable60){
                if(wet60){
                    if(ret60>2+ret51){
                        d60=d51;
                        ret60 = 2+ret51;
                    }
                }else{
                    if(ret60>1+ret51){
                        d60=d51;
                        ret60 = 1+ret51;
                    }
                }
            }

            if(movable52){
                if(wet52){
                    if(ret52>2+ret51){
                        d52=d51;
                        ret52 = 2+ret51;
                    }
                }else{
                    if(ret52>1+ret51){
                        d52=d51;
                        ret52 = 1+ret51;
                    }
                }
            }

            if(movable61){
                if(wet61){
                    if(ret61>2+ret51){
                        d61=d51;
                        ret61 = 2+ret51;
                    }
                }else{
                    if(ret61>1+ret51){
                        d61=d51;
                        ret61 = 1+ret51;
                    }
                }
            }

            if(movable59){
                if(wet59){
                    if(ret59>2+ret51){
                        d59=d51;
                        ret59 = 2+ret51;
                    }
                }else{
                    if(ret59>1+ret51){
                        d59=d51;
                        ret59 = 1+ret51;
                    }
                }
            }

            if(movable50){
                if(wet50){
                    if(ret50>2+ret51){
                        d50=d51;
                        ret50 = 2+ret51;
                    }
                }else{
                    if(ret50>1+ret51){
                        d50=d51;
                        ret50 = 1+ret51;
                    }
                }
            }

        }
        if(ret60!=10000){
            if(movable69){
                if(wet69){
                    if(ret69>2+ret60){
                        d69=d60;
                        ret69 = 2+ret60;
                    }
                }else{
                    if(ret69>1+ret60){
                        d69=d60;
                        ret69 = 1+ret60;
                    }
                }
            }

            if(movable61){
                if(wet61){
                    if(ret61>2+ret60){
                        d61=d60;
                        ret61 = 2+ret60;
                    }
                }else{
                    if(ret61>1+ret60){
                        d61=d60;
                        ret61 = 1+ret60;
                    }
                }
            }

            if(movable70){
                if(wet70){
                    if(ret70>2+ret60){
                        d70=d60;
                        ret70 = 2+ret60;
                    }
                }else{
                    if(ret70>1+ret60){
                        d70=d60;
                        ret70 = 1+ret60;
                    }
                }
            }

            if(movable68){
                if(wet68){
                    if(ret68>2+ret60){
                        d68=d60;
                        ret68 = 2+ret60;
                    }
                }else{
                    if(ret68>1+ret60){
                        d68=d60;
                        ret68 = 1+ret60;
                    }
                }
            }

            if(movable59){
                if(wet59){
                    if(ret59>2+ret60){
                        d59=d60;
                        ret59 = 2+ret60;
                    }
                }else{
                    if(ret59>1+ret60){
                        d59=d60;
                        ret59 = 1+ret60;
                    }
                }
            }

        }
        if(ret56!=10000){
            if(movable65){
                if(wet65){
                    if(ret65>2+ret56){
                        d65=d56;
                        ret65 = 2+ret56;
                    }
                }else{
                    if(ret65>1+ret56){
                        d65=d56;
                        ret65 = 1+ret56;
                    }
                }
            }

            if(movable57){
                if(wet57){
                    if(ret57>2+ret56){
                        d57=d56;
                        ret57 = 2+ret56;
                    }
                }else{
                    if(ret57>1+ret56){
                        d57=d56;
                        ret57 = 1+ret56;
                    }
                }
            }

            if(movable66){
                if(wet66){
                    if(ret66>2+ret56){
                        d66=d56;
                        ret66 = 2+ret56;
                    }
                }else{
                    if(ret66>1+ret56){
                        d66=d56;
                        ret66 = 1+ret56;
                    }
                }
            }

            if(movable64){
                if(wet64){
                    if(ret64>2+ret56){
                        d64=d56;
                        ret64 = 2+ret56;
                    }
                }else{
                    if(ret64>1+ret56){
                        d64=d56;
                        ret64 = 1+ret56;
                    }
                }
            }

            if(movable55){
                if(wet55){
                    if(ret55>2+ret56){
                        d55=d56;
                        ret55 = 2+ret56;
                    }
                }else{
                    if(ret55>1+ret56){
                        d55=d56;
                        ret55 = 1+ret56;
                    }
                }
            }

        }
        if(ret47!=10000){
            if(movable56){
                if(wet56){
                    if(ret56>2+ret47){
                        d56=d47;
                        ret56 = 2+ret47;
                    }
                }else{
                    if(ret56>1+ret47){
                        d56=d47;
                        ret56 = 1+ret47;
                    }
                }
            }

            if(movable48){
                if(wet48){
                    if(ret48>2+ret47){
                        d48=d47;
                        ret48 = 2+ret47;
                    }
                }else{
                    if(ret48>1+ret47){
                        d48=d47;
                        ret48 = 1+ret47;
                    }
                }
            }

            if(movable57){
                if(wet57){
                    if(ret57>2+ret47){
                        d57=d47;
                        ret57 = 2+ret47;
                    }
                }else{
                    if(ret57>1+ret47){
                        d57=d47;
                        ret57 = 1+ret47;
                    }
                }
            }

            if(movable55){
                if(wet55){
                    if(ret55>2+ret47){
                        d55=d47;
                        ret55 = 2+ret47;
                    }
                }else{
                    if(ret55>1+ret47){
                        d55=d47;
                        ret55 = 1+ret47;
                    }
                }
            }

            if(movable46){
                if(wet46){
                    if(ret46>2+ret47){
                        d46=d47;
                        ret46 = 2+ret47;
                    }
                }else{
                    if(ret46>1+ret47){
                        d46=d47;
                        ret46 = 1+ret47;
                    }
                }
            }

        }
        if(ret38!=10000){
            if(movable47){
                if(wet47){
                    if(ret47>2+ret38){
                        d47=d38;
                        ret47 = 2+ret38;
                    }
                }else{
                    if(ret47>1+ret38){
                        d47=d38;
                        ret47 = 1+ret38;
                    }
                }
            }

            if(movable39){
                if(wet39){
                    if(ret39>2+ret38){
                        d39=d38;
                        ret39 = 2+ret38;
                    }
                }else{
                    if(ret39>1+ret38){
                        d39=d38;
                        ret39 = 1+ret38;
                    }
                }
            }

            if(movable48){
                if(wet48){
                    if(ret48>2+ret38){
                        d48=d38;
                        ret48 = 2+ret38;
                    }
                }else{
                    if(ret48>1+ret38){
                        d48=d38;
                        ret48 = 1+ret38;
                    }
                }
            }

            if(movable46){
                if(wet46){
                    if(ret46>2+ret38){
                        d46=d38;
                        ret46 = 2+ret38;
                    }
                }else{
                    if(ret46>1+ret38){
                        d46=d38;
                        ret46 = 1+ret38;
                    }
                }
            }

            if(movable37){
                if(wet37){
                    if(ret37>2+ret38){
                        d37=d38;
                        ret37 = 2+ret38;
                    }
                }else{
                    if(ret37>1+ret38){
                        d37=d38;
                        ret37 = 1+ret38;
                    }
                }
            }

        }
        if(ret67!=10000){
            if(movable76){
                if(wet76){
                    if(ret76>2+ret67){
                        d76=d67;
                        ret76 = 2+ret67;
                    }
                }else{
                    if(ret76>1+ret67){
                        d76=d67;
                        ret76 = 1+ret67;
                    }
                }
            }

            if(movable68){
                if(wet68){
                    if(ret68>2+ret67){
                        d68=d67;
                        ret68 = 2+ret67;
                    }
                }else{
                    if(ret68>1+ret67){
                        d68=d67;
                        ret68 = 1+ret67;
                    }
                }
            }

            if(movable77){
                if(wet77){
                    if(ret77>2+ret67){
                        d77=d67;
                        ret77 = 2+ret67;
                    }
                }else{
                    if(ret77>1+ret67){
                        d77=d67;
                        ret77 = 1+ret67;
                    }
                }
            }

            if(movable75){
                if(wet75){
                    if(ret75>2+ret67){
                        d75=d67;
                        ret75 = 2+ret67;
                    }
                }else{
                    if(ret75>1+ret67){
                        d75=d67;
                        ret75 = 1+ret67;
                    }
                }
            }

            if(movable66){
                if(wet66){
                    if(ret66>2+ret67){
                        d66=d67;
                        ret66 = 2+ret67;
                    }
                }else{
                    if(ret66>1+ret67){
                        d66=d67;
                        ret66 = 1+ret67;
                    }
                }
            }

        }
        if(ret68!=10000){
            if(movable77){
                if(wet77){
                    if(ret77>2+ret68){
                        d77=d68;
                        ret77 = 2+ret68;
                    }
                }else{
                    if(ret77>1+ret68){
                        d77=d68;
                        ret77 = 1+ret68;
                    }
                }
            }

            if(movable69){
                if(wet69){
                    if(ret69>2+ret68){
                        d69=d68;
                        ret69 = 2+ret68;
                    }
                }else{
                    if(ret69>1+ret68){
                        d69=d68;
                        ret69 = 1+ret68;
                    }
                }
            }

            if(movable78){
                if(wet78){
                    if(ret78>2+ret68){
                        d78=d68;
                        ret78 = 2+ret68;
                    }
                }else{
                    if(ret78>1+ret68){
                        d78=d68;
                        ret78 = 1+ret68;
                    }
                }
            }

            if(movable76){
                if(wet76){
                    if(ret76>2+ret68){
                        d76=d68;
                        ret76 = 2+ret68;
                    }
                }else{
                    if(ret76>1+ret68){
                        d76=d68;
                        ret76 = 1+ret68;
                    }
                }
            }

            if(movable67){
                if(wet67){
                    if(ret67>2+ret68){
                        d67=d68;
                        ret67 = 2+ret68;
                    }
                }else{
                    if(ret67>1+ret68){
                        d67=d68;
                        ret67 = 1+ret68;
                    }
                }
            }

        }
        if(ret66!=10000){
            if(movable75){
                if(wet75){
                    if(ret75>2+ret66){
                        d75=d66;
                        ret75 = 2+ret66;
                    }
                }else{
                    if(ret75>1+ret66){
                        d75=d66;
                        ret75 = 1+ret66;
                    }
                }
            }

            if(movable67){
                if(wet67){
                    if(ret67>2+ret66){
                        d67=d66;
                        ret67 = 2+ret66;
                    }
                }else{
                    if(ret67>1+ret66){
                        d67=d66;
                        ret67 = 1+ret66;
                    }
                }
            }

            if(movable76){
                if(wet76){
                    if(ret76>2+ret66){
                        d76=d66;
                        ret76 = 2+ret66;
                    }
                }else{
                    if(ret76>1+ret66){
                        d76=d66;
                        ret76 = 1+ret66;
                    }
                }
            }

            if(movable74){
                if(wet74){
                    if(ret74>2+ret66){
                        d74=d66;
                        ret74 = 2+ret66;
                    }
                }else{
                    if(ret74>1+ret66){
                        d74=d66;
                        ret74 = 1+ret66;
                    }
                }
            }

            if(movable65){
                if(wet65){
                    if(ret65>2+ret66){
                        d65=d66;
                        ret65 = 2+ret66;
                    }
                }else{
                    if(ret65>1+ret66){
                        d65=d66;
                        ret65 = 1+ret66;
                    }
                }
            }

        }
        if(ret69!=10000){
            if(movable78){
                if(wet78){
                    if(ret78>2+ret69){
                        d78=d69;
                        ret78 = 2+ret69;
                    }
                }else{
                    if(ret78>1+ret69){
                        d78=d69;
                        ret78 = 1+ret69;
                    }
                }
            }

            if(movable70){
                if(wet70){
                    if(ret70>2+ret69){
                        d70=d69;
                        ret70 = 2+ret69;
                    }
                }else{
                    if(ret70>1+ret69){
                        d70=d69;
                        ret70 = 1+ret69;
                    }
                }
            }

            if(movable77){
                if(wet77){
                    if(ret77>2+ret69){
                        d77=d69;
                        ret77 = 2+ret69;
                    }
                }else{
                    if(ret77>1+ret69){
                        d77=d69;
                        ret77 = 1+ret69;
                    }
                }
            }

            if(movable68){
                if(wet68){
                    if(ret68>2+ret69){
                        d68=d69;
                        ret68 = 2+ret69;
                    }
                }else{
                    if(ret68>1+ret69){
                        d68=d69;
                        ret68 = 1+ret69;
                    }
                }
            }

        }
        if(ret65!=10000){
            if(movable74){
                if(wet74){
                    if(ret74>2+ret65){
                        d74=d65;
                        ret74 = 2+ret65;
                    }
                }else{
                    if(ret74>1+ret65){
                        d74=d65;
                        ret74 = 1+ret65;
                    }
                }
            }

            if(movable66){
                if(wet66){
                    if(ret66>2+ret65){
                        d66=d65;
                        ret66 = 2+ret65;
                    }
                }else{
                    if(ret66>1+ret65){
                        d66=d65;
                        ret66 = 1+ret65;
                    }
                }
            }

            if(movable75){
                if(wet75){
                    if(ret75>2+ret65){
                        d75=d65;
                        ret75 = 2+ret65;
                    }
                }else{
                    if(ret75>1+ret65){
                        d75=d65;
                        ret75 = 1+ret65;
                    }
                }
            }

            if(movable64){
                if(wet64){
                    if(ret64>2+ret65){
                        d64=d65;
                        ret64 = 2+ret65;
                    }
                }else{
                    if(ret64>1+ret65){
                        d64=d65;
                        ret64 = 1+ret65;
                    }
                }
            }

        }
        if(ret43!=10000){
            if(movable52){
                if(wet52){
                    if(ret52>2+ret43){
                        d52=d43;
                        ret52 = 2+ret43;
                    }
                }else{
                    if(ret52>1+ret43){
                        d52=d43;
                        ret52 = 1+ret43;
                    }
                }
            }

            if(movable44){
                if(wet44){
                    if(ret44>2+ret43){
                        d44=d43;
                        ret44 = 2+ret43;
                    }
                }else{
                    if(ret44>1+ret43){
                        d44=d43;
                        ret44 = 1+ret43;
                    }
                }
            }

            if(movable53){
                if(wet53){
                    if(ret53>2+ret43){
                        d53=d43;
                        ret53 = 2+ret43;
                    }
                }else{
                    if(ret53>1+ret43){
                        d53=d43;
                        ret53 = 1+ret43;
                    }
                }
            }

            if(movable51){
                if(wet51){
                    if(ret51>2+ret43){
                        d51=d43;
                        ret51 = 2+ret43;
                    }
                }else{
                    if(ret51>1+ret43){
                        d51=d43;
                        ret51 = 1+ret43;
                    }
                }
            }

            if(movable42){
                if(wet42){
                    if(ret42>2+ret43){
                        d42=d43;
                        ret42 = 2+ret43;
                    }
                }else{
                    if(ret42>1+ret43){
                        d42=d43;
                        ret42 = 1+ret43;
                    }
                }
            }

        }
        if(ret52!=10000){
            if(movable61){
                if(wet61){
                    if(ret61>2+ret52){
                        d61=d52;
                        ret61 = 2+ret52;
                    }
                }else{
                    if(ret61>1+ret52){
                        d61=d52;
                        ret61 = 1+ret52;
                    }
                }
            }

            if(movable53){
                if(wet53){
                    if(ret53>2+ret52){
                        d53=d52;
                        ret53 = 2+ret52;
                    }
                }else{
                    if(ret53>1+ret52){
                        d53=d52;
                        ret53 = 1+ret52;
                    }
                }
            }

            if(movable62){
                if(wet62){
                    if(ret62>2+ret52){
                        d62=d52;
                        ret62 = 2+ret52;
                    }
                }else{
                    if(ret62>1+ret52){
                        d62=d52;
                        ret62 = 1+ret52;
                    }
                }
            }

            if(movable60){
                if(wet60){
                    if(ret60>2+ret52){
                        d60=d52;
                        ret60 = 2+ret52;
                    }
                }else{
                    if(ret60>1+ret52){
                        d60=d52;
                        ret60 = 1+ret52;
                    }
                }
            }

            if(movable51){
                if(wet51){
                    if(ret51>2+ret52){
                        d51=d52;
                        ret51 = 2+ret52;
                    }
                }else{
                    if(ret51>1+ret52){
                        d51=d52;
                        ret51 = 1+ret52;
                    }
                }
            }

        }
        if(ret61!=10000){
            if(movable70){
                if(wet70){
                    if(ret70>2+ret61){
                        d70=d61;
                        ret70 = 2+ret61;
                    }
                }else{
                    if(ret70>1+ret61){
                        d70=d61;
                        ret70 = 1+ret61;
                    }
                }
            }

            if(movable62){
                if(wet62){
                    if(ret62>2+ret61){
                        d62=d61;
                        ret62 = 2+ret61;
                    }
                }else{
                    if(ret62>1+ret61){
                        d62=d61;
                        ret62 = 1+ret61;
                    }
                }
            }

            if(movable69){
                if(wet69){
                    if(ret69>2+ret61){
                        d69=d61;
                        ret69 = 2+ret61;
                    }
                }else{
                    if(ret69>1+ret61){
                        d69=d61;
                        ret69 = 1+ret61;
                    }
                }
            }

            if(movable60){
                if(wet60){
                    if(ret60>2+ret61){
                        d60=d61;
                        ret60 = 2+ret61;
                    }
                }else{
                    if(ret60>1+ret61){
                        d60=d61;
                        ret60 = 1+ret61;
                    }
                }
            }

        }
        if(ret70!=10000){
            if(movable78){
                if(wet78){
                    if(ret78>2+ret70){
                        d78=d70;
                        ret78 = 2+ret70;
                    }
                }else{
                    if(ret78>1+ret70){
                        d78=d70;
                        ret78 = 1+ret70;
                    }
                }
            }

            if(movable69){
                if(wet69){
                    if(ret69>2+ret70){
                        d69=d70;
                        ret69 = 2+ret70;
                    }
                }else{
                    if(ret69>1+ret70){
                        d69=d70;
                        ret69 = 1+ret70;
                    }
                }
            }

        }
        if(ret64!=10000){
            if(movable65){
                if(wet65){
                    if(ret65>2+ret64){
                        d65=d64;
                        ret65 = 2+ret64;
                    }
                }else{
                    if(ret65>1+ret64){
                        d65=d64;
                        ret65 = 1+ret64;
                    }
                }
            }

            if(movable74){
                if(wet74){
                    if(ret74>2+ret64){
                        d74=d64;
                        ret74 = 2+ret64;
                    }
                }else{
                    if(ret74>1+ret64){
                        d74=d64;
                        ret74 = 1+ret64;
                    }
                }
            }

        }
        if(ret55!=10000){
            if(movable64){
                if(wet64){
                    if(ret64>2+ret55){
                        d64=d55;
                        ret64 = 2+ret55;
                    }
                }else{
                    if(ret64>1+ret55){
                        d64=d55;
                        ret64 = 1+ret55;
                    }
                }
            }

            if(movable56){
                if(wet56){
                    if(ret56>2+ret55){
                        d56=d55;
                        ret56 = 2+ret55;
                    }
                }else{
                    if(ret56>1+ret55){
                        d56=d55;
                        ret56 = 1+ret55;
                    }
                }
            }

            if(movable65){
                if(wet65){
                    if(ret65>2+ret55){
                        d65=d55;
                        ret65 = 2+ret55;
                    }
                }else{
                    if(ret65>1+ret55){
                        d65=d55;
                        ret65 = 1+ret55;
                    }
                }
            }

            if(movable54){
                if(wet54){
                    if(ret54>2+ret55){
                        d54=d55;
                        ret54 = 2+ret55;
                    }
                }else{
                    if(ret54>1+ret55){
                        d54=d55;
                        ret54 = 1+ret55;
                    }
                }
            }

        }
        if(ret46!=10000){
            if(movable55){
                if(wet55){
                    if(ret55>2+ret46){
                        d55=d46;
                        ret55 = 2+ret46;
                    }
                }else{
                    if(ret55>1+ret46){
                        d55=d46;
                        ret55 = 1+ret46;
                    }
                }
            }

            if(movable47){
                if(wet47){
                    if(ret47>2+ret46){
                        d47=d46;
                        ret47 = 2+ret46;
                    }
                }else{
                    if(ret47>1+ret46){
                        d47=d46;
                        ret47 = 1+ret46;
                    }
                }
            }

            if(movable56){
                if(wet56){
                    if(ret56>2+ret46){
                        d56=d46;
                        ret56 = 2+ret46;
                    }
                }else{
                    if(ret56>1+ret46){
                        d56=d46;
                        ret56 = 1+ret46;
                    }
                }
            }

            if(movable54){
                if(wet54){
                    if(ret54>2+ret46){
                        d54=d46;
                        ret54 = 2+ret46;
                    }
                }else{
                    if(ret54>1+ret46){
                        d54=d46;
                        ret54 = 1+ret46;
                    }
                }
            }

            if(movable45){
                if(wet45){
                    if(ret45>2+ret46){
                        d45=d46;
                        ret45 = 2+ret46;
                    }
                }else{
                    if(ret45>1+ret46){
                        d45=d46;
                        ret45 = 1+ret46;
                    }
                }
            }

        }
        if(ret37!=10000){
            if(movable46){
                if(wet46){
                    if(ret46>2+ret37){
                        d46=d37;
                        ret46 = 2+ret37;
                    }
                }else{
                    if(ret46>1+ret37){
                        d46=d37;
                        ret46 = 1+ret37;
                    }
                }
            }

            if(movable38){
                if(wet38){
                    if(ret38>2+ret37){
                        d38=d37;
                        ret38 = 2+ret37;
                    }
                }else{
                    if(ret38>1+ret37){
                        d38=d37;
                        ret38 = 1+ret37;
                    }
                }
            }

            if(movable47){
                if(wet47){
                    if(ret47>2+ret37){
                        d47=d37;
                        ret47 = 2+ret37;
                    }
                }else{
                    if(ret47>1+ret37){
                        d47=d37;
                        ret47 = 1+ret37;
                    }
                }
            }

            if(movable45){
                if(wet45){
                    if(ret45>2+ret37){
                        d45=d37;
                        ret45 = 2+ret37;
                    }
                }else{
                    if(ret45>1+ret37){
                        d45=d37;
                        ret45 = 1+ret37;
                    }
                }
            }

            if(movable36){
                if(wet36){
                    if(ret36>2+ret37){
                        d36=d37;
                        ret36 = 2+ret37;
                    }
                }else{
                    if(ret36>1+ret37){
                        d36=d37;
                        ret36 = 1+ret37;
                    }
                }
            }

        }
        if(ret76!=10000){
            if(movable77){
                if(wet77){
                    if(ret77>2+ret76){
                        d77=d76;
                        ret77 = 2+ret76;
                    }
                }else{
                    if(ret77>1+ret76){
                        d77=d76;
                        ret77 = 1+ret76;
                    }
                }
            }

            if(movable75){
                if(wet75){
                    if(ret75>2+ret76){
                        d75=d76;
                        ret75 = 2+ret76;
                    }
                }else{
                    if(ret75>1+ret76){
                        d75=d76;
                        ret75 = 1+ret76;
                    }
                }
            }

        }
        if(ret77!=10000){
            if(movable78){
                if(wet78){
                    if(ret78>2+ret77){
                        d78=d77;
                        ret78 = 2+ret77;
                    }
                }else{
                    if(ret78>1+ret77){
                        d78=d77;
                        ret78 = 1+ret77;
                    }
                }
            }

            if(movable76){
                if(wet76){
                    if(ret76>2+ret77){
                        d76=d77;
                        ret76 = 2+ret77;
                    }
                }else{
                    if(ret76>1+ret77){
                        d76=d77;
                        ret76 = 1+ret77;
                    }
                }
            }

        }
        if(ret75!=10000){
            if(movable76){
                if(wet76){
                    if(ret76>2+ret75){
                        d76=d75;
                        ret76 = 2+ret75;
                    }
                }else{
                    if(ret76>1+ret75){
                        d76=d75;
                        ret76 = 1+ret75;
                    }
                }
            }

            if(movable74){
                if(wet74){
                    if(ret74>2+ret75){
                        d74=d75;
                        ret74 = 2+ret75;
                    }
                }else{
                    if(ret74>1+ret75){
                        d74=d75;
                        ret74 = 1+ret75;
                    }
                }
            }

        }
        if(ret78!=10000){
            if(movable77){
                if(wet77){
                    if(ret77>2+ret78){
                        d77=d78;
                        ret77 = 2+ret78;
                    }
                }else{
                    if(ret77>1+ret78){
                        d77=d78;
                        ret77 = 1+ret78;
                    }
                }
            }

        }
        if(ret74!=10000){
            if(movable75){
                if(wet75){
                    if(ret75>2+ret74){
                        d75=d74;
                        ret75 = 2+ret74;
                    }
                }else{
                    if(ret75>1+ret74){
                        d75=d74;
                        ret75 = 1+ret74;
                    }
                }
            }

        }
        if(ret44!=10000){
            if(movable53){
                if(wet53){
                    if(ret53>2+ret44){
                        d53=d44;
                        ret53 = 2+ret44;
                    }
                }else{
                    if(ret53>1+ret44){
                        d53=d44;
                        ret53 = 1+ret44;
                    }
                }
            }

            if(movable52){
                if(wet52){
                    if(ret52>2+ret44){
                        d52=d44;
                        ret52 = 2+ret44;
                    }
                }else{
                    if(ret52>1+ret44){
                        d52=d44;
                        ret52 = 1+ret44;
                    }
                }
            }

            if(movable43){
                if(wet43){
                    if(ret43>2+ret44){
                        d43=d44;
                        ret43 = 2+ret44;
                    }
                }else{
                    if(ret43>1+ret44){
                        d43=d44;
                        ret43 = 1+ret44;
                    }
                }
            }

        }
        if(ret53!=10000){
            if(movable62){
                if(wet62){
                    if(ret62>2+ret53){
                        d62=d53;
                        ret62 = 2+ret53;
                    }
                }else{
                    if(ret62>1+ret53){
                        d62=d53;
                        ret62 = 1+ret53;
                    }
                }
            }

            if(movable61){
                if(wet61){
                    if(ret61>2+ret53){
                        d61=d53;
                        ret61 = 2+ret53;
                    }
                }else{
                    if(ret61>1+ret53){
                        d61=d53;
                        ret61 = 1+ret53;
                    }
                }
            }

            if(movable52){
                if(wet52){
                    if(ret52>2+ret53){
                        d52=d53;
                        ret52 = 2+ret53;
                    }
                }else{
                    if(ret52>1+ret53){
                        d52=d53;
                        ret52 = 1+ret53;
                    }
                }
            }

        }
        if(ret62!=10000){
            if(movable70){
                if(wet70){
                    if(ret70>2+ret62){
                        d70=d62;
                        ret70 = 2+ret62;
                    }
                }else{
                    if(ret70>1+ret62){
                        d70=d62;
                        ret70 = 1+ret62;
                    }
                }
            }

            if(movable61){
                if(wet61){
                    if(ret61>2+ret62){
                        d61=d62;
                        ret61 = 2+ret62;
                    }
                }else{
                    if(ret61>1+ret62){
                        d61=d62;
                        ret61 = 1+ret62;
                    }
                }
            }

        }
        if(ret54!=10000){
            if(movable55){
                if(wet55){
                    if(ret55>2+ret54){
                        d55=d54;
                        ret55 = 2+ret54;
                    }
                }else{
                    if(ret55>1+ret54){
                        d55=d54;
                        ret55 = 1+ret54;
                    }
                }
            }

            if(movable64){
                if(wet64){
                    if(ret64>2+ret54){
                        d64=d54;
                        ret64 = 2+ret54;
                    }
                }else{
                    if(ret64>1+ret54){
                        d64=d54;
                        ret64 = 1+ret54;
                    }
                }
            }

        }
        if(ret45!=10000){
            if(movable54){
                if(wet54){
                    if(ret54>2+ret45){
                        d54=d45;
                        ret54 = 2+ret45;
                    }
                }else{
                    if(ret54>1+ret45){
                        d54=d45;
                        ret54 = 1+ret45;
                    }
                }
            }

            if(movable46){
                if(wet46){
                    if(ret46>2+ret45){
                        d46=d45;
                        ret46 = 2+ret45;
                    }
                }else{
                    if(ret46>1+ret45){
                        d46=d45;
                        ret46 = 1+ret45;
                    }
                }
            }

            if(movable55){
                if(wet55){
                    if(ret55>2+ret45){
                        d55=d45;
                        ret55 = 2+ret45;
                    }
                }else{
                    if(ret55>1+ret45){
                        d55=d45;
                        ret55 = 1+ret45;
                    }
                }
            }

        }
        if(ret36!=10000){
            if(movable45){
                if(wet45){
                    if(ret45>2+ret36){
                        d45=d36;
                        ret45 = 2+ret36;
                    }
                }else{
                    if(ret45>1+ret36){
                        d45=d36;
                        ret45 = 1+ret36;
                    }
                }
            }

            if(movable37){
                if(wet37){
                    if(ret37>2+ret36){
                        d37=d36;
                        ret37 = 2+ret36;
                    }
                }else{
                    if(ret37>1+ret36){
                        d37=d36;
                        ret37 = 1+ret36;
                    }
                }
            }

            if(movable46){
                if(wet46){
                    if(ret46>2+ret36){
                        d46=d36;
                        ret46 = 2+ret36;
                    }
                }else{
                    if(ret46>1+ret36){
                        d46=d36;
                        ret46 = 1+ret36;
                    }
                }
            }

        }

        double initialDist = Math.sqrt(m40.distanceSquaredTo(target));
        Direction ans= Direction.CENTER;
        double cmax= 0;
        double dist39 = (initialDist-Math.sqrt(m39.distanceSquaredTo(target)))/(double)ret39;
        if(movable39&&dist39 > cmax){
            cmax= dist39;
            ans = d39;
        }

        double dist41 = (initialDist-Math.sqrt(m41.distanceSquaredTo(target)))/(double)ret41;
        if(movable41&&dist41 > cmax){
            cmax= dist41;
            ans = d41;
        }

        double dist48 = (initialDist-Math.sqrt(m48.distanceSquaredTo(target)))/(double)ret48;
        if(movable48&&dist48 > cmax){
            cmax= dist48;
            ans = d48;
        }

        double dist49 = (initialDist-Math.sqrt(m49.distanceSquaredTo(target)))/(double)ret49;
        if(movable49&&dist49 > cmax){
            cmax= dist49;
            ans = d49;
        }

        double dist50 = (initialDist-Math.sqrt(m50.distanceSquaredTo(target)))/(double)ret50;
        if(movable50&&dist50 > cmax){
            cmax= dist50;
            ans = d50;
        }

        double dist38 = (initialDist-Math.sqrt(m38.distanceSquaredTo(target)))/(double)ret38;
        if(movable38&&dist38 > cmax){
            cmax= dist38;
            ans = d38;
        }

        double dist42 = (initialDist-Math.sqrt(m42.distanceSquaredTo(target)))/(double)ret42;
        if(movable42&&dist42 > cmax){
            cmax= dist42;
            ans = d42;
        }

        double dist47 = (initialDist-Math.sqrt(m47.distanceSquaredTo(target)))/(double)ret47;
        if(movable47&&dist47 > cmax){
            cmax= dist47;
            ans = d47;
        }

        double dist51 = (initialDist-Math.sqrt(m51.distanceSquaredTo(target)))/(double)ret51;
        if(movable51&&dist51 > cmax){
            cmax= dist51;
            ans = d51;
        }

        double dist56 = (initialDist-Math.sqrt(m56.distanceSquaredTo(target)))/(double)ret56;
        if(movable56&&dist56 > cmax){
            cmax= dist56;
            ans = d56;
        }

        double dist57 = (initialDist-Math.sqrt(m57.distanceSquaredTo(target)))/(double)ret57;
        if(movable57&&dist57 > cmax){
            cmax= dist57;
            ans = d57;
        }

        double dist58 = (initialDist-Math.sqrt(m58.distanceSquaredTo(target)))/(double)ret58;
        if(movable58&&dist58 > cmax){
            cmax= dist58;
            ans = d58;
        }

        double dist59 = (initialDist-Math.sqrt(m59.distanceSquaredTo(target)))/(double)ret59;
        if(movable59&&dist59 > cmax){
            cmax= dist59;
            ans = d59;
        }

        double dist60 = (initialDist-Math.sqrt(m60.distanceSquaredTo(target)))/(double)ret60;
        if(movable60&&dist60 > cmax){
            cmax= dist60;
            ans = d60;
        }

        double dist37 = (initialDist-Math.sqrt(m37.distanceSquaredTo(target)))/(double)ret37;
        if(movable37&&dist37 > cmax){
            cmax= dist37;
            ans = d37;
        }

        double dist43 = (initialDist-Math.sqrt(m43.distanceSquaredTo(target)))/(double)ret43;
        if(movable43&&dist43 > cmax){
            cmax= dist43;
            ans = d43;
        }

        double dist46 = (initialDist-Math.sqrt(m46.distanceSquaredTo(target)))/(double)ret46;
        if(movable46&&dist46 > cmax){
            cmax= dist46;
            ans = d46;
        }

        double dist52 = (initialDist-Math.sqrt(m52.distanceSquaredTo(target)))/(double)ret52;
        if(movable52&&dist52 > cmax){
            cmax= dist52;
            ans = d52;
        }

        double dist55 = (initialDist-Math.sqrt(m55.distanceSquaredTo(target)))/(double)ret55;
        if(movable55&&dist55 > cmax){
            cmax= dist55;
            ans = d55;
        }

        double dist61 = (initialDist-Math.sqrt(m61.distanceSquaredTo(target)))/(double)ret61;
        if(movable61&&dist61 > cmax){
            cmax= dist61;
            ans = d61;
        }

        double dist64 = (initialDist-Math.sqrt(m64.distanceSquaredTo(target)))/(double)ret64;
        if(movable64&&dist64 > cmax){
            cmax= dist64;
            ans = d64;
        }

        double dist65 = (initialDist-Math.sqrt(m65.distanceSquaredTo(target)))/(double)ret65;
        if(movable65&&dist65 > cmax){
            cmax= dist65;
            ans = d65;
        }

        double dist66 = (initialDist-Math.sqrt(m66.distanceSquaredTo(target)))/(double)ret66;
        if(movable66&&dist66 > cmax){
            cmax= dist66;
            ans = d66;
        }

        double dist67 = (initialDist-Math.sqrt(m67.distanceSquaredTo(target)))/(double)ret67;
        if(movable67&&dist67 > cmax){
            cmax= dist67;
            ans = d67;
        }

        double dist68 = (initialDist-Math.sqrt(m68.distanceSquaredTo(target)))/(double)ret68;
        if(movable68&&dist68 > cmax){
            cmax= dist68;
            ans = d68;
        }

        double dist69 = (initialDist-Math.sqrt(m69.distanceSquaredTo(target)))/(double)ret69;
        if(movable69&&dist69 > cmax){
            cmax= dist69;
            ans = d69;
        }

        double dist70 = (initialDist-Math.sqrt(m70.distanceSquaredTo(target)))/(double)ret70;
        if(movable70&&dist70 > cmax){
            cmax= dist70;
            ans = d70;
        }

        double dist36 = (initialDist-Math.sqrt(m36.distanceSquaredTo(target)))/(double)ret36;
        if(movable36&&dist36 > cmax){
            cmax= dist36;
            ans = d36;
        }

        double dist44 = (initialDist-Math.sqrt(m44.distanceSquaredTo(target)))/(double)ret44;
        if(movable44&&dist44 > cmax){
            cmax= dist44;
            ans = d44;
        }

        double dist45 = (initialDist-Math.sqrt(m45.distanceSquaredTo(target)))/(double)ret45;
        if(movable45&&dist45 > cmax){
            cmax= dist45;
            ans = d45;
        }

        double dist53 = (initialDist-Math.sqrt(m53.distanceSquaredTo(target)))/(double)ret53;
        if(movable53&&dist53 > cmax){
            cmax= dist53;
            ans = d53;
        }

        double dist54 = (initialDist-Math.sqrt(m54.distanceSquaredTo(target)))/(double)ret54;
        if(movable54&&dist54 > cmax){
            cmax= dist54;
            ans = d54;
        }

        double dist62 = (initialDist-Math.sqrt(m62.distanceSquaredTo(target)))/(double)ret62;
        if(movable62&&dist62 > cmax){
            cmax= dist62;
            ans = d62;
        }

        double dist74 = (initialDist-Math.sqrt(m74.distanceSquaredTo(target)))/(double)ret74;
        if(movable74&&dist74 > cmax){
            cmax= dist74;
            ans = d74;
        }

        double dist75 = (initialDist-Math.sqrt(m75.distanceSquaredTo(target)))/(double)ret75;
        if(movable75&&dist75 > cmax){
            cmax= dist75;
            ans = d75;
        }

        double dist76 = (initialDist-Math.sqrt(m76.distanceSquaredTo(target)))/(double)ret76;
        if(movable76&&dist76 > cmax){
            cmax= dist76;
            ans = d76;
        }

        double dist77 = (initialDist-Math.sqrt(m77.distanceSquaredTo(target)))/(double)ret77;
        if(movable77&&dist77 > cmax){
            cmax= dist77;
            ans = d77;
        }

        double dist78 = (initialDist-Math.sqrt(m78.distanceSquaredTo(target)))/(double)ret78;
        if(movable78&&dist78 > cmax){
            cmax= dist78;
            ans = d78;
        }

        if(ans!=null){
            return ans;
        }else{
            return Direction.CENTER;
        }

    }

}
