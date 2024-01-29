package waxthebuilder.BFS;
import battlecode.common.*;

public class BFSNorthWest {
    public static Direction findBestDirection(RobotController rc, MapLocation target) throws GameActionException {
        MapLocation m40 = rc.getLocation();
        int ret40= 0;
        MapLocation m31= m40.add(Direction.NORTH);
        Direction d31 = null;
        int ret31= 10000;
        boolean wet31 = false;
        MapLocation m32= m40.add(Direction.NORTHEAST);
        Direction d32 = null;
        int ret32= 10000;
        boolean wet32 = false;
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
        MapLocation m23= m31.add(Direction.NORTHEAST);
        Direction d23 = null;
        int ret23= 10000;
        boolean wet23 = false;
        MapLocation m21= m31.add(Direction.NORTHWEST);
        Direction d21 = null;
        int ret21= 10000;
        boolean wet21 = false;
        MapLocation m24= m32.add(Direction.NORTHEAST);
        Direction d24 = null;
        int ret24= 10000;
        boolean wet24 = false;
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
        MapLocation m14= m22.add(Direction.NORTHEAST);
        Direction d14 = null;
        int ret14= 10000;
        boolean wet14 = false;
        MapLocation m12= m22.add(Direction.NORTHWEST);
        Direction d12 = null;
        int ret12= 10000;
        boolean wet12 = false;
        MapLocation m15= m23.add(Direction.NORTHEAST);
        Direction d15 = null;
        int ret15= 10000;
        boolean wet15 = false;
        MapLocation m11= m21.add(Direction.NORTHWEST);
        Direction d11 = null;
        int ret11= 10000;
        boolean wet11 = false;
        MapLocation m16= m24.add(Direction.NORTHEAST);
        Direction d16 = null;
        int ret16= 10000;
        boolean wet16 = false;
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
        MapLocation m5= m13.add(Direction.NORTHEAST);
        Direction d5 = null;
        int ret5= 10000;
        boolean wet5 = false;
        MapLocation m3= m13.add(Direction.NORTHWEST);
        Direction d3 = null;
        int ret3= 10000;
        boolean wet3 = false;
        MapLocation m6= m14.add(Direction.NORTHEAST);
        Direction d6 = null;
        int ret6= 10000;
        boolean wet6 = false;
        MapLocation m2= m12.add(Direction.NORTHWEST);
        Direction d2 = null;
        int ret2= 10000;
        boolean wet2 = false;
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

        boolean movable32 = false;
        if(rc.canSenseLocation(m32)){
            mpinfo = rc.senseMapInfo(m32);
            wet32 = mpinfo.isWater();
            if(!rc.isLocationOccupied(m32)&&(mpinfo.isPassable() || wet32)){
                movable32 = true;
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

        boolean movable23 = false;
        if(rc.canSenseLocation(m23)){
            mpinfo = rc.senseMapInfo(m23);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet23 = mpinfo.isWater();
                movable23 = true;
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

        boolean movable24 = false;
        if(rc.canSenseLocation(m24)){
            mpinfo = rc.senseMapInfo(m24);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet24 = mpinfo.isWater();
                movable24 = true;
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

        boolean movable14 = false;
        if(rc.canSenseLocation(m14)){
            mpinfo = rc.senseMapInfo(m14);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet14 = mpinfo.isWater();
                movable14 = true;
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

        boolean movable15 = false;
        if(rc.canSenseLocation(m15)){
            mpinfo = rc.senseMapInfo(m15);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet15 = mpinfo.isWater();
                movable15 = true;
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

        boolean movable16 = false;
        if(rc.canSenseLocation(m16)){
            mpinfo = rc.senseMapInfo(m16);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet16 = mpinfo.isWater();
                movable16 = true;
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

        boolean movable5 = false;
        if(rc.canSenseLocation(m5)){
            mpinfo = rc.senseMapInfo(m5);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet5 = mpinfo.isWater();
                movable5 = true;
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

        boolean movable6 = false;
        if(rc.canSenseLocation(m6)){
            mpinfo = rc.senseMapInfo(m6);
            if(!mpinfo.isDam()&&!mpinfo.isWall()){
                wet6 = mpinfo.isWater();
                movable6 = true;
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

            if(movable32){
                if(wet32){
                    if(ret32>3+ret40){
                        d32=Direction.NORTHEAST;
                        ret32 = 3+ret40;
                    }
                }else{
                    if(ret32>2+ret40){
                        d32=Direction.NORTHEAST;
                        ret32 = 2+ret40;
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

            if(movable23){
                if(wet23){
                    if(ret23>2+ret31){
                        d23=d31;
                        ret23 = 2+ret31;
                    }
                }else{
                    if(ret23>1+ret31){
                        d23=d31;
                        ret23 = 1+ret31;
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
        if(ret32!=10000){
            if(movable23){
                if(wet23){
                    if(ret23>2+ret32){
                        d23=d32;
                        ret23 = 2+ret32;
                    }
                }else{
                    if(ret23>1+ret32){
                        d23=d32;
                        ret23 = 1+ret32;
                    }
                }
            }

            if(movable24){
                if(wet24){
                    if(ret24>2+ret32){
                        d24=d32;
                        ret24 = 2+ret32;
                    }
                }else{
                    if(ret24>1+ret32){
                        d24=d32;
                        ret24 = 1+ret32;
                    }
                }
            }

            if(movable22){
                if(wet22){
                    if(ret22>2+ret32){
                        d22=d32;
                        ret22 = 2+ret32;
                    }
                }else{
                    if(ret22>1+ret32){
                        d22=d32;
                        ret22 = 1+ret32;
                    }
                }
            }

            if(movable31){
                if(wet31){
                    if(ret31>2+ret32){
                        d31=d32;
                        ret31 = 2+ret32;
                    }
                }else{
                    if(ret31>1+ret32){
                        d31=d32;
                        ret31 = 1+ret32;
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

            if(movable22){
                if(wet22){
                    if(ret22>2+ret30){
                        d22=d30;
                        ret22 = 2+ret30;
                    }
                }else{
                    if(ret22>1+ret30){
                        d22=d30;
                        ret22 = 1+ret30;
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

            if(movable31){
                if(wet31){
                    if(ret31>2+ret39){
                        d31=d39;
                        ret31 = 2+ret39;
                    }
                }else{
                    if(ret31>1+ret39){
                        d31=d39;
                        ret31 = 1+ret39;
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

            if(movable14){
                if(wet14){
                    if(ret14>2+ret22){
                        d14=d22;
                        ret14 = 2+ret22;
                    }
                }else{
                    if(ret14>1+ret22){
                        d14=d22;
                        ret14 = 1+ret22;
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
        if(ret23!=10000){
            if(movable14){
                if(wet14){
                    if(ret14>2+ret23){
                        d14=d23;
                        ret14 = 2+ret23;
                    }
                }else{
                    if(ret14>1+ret23){
                        d14=d23;
                        ret14 = 1+ret23;
                    }
                }
            }

            if(movable15){
                if(wet15){
                    if(ret15>2+ret23){
                        d15=d23;
                        ret15 = 2+ret23;
                    }
                }else{
                    if(ret15>1+ret23){
                        d15=d23;
                        ret15 = 1+ret23;
                    }
                }
            }

            if(movable31){
                if(wet31){
                    if(ret31>2+ret23){
                        d31=d23;
                        ret31 = 2+ret23;
                    }
                }else{
                    if(ret31>1+ret23){
                        d31=d23;
                        ret31 = 1+ret23;
                    }
                }
            }

            if(movable13){
                if(wet13){
                    if(ret13>2+ret23){
                        d13=d23;
                        ret13 = 2+ret23;
                    }
                }else{
                    if(ret13>1+ret23){
                        d13=d23;
                        ret13 = 1+ret23;
                    }
                }
            }

            if(movable22){
                if(wet22){
                    if(ret22>2+ret23){
                        d22=d23;
                        ret22 = 2+ret23;
                    }
                }else{
                    if(ret22>1+ret23){
                        d22=d23;
                        ret22 = 1+ret23;
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

            if(movable13){
                if(wet13){
                    if(ret13>2+ret21){
                        d13=d21;
                        ret13 = 2+ret21;
                    }
                }else{
                    if(ret13>1+ret21){
                        d13=d21;
                        ret13 = 1+ret21;
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
        if(ret24!=10000){
            if(movable15){
                if(wet15){
                    if(ret15>2+ret24){
                        d15=d24;
                        ret15 = 2+ret24;
                    }
                }else{
                    if(ret15>1+ret24){
                        d15=d24;
                        ret15 = 1+ret24;
                    }
                }
            }

            if(movable16){
                if(wet16){
                    if(ret16>2+ret24){
                        d16=d24;
                        ret16 = 2+ret24;
                    }
                }else{
                    if(ret16>1+ret24){
                        d16=d24;
                        ret16 = 1+ret24;
                    }
                }
            }

            if(movable32){
                if(wet32){
                    if(ret32>2+ret24){
                        d32=d24;
                        ret32 = 2+ret24;
                    }
                }else{
                    if(ret32>1+ret24){
                        d32=d24;
                        ret32 = 1+ret24;
                    }
                }
            }

            if(movable14){
                if(wet14){
                    if(ret14>2+ret24){
                        d14=d24;
                        ret14 = 2+ret24;
                    }
                }else{
                    if(ret14>1+ret24){
                        d14=d24;
                        ret14 = 1+ret24;
                    }
                }
            }

            if(movable23){
                if(wet23){
                    if(ret23>2+ret24){
                        d23=d24;
                        ret23 = 2+ret24;
                    }
                }else{
                    if(ret23>1+ret24){
                        d23=d24;
                        ret23 = 1+ret24;
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

            if(movable48){
                if(wet48){
                    if(ret48>2+ret56){
                        d48=d56;
                        ret48 = 2+ret56;
                    }
                }else{
                    if(ret48>1+ret56){
                        d48=d56;
                        ret48 = 1+ret56;
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

            if(movable30){
                if(wet30){
                    if(ret30>2+ret38){
                        d30=d38;
                        ret30 = 2+ret38;
                    }
                }else{
                    if(ret30>1+ret38){
                        d30=d38;
                        ret30 = 1+ret38;
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

            if(movable39){
                if(wet39){
                    if(ret39>2+ret47){
                        d39=d47;
                        ret39 = 2+ret47;
                    }
                }else{
                    if(ret39>1+ret47){
                        d39=d47;
                        ret39 = 1+ret47;
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

            if(movable12){
                if(wet12){
                    if(ret12>2+ret20){
                        d12=d20;
                        ret12 = 2+ret20;
                    }
                }else{
                    if(ret12>1+ret20){
                        d12=d20;
                        ret12 = 1+ret20;
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

            if(movable21){
                if(wet21){
                    if(ret21>2+ret29){
                        d21=d29;
                        ret21 = 2+ret29;
                    }
                }else{
                    if(ret21>1+ret29){
                        d21=d29;
                        ret21 = 1+ret29;
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

            if(movable5){
                if(wet5){
                    if(ret5>2+ret13){
                        d5=d13;
                        ret5 = 2+ret13;
                    }
                }else{
                    if(ret5>1+ret13){
                        d5=d13;
                        ret5 = 1+ret13;
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
        if(ret14!=10000){
            if(movable5){
                if(wet5){
                    if(ret5>2+ret14){
                        d5=d14;
                        ret5 = 2+ret14;
                    }
                }else{
                    if(ret5>1+ret14){
                        d5=d14;
                        ret5 = 1+ret14;
                    }
                }
            }

            if(movable6){
                if(wet6){
                    if(ret6>2+ret14){
                        d6=d14;
                        ret6 = 2+ret14;
                    }
                }else{
                    if(ret6>1+ret14){
                        d6=d14;
                        ret6 = 1+ret14;
                    }
                }
            }

            if(movable22){
                if(wet22){
                    if(ret22>2+ret14){
                        d22=d14;
                        ret22 = 2+ret14;
                    }
                }else{
                    if(ret22>1+ret14){
                        d22=d14;
                        ret22 = 1+ret14;
                    }
                }
            }

            if(movable4){
                if(wet4){
                    if(ret4>2+ret14){
                        d4=d14;
                        ret4 = 2+ret14;
                    }
                }else{
                    if(ret4>1+ret14){
                        d4=d14;
                        ret4 = 1+ret14;
                    }
                }
            }

            if(movable13){
                if(wet13){
                    if(ret13>2+ret14){
                        d13=d14;
                        ret13 = 2+ret14;
                    }
                }else{
                    if(ret13>1+ret14){
                        d13=d14;
                        ret13 = 1+ret14;
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

            if(movable4){
                if(wet4){
                    if(ret4>2+ret12){
                        d4=d12;
                        ret4 = 2+ret12;
                    }
                }else{
                    if(ret4>1+ret12){
                        d4=d12;
                        ret4 = 1+ret12;
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
        if(ret15!=10000){
            if(movable6){
                if(wet6){
                    if(ret6>2+ret15){
                        d6=d15;
                        ret6 = 2+ret15;
                    }
                }else{
                    if(ret6>1+ret15){
                        d6=d15;
                        ret6 = 1+ret15;
                    }
                }
            }

            if(movable23){
                if(wet23){
                    if(ret23>2+ret15){
                        d23=d15;
                        ret23 = 2+ret15;
                    }
                }else{
                    if(ret23>1+ret15){
                        d23=d15;
                        ret23 = 1+ret15;
                    }
                }
            }

            if(movable5){
                if(wet5){
                    if(ret5>2+ret15){
                        d5=d15;
                        ret5 = 2+ret15;
                    }
                }else{
                    if(ret5>1+ret15){
                        d5=d15;
                        ret5 = 1+ret15;
                    }
                }
            }

            if(movable14){
                if(wet14){
                    if(ret14>2+ret15){
                        d14=d15;
                        ret14 = 2+ret15;
                    }
                }else{
                    if(ret14>1+ret15){
                        d14=d15;
                        ret14 = 1+ret15;
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

            if(movable3){
                if(wet3){
                    if(ret3>2+ret11){
                        d3=d11;
                        ret3 = 2+ret11;
                    }
                }else{
                    if(ret3>1+ret11){
                        d3=d11;
                        ret3 = 1+ret11;
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
        if(ret16!=10000){
            if(movable24){
                if(wet24){
                    if(ret24>2+ret16){
                        d24=d16;
                        ret24 = 2+ret16;
                    }
                }else{
                    if(ret24>1+ret16){
                        d24=d16;
                        ret24 = 1+ret16;
                    }
                }
            }

            if(movable6){
                if(wet6){
                    if(ret6>2+ret16){
                        d6=d16;
                        ret6 = 2+ret16;
                    }
                }else{
                    if(ret6>1+ret16){
                        d6=d16;
                        ret6 = 1+ret16;
                    }
                }
            }

            if(movable15){
                if(wet15){
                    if(ret15>2+ret16){
                        d15=d16;
                        ret15 = 2+ret16;
                    }
                }else{
                    if(ret15>1+ret16){
                        d15=d16;
                        ret15 = 1+ret16;
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

            if(movable56){
                if(wet56){
                    if(ret56>2+ret64){
                        d56=d64;
                        ret56 = 2+ret64;
                    }
                }else{
                    if(ret56>1+ret64){
                        d56=d64;
                        ret56 = 1+ret64;
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

            if(movable38){
                if(wet38){
                    if(ret38>2+ret46){
                        d38=d46;
                        ret38 = 2+ret46;
                    }
                }else{
                    if(ret38>1+ret46){
                        d38=d46;
                        ret38 = 1+ret46;
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

            if(movable47){
                if(wet47){
                    if(ret47>2+ret55){
                        d47=d55;
                        ret47 = 2+ret55;
                    }
                }else{
                    if(ret47>1+ret55){
                        d47=d55;
                        ret47 = 1+ret55;
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

            if(movable20){
                if(wet20){
                    if(ret20>2+ret28){
                        d20=d28;
                        ret20 = 2+ret28;
                    }
                }else{
                    if(ret20>1+ret28){
                        d20=d28;
                        ret20 = 1+ret28;
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

            if(movable29){
                if(wet29){
                    if(ret29>2+ret37){
                        d29=d37;
                        ret29 = 2+ret37;
                    }
                }else{
                    if(ret29>1+ret37){
                        d29=d37;
                        ret29 = 1+ret37;
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
            if(movable2){
                if(wet2){
                    if(ret2>2+ret10){
                        d2=d10;
                        ret2 = 2+ret10;
                    }
                }else{
                    if(ret2>1+ret10){
                        d2=d10;
                        ret2 = 1+ret10;
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

            if(movable11){
                if(wet11){
                    if(ret11>2+ret19){
                        d11=d19;
                        ret11 = 2+ret19;
                    }
                }else{
                    if(ret11>1+ret19){
                        d11=d19;
                        ret11 = 1+ret19;
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
        if(ret5!=10000){
            if(movable13){
                if(wet13){
                    if(ret13>2+ret5){
                        d13=d5;
                        ret13 = 2+ret5;
                    }
                }else{
                    if(ret13>1+ret5){
                        d13=d5;
                        ret13 = 1+ret5;
                    }
                }
            }

            if(movable4){
                if(wet4){
                    if(ret4>2+ret5){
                        d4=d5;
                        ret4 = 2+ret5;
                    }
                }else{
                    if(ret4>1+ret5){
                        d4=d5;
                        ret4 = 1+ret5;
                    }
                }
            }

        }
        if(ret3!=10000){
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
        if(ret6!=10000){
            if(movable14){
                if(wet14){
                    if(ret14>2+ret6){
                        d14=d6;
                        ret14 = 2+ret6;
                    }
                }else{
                    if(ret14>1+ret6){
                        d14=d6;
                        ret14 = 1+ret6;
                    }
                }
            }

            if(movable5){
                if(wet5){
                    if(ret5>2+ret6){
                        d5=d6;
                        ret5 = 2+ret6;
                    }
                }else{
                    if(ret5>1+ret6){
                        d5=d6;
                        ret5 = 1+ret6;
                    }
                }
            }

        }
        if(ret2!=10000){
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

            if(movable46){
                if(wet46){
                    if(ret46>2+ret54){
                        d46=d54;
                        ret46 = 2+ret54;
                    }
                }else{
                    if(ret46>1+ret54){
                        d46=d54;
                        ret46 = 1+ret54;
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

            if(movable28){
                if(wet28){
                    if(ret28>2+ret36){
                        d28=d36;
                        ret28 = 2+ret36;
                    }
                }else{
                    if(ret28>1+ret36){
                        d28=d36;
                        ret28 = 1+ret36;
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

            if(movable37){
                if(wet37){
                    if(ret37>2+ret45){
                        d37=d45;
                        ret37 = 2+ret45;
                    }
                }else{
                    if(ret37>1+ret45){
                        d37=d45;
                        ret37 = 1+ret45;
                    }
                }
            }

        }
        if(ret18!=10000){
            if(movable10){
                if(wet10){
                    if(ret10>2+ret18){
                        d10=d18;
                        ret10 = 2+ret18;
                    }
                }else{
                    if(ret10>1+ret18){
                        d10=d18;
                        ret10 = 1+ret18;
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

            if(movable19){
                if(wet19){
                    if(ret19>2+ret27){
                        d19=d27;
                        ret19 = 2+ret27;
                    }
                }else{
                    if(ret19>1+ret27){
                        d19=d27;
                        ret19 = 1+ret27;
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

        double dist32 = (initialDist-Math.sqrt(m32.distanceSquaredTo(target)))/(double)ret32;
        if(movable32&&dist32 > cmax){
            cmax= dist32;
            ans = d32;
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

        double dist23 = (initialDist-Math.sqrt(m23.distanceSquaredTo(target)))/(double)ret23;
        if(movable23&&dist23 > cmax){
            cmax= dist23;
            ans = d23;
        }

        double dist24 = (initialDist-Math.sqrt(m24.distanceSquaredTo(target)))/(double)ret24;
        if(movable24&&dist24 > cmax){
            cmax= dist24;
            ans = d24;
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

        double dist14 = (initialDist-Math.sqrt(m14.distanceSquaredTo(target)))/(double)ret14;
        if(movable14&&dist14 > cmax){
            cmax= dist14;
            ans = d14;
        }

        double dist15 = (initialDist-Math.sqrt(m15.distanceSquaredTo(target)))/(double)ret15;
        if(movable15&&dist15 > cmax){
            cmax= dist15;
            ans = d15;
        }

        double dist16 = (initialDist-Math.sqrt(m16.distanceSquaredTo(target)))/(double)ret16;
        if(movable16&&dist16 > cmax){
            cmax= dist16;
            ans = d16;
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

        double dist5 = (initialDist-Math.sqrt(m5.distanceSquaredTo(target)))/(double)ret5;
        if(movable5&&dist5 > cmax){
            cmax= dist5;
            ans = d5;
        }

        double dist6 = (initialDist-Math.sqrt(m6.distanceSquaredTo(target)))/(double)ret6;
        if(movable6&&dist6 > cmax){
            cmax= dist6;
            ans = d6;
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

        if(ans!=null){
            return ans;
        }else{
            return Direction.CENTER;
        }

    }
}
