package moon.BFS;
import battlecode.common.*;

public class BFSWest {
    public static Direction findBestDirection(RobotController rc, MapLocation target) throws GameActionException {
        MapLocation m40 = rc.getLocation();
        int ret40= 0;
        MapLocation m31= m40.add(Direction.NORTH);
        Direction d31 = null;
        int ret31= 10000;
        boolean wet31 = false;
        MapLocation m49= m40.add(Direction.SOUTH);
        Direction d49 = null;
        int ret49= 10000;
        boolean wet49 = false;
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
        MapLocation m22= m31.add(Direction.NORTH);
        Direction d22 = null;
        int ret22= 10000;
        boolean wet22 = false;
        MapLocation m21= m31.add(Direction.NORTHWEST);
        Direction d21 = null;
        int ret21= 10000;
        boolean wet21 = false;
        MapLocation m58= m49.add(Direction.SOUTH);
        Direction d58 = null;
        int ret58= 10000;
        boolean wet58 = false;
        MapLocation m57= m49.add(Direction.SOUTHWEST);
        Direction d57 = null;
        int ret57= 10000;
        boolean wet57 = false;
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
        MapLocation m13= m22.add(Direction.NORTH);
        Direction d13 = null;
        int ret13= 10000;
        boolean wet13 = false;
        MapLocation m12= m22.add(Direction.NORTHWEST);
        Direction d12 = null;
        int ret12= 10000;
        boolean wet12 = false;
        MapLocation m11= m21.add(Direction.NORTHWEST);
        Direction d11 = null;
        int ret11= 10000;
        boolean wet11 = false;
        MapLocation m67= m58.add(Direction.SOUTH);
        Direction d67 = null;
        int ret67= 10000;
        boolean wet67 = false;
        MapLocation m66= m58.add(Direction.SOUTHWEST);
        Direction d66 = null;
        int ret66= 10000;
        boolean wet66 = false;
        MapLocation m65= m57.add(Direction.SOUTHWEST);
        Direction d65 = null;
        int ret65= 10000;
        boolean wet65 = false;
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
        MapLocation m4= m13.add(Direction.NORTH);
        Direction d4 = null;
        int ret4= 10000;
        boolean wet4 = false;
        MapLocation m3= m13.add(Direction.NORTHWEST);
        Direction d3 = null;
        int ret3= 10000;
        boolean wet3 = false;
        MapLocation m2= m12.add(Direction.NORTHWEST);
        Direction d2 = null;
        int ret2= 10000;
        boolean wet2 = false;
        MapLocation m76= m67.add(Direction.SOUTH);
        Direction d76 = null;
        int ret76= 10000;
        boolean wet76 = false;
        MapLocation m75= m67.add(Direction.SOUTHWEST);
        Direction d75 = null;
        int ret75= 10000;
        boolean wet75 = false;
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
        boolean movable31 = false;
        if(rc.canSenseLocation(m31)){
            mpinfo = rc.senseMapInfo(m31);
            wet31 = mpinfo.isWater();
            if(!rc.isLocationOccupied(m31)&&(mpinfo.isPassable() || wet31)){
                movable31 = true;
            }
        }

        boolean movable49 = false;
        if(rc.canSenseLocation(m49)){
            mpinfo = rc.senseMapInfo(m49);
            wet49 = mpinfo.isWater();
            if(!rc.isLocationOccupied(m49)&&(mpinfo.isPassable() || wet49)){
                movable49 = true;
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

        boolean movable22 = false;
        if(rc.canSenseLocation(m22)){
            mpinfo = rc.senseMapInfo(m22);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet22 = mpinfo.isWater();
                movable22 = true;
            }
        }

        boolean movable21 = false;
        if(rc.canSenseLocation(m21)){
            mpinfo = rc.senseMapInfo(m21);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet21 = mpinfo.isWater();
                movable21 = true;
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

        boolean movable57 = false;
        if(rc.canSenseLocation(m57)){
            mpinfo = rc.senseMapInfo(m57);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet57 = mpinfo.isWater();
                movable57 = true;
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

        boolean movable13 = false;
        if(rc.canSenseLocation(m13)){
            mpinfo = rc.senseMapInfo(m13);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet13 = mpinfo.isWater();
                movable13 = true;
            }
        }

        boolean movable12 = false;
        if(rc.canSenseLocation(m12)){
            mpinfo = rc.senseMapInfo(m12);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet12 = mpinfo.isWater();
                movable12 = true;
            }
        }

        boolean movable11 = false;
        if(rc.canSenseLocation(m11)){
            mpinfo = rc.senseMapInfo(m11);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet11 = mpinfo.isWater();
                movable11 = true;
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

        boolean movable66 = false;
        if(rc.canSenseLocation(m66)){
            mpinfo = rc.senseMapInfo(m66);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet66 = mpinfo.isWater();
                movable66 = true;
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

        boolean movable4 = false;
        if(rc.canSenseLocation(m4)){
            mpinfo = rc.senseMapInfo(m4);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet4 = mpinfo.isWater();
                movable4 = true;
            }
        }

        boolean movable3 = false;
        if(rc.canSenseLocation(m3)){
            mpinfo = rc.senseMapInfo(m3);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet3 = mpinfo.isWater();
                movable3 = true;
            }
        }

        boolean movable2 = false;
        if(rc.canSenseLocation(m2)){
            mpinfo = rc.senseMapInfo(m2);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet2 = mpinfo.isWater();
                movable2 = true;
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

        boolean movable75 = false;
        if(rc.canSenseLocation(m75)){
            mpinfo = rc.senseMapInfo(m75);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet75 = mpinfo.isWater();
                movable75 = true;
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
            if(movable31){
                if(wet31){
                    if(ret31>2+ret40){
                        d31=Direction.NORTH;
                        ret31 = 2+ret40;
                    }
                }else{
                    if(ret31>1+ret40){
                        d31=Direction.NORTH;
                        ret31 = 1+ret40;
                    }
                }
            }

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
        if(ret31!=10000){
            if(movable22){
                if(wet22){
                    if(ret22>2+ret31){
                        d22=d31;
                        ret22 = 2+ret31;
                    }
                }else{
                    if(ret22>1+ret31){
                        d22=d31;
                        ret22 = 1+ret31;
                    }
                }
            }

            if(movable39){
                if(wet39){
                    if(ret39>2+ret31){
                        d39=d31;
                        ret39 = 2+ret31;
                    }
                }else{
                    if(ret39>1+ret31){
                        d39=d31;
                        ret39 = 1+ret31;
                    }
                }
            }

            if(movable21){
                if(wet21){
                    if(ret21>2+ret31){
                        d21=d31;
                        ret21 = 2+ret31;
                    }
                }else{
                    if(ret21>1+ret31){
                        d21=d31;
                        ret21 = 1+ret31;
                    }
                }
            }

            if(movable30){
                if(wet30){
                    if(ret30>2+ret31){
                        d30=d31;
                        ret30 = 2+ret31;
                    }
                }else{
                    if(ret30>1+ret31){
                        d30=d31;
                        ret30 = 1+ret31;
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
        if(ret48!=10000){
            if(movable39){
                if(wet39){
                    if(ret39>2+ret48){
                        d39=d48;
                        ret39 = 2+ret48;
                    }
                }else{
                    if(ret39>1+ret48){
                        d39=d48;
                        ret39 = 1+ret48;
                    }
                }
            }

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
            if(movable21){
                if(wet21){
                    if(ret21>2+ret30){
                        d21=d30;
                        ret21 = 2+ret30;
                    }
                }else{
                    if(ret21>1+ret30){
                        d21=d30;
                        ret21 = 1+ret30;
                    }
                }
            }

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
            if(movable30){
                if(wet30){
                    if(ret30>2+ret39){
                        d30=d39;
                        ret30 = 2+ret39;
                    }
                }else{
                    if(ret30>1+ret39){
                        d30=d39;
                        ret30 = 1+ret39;
                    }
                }
            }

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
        if(ret22!=10000){
            if(movable13){
                if(wet13){
                    if(ret13>2+ret22){
                        d13=d22;
                        ret13 = 2+ret22;
                    }
                }else{
                    if(ret13>1+ret22){
                        d13=d22;
                        ret13 = 1+ret22;
                    }
                }
            }

            if(movable31){
                if(wet31){
                    if(ret31>2+ret22){
                        d31=d22;
                        ret31 = 2+ret22;
                    }
                }else{
                    if(ret31>1+ret22){
                        d31=d22;
                        ret31 = 1+ret22;
                    }
                }
            }

            if(movable30){
                if(wet30){
                    if(ret30>2+ret22){
                        d30=d22;
                        ret30 = 2+ret22;
                    }
                }else{
                    if(ret30>1+ret22){
                        d30=d22;
                        ret30 = 1+ret22;
                    }
                }
            }

            if(movable12){
                if(wet12){
                    if(ret12>2+ret22){
                        d12=d22;
                        ret12 = 2+ret22;
                    }
                }else{
                    if(ret12>1+ret22){
                        d12=d22;
                        ret12 = 1+ret22;
                    }
                }
            }

            if(movable21){
                if(wet21){
                    if(ret21>2+ret22){
                        d21=d22;
                        ret21 = 2+ret22;
                    }
                }else{
                    if(ret21>1+ret22){
                        d21=d22;
                        ret21 = 1+ret22;
                    }
                }
            }

        }
        if(ret21!=10000){
            if(movable12){
                if(wet12){
                    if(ret12>2+ret21){
                        d12=d21;
                        ret12 = 2+ret21;
                    }
                }else{
                    if(ret12>1+ret21){
                        d12=d21;
                        ret12 = 1+ret21;
                    }
                }
            }

            if(movable30){
                if(wet30){
                    if(ret30>2+ret21){
                        d30=d21;
                        ret30 = 2+ret21;
                    }
                }else{
                    if(ret30>1+ret21){
                        d30=d21;
                        ret30 = 1+ret21;
                    }
                }
            }

            if(movable29){
                if(wet29){
                    if(ret29>2+ret21){
                        d29=d21;
                        ret29 = 2+ret21;
                    }
                }else{
                    if(ret29>1+ret21){
                        d29=d21;
                        ret29 = 1+ret21;
                    }
                }
            }

            if(movable11){
                if(wet11){
                    if(ret11>2+ret21){
                        d11=d21;
                        ret11 = 2+ret21;
                    }
                }else{
                    if(ret11>1+ret21){
                        d11=d21;
                        ret11 = 1+ret21;
                    }
                }
            }

            if(movable20){
                if(wet20){
                    if(ret20>2+ret21){
                        d20=d21;
                        ret20 = 2+ret21;
                    }
                }else{
                    if(ret20>1+ret21){
                        d20=d21;
                        ret20 = 1+ret21;
                    }
                }
            }

        }
        if(ret58!=10000){
            if(movable49){
                if(wet49){
                    if(ret49>2+ret58){
                        d49=d58;
                        ret49 = 2+ret58;
                    }
                }else{
                    if(ret49>1+ret58){
                        d49=d58;
                        ret49 = 1+ret58;
                    }
                }
            }

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
        if(ret57!=10000){
            if(movable48){
                if(wet48){
                    if(ret48>2+ret57){
                        d48=d57;
                        ret48 = 2+ret57;
                    }
                }else{
                    if(ret48>1+ret57){
                        d48=d57;
                        ret48 = 1+ret57;
                    }
                }
            }

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
        if(ret56!=10000){
            if(movable47){
                if(wet47){
                    if(ret47>2+ret56){
                        d47=d56;
                        ret47 = 2+ret56;
                    }
                }else{
                    if(ret47>1+ret56){
                        d47=d56;
                        ret47 = 1+ret56;
                    }
                }
            }

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
            if(movable29){
                if(wet29){
                    if(ret29>2+ret38){
                        d29=d38;
                        ret29 = 2+ret38;
                    }
                }else{
                    if(ret29>1+ret38){
                        d29=d38;
                        ret29 = 1+ret38;
                    }
                }
            }

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
            if(movable38){
                if(wet38){
                    if(ret38>2+ret47){
                        d38=d47;
                        ret38 = 2+ret47;
                    }
                }else{
                    if(ret38>1+ret47){
                        d38=d47;
                        ret38 = 1+ret47;
                    }
                }
            }

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
            if(movable11){
                if(wet11){
                    if(ret11>2+ret20){
                        d11=d20;
                        ret11 = 2+ret20;
                    }
                }else{
                    if(ret11>1+ret20){
                        d11=d20;
                        ret11 = 1+ret20;
                    }
                }
            }

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
            if(movable20){
                if(wet20){
                    if(ret20>2+ret29){
                        d20=d29;
                        ret20 = 2+ret29;
                    }
                }else{
                    if(ret20>1+ret29){
                        d20=d29;
                        ret20 = 1+ret29;
                    }
                }
            }

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
        if(ret13!=10000){
            if(movable4){
                if(wet4){
                    if(ret4>2+ret13){
                        d4=d13;
                        ret4 = 2+ret13;
                    }
                }else{
                    if(ret4>1+ret13){
                        d4=d13;
                        ret4 = 1+ret13;
                    }
                }
            }

            if(movable22){
                if(wet22){
                    if(ret22>2+ret13){
                        d22=d13;
                        ret22 = 2+ret13;
                    }
                }else{
                    if(ret22>1+ret13){
                        d22=d13;
                        ret22 = 1+ret13;
                    }
                }
            }

            if(movable21){
                if(wet21){
                    if(ret21>2+ret13){
                        d21=d13;
                        ret21 = 2+ret13;
                    }
                }else{
                    if(ret21>1+ret13){
                        d21=d13;
                        ret21 = 1+ret13;
                    }
                }
            }

            if(movable3){
                if(wet3){
                    if(ret3>2+ret13){
                        d3=d13;
                        ret3 = 2+ret13;
                    }
                }else{
                    if(ret3>1+ret13){
                        d3=d13;
                        ret3 = 1+ret13;
                    }
                }
            }

            if(movable12){
                if(wet12){
                    if(ret12>2+ret13){
                        d12=d13;
                        ret12 = 2+ret13;
                    }
                }else{
                    if(ret12>1+ret13){
                        d12=d13;
                        ret12 = 1+ret13;
                    }
                }
            }

        }
        if(ret12!=10000){
            if(movable3){
                if(wet3){
                    if(ret3>2+ret12){
                        d3=d12;
                        ret3 = 2+ret12;
                    }
                }else{
                    if(ret3>1+ret12){
                        d3=d12;
                        ret3 = 1+ret12;
                    }
                }
            }

            if(movable21){
                if(wet21){
                    if(ret21>2+ret12){
                        d21=d12;
                        ret21 = 2+ret12;
                    }
                }else{
                    if(ret21>1+ret12){
                        d21=d12;
                        ret21 = 1+ret12;
                    }
                }
            }

            if(movable20){
                if(wet20){
                    if(ret20>2+ret12){
                        d20=d12;
                        ret20 = 2+ret12;
                    }
                }else{
                    if(ret20>1+ret12){
                        d20=d12;
                        ret20 = 1+ret12;
                    }
                }
            }

            if(movable2){
                if(wet2){
                    if(ret2>2+ret12){
                        d2=d12;
                        ret2 = 2+ret12;
                    }
                }else{
                    if(ret2>1+ret12){
                        d2=d12;
                        ret2 = 1+ret12;
                    }
                }
            }

            if(movable11){
                if(wet11){
                    if(ret11>2+ret12){
                        d11=d12;
                        ret11 = 2+ret12;
                    }
                }else{
                    if(ret11>1+ret12){
                        d11=d12;
                        ret11 = 1+ret12;
                    }
                }
            }

        }
        if(ret11!=10000){
            if(movable2){
                if(wet2){
                    if(ret2>2+ret11){
                        d2=d11;
                        ret2 = 2+ret11;
                    }
                }else{
                    if(ret2>1+ret11){
                        d2=d11;
                        ret2 = 1+ret11;
                    }
                }
            }

            if(movable20){
                if(wet20){
                    if(ret20>2+ret11){
                        d20=d11;
                        ret20 = 2+ret11;
                    }
                }else{
                    if(ret20>1+ret11){
                        d20=d11;
                        ret20 = 1+ret11;
                    }
                }
            }

            if(movable19){
                if(wet19){
                    if(ret19>2+ret11){
                        d19=d11;
                        ret19 = 2+ret11;
                    }
                }else{
                    if(ret19>1+ret11){
                        d19=d11;
                        ret19 = 1+ret11;
                    }
                }
            }

            if(movable10){
                if(wet10){
                    if(ret10>2+ret11){
                        d10=d11;
                        ret10 = 2+ret11;
                    }
                }else{
                    if(ret10>1+ret11){
                        d10=d11;
                        ret10 = 1+ret11;
                    }
                }
            }

        }
        if(ret67!=10000){
            if(movable58){
                if(wet58){
                    if(ret58>2+ret67){
                        d58=d67;
                        ret58 = 2+ret67;
                    }
                }else{
                    if(ret58>1+ret67){
                        d58=d67;
                        ret58 = 1+ret67;
                    }
                }
            }

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
        if(ret66!=10000){
            if(movable57){
                if(wet57){
                    if(ret57>2+ret66){
                        d57=d66;
                        ret57 = 2+ret66;
                    }
                }else{
                    if(ret57>1+ret66){
                        d57=d66;
                        ret57 = 1+ret66;
                    }
                }
            }

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
        if(ret65!=10000){
            if(movable56){
                if(wet56){
                    if(ret56>2+ret65){
                        d56=d65;
                        ret56 = 2+ret65;
                    }
                }else{
                    if(ret56>1+ret65){
                        d56=d65;
                        ret56 = 1+ret65;
                    }
                }
            }

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
        if(ret64!=10000){
            if(movable55){
                if(wet55){
                    if(ret55>2+ret64){
                        d55=d64;
                        ret55 = 2+ret64;
                    }
                }else{
                    if(ret55>1+ret64){
                        d55=d64;
                        ret55 = 1+ret64;
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
            if(movable37){
                if(wet37){
                    if(ret37>2+ret46){
                        d37=d46;
                        ret37 = 2+ret46;
                    }
                }else{
                    if(ret37>1+ret46){
                        d37=d46;
                        ret37 = 1+ret46;
                    }
                }
            }

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
            if(movable46){
                if(wet46){
                    if(ret46>2+ret55){
                        d46=d55;
                        ret46 = 2+ret55;
                    }
                }else{
                    if(ret46>1+ret55){
                        d46=d55;
                        ret46 = 1+ret55;
                    }
                }
            }

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
            if(movable19){
                if(wet19){
                    if(ret19>2+ret28){
                        d19=d28;
                        ret19 = 2+ret28;
                    }
                }else{
                    if(ret19>1+ret28){
                        d19=d28;
                        ret19 = 1+ret28;
                    }
                }
            }

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
            if(movable28){
                if(wet28){
                    if(ret28>2+ret37){
                        d28=d37;
                        ret28 = 2+ret37;
                    }
                }else{
                    if(ret28>1+ret37){
                        d28=d37;
                        ret28 = 1+ret37;
                    }
                }
            }

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
            if(movable10){
                if(wet10){
                    if(ret10>2+ret19){
                        d10=d19;
                        ret10 = 2+ret19;
                    }
                }else{
                    if(ret10>1+ret19){
                        d10=d19;
                        ret10 = 1+ret19;
                    }
                }
            }

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
        if(ret4!=10000){
            if(movable13){
                if(wet13){
                    if(ret13>2+ret4){
                        d13=d4;
                        ret13 = 2+ret4;
                    }
                }else{
                    if(ret13>1+ret4){
                        d13=d4;
                        ret13 = 1+ret4;
                    }
                }
            }

            if(movable12){
                if(wet12){
                    if(ret12>2+ret4){
                        d12=d4;
                        ret12 = 2+ret4;
                    }
                }else{
                    if(ret12>1+ret4){
                        d12=d4;
                        ret12 = 1+ret4;
                    }
                }
            }

            if(movable3){
                if(wet3){
                    if(ret3>2+ret4){
                        d3=d4;
                        ret3 = 2+ret4;
                    }
                }else{
                    if(ret3>1+ret4){
                        d3=d4;
                        ret3 = 1+ret4;
                    }
                }
            }

        }
        if(ret3!=10000){
            if(movable12){
                if(wet12){
                    if(ret12>2+ret3){
                        d12=d3;
                        ret12 = 2+ret3;
                    }
                }else{
                    if(ret12>1+ret3){
                        d12=d3;
                        ret12 = 1+ret3;
                    }
                }
            }

            if(movable11){
                if(wet11){
                    if(ret11>2+ret3){
                        d11=d3;
                        ret11 = 2+ret3;
                    }
                }else{
                    if(ret11>1+ret3){
                        d11=d3;
                        ret11 = 1+ret3;
                    }
                }
            }

            if(movable2){
                if(wet2){
                    if(ret2>2+ret3){
                        d2=d3;
                        ret2 = 2+ret3;
                    }
                }else{
                    if(ret2>1+ret3){
                        d2=d3;
                        ret2 = 1+ret3;
                    }
                }
            }

        }
        if(ret2!=10000){
            if(movable11){
                if(wet11){
                    if(ret11>2+ret2){
                        d11=d2;
                        ret11 = 2+ret2;
                    }
                }else{
                    if(ret11>1+ret2){
                        d11=d2;
                        ret11 = 1+ret2;
                    }
                }
            }

            if(movable10){
                if(wet10){
                    if(ret10>2+ret2){
                        d10=d2;
                        ret10 = 2+ret2;
                    }
                }else{
                    if(ret10>1+ret2){
                        d10=d2;
                        ret10 = 1+ret2;
                    }
                }
            }

        }
        if(ret76!=10000){
            if(movable67){
                if(wet67){
                    if(ret67>2+ret76){
                        d67=d76;
                        ret67 = 2+ret76;
                    }
                }else{
                    if(ret67>1+ret76){
                        d67=d76;
                        ret67 = 1+ret76;
                    }
                }
            }

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
        if(ret75!=10000){
            if(movable66){
                if(wet66){
                    if(ret66>2+ret75){
                        d66=d75;
                        ret66 = 2+ret75;
                    }
                }else{
                    if(ret66>1+ret75){
                        d66=d75;
                        ret66 = 1+ret75;
                    }
                }
            }

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
        if(ret74!=10000){
            if(movable65){
                if(wet65){
                    if(ret65>2+ret74){
                        d65=d74;
                        ret65 = 2+ret74;
                    }
                }else{
                    if(ret65>1+ret74){
                        d65=d74;
                        ret65 = 1+ret74;
                    }
                }
            }

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
            if(movable45){
                if(wet45){
                    if(ret45>2+ret54){
                        d45=d54;
                        ret45 = 2+ret54;
                    }
                }else{
                    if(ret45>1+ret54){
                        d45=d54;
                        ret45 = 1+ret54;
                    }
                }
            }

        }
        if(ret36!=10000){
            if(movable27){
                if(wet27){
                    if(ret27>2+ret36){
                        d27=d36;
                        ret27 = 2+ret36;
                    }
                }else{
                    if(ret27>1+ret36){
                        d27=d36;
                        ret27 = 1+ret36;
                    }
                }
            }

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

        }
        if(ret45!=10000){
            if(movable36){
                if(wet36){
                    if(ret36>2+ret45){
                        d36=d45;
                        ret36 = 2+ret45;
                    }
                }else{
                    if(ret36>1+ret45){
                        d36=d45;
                        ret36 = 1+ret45;
                    }
                }
            }

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

        }
        if(ret27!=10000){
            if(movable18){
                if(wet18){
                    if(ret18>2+ret27){
                        d18=d27;
                        ret18 = 2+ret27;
                    }
                }else{
                    if(ret18>1+ret27){
                        d18=d27;
                        ret18 = 1+ret27;
                    }
                }
            }

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

        }

        double initialDist = Math.sqrt(m40.distanceSquaredTo(target));
        Direction ans= Direction.CENTER;
        double cmax= 0;
        double dist30 = (initialDist-Math.sqrt(m30.distanceSquaredTo(target)))/(double)ret30;
        if(movable30&&dist30 > cmax){
            cmax= dist30;
            ans = d30;
        }

        double dist31 = (initialDist-Math.sqrt(m31.distanceSquaredTo(target)))/(double)ret31;
        if(movable31&&dist31 > cmax){
            cmax= dist31;
            ans = d31;
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

        double dist20 = (initialDist-Math.sqrt(m20.distanceSquaredTo(target)))/(double)ret20;
        if(movable20&&dist20 > cmax){
            cmax= dist20;
            ans = d20;
        }

        double dist21 = (initialDist-Math.sqrt(m21.distanceSquaredTo(target)))/(double)ret21;
        if(movable21&&dist21 > cmax){
            cmax= dist21;
            ans = d21;
        }

        double dist22 = (initialDist-Math.sqrt(m22.distanceSquaredTo(target)))/(double)ret22;
        if(movable22&&dist22 > cmax){
            cmax= dist22;
            ans = d22;
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

        double dist10 = (initialDist-Math.sqrt(m10.distanceSquaredTo(target)))/(double)ret10;
        if(movable10&&dist10 > cmax){
            cmax= dist10;
            ans = d10;
        }

        double dist11 = (initialDist-Math.sqrt(m11.distanceSquaredTo(target)))/(double)ret11;
        if(movable11&&dist11 > cmax){
            cmax= dist11;
            ans = d11;
        }

        double dist12 = (initialDist-Math.sqrt(m12.distanceSquaredTo(target)))/(double)ret12;
        if(movable12&&dist12 > cmax){
            cmax= dist12;
            ans = d12;
        }

        double dist13 = (initialDist-Math.sqrt(m13.distanceSquaredTo(target)))/(double)ret13;
        if(movable13&&dist13 > cmax){
            cmax= dist13;
            ans = d13;
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

        double dist2 = (initialDist-Math.sqrt(m2.distanceSquaredTo(target)))/(double)ret2;
        if(movable2&&dist2 > cmax){
            cmax= dist2;
            ans = d2;
        }

        double dist3 = (initialDist-Math.sqrt(m3.distanceSquaredTo(target)))/(double)ret3;
        if(movable3&&dist3 > cmax){
            cmax= dist3;
            ans = d3;
        }

        double dist4 = (initialDist-Math.sqrt(m4.distanceSquaredTo(target)))/(double)ret4;
        if(movable4&&dist4 > cmax){
            cmax= dist4;
            ans = d4;
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

        if(ans!=null){
            return ans;
        }else{
            return Direction.CENTER;
        }

    }

}
