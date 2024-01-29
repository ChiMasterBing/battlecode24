package waxoldbuilder.BFS;
import battlecode.common.*;

public class BFSSouthWest {
    public static Direction findBestDirection(RobotController rc, MapLocation target) throws GameActionException {
        MapLocation m40 = rc.getLocation();
        int ret40= 0;
        MapLocation m49= m40.add(Direction.SOUTH);
        Direction d49 = null;
        int ret49= 10000;
        boolean wet49 = false;
        MapLocation m50= m40.add(Direction.SOUTHEAST);
        Direction d50 = null;
        int ret50= 10000;
        boolean wet50 = false;
        MapLocation m48= m40.add(Direction.SOUTHWEST);
        Direction d48 = null;
        int ret48= 10000;
        boolean wet48 = false;
        MapLocation m30= m40.add(Direction.NORTHWEST);
        Direction d30 = null;
        int ret30= 10000;
        boolean wet30 = false;
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
        MapLocation m60= m50.add(Direction.SOUTHEAST);
        Direction d60 = null;
        int ret60= 10000;
        boolean wet60 = false;
        MapLocation m56= m48.add(Direction.SOUTHWEST);
        Direction d56 = null;
        int ret56= 10000;
        boolean wet56 = false;
        MapLocation m38= m48.add(Direction.NORTHWEST);
        Direction d38 = null;
        int ret38= 10000;
        boolean wet38 = false;
        MapLocation m47= m48.add(Direction.WEST);
        Direction d47 = null;
        int ret47= 10000;
        boolean wet47 = false;
        MapLocation m20= m30.add(Direction.NORTHWEST);
        Direction d20 = null;
        int ret20= 10000;
        boolean wet20 = false;
        MapLocation m29= m30.add(Direction.WEST);
        Direction d29 = null;
        int ret29= 10000;
        boolean wet29 = false;
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
        MapLocation m70= m60.add(Direction.SOUTHEAST);
        Direction d70 = null;
        int ret70= 10000;
        boolean wet70 = false;
        MapLocation m64= m56.add(Direction.SOUTHWEST);
        Direction d64 = null;
        int ret64= 10000;
        boolean wet64 = false;
        MapLocation m46= m56.add(Direction.NORTHWEST);
        Direction d46 = null;
        int ret46= 10000;
        boolean wet46 = false;
        MapLocation m55= m56.add(Direction.WEST);
        Direction d55 = null;
        int ret55= 10000;
        boolean wet55 = false;
        MapLocation m28= m38.add(Direction.NORTHWEST);
        Direction d28 = null;
        int ret28= 10000;
        boolean wet28 = false;
        MapLocation m37= m38.add(Direction.WEST);
        Direction d37 = null;
        int ret37= 10000;
        boolean wet37 = false;
        MapLocation m10= m20.add(Direction.NORTHWEST);
        Direction d10 = null;
        int ret10= 10000;
        boolean wet10 = false;
        MapLocation m19= m20.add(Direction.WEST);
        Direction d19 = null;
        int ret19= 10000;
        boolean wet19 = false;
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
        MapLocation m54= m64.add(Direction.NORTHWEST);
        Direction d54 = null;
        int ret54= 10000;
        boolean wet54 = false;
        MapLocation m36= m46.add(Direction.NORTHWEST);
        Direction d36 = null;
        int ret36= 10000;
        boolean wet36 = false;
        MapLocation m45= m46.add(Direction.WEST);
        Direction d45 = null;
        int ret45= 10000;
        boolean wet45 = false;
        MapLocation m18= m28.add(Direction.NORTHWEST);
        Direction d18 = null;
        int ret18= 10000;
        boolean wet18 = false;
        MapLocation m27= m28.add(Direction.WEST);
        Direction d27 = null;
        int ret27= 10000;
        boolean wet27 = false;
        MapInfo mpinfo;
        boolean movable49 = false;
        if(rc.canSenseLocation(m49)){
            mpinfo = rc.senseMapInfo(m49);
            wet49 = mpinfo.isWater();
            if(!rc.isLocationOccupied(m49)&&(mpinfo.isPassable() || wet49)){
                movable49 = true;
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

        boolean movable30 = false;
        if(rc.canSenseLocation(m30)){
            mpinfo = rc.senseMapInfo(m30);
            wet30 = mpinfo.isWater();
            if(!rc.isLocationOccupied(m30)&&(mpinfo.isPassable() || wet30)){
                movable30 = true;
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

        boolean movable38 = false;
        if(rc.canSenseLocation(m38)){
            mpinfo = rc.senseMapInfo(m38);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet38 = mpinfo.isWater();
                movable38 = true;
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

        boolean movable20 = false;
        if(rc.canSenseLocation(m20)){
            mpinfo = rc.senseMapInfo(m20);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet20 = mpinfo.isWater();
                movable20 = true;
            }
        }

        boolean movable29 = false;
        if(rc.canSenseLocation(m29)){
            mpinfo = rc.senseMapInfo(m29);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet29 = mpinfo.isWater();
                movable29 = true;
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

        boolean movable46 = false;
        if(rc.canSenseLocation(m46)){
            mpinfo = rc.senseMapInfo(m46);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet46 = mpinfo.isWater();
                movable46 = true;
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

        boolean movable28 = false;
        if(rc.canSenseLocation(m28)){
            mpinfo = rc.senseMapInfo(m28);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet28 = mpinfo.isWater();
                movable28 = true;
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

        boolean movable10 = false;
        if(rc.canSenseLocation(m10)){
            mpinfo = rc.senseMapInfo(m10);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet10 = mpinfo.isWater();
                movable10 = true;
            }
        }

        boolean movable19 = false;
        if(rc.canSenseLocation(m19)){
            mpinfo = rc.senseMapInfo(m19);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet19 = mpinfo.isWater();
                movable19 = true;
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

        boolean movable54 = false;
        if(rc.canSenseLocation(m54)){
            mpinfo = rc.senseMapInfo(m54);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet54 = mpinfo.isWater();
                movable54 = true;
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

        boolean movable45 = false;
        if(rc.canSenseLocation(m45)){
            mpinfo = rc.senseMapInfo(m45);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet45 = mpinfo.isWater();
                movable45 = true;
            }
        }

        boolean movable18 = false;
        if(rc.canSenseLocation(m18)){
            mpinfo = rc.senseMapInfo(m18);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet18 = mpinfo.isWater();
                movable18 = true;
            }
        }

        boolean movable27 = false;
        if(rc.canSenseLocation(m27)){
            mpinfo = rc.senseMapInfo(m27);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet27 = mpinfo.isWater();
                movable27 = true;
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

            if(movable30){
                if(wet30){
                    if(ret30>3+ret40){
                        d30=Direction.NORTHWEST;
                        ret30 = 3+ret40;
                    }
                }else{
                    if(ret30>2+ret40){
                        d30=Direction.NORTHWEST;
                        ret30 = 2+ret40;
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

            if(movable39){
                if(wet39){
                    if(ret39>2+ret49){
                        d39=d49;
                        ret39 = 2+ret49;
                    }
                }else{
                    if(ret39>1+ret49){
                        d39=d49;
                        ret39 = 1+ret49;
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

            if(movable38){
                if(wet38){
                    if(ret38>2+ret48){
                        d38=d48;
                        ret38 = 2+ret48;
                    }
                }else{
                    if(ret38>1+ret48){
                        d38=d48;
                        ret38 = 1+ret48;
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
        if(ret30!=10000){
            if(movable39){
                if(wet39){
                    if(ret39>2+ret30){
                        d39=d30;
                        ret39 = 2+ret30;
                    }
                }else{
                    if(ret39>1+ret30){
                        d39=d30;
                        ret39 = 1+ret30;
                    }
                }
            }

            if(movable38){
                if(wet38){
                    if(ret38>2+ret30){
                        d38=d30;
                        ret38 = 2+ret30;
                    }
                }else{
                    if(ret38>1+ret30){
                        d38=d30;
                        ret38 = 1+ret30;
                    }
                }
            }

            if(movable20){
                if(wet20){
                    if(ret20>2+ret30){
                        d20=d30;
                        ret20 = 2+ret30;
                    }
                }else{
                    if(ret20>1+ret30){
                        d20=d30;
                        ret20 = 1+ret30;
                    }
                }
            }

            if(movable29){
                if(wet29){
                    if(ret29>2+ret30){
                        d29=d30;
                        ret29 = 2+ret30;
                    }
                }else{
                    if(ret29>1+ret30){
                        d29=d30;
                        ret29 = 1+ret30;
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

            if(movable29){
                if(wet29){
                    if(ret29>2+ret39){
                        d29=d39;
                        ret29 = 2+ret39;
                    }
                }else{
                    if(ret29>1+ret39){
                        d29=d39;
                        ret29 = 1+ret39;
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

            if(movable48){
                if(wet48){
                    if(ret48>2+ret58){
                        d48=d58;
                        ret48 = 2+ret58;
                    }
                }else{
                    if(ret48>1+ret58){
                        d48=d58;
                        ret48 = 1+ret58;
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

            if(movable49){
                if(wet49){
                    if(ret49>2+ret59){
                        d49=d59;
                        ret49 = 2+ret59;
                    }
                }else{
                    if(ret49>1+ret59){
                        d49=d59;
                        ret49 = 1+ret59;
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

            if(movable47){
                if(wet47){
                    if(ret47>2+ret57){
                        d47=d57;
                        ret47 = 2+ret57;
                    }
                }else{
                    if(ret47>1+ret57){
                        d47=d57;
                        ret47 = 1+ret57;
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

            if(movable50){
                if(wet50){
                    if(ret50>2+ret60){
                        d50=d60;
                        ret50 = 2+ret60;
                    }
                }else{
                    if(ret50>1+ret60){
                        d50=d60;
                        ret50 = 1+ret60;
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

            if(movable46){
                if(wet46){
                    if(ret46>2+ret56){
                        d46=d56;
                        ret46 = 2+ret56;
                    }
                }else{
                    if(ret46>1+ret56){
                        d46=d56;
                        ret46 = 1+ret56;
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

            if(movable28){
                if(wet28){
                    if(ret28>2+ret38){
                        d28=d38;
                        ret28 = 2+ret38;
                    }
                }else{
                    if(ret28>1+ret38){
                        d28=d38;
                        ret28 = 1+ret38;
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

            if(movable37){
                if(wet37){
                    if(ret37>2+ret47){
                        d37=d47;
                        ret37 = 2+ret47;
                    }
                }else{
                    if(ret37>1+ret47){
                        d37=d47;
                        ret37 = 1+ret47;
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
        if(ret20!=10000){
            if(movable29){
                if(wet29){
                    if(ret29>2+ret20){
                        d29=d20;
                        ret29 = 2+ret20;
                    }
                }else{
                    if(ret29>1+ret20){
                        d29=d20;
                        ret29 = 1+ret20;
                    }
                }
            }

            if(movable30){
                if(wet30){
                    if(ret30>2+ret20){
                        d30=d20;
                        ret30 = 2+ret20;
                    }
                }else{
                    if(ret30>1+ret20){
                        d30=d20;
                        ret30 = 1+ret20;
                    }
                }
            }

            if(movable28){
                if(wet28){
                    if(ret28>2+ret20){
                        d28=d20;
                        ret28 = 2+ret20;
                    }
                }else{
                    if(ret28>1+ret20){
                        d28=d20;
                        ret28 = 1+ret20;
                    }
                }
            }

            if(movable10){
                if(wet10){
                    if(ret10>2+ret20){
                        d10=d20;
                        ret10 = 2+ret20;
                    }
                }else{
                    if(ret10>1+ret20){
                        d10=d20;
                        ret10 = 1+ret20;
                    }
                }
            }

            if(movable19){
                if(wet19){
                    if(ret19>2+ret20){
                        d19=d20;
                        ret19 = 2+ret20;
                    }
                }else{
                    if(ret19>1+ret20){
                        d19=d20;
                        ret19 = 1+ret20;
                    }
                }
            }

        }
        if(ret29!=10000){
            if(movable38){
                if(wet38){
                    if(ret38>2+ret29){
                        d38=d29;
                        ret38 = 2+ret29;
                    }
                }else{
                    if(ret38>1+ret29){
                        d38=d29;
                        ret38 = 1+ret29;
                    }
                }
            }

            if(movable39){
                if(wet39){
                    if(ret39>2+ret29){
                        d39=d29;
                        ret39 = 2+ret29;
                    }
                }else{
                    if(ret39>1+ret29){
                        d39=d29;
                        ret39 = 1+ret29;
                    }
                }
            }

            if(movable37){
                if(wet37){
                    if(ret37>2+ret29){
                        d37=d29;
                        ret37 = 2+ret29;
                    }
                }else{
                    if(ret37>1+ret29){
                        d37=d29;
                        ret37 = 1+ret29;
                    }
                }
            }

            if(movable19){
                if(wet19){
                    if(ret19>2+ret29){
                        d19=d29;
                        ret19 = 2+ret29;
                    }
                }else{
                    if(ret19>1+ret29){
                        d19=d29;
                        ret19 = 1+ret29;
                    }
                }
            }

            if(movable28){
                if(wet28){
                    if(ret28>2+ret29){
                        d28=d29;
                        ret28 = 2+ret29;
                    }
                }else{
                    if(ret28>1+ret29){
                        d28=d29;
                        ret28 = 1+ret29;
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

            if(movable57){
                if(wet57){
                    if(ret57>2+ret67){
                        d57=d67;
                        ret57 = 2+ret67;
                    }
                }else{
                    if(ret57>1+ret67){
                        d57=d67;
                        ret57 = 1+ret67;
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

            if(movable58){
                if(wet58){
                    if(ret58>2+ret68){
                        d58=d68;
                        ret58 = 2+ret68;
                    }
                }else{
                    if(ret58>1+ret68){
                        d58=d68;
                        ret58 = 1+ret68;
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

            if(movable56){
                if(wet56){
                    if(ret56>2+ret66){
                        d56=d66;
                        ret56 = 2+ret66;
                    }
                }else{
                    if(ret56>1+ret66){
                        d56=d66;
                        ret56 = 1+ret66;
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

            if(movable59){
                if(wet59){
                    if(ret59>2+ret69){
                        d59=d69;
                        ret59 = 2+ret69;
                    }
                }else{
                    if(ret59>1+ret69){
                        d59=d69;
                        ret59 = 1+ret69;
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

            if(movable55){
                if(wet55){
                    if(ret55>2+ret65){
                        d55=d65;
                        ret55 = 2+ret65;
                    }
                }else{
                    if(ret55>1+ret65){
                        d55=d65;
                        ret55 = 1+ret65;
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

            if(movable60){
                if(wet60){
                    if(ret60>2+ret70){
                        d60=d70;
                        ret60 = 2+ret70;
                    }
                }else{
                    if(ret60>1+ret70){
                        d60=d70;
                        ret60 = 1+ret70;
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

            if(movable54){
                if(wet54){
                    if(ret54>2+ret64){
                        d54=d64;
                        ret54 = 2+ret64;
                    }
                }else{
                    if(ret54>1+ret64){
                        d54=d64;
                        ret54 = 1+ret64;
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

            if(movable36){
                if(wet36){
                    if(ret36>2+ret46){
                        d36=d46;
                        ret36 = 2+ret46;
                    }
                }else{
                    if(ret36>1+ret46){
                        d36=d46;
                        ret36 = 1+ret46;
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

            if(movable45){
                if(wet45){
                    if(ret45>2+ret55){
                        d45=d55;
                        ret45 = 2+ret55;
                    }
                }else{
                    if(ret45>1+ret55){
                        d45=d55;
                        ret45 = 1+ret55;
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
        if(ret28!=10000){
            if(movable37){
                if(wet37){
                    if(ret37>2+ret28){
                        d37=d28;
                        ret37 = 2+ret28;
                    }
                }else{
                    if(ret37>1+ret28){
                        d37=d28;
                        ret37 = 1+ret28;
                    }
                }
            }

            if(movable38){
                if(wet38){
                    if(ret38>2+ret28){
                        d38=d28;
                        ret38 = 2+ret28;
                    }
                }else{
                    if(ret38>1+ret28){
                        d38=d28;
                        ret38 = 1+ret28;
                    }
                }
            }

            if(movable36){
                if(wet36){
                    if(ret36>2+ret28){
                        d36=d28;
                        ret36 = 2+ret28;
                    }
                }else{
                    if(ret36>1+ret28){
                        d36=d28;
                        ret36 = 1+ret28;
                    }
                }
            }

            if(movable18){
                if(wet18){
                    if(ret18>2+ret28){
                        d18=d28;
                        ret18 = 2+ret28;
                    }
                }else{
                    if(ret18>1+ret28){
                        d18=d28;
                        ret18 = 1+ret28;
                    }
                }
            }

            if(movable27){
                if(wet27){
                    if(ret27>2+ret28){
                        d27=d28;
                        ret27 = 2+ret28;
                    }
                }else{
                    if(ret27>1+ret28){
                        d27=d28;
                        ret27 = 1+ret28;
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

            if(movable27){
                if(wet27){
                    if(ret27>2+ret37){
                        d27=d37;
                        ret27 = 2+ret37;
                    }
                }else{
                    if(ret27>1+ret37){
                        d27=d37;
                        ret27 = 1+ret37;
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
        if(ret10!=10000){
            if(movable19){
                if(wet19){
                    if(ret19>2+ret10){
                        d19=d10;
                        ret19 = 2+ret10;
                    }
                }else{
                    if(ret19>1+ret10){
                        d19=d10;
                        ret19 = 1+ret10;
                    }
                }
            }

            if(movable20){
                if(wet20){
                    if(ret20>2+ret10){
                        d20=d10;
                        ret20 = 2+ret10;
                    }
                }else{
                    if(ret20>1+ret10){
                        d20=d10;
                        ret20 = 1+ret10;
                    }
                }
            }

            if(movable18){
                if(wet18){
                    if(ret18>2+ret10){
                        d18=d10;
                        ret18 = 2+ret10;
                    }
                }else{
                    if(ret18>1+ret10){
                        d18=d10;
                        ret18 = 1+ret10;
                    }
                }
            }

        }
        if(ret19!=10000){
            if(movable28){
                if(wet28){
                    if(ret28>2+ret19){
                        d28=d19;
                        ret28 = 2+ret19;
                    }
                }else{
                    if(ret28>1+ret19){
                        d28=d19;
                        ret28 = 1+ret19;
                    }
                }
            }

            if(movable29){
                if(wet29){
                    if(ret29>2+ret19){
                        d29=d19;
                        ret29 = 2+ret19;
                    }
                }else{
                    if(ret29>1+ret19){
                        d29=d19;
                        ret29 = 1+ret19;
                    }
                }
            }

            if(movable27){
                if(wet27){
                    if(ret27>2+ret19){
                        d27=d19;
                        ret27 = 2+ret19;
                    }
                }else{
                    if(ret27>1+ret19){
                        d27=d19;
                        ret27 = 1+ret19;
                    }
                }
            }

            if(movable18){
                if(wet18){
                    if(ret18>2+ret19){
                        d18=d19;
                        ret18 = 2+ret19;
                    }
                }else{
                    if(ret18>1+ret19){
                        d18=d19;
                        ret18 = 1+ret19;
                    }
                }
            }

        }
        if(ret76!=10000){
            if(movable66){
                if(wet66){
                    if(ret66>2+ret76){
                        d66=d76;
                        ret66 = 2+ret76;
                    }
                }else{
                    if(ret66>1+ret76){
                        d66=d76;
                        ret66 = 1+ret76;
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
            if(movable67){
                if(wet67){
                    if(ret67>2+ret77){
                        d67=d77;
                        ret67 = 2+ret77;
                    }
                }else{
                    if(ret67>1+ret77){
                        d67=d77;
                        ret67 = 1+ret77;
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
            if(movable65){
                if(wet65){
                    if(ret65>2+ret75){
                        d65=d75;
                        ret65 = 2+ret75;
                    }
                }else{
                    if(ret65>1+ret75){
                        d65=d75;
                        ret65 = 1+ret75;
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
            if(movable68){
                if(wet68){
                    if(ret68>2+ret78){
                        d68=d78;
                        ret68 = 2+ret78;
                    }
                }else{
                    if(ret68>1+ret78){
                        d68=d78;
                        ret68 = 1+ret78;
                    }
                }
            }

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
            if(movable64){
                if(wet64){
                    if(ret64>2+ret74){
                        d64=d74;
                        ret64 = 2+ret74;
                    }
                }else{
                    if(ret64>1+ret74){
                        d64=d74;
                        ret64 = 1+ret74;
                    }
                }
            }

        }
        if(ret54!=10000){
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
        if(ret18!=10000){
            if(movable27){
                if(wet27){
                    if(ret27>2+ret18){
                        d27=d18;
                        ret27 = 2+ret18;
                    }
                }else{
                    if(ret27>1+ret18){
                        d27=d18;
                        ret27 = 1+ret18;
                    }
                }
            }

            if(movable28){
                if(wet28){
                    if(ret28>2+ret18){
                        d28=d18;
                        ret28 = 2+ret18;
                    }
                }else{
                    if(ret28>1+ret18){
                        d28=d18;
                        ret28 = 1+ret18;
                    }
                }
            }

        }
        if(ret27!=10000){
            if(movable36){
                if(wet36){
                    if(ret36>2+ret27){
                        d36=d27;
                        ret36 = 2+ret27;
                    }
                }else{
                    if(ret36>1+ret27){
                        d36=d27;
                        ret36 = 1+ret27;
                    }
                }
            }

            if(movable37){
                if(wet37){
                    if(ret37>2+ret27){
                        d37=d27;
                        ret37 = 2+ret27;
                    }
                }else{
                    if(ret37>1+ret27){
                        d37=d27;
                        ret37 = 1+ret27;
                    }
                }
            }

        }

        double initialDist = Math.sqrt(m40.distanceSquaredTo(target));
        Direction ans= Direction.CENTER;
        double cmax= 0;
        double dist30 = (initialDist-Math.sqrt(m30.distanceSquaredTo(target)))/(double)ret30;
        if(movable30&&dist30 > cmax){
            cmax= dist30;
            ans = d30;
        }

        double dist39 = (initialDist-Math.sqrt(m39.distanceSquaredTo(target)))/(double)ret39;
        if(movable39&&dist39 > cmax){
            cmax= dist39;
            ans = d39;
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

        double dist20 = (initialDist-Math.sqrt(m20.distanceSquaredTo(target)))/(double)ret20;
        if(movable20&&dist20 > cmax){
            cmax= dist20;
            ans = d20;
        }

        double dist29 = (initialDist-Math.sqrt(m29.distanceSquaredTo(target)))/(double)ret29;
        if(movable29&&dist29 > cmax){
            cmax= dist29;
            ans = d29;
        }

        double dist38 = (initialDist-Math.sqrt(m38.distanceSquaredTo(target)))/(double)ret38;
        if(movable38&&dist38 > cmax){
            cmax= dist38;
            ans = d38;
        }

        double dist47 = (initialDist-Math.sqrt(m47.distanceSquaredTo(target)))/(double)ret47;
        if(movable47&&dist47 > cmax){
            cmax= dist47;
            ans = d47;
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

        double dist10 = (initialDist-Math.sqrt(m10.distanceSquaredTo(target)))/(double)ret10;
        if(movable10&&dist10 > cmax){
            cmax= dist10;
            ans = d10;
        }

        double dist19 = (initialDist-Math.sqrt(m19.distanceSquaredTo(target)))/(double)ret19;
        if(movable19&&dist19 > cmax){
            cmax= dist19;
            ans = d19;
        }

        double dist28 = (initialDist-Math.sqrt(m28.distanceSquaredTo(target)))/(double)ret28;
        if(movable28&&dist28 > cmax){
            cmax= dist28;
            ans = d28;
        }

        double dist37 = (initialDist-Math.sqrt(m37.distanceSquaredTo(target)))/(double)ret37;
        if(movable37&&dist37 > cmax){
            cmax= dist37;
            ans = d37;
        }

        double dist46 = (initialDist-Math.sqrt(m46.distanceSquaredTo(target)))/(double)ret46;
        if(movable46&&dist46 > cmax){
            cmax= dist46;
            ans = d46;
        }

        double dist55 = (initialDist-Math.sqrt(m55.distanceSquaredTo(target)))/(double)ret55;
        if(movable55&&dist55 > cmax){
            cmax= dist55;
            ans = d55;
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

        double dist18 = (initialDist-Math.sqrt(m18.distanceSquaredTo(target)))/(double)ret18;
        if(movable18&&dist18 > cmax){
            cmax= dist18;
            ans = d18;
        }

        double dist27 = (initialDist-Math.sqrt(m27.distanceSquaredTo(target)))/(double)ret27;
        if(movable27&&dist27 > cmax){
            cmax= dist27;
            ans = d27;
        }

        double dist36 = (initialDist-Math.sqrt(m36.distanceSquaredTo(target)))/(double)ret36;
        if(movable36&&dist36 > cmax){
            cmax= dist36;
            ans = d36;
        }

        double dist45 = (initialDist-Math.sqrt(m45.distanceSquaredTo(target)))/(double)ret45;
        if(movable45&&dist45 > cmax){
            cmax= dist45;
            ans = d45;
        }

        double dist54 = (initialDist-Math.sqrt(m54.distanceSquaredTo(target)))/(double)ret54;
        if(movable54&&dist54 > cmax){
            cmax= dist54;
            ans = d54;
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
