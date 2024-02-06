package moon2.BFS;
import battlecode.common.*;

public class BFSNorthEast {
    public static Direction findBestDirection(RobotController rc, MapLocation target) throws GameActionException {
        MapLocation m40 = rc.getLocation();
        int ret40 = 0;
        MapLocation m31 = m40.add(Direction.NORTH);
        Direction d31 = null;
        int ret31 = 10000;
        boolean wet31 = false;
        MapLocation m41 = m40.add(Direction.EAST);
        Direction d41 = null;
        int ret41 = 10000;
        boolean wet41 = false;
        MapLocation m50 = m40.add(Direction.SOUTHEAST);
        Direction d50 = null;
        int ret50 = 10000;
        boolean wet50 = false;
        MapLocation m32 = m40.add(Direction.NORTHEAST);
        Direction d32 = null;
        int ret32 = 10000;
        boolean wet32 = false;
        MapLocation m30 = m40.add(Direction.NORTHWEST);
        Direction d30 = null;
        int ret30 = 10000;
        boolean wet30 = false;
        MapLocation m22 = m31.add(Direction.NORTH);
        Direction d22 = null;
        int ret22 = 10000;
        boolean wet22 = false;
        MapLocation m23 = m31.add(Direction.NORTHEAST);
        Direction d23 = null;
        int ret23 = 10000;
        boolean wet23 = false;
        MapLocation m21 = m31.add(Direction.NORTHWEST);
        Direction d21 = null;
        int ret21 = 10000;
        boolean wet21 = false;
        MapLocation m42 = m41.add(Direction.EAST);
        Direction d42 = null;
        int ret42 = 10000;
        boolean wet42 = false;
        MapLocation m51 = m41.add(Direction.SOUTHEAST);
        Direction d51 = null;
        int ret51 = 10000;
        boolean wet51 = false;
        MapLocation m33 = m41.add(Direction.NORTHEAST);
        Direction d33 = null;
        int ret33 = 10000;
        boolean wet33 = false;
        MapLocation m60 = m50.add(Direction.SOUTHEAST);
        Direction d60 = null;
        int ret60 = 10000;
        boolean wet60 = false;
        MapLocation m24 = m32.add(Direction.NORTHEAST);
        Direction d24 = null;
        int ret24 = 10000;
        boolean wet24 = false;
        MapLocation m20 = m30.add(Direction.NORTHWEST);
        Direction d20 = null;
        int ret20 = 10000;
        boolean wet20 = false;
        MapLocation m13 = m22.add(Direction.NORTH);
        Direction d13 = null;
        int ret13 = 10000;
        boolean wet13 = false;
        MapLocation m14 = m22.add(Direction.NORTHEAST);
        Direction d14 = null;
        int ret14 = 10000;
        boolean wet14 = false;
        MapLocation m12 = m22.add(Direction.NORTHWEST);
        Direction d12 = null;
        int ret12 = 10000;
        boolean wet12 = false;
        MapLocation m15 = m23.add(Direction.NORTHEAST);
        Direction d15 = null;
        int ret15 = 10000;
        boolean wet15 = false;
        MapLocation m11 = m21.add(Direction.NORTHWEST);
        Direction d11 = null;
        int ret11 = 10000;
        boolean wet11 = false;
        MapLocation m43 = m42.add(Direction.EAST);
        Direction d43 = null;
        int ret43 = 10000;
        boolean wet43 = false;
        MapLocation m52 = m42.add(Direction.SOUTHEAST);
        Direction d52 = null;
        int ret52 = 10000;
        boolean wet52 = false;
        MapLocation m34 = m42.add(Direction.NORTHEAST);
        Direction d34 = null;
        int ret34 = 10000;
        boolean wet34 = false;
        MapLocation m61 = m51.add(Direction.SOUTHEAST);
        Direction d61 = null;
        int ret61 = 10000;
        boolean wet61 = false;
        MapLocation m25 = m33.add(Direction.NORTHEAST);
        Direction d25 = null;
        int ret25 = 10000;
        boolean wet25 = false;
        MapLocation m70 = m60.add(Direction.SOUTHEAST);
        Direction d70 = null;
        int ret70 = 10000;
        boolean wet70 = false;
        MapLocation m16 = m24.add(Direction.NORTHEAST);
        Direction d16 = null;
        int ret16 = 10000;
        boolean wet16 = false;
        MapLocation m10 = m20.add(Direction.NORTHWEST);
        Direction d10 = null;
        int ret10 = 10000;
        boolean wet10 = false;
        MapLocation m4 = m13.add(Direction.NORTH);
        Direction d4 = null;
        int ret4 = 10000;
        boolean wet4 = false;
        MapLocation m5 = m13.add(Direction.NORTHEAST);
        Direction d5 = null;
        int ret5 = 10000;
        boolean wet5 = false;
        MapLocation m3 = m13.add(Direction.NORTHWEST);
        Direction d3 = null;
        int ret3 = 10000;
        boolean wet3 = false;
        MapLocation m6 = m14.add(Direction.NORTHEAST);
        Direction d6 = null;
        int ret6 = 10000;
        boolean wet6 = false;
        MapLocation m2 = m12.add(Direction.NORTHWEST);
        Direction d2 = null;
        int ret2 = 10000;
        boolean wet2 = false;
        MapLocation m44 = m43.add(Direction.EAST);
        Direction d44 = null;
        int ret44 = 10000;
        boolean wet44 = false;
        MapLocation m53 = m43.add(Direction.SOUTHEAST);
        Direction d53 = null;
        int ret53 = 10000;
        boolean wet53 = false;
        MapLocation m35 = m43.add(Direction.NORTHEAST);
        Direction d35 = null;
        int ret35 = 10000;
        boolean wet35 = false;
        MapLocation m62 = m52.add(Direction.SOUTHEAST);
        Direction d62 = null;
        int ret62 = 10000;
        boolean wet62 = false;
        MapLocation m26 = m34.add(Direction.NORTHEAST);
        Direction d26 = null;
        int ret26 = 10000;
        boolean wet26 = false;
        MapInfo mpinfo;
        boolean movable31 = false;
        if (rc.canSenseLocation(m31)) {
            mpinfo = rc.senseMapInfo(m31);
            wet31 = mpinfo.isWater();
            if (!rc.isLocationOccupied(m31) && (mpinfo.isPassable() || wet31)) {
                movable31 = true;
            }
        }

        boolean movable41 = false;
        if (rc.canSenseLocation(m41)) {
            mpinfo = rc.senseMapInfo(m41);
            wet41 = mpinfo.isWater();
            if (!rc.isLocationOccupied(m41) && (mpinfo.isPassable() || wet41)) {
                movable41 = true;
            }
        }

        boolean movable50 = false;
        if (rc.canSenseLocation(m50)) {
            mpinfo = rc.senseMapInfo(m50);
            wet50 = mpinfo.isWater();
            if (!rc.isLocationOccupied(m50) && (mpinfo.isPassable() || wet50)) {
                movable50 = true;
            }
        }

        boolean movable32 = false;
        if (rc.canSenseLocation(m32)) {
            mpinfo = rc.senseMapInfo(m32);
            wet32 = mpinfo.isWater();
            if (!rc.isLocationOccupied(m32) && (mpinfo.isPassable() || wet32)) {
                movable32 = true;
            }
        }

        boolean movable30 = false;
        if (rc.canSenseLocation(m30)) {
            mpinfo = rc.senseMapInfo(m30);
            wet30 = mpinfo.isWater();
            if (!rc.isLocationOccupied(m30) && (mpinfo.isPassable() || wet30)) {
                movable30 = true;
            }
        }

        boolean movable22 = false;
        if (rc.canSenseLocation(m22)) {
            mpinfo = rc.senseMapInfo(m22);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet22 = mpinfo.isWater();
                movable22 = true;
            }
        }

        boolean movable23 = false;
        if (rc.canSenseLocation(m23)) {
            mpinfo = rc.senseMapInfo(m23);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet23 = mpinfo.isWater();
                movable23 = true;
            }
        }

        boolean movable21 = false;
        if (rc.canSenseLocation(m21)) {
            mpinfo = rc.senseMapInfo(m21);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet21 = mpinfo.isWater();
                movable21 = true;
            }
        }

        boolean movable42 = false;
        if (rc.canSenseLocation(m42)) {
            mpinfo = rc.senseMapInfo(m42);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet42 = mpinfo.isWater();
                movable42 = true;
            }
        }

        boolean movable51 = false;
        if (rc.canSenseLocation(m51)) {
            mpinfo = rc.senseMapInfo(m51);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet51 = mpinfo.isWater();
                movable51 = true;
            }
        }

        boolean movable33 = false;
        if (rc.canSenseLocation(m33)) {
            mpinfo = rc.senseMapInfo(m33);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet33 = mpinfo.isWater();
                movable33 = true;
            }
        }

        boolean movable60 = false;
        if (rc.canSenseLocation(m60)) {
            mpinfo = rc.senseMapInfo(m60);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet60 = mpinfo.isWater();
                movable60 = true;
            }
        }

        boolean movable24 = false;
        if (rc.canSenseLocation(m24)) {
            mpinfo = rc.senseMapInfo(m24);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet24 = mpinfo.isWater();
                movable24 = true;
            }
        }

        boolean movable20 = false;
        if (rc.canSenseLocation(m20)) {
            mpinfo = rc.senseMapInfo(m20);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet20 = mpinfo.isWater();
                movable20 = true;
            }
        }

        boolean movable13 = false;
        if (rc.canSenseLocation(m13)) {
            mpinfo = rc.senseMapInfo(m13);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet13 = mpinfo.isWater();
                movable13 = true;
            }
        }

        boolean movable14 = false;
        if (rc.canSenseLocation(m14)) {
            mpinfo = rc.senseMapInfo(m14);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet14 = mpinfo.isWater();
                movable14 = true;
            }
        }

        boolean movable12 = false;
        if (rc.canSenseLocation(m12)) {
            mpinfo = rc.senseMapInfo(m12);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet12 = mpinfo.isWater();
                movable12 = true;
            }
        }

        boolean movable15 = false;
        if (rc.canSenseLocation(m15)) {
            mpinfo = rc.senseMapInfo(m15);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet15 = mpinfo.isWater();
                movable15 = true;
            }
        }

        boolean movable11 = false;
        if (rc.canSenseLocation(m11)) {
            mpinfo = rc.senseMapInfo(m11);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet11 = mpinfo.isWater();
                movable11 = true;
            }
        }

        boolean movable43 = false;
        if (rc.canSenseLocation(m43)) {
            mpinfo = rc.senseMapInfo(m43);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet43 = mpinfo.isWater();
                movable43 = true;
            }
        }

        boolean movable52 = false;
        if (rc.canSenseLocation(m52)) {
            mpinfo = rc.senseMapInfo(m52);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet52 = mpinfo.isWater();
                movable52 = true;
            }
        }

        boolean movable34 = false;
        if (rc.canSenseLocation(m34)) {
            mpinfo = rc.senseMapInfo(m34);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet34 = mpinfo.isWater();
                movable34 = true;
            }
        }

        boolean movable61 = false;
        if (rc.canSenseLocation(m61)) {
            mpinfo = rc.senseMapInfo(m61);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet61 = mpinfo.isWater();
                movable61 = true;
            }
        }

        boolean movable25 = false;
        if (rc.canSenseLocation(m25)) {
            mpinfo = rc.senseMapInfo(m25);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet25 = mpinfo.isWater();
                movable25 = true;
            }
        }

        boolean movable70 = false;
        if (rc.canSenseLocation(m70)) {
            mpinfo = rc.senseMapInfo(m70);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet70 = mpinfo.isWater();
                movable70 = true;
            }
        }

        boolean movable16 = false;
        if (rc.canSenseLocation(m16)) {
            mpinfo = rc.senseMapInfo(m16);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet16 = mpinfo.isWater();
                movable16 = true;
            }
        }

        boolean movable10 = false;
        if (rc.canSenseLocation(m10)) {
            mpinfo = rc.senseMapInfo(m10);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet10 = mpinfo.isWater();
                movable10 = true;
            }
        }

        boolean movable4 = false;
        if (rc.canSenseLocation(m4)) {
            mpinfo = rc.senseMapInfo(m4);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet4 = mpinfo.isWater();
                movable4 = true;
            }
        }

        boolean movable5 = false;
        if (rc.canSenseLocation(m5)) {
            mpinfo = rc.senseMapInfo(m5);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet5 = mpinfo.isWater();
                movable5 = true;
            }
        }

        boolean movable3 = false;
        if (rc.canSenseLocation(m3)) {
            mpinfo = rc.senseMapInfo(m3);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet3 = mpinfo.isWater();
                movable3 = true;
            }
        }

        boolean movable6 = false;
        if (rc.canSenseLocation(m6)) {
            mpinfo = rc.senseMapInfo(m6);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet6 = mpinfo.isWater();
                movable6 = true;
            }
        }

        boolean movable2 = false;
        if (rc.canSenseLocation(m2)) {
            mpinfo = rc.senseMapInfo(m2);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet2 = mpinfo.isWater();
                movable2 = true;
            }
        }

        boolean movable44 = false;
        if (rc.canSenseLocation(m44)) {
            mpinfo = rc.senseMapInfo(m44);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet44 = mpinfo.isWater();
                movable44 = true;
            }
        }

        boolean movable53 = false;
        if (rc.canSenseLocation(m53)) {
            mpinfo = rc.senseMapInfo(m53);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet53 = mpinfo.isWater();
                movable53 = true;
            }
        }

        boolean movable35 = false;
        if (rc.canSenseLocation(m35)) {
            mpinfo = rc.senseMapInfo(m35);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet35 = mpinfo.isWater();
                movable35 = true;
            }
        }

        boolean movable62 = false;
        if (rc.canSenseLocation(m62)) {
            mpinfo = rc.senseMapInfo(m62);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet62 = mpinfo.isWater();
                movable62 = true;
            }
        }

        boolean movable26 = false;
        if (rc.canSenseLocation(m26)) {
            mpinfo = rc.senseMapInfo(m26);
            if (!mpinfo.isDam() && !mpinfo.isWall()) {
                wet26 = mpinfo.isWater();
                movable26 = true;
            }
        }

        if (ret40 != 10000) {
            if (movable31) {
                if (wet31) {
                    if (ret31 > 2 + ret40) {
                        d31 = Direction.NORTH;
                        ret31 = 2 + ret40;
                    }
                } else {
                    if (ret31 > 1 + ret40) {
                        d31 = Direction.NORTH;
                        ret31 = 1 + ret40;
                    }
                }
            }

            if (movable41) {
                if (wet41) {
                    if (ret41 > 2 + ret40) {
                        d41 = Direction.EAST;
                        ret41 = 2 + ret40;
                    }
                } else {
                    if (ret41 > 1 + ret40) {
                        d41 = Direction.EAST;
                        ret41 = 1 + ret40;
                    }
                }
            }

            if (movable50) {
                if (wet50) {
                    if (ret50 > 3 + ret40) {
                        d50 = Direction.SOUTHEAST;
                        ret50 = 3 + ret40;
                    }
                } else {
                    if (ret50 > 2 + ret40) {
                        d50 = Direction.SOUTHEAST;
                        ret50 = 2 + ret40;
                    }
                }
            }

            if (movable32) {
                if (wet32) {
                    if (ret32 > 3 + ret40) {
                        d32 = Direction.NORTHEAST;
                        ret32 = 3 + ret40;
                    }
                } else {
                    if (ret32 > 2 + ret40) {
                        d32 = Direction.NORTHEAST;
                        ret32 = 2 + ret40;
                    }
                }
            }

            if (movable30) {
                if (wet30) {
                    if (ret30 > 3 + ret40) {
                        d30 = Direction.NORTHWEST;
                        ret30 = 3 + ret40;
                    }
                } else {
                    if (ret30 > 2 + ret40) {
                        d30 = Direction.NORTHWEST;
                        ret30 = 2 + ret40;
                    }
                }
            }

        }
        if (ret31 != 10000) {
            if (movable22) {
                if (wet22) {
                    if (ret22 > 2 + ret31) {
                        d22 = d31;
                        ret22 = 2 + ret31;
                    }
                } else {
                    if (ret22 > 1 + ret31) {
                        d22 = d31;
                        ret22 = 1 + ret31;
                    }
                }
            }

            if (movable32) {
                if (wet32) {
                    if (ret32 > 2 + ret31) {
                        d32 = d31;
                        ret32 = 2 + ret31;
                    }
                } else {
                    if (ret32 > 1 + ret31) {
                        d32 = d31;
                        ret32 = 1 + ret31;
                    }
                }
            }

            if (movable41) {
                if (wet41) {
                    if (ret41 > 2 + ret31) {
                        d41 = d31;
                        ret41 = 2 + ret31;
                    }
                } else {
                    if (ret41 > 1 + ret31) {
                        d41 = d31;
                        ret41 = 1 + ret31;
                    }
                }
            }

            if (movable23) {
                if (wet23) {
                    if (ret23 > 2 + ret31) {
                        d23 = d31;
                        ret23 = 2 + ret31;
                    }
                } else {
                    if (ret23 > 1 + ret31) {
                        d23 = d31;
                        ret23 = 1 + ret31;
                    }
                }
            }

            if (movable21) {
                if (wet21) {
                    if (ret21 > 2 + ret31) {
                        d21 = d31;
                        ret21 = 2 + ret31;
                    }
                } else {
                    if (ret21 > 1 + ret31) {
                        d21 = d31;
                        ret21 = 1 + ret31;
                    }
                }
            }

        }
        if (ret41 != 10000) {
            if (movable32) {
                if (wet32) {
                    if (ret32 > 2 + ret41) {
                        d32 = d41;
                        ret32 = 2 + ret41;
                    }
                } else {
                    if (ret32 > 1 + ret41) {
                        d32 = d41;
                        ret32 = 1 + ret41;
                    }
                }
            }

            if (movable42) {
                if (wet42) {
                    if (ret42 > 2 + ret41) {
                        d42 = d41;
                        ret42 = 2 + ret41;
                    }
                } else {
                    if (ret42 > 1 + ret41) {
                        d42 = d41;
                        ret42 = 1 + ret41;
                    }
                }
            }

            if (movable51) {
                if (wet51) {
                    if (ret51 > 2 + ret41) {
                        d51 = d41;
                        ret51 = 2 + ret41;
                    }
                } else {
                    if (ret51 > 1 + ret41) {
                        d51 = d41;
                        ret51 = 1 + ret41;
                    }
                }
            }

            if (movable33) {
                if (wet33) {
                    if (ret33 > 2 + ret41) {
                        d33 = d41;
                        ret33 = 2 + ret41;
                    }
                } else {
                    if (ret33 > 1 + ret41) {
                        d33 = d41;
                        ret33 = 1 + ret41;
                    }
                }
            }

            if (movable31) {
                if (wet31) {
                    if (ret31 > 2 + ret41) {
                        d31 = d41;
                        ret31 = 2 + ret41;
                    }
                } else {
                    if (ret31 > 1 + ret41) {
                        d31 = d41;
                        ret31 = 1 + ret41;
                    }
                }
            }

        }
        if (ret50 != 10000) {
            if (movable41) {
                if (wet41) {
                    if (ret41 > 2 + ret50) {
                        d41 = d50;
                        ret41 = 2 + ret50;
                    }
                } else {
                    if (ret41 > 1 + ret50) {
                        d41 = d50;
                        ret41 = 1 + ret50;
                    }
                }
            }

            if (movable51) {
                if (wet51) {
                    if (ret51 > 2 + ret50) {
                        d51 = d50;
                        ret51 = 2 + ret50;
                    }
                } else {
                    if (ret51 > 1 + ret50) {
                        d51 = d50;
                        ret51 = 1 + ret50;
                    }
                }
            }

            if (movable60) {
                if (wet60) {
                    if (ret60 > 2 + ret50) {
                        d60 = d50;
                        ret60 = 2 + ret50;
                    }
                } else {
                    if (ret60 > 1 + ret50) {
                        d60 = d50;
                        ret60 = 1 + ret50;
                    }
                }
            }

            if (movable42) {
                if (wet42) {
                    if (ret42 > 2 + ret50) {
                        d42 = d50;
                        ret42 = 2 + ret50;
                    }
                } else {
                    if (ret42 > 1 + ret50) {
                        d42 = d50;
                        ret42 = 1 + ret50;
                    }
                }
            }

        }
        if (ret32 != 10000) {
            if (movable23) {
                if (wet23) {
                    if (ret23 > 2 + ret32) {
                        d23 = d32;
                        ret23 = 2 + ret32;
                    }
                } else {
                    if (ret23 > 1 + ret32) {
                        d23 = d32;
                        ret23 = 1 + ret32;
                    }
                }
            }

            if (movable33) {
                if (wet33) {
                    if (ret33 > 2 + ret32) {
                        d33 = d32;
                        ret33 = 2 + ret32;
                    }
                } else {
                    if (ret33 > 1 + ret32) {
                        d33 = d32;
                        ret33 = 1 + ret32;
                    }
                }
            }

            if (movable42) {
                if (wet42) {
                    if (ret42 > 2 + ret32) {
                        d42 = d32;
                        ret42 = 2 + ret32;
                    }
                } else {
                    if (ret42 > 1 + ret32) {
                        d42 = d32;
                        ret42 = 1 + ret32;
                    }
                }
            }

            if (movable24) {
                if (wet24) {
                    if (ret24 > 2 + ret32) {
                        d24 = d32;
                        ret24 = 2 + ret32;
                    }
                } else {
                    if (ret24 > 1 + ret32) {
                        d24 = d32;
                        ret24 = 1 + ret32;
                    }
                }
            }

            if (movable22) {
                if (wet22) {
                    if (ret22 > 2 + ret32) {
                        d22 = d32;
                        ret22 = 2 + ret32;
                    }
                } else {
                    if (ret22 > 1 + ret32) {
                        d22 = d32;
                        ret22 = 1 + ret32;
                    }
                }
            }

        }
        if (ret30 != 10000) {
            if (movable21) {
                if (wet21) {
                    if (ret21 > 2 + ret30) {
                        d21 = d30;
                        ret21 = 2 + ret30;
                    }
                } else {
                    if (ret21 > 1 + ret30) {
                        d21 = d30;
                        ret21 = 1 + ret30;
                    }
                }
            }

            if (movable31) {
                if (wet31) {
                    if (ret31 > 2 + ret30) {
                        d31 = d30;
                        ret31 = 2 + ret30;
                    }
                } else {
                    if (ret31 > 1 + ret30) {
                        d31 = d30;
                        ret31 = 1 + ret30;
                    }
                }
            }

            if (movable22) {
                if (wet22) {
                    if (ret22 > 2 + ret30) {
                        d22 = d30;
                        ret22 = 2 + ret30;
                    }
                } else {
                    if (ret22 > 1 + ret30) {
                        d22 = d30;
                        ret22 = 1 + ret30;
                    }
                }
            }

            if (movable20) {
                if (wet20) {
                    if (ret20 > 2 + ret30) {
                        d20 = d30;
                        ret20 = 2 + ret30;
                    }
                } else {
                    if (ret20 > 1 + ret30) {
                        d20 = d30;
                        ret20 = 1 + ret30;
                    }
                }
            }

        }
        if (ret22 != 10000) {
            if (movable13) {
                if (wet13) {
                    if (ret13 > 2 + ret22) {
                        d13 = d22;
                        ret13 = 2 + ret22;
                    }
                } else {
                    if (ret13 > 1 + ret22) {
                        d13 = d22;
                        ret13 = 1 + ret22;
                    }
                }
            }

            if (movable23) {
                if (wet23) {
                    if (ret23 > 2 + ret22) {
                        d23 = d22;
                        ret23 = 2 + ret22;
                    }
                } else {
                    if (ret23 > 1 + ret22) {
                        d23 = d22;
                        ret23 = 1 + ret22;
                    }
                }
            }

            if (movable32) {
                if (wet32) {
                    if (ret32 > 2 + ret22) {
                        d32 = d22;
                        ret32 = 2 + ret22;
                    }
                } else {
                    if (ret32 > 1 + ret22) {
                        d32 = d22;
                        ret32 = 1 + ret22;
                    }
                }
            }

            if (movable14) {
                if (wet14) {
                    if (ret14 > 2 + ret22) {
                        d14 = d22;
                        ret14 = 2 + ret22;
                    }
                } else {
                    if (ret14 > 1 + ret22) {
                        d14 = d22;
                        ret14 = 1 + ret22;
                    }
                }
            }

            if (movable12) {
                if (wet12) {
                    if (ret12 > 2 + ret22) {
                        d12 = d22;
                        ret12 = 2 + ret22;
                    }
                } else {
                    if (ret12 > 1 + ret22) {
                        d12 = d22;
                        ret12 = 1 + ret22;
                    }
                }
            }

        }
        if (ret23 != 10000) {
            if (movable14) {
                if (wet14) {
                    if (ret14 > 2 + ret23) {
                        d14 = d23;
                        ret14 = 2 + ret23;
                    }
                } else {
                    if (ret14 > 1 + ret23) {
                        d14 = d23;
                        ret14 = 1 + ret23;
                    }
                }
            }

            if (movable24) {
                if (wet24) {
                    if (ret24 > 2 + ret23) {
                        d24 = d23;
                        ret24 = 2 + ret23;
                    }
                } else {
                    if (ret24 > 1 + ret23) {
                        d24 = d23;
                        ret24 = 1 + ret23;
                    }
                }
            }

            if (movable33) {
                if (wet33) {
                    if (ret33 > 2 + ret23) {
                        d33 = d23;
                        ret33 = 2 + ret23;
                    }
                } else {
                    if (ret33 > 1 + ret23) {
                        d33 = d23;
                        ret33 = 1 + ret23;
                    }
                }
            }

            if (movable15) {
                if (wet15) {
                    if (ret15 > 2 + ret23) {
                        d15 = d23;
                        ret15 = 2 + ret23;
                    }
                } else {
                    if (ret15 > 1 + ret23) {
                        d15 = d23;
                        ret15 = 1 + ret23;
                    }
                }
            }

            if (movable13) {
                if (wet13) {
                    if (ret13 > 2 + ret23) {
                        d13 = d23;
                        ret13 = 2 + ret23;
                    }
                } else {
                    if (ret13 > 1 + ret23) {
                        d13 = d23;
                        ret13 = 1 + ret23;
                    }
                }
            }

        }
        if (ret21 != 10000) {
            if (movable12) {
                if (wet12) {
                    if (ret12 > 2 + ret21) {
                        d12 = d21;
                        ret12 = 2 + ret21;
                    }
                } else {
                    if (ret12 > 1 + ret21) {
                        d12 = d21;
                        ret12 = 1 + ret21;
                    }
                }
            }

            if (movable22) {
                if (wet22) {
                    if (ret22 > 2 + ret21) {
                        d22 = d21;
                        ret22 = 2 + ret21;
                    }
                } else {
                    if (ret22 > 1 + ret21) {
                        d22 = d21;
                        ret22 = 1 + ret21;
                    }
                }
            }

            if (movable31) {
                if (wet31) {
                    if (ret31 > 2 + ret21) {
                        d31 = d21;
                        ret31 = 2 + ret21;
                    }
                } else {
                    if (ret31 > 1 + ret21) {
                        d31 = d21;
                        ret31 = 1 + ret21;
                    }
                }
            }

            if (movable13) {
                if (wet13) {
                    if (ret13 > 2 + ret21) {
                        d13 = d21;
                        ret13 = 2 + ret21;
                    }
                } else {
                    if (ret13 > 1 + ret21) {
                        d13 = d21;
                        ret13 = 1 + ret21;
                    }
                }
            }

            if (movable11) {
                if (wet11) {
                    if (ret11 > 2 + ret21) {
                        d11 = d21;
                        ret11 = 2 + ret21;
                    }
                } else {
                    if (ret11 > 1 + ret21) {
                        d11 = d21;
                        ret11 = 1 + ret21;
                    }
                }
            }

        }
        if (ret42 != 10000) {
            if (movable33) {
                if (wet33) {
                    if (ret33 > 2 + ret42) {
                        d33 = d42;
                        ret33 = 2 + ret42;
                    }
                } else {
                    if (ret33 > 1 + ret42) {
                        d33 = d42;
                        ret33 = 1 + ret42;
                    }
                }
            }

            if (movable43) {
                if (wet43) {
                    if (ret43 > 2 + ret42) {
                        d43 = d42;
                        ret43 = 2 + ret42;
                    }
                } else {
                    if (ret43 > 1 + ret42) {
                        d43 = d42;
                        ret43 = 1 + ret42;
                    }
                }
            }

            if (movable52) {
                if (wet52) {
                    if (ret52 > 2 + ret42) {
                        d52 = d42;
                        ret52 = 2 + ret42;
                    }
                } else {
                    if (ret52 > 1 + ret42) {
                        d52 = d42;
                        ret52 = 1 + ret42;
                    }
                }
            }

            if (movable34) {
                if (wet34) {
                    if (ret34 > 2 + ret42) {
                        d34 = d42;
                        ret34 = 2 + ret42;
                    }
                } else {
                    if (ret34 > 1 + ret42) {
                        d34 = d42;
                        ret34 = 1 + ret42;
                    }
                }
            }

            if (movable32) {
                if (wet32) {
                    if (ret32 > 2 + ret42) {
                        d32 = d42;
                        ret32 = 2 + ret42;
                    }
                } else {
                    if (ret32 > 1 + ret42) {
                        d32 = d42;
                        ret32 = 1 + ret42;
                    }
                }
            }

        }
        if (ret51 != 10000) {
            if (movable42) {
                if (wet42) {
                    if (ret42 > 2 + ret51) {
                        d42 = d51;
                        ret42 = 2 + ret51;
                    }
                } else {
                    if (ret42 > 1 + ret51) {
                        d42 = d51;
                        ret42 = 1 + ret51;
                    }
                }
            }

            if (movable52) {
                if (wet52) {
                    if (ret52 > 2 + ret51) {
                        d52 = d51;
                        ret52 = 2 + ret51;
                    }
                } else {
                    if (ret52 > 1 + ret51) {
                        d52 = d51;
                        ret52 = 1 + ret51;
                    }
                }
            }

            if (movable61) {
                if (wet61) {
                    if (ret61 > 2 + ret51) {
                        d61 = d51;
                        ret61 = 2 + ret51;
                    }
                } else {
                    if (ret61 > 1 + ret51) {
                        d61 = d51;
                        ret61 = 1 + ret51;
                    }
                }
            }

            if (movable43) {
                if (wet43) {
                    if (ret43 > 2 + ret51) {
                        d43 = d51;
                        ret43 = 2 + ret51;
                    }
                } else {
                    if (ret43 > 1 + ret51) {
                        d43 = d51;
                        ret43 = 1 + ret51;
                    }
                }
            }

            if (movable41) {
                if (wet41) {
                    if (ret41 > 2 + ret51) {
                        d41 = d51;
                        ret41 = 2 + ret51;
                    }
                } else {
                    if (ret41 > 1 + ret51) {
                        d41 = d51;
                        ret41 = 1 + ret51;
                    }
                }
            }

        }
        if (ret33 != 10000) {
            if (movable24) {
                if (wet24) {
                    if (ret24 > 2 + ret33) {
                        d24 = d33;
                        ret24 = 2 + ret33;
                    }
                } else {
                    if (ret24 > 1 + ret33) {
                        d24 = d33;
                        ret24 = 1 + ret33;
                    }
                }
            }

            if (movable34) {
                if (wet34) {
                    if (ret34 > 2 + ret33) {
                        d34 = d33;
                        ret34 = 2 + ret33;
                    }
                } else {
                    if (ret34 > 1 + ret33) {
                        d34 = d33;
                        ret34 = 1 + ret33;
                    }
                }
            }

            if (movable43) {
                if (wet43) {
                    if (ret43 > 2 + ret33) {
                        d43 = d33;
                        ret43 = 2 + ret33;
                    }
                } else {
                    if (ret43 > 1 + ret33) {
                        d43 = d33;
                        ret43 = 1 + ret33;
                    }
                }
            }

            if (movable25) {
                if (wet25) {
                    if (ret25 > 2 + ret33) {
                        d25 = d33;
                        ret25 = 2 + ret33;
                    }
                } else {
                    if (ret25 > 1 + ret33) {
                        d25 = d33;
                        ret25 = 1 + ret33;
                    }
                }
            }

            if (movable23) {
                if (wet23) {
                    if (ret23 > 2 + ret33) {
                        d23 = d33;
                        ret23 = 2 + ret33;
                    }
                } else {
                    if (ret23 > 1 + ret33) {
                        d23 = d33;
                        ret23 = 1 + ret33;
                    }
                }
            }

        }
        if (ret60 != 10000) {
            if (movable51) {
                if (wet51) {
                    if (ret51 > 2 + ret60) {
                        d51 = d60;
                        ret51 = 2 + ret60;
                    }
                } else {
                    if (ret51 > 1 + ret60) {
                        d51 = d60;
                        ret51 = 1 + ret60;
                    }
                }
            }

            if (movable61) {
                if (wet61) {
                    if (ret61 > 2 + ret60) {
                        d61 = d60;
                        ret61 = 2 + ret60;
                    }
                } else {
                    if (ret61 > 1 + ret60) {
                        d61 = d60;
                        ret61 = 1 + ret60;
                    }
                }
            }

            if (movable70) {
                if (wet70) {
                    if (ret70 > 2 + ret60) {
                        d70 = d60;
                        ret70 = 2 + ret60;
                    }
                } else {
                    if (ret70 > 1 + ret60) {
                        d70 = d60;
                        ret70 = 1 + ret60;
                    }
                }
            }

            if (movable52) {
                if (wet52) {
                    if (ret52 > 2 + ret60) {
                        d52 = d60;
                        ret52 = 2 + ret60;
                    }
                } else {
                    if (ret52 > 1 + ret60) {
                        d52 = d60;
                        ret52 = 1 + ret60;
                    }
                }
            }

            if (movable50) {
                if (wet50) {
                    if (ret50 > 2 + ret60) {
                        d50 = d60;
                        ret50 = 2 + ret60;
                    }
                } else {
                    if (ret50 > 1 + ret60) {
                        d50 = d60;
                        ret50 = 1 + ret60;
                    }
                }
            }

        }
        if (ret24 != 10000) {
            if (movable15) {
                if (wet15) {
                    if (ret15 > 2 + ret24) {
                        d15 = d24;
                        ret15 = 2 + ret24;
                    }
                } else {
                    if (ret15 > 1 + ret24) {
                        d15 = d24;
                        ret15 = 1 + ret24;
                    }
                }
            }

            if (movable25) {
                if (wet25) {
                    if (ret25 > 2 + ret24) {
                        d25 = d24;
                        ret25 = 2 + ret24;
                    }
                } else {
                    if (ret25 > 1 + ret24) {
                        d25 = d24;
                        ret25 = 1 + ret24;
                    }
                }
            }

            if (movable34) {
                if (wet34) {
                    if (ret34 > 2 + ret24) {
                        d34 = d24;
                        ret34 = 2 + ret24;
                    }
                } else {
                    if (ret34 > 1 + ret24) {
                        d34 = d24;
                        ret34 = 1 + ret24;
                    }
                }
            }

            if (movable16) {
                if (wet16) {
                    if (ret16 > 2 + ret24) {
                        d16 = d24;
                        ret16 = 2 + ret24;
                    }
                } else {
                    if (ret16 > 1 + ret24) {
                        d16 = d24;
                        ret16 = 1 + ret24;
                    }
                }
            }

            if (movable14) {
                if (wet14) {
                    if (ret14 > 2 + ret24) {
                        d14 = d24;
                        ret14 = 2 + ret24;
                    }
                } else {
                    if (ret14 > 1 + ret24) {
                        d14 = d24;
                        ret14 = 1 + ret24;
                    }
                }
            }

        }
        if (ret20 != 10000) {
            if (movable11) {
                if (wet11) {
                    if (ret11 > 2 + ret20) {
                        d11 = d20;
                        ret11 = 2 + ret20;
                    }
                } else {
                    if (ret11 > 1 + ret20) {
                        d11 = d20;
                        ret11 = 1 + ret20;
                    }
                }
            }

            if (movable21) {
                if (wet21) {
                    if (ret21 > 2 + ret20) {
                        d21 = d20;
                        ret21 = 2 + ret20;
                    }
                } else {
                    if (ret21 > 1 + ret20) {
                        d21 = d20;
                        ret21 = 1 + ret20;
                    }
                }
            }

            if (movable30) {
                if (wet30) {
                    if (ret30 > 2 + ret20) {
                        d30 = d20;
                        ret30 = 2 + ret20;
                    }
                } else {
                    if (ret30 > 1 + ret20) {
                        d30 = d20;
                        ret30 = 1 + ret20;
                    }
                }
            }

            if (movable12) {
                if (wet12) {
                    if (ret12 > 2 + ret20) {
                        d12 = d20;
                        ret12 = 2 + ret20;
                    }
                } else {
                    if (ret12 > 1 + ret20) {
                        d12 = d20;
                        ret12 = 1 + ret20;
                    }
                }
            }

            if (movable10) {
                if (wet10) {
                    if (ret10 > 2 + ret20) {
                        d10 = d20;
                        ret10 = 2 + ret20;
                    }
                } else {
                    if (ret10 > 1 + ret20) {
                        d10 = d20;
                        ret10 = 1 + ret20;
                    }
                }
            }

        }
        if (ret13 != 10000) {
            if (movable4) {
                if (wet4) {
                    if (ret4 > 2 + ret13) {
                        d4 = d13;
                        ret4 = 2 + ret13;
                    }
                } else {
                    if (ret4 > 1 + ret13) {
                        d4 = d13;
                        ret4 = 1 + ret13;
                    }
                }
            }

            if (movable14) {
                if (wet14) {
                    if (ret14 > 2 + ret13) {
                        d14 = d13;
                        ret14 = 2 + ret13;
                    }
                } else {
                    if (ret14 > 1 + ret13) {
                        d14 = d13;
                        ret14 = 1 + ret13;
                    }
                }
            }

            if (movable23) {
                if (wet23) {
                    if (ret23 > 2 + ret13) {
                        d23 = d13;
                        ret23 = 2 + ret13;
                    }
                } else {
                    if (ret23 > 1 + ret13) {
                        d23 = d13;
                        ret23 = 1 + ret13;
                    }
                }
            }

            if (movable5) {
                if (wet5) {
                    if (ret5 > 2 + ret13) {
                        d5 = d13;
                        ret5 = 2 + ret13;
                    }
                } else {
                    if (ret5 > 1 + ret13) {
                        d5 = d13;
                        ret5 = 1 + ret13;
                    }
                }
            }

            if (movable3) {
                if (wet3) {
                    if (ret3 > 2 + ret13) {
                        d3 = d13;
                        ret3 = 2 + ret13;
                    }
                } else {
                    if (ret3 > 1 + ret13) {
                        d3 = d13;
                        ret3 = 1 + ret13;
                    }
                }
            }

        }
        if (ret14 != 10000) {
            if (movable5) {
                if (wet5) {
                    if (ret5 > 2 + ret14) {
                        d5 = d14;
                        ret5 = 2 + ret14;
                    }
                } else {
                    if (ret5 > 1 + ret14) {
                        d5 = d14;
                        ret5 = 1 + ret14;
                    }
                }
            }

            if (movable15) {
                if (wet15) {
                    if (ret15 > 2 + ret14) {
                        d15 = d14;
                        ret15 = 2 + ret14;
                    }
                } else {
                    if (ret15 > 1 + ret14) {
                        d15 = d14;
                        ret15 = 1 + ret14;
                    }
                }
            }

            if (movable24) {
                if (wet24) {
                    if (ret24 > 2 + ret14) {
                        d24 = d14;
                        ret24 = 2 + ret14;
                    }
                } else {
                    if (ret24 > 1 + ret14) {
                        d24 = d14;
                        ret24 = 1 + ret14;
                    }
                }
            }

            if (movable6) {
                if (wet6) {
                    if (ret6 > 2 + ret14) {
                        d6 = d14;
                        ret6 = 2 + ret14;
                    }
                } else {
                    if (ret6 > 1 + ret14) {
                        d6 = d14;
                        ret6 = 1 + ret14;
                    }
                }
            }

            if (movable4) {
                if (wet4) {
                    if (ret4 > 2 + ret14) {
                        d4 = d14;
                        ret4 = 2 + ret14;
                    }
                } else {
                    if (ret4 > 1 + ret14) {
                        d4 = d14;
                        ret4 = 1 + ret14;
                    }
                }
            }

        }
        if (ret12 != 10000) {
            if (movable3) {
                if (wet3) {
                    if (ret3 > 2 + ret12) {
                        d3 = d12;
                        ret3 = 2 + ret12;
                    }
                } else {
                    if (ret3 > 1 + ret12) {
                        d3 = d12;
                        ret3 = 1 + ret12;
                    }
                }
            }

            if (movable13) {
                if (wet13) {
                    if (ret13 > 2 + ret12) {
                        d13 = d12;
                        ret13 = 2 + ret12;
                    }
                } else {
                    if (ret13 > 1 + ret12) {
                        d13 = d12;
                        ret13 = 1 + ret12;
                    }
                }
            }

            if (movable22) {
                if (wet22) {
                    if (ret22 > 2 + ret12) {
                        d22 = d12;
                        ret22 = 2 + ret12;
                    }
                } else {
                    if (ret22 > 1 + ret12) {
                        d22 = d12;
                        ret22 = 1 + ret12;
                    }
                }
            }

            if (movable4) {
                if (wet4) {
                    if (ret4 > 2 + ret12) {
                        d4 = d12;
                        ret4 = 2 + ret12;
                    }
                } else {
                    if (ret4 > 1 + ret12) {
                        d4 = d12;
                        ret4 = 1 + ret12;
                    }
                }
            }

            if (movable2) {
                if (wet2) {
                    if (ret2 > 2 + ret12) {
                        d2 = d12;
                        ret2 = 2 + ret12;
                    }
                } else {
                    if (ret2 > 1 + ret12) {
                        d2 = d12;
                        ret2 = 1 + ret12;
                    }
                }
            }

        }
        if (ret15 != 10000) {
            if (movable6) {
                if (wet6) {
                    if (ret6 > 2 + ret15) {
                        d6 = d15;
                        ret6 = 2 + ret15;
                    }
                } else {
                    if (ret6 > 1 + ret15) {
                        d6 = d15;
                        ret6 = 1 + ret15;
                    }
                }
            }

            if (movable16) {
                if (wet16) {
                    if (ret16 > 2 + ret15) {
                        d16 = d15;
                        ret16 = 2 + ret15;
                    }
                } else {
                    if (ret16 > 1 + ret15) {
                        d16 = d15;
                        ret16 = 1 + ret15;
                    }
                }
            }

            if (movable25) {
                if (wet25) {
                    if (ret25 > 2 + ret15) {
                        d25 = d15;
                        ret25 = 2 + ret15;
                    }
                } else {
                    if (ret25 > 1 + ret15) {
                        d25 = d15;
                        ret25 = 1 + ret15;
                    }
                }
            }

            if (movable5) {
                if (wet5) {
                    if (ret5 > 2 + ret15) {
                        d5 = d15;
                        ret5 = 2 + ret15;
                    }
                } else {
                    if (ret5 > 1 + ret15) {
                        d5 = d15;
                        ret5 = 1 + ret15;
                    }
                }
            }

        }
        if (ret11 != 10000) {
            if (movable2) {
                if (wet2) {
                    if (ret2 > 2 + ret11) {
                        d2 = d11;
                        ret2 = 2 + ret11;
                    }
                } else {
                    if (ret2 > 1 + ret11) {
                        d2 = d11;
                        ret2 = 1 + ret11;
                    }
                }
            }

            if (movable12) {
                if (wet12) {
                    if (ret12 > 2 + ret11) {
                        d12 = d11;
                        ret12 = 2 + ret11;
                    }
                } else {
                    if (ret12 > 1 + ret11) {
                        d12 = d11;
                        ret12 = 1 + ret11;
                    }
                }
            }

            if (movable21) {
                if (wet21) {
                    if (ret21 > 2 + ret11) {
                        d21 = d11;
                        ret21 = 2 + ret11;
                    }
                } else {
                    if (ret21 > 1 + ret11) {
                        d21 = d11;
                        ret21 = 1 + ret11;
                    }
                }
            }

            if (movable3) {
                if (wet3) {
                    if (ret3 > 2 + ret11) {
                        d3 = d11;
                        ret3 = 2 + ret11;
                    }
                } else {
                    if (ret3 > 1 + ret11) {
                        d3 = d11;
                        ret3 = 1 + ret11;
                    }
                }
            }

        }
        if (ret43 != 10000) {
            if (movable34) {
                if (wet34) {
                    if (ret34 > 2 + ret43) {
                        d34 = d43;
                        ret34 = 2 + ret43;
                    }
                } else {
                    if (ret34 > 1 + ret43) {
                        d34 = d43;
                        ret34 = 1 + ret43;
                    }
                }
            }

            if (movable44) {
                if (wet44) {
                    if (ret44 > 2 + ret43) {
                        d44 = d43;
                        ret44 = 2 + ret43;
                    }
                } else {
                    if (ret44 > 1 + ret43) {
                        d44 = d43;
                        ret44 = 1 + ret43;
                    }
                }
            }

            if (movable53) {
                if (wet53) {
                    if (ret53 > 2 + ret43) {
                        d53 = d43;
                        ret53 = 2 + ret43;
                    }
                } else {
                    if (ret53 > 1 + ret43) {
                        d53 = d43;
                        ret53 = 1 + ret43;
                    }
                }
            }

            if (movable35) {
                if (wet35) {
                    if (ret35 > 2 + ret43) {
                        d35 = d43;
                        ret35 = 2 + ret43;
                    }
                } else {
                    if (ret35 > 1 + ret43) {
                        d35 = d43;
                        ret35 = 1 + ret43;
                    }
                }
            }

            if (movable33) {
                if (wet33) {
                    if (ret33 > 2 + ret43) {
                        d33 = d43;
                        ret33 = 2 + ret43;
                    }
                } else {
                    if (ret33 > 1 + ret43) {
                        d33 = d43;
                        ret33 = 1 + ret43;
                    }
                }
            }

        }
        if (ret52 != 10000) {
            if (movable43) {
                if (wet43) {
                    if (ret43 > 2 + ret52) {
                        d43 = d52;
                        ret43 = 2 + ret52;
                    }
                } else {
                    if (ret43 > 1 + ret52) {
                        d43 = d52;
                        ret43 = 1 + ret52;
                    }
                }
            }

            if (movable53) {
                if (wet53) {
                    if (ret53 > 2 + ret52) {
                        d53 = d52;
                        ret53 = 2 + ret52;
                    }
                } else {
                    if (ret53 > 1 + ret52) {
                        d53 = d52;
                        ret53 = 1 + ret52;
                    }
                }
            }

            if (movable62) {
                if (wet62) {
                    if (ret62 > 2 + ret52) {
                        d62 = d52;
                        ret62 = 2 + ret52;
                    }
                } else {
                    if (ret62 > 1 + ret52) {
                        d62 = d52;
                        ret62 = 1 + ret52;
                    }
                }
            }

            if (movable44) {
                if (wet44) {
                    if (ret44 > 2 + ret52) {
                        d44 = d52;
                        ret44 = 2 + ret52;
                    }
                } else {
                    if (ret44 > 1 + ret52) {
                        d44 = d52;
                        ret44 = 1 + ret52;
                    }
                }
            }

            if (movable42) {
                if (wet42) {
                    if (ret42 > 2 + ret52) {
                        d42 = d52;
                        ret42 = 2 + ret52;
                    }
                } else {
                    if (ret42 > 1 + ret52) {
                        d42 = d52;
                        ret42 = 1 + ret52;
                    }
                }
            }

        }
        if (ret34 != 10000) {
            if (movable25) {
                if (wet25) {
                    if (ret25 > 2 + ret34) {
                        d25 = d34;
                        ret25 = 2 + ret34;
                    }
                } else {
                    if (ret25 > 1 + ret34) {
                        d25 = d34;
                        ret25 = 1 + ret34;
                    }
                }
            }

            if (movable35) {
                if (wet35) {
                    if (ret35 > 2 + ret34) {
                        d35 = d34;
                        ret35 = 2 + ret34;
                    }
                } else {
                    if (ret35 > 1 + ret34) {
                        d35 = d34;
                        ret35 = 1 + ret34;
                    }
                }
            }

            if (movable44) {
                if (wet44) {
                    if (ret44 > 2 + ret34) {
                        d44 = d34;
                        ret44 = 2 + ret34;
                    }
                } else {
                    if (ret44 > 1 + ret34) {
                        d44 = d34;
                        ret44 = 1 + ret34;
                    }
                }
            }

            if (movable26) {
                if (wet26) {
                    if (ret26 > 2 + ret34) {
                        d26 = d34;
                        ret26 = 2 + ret34;
                    }
                } else {
                    if (ret26 > 1 + ret34) {
                        d26 = d34;
                        ret26 = 1 + ret34;
                    }
                }
            }

            if (movable24) {
                if (wet24) {
                    if (ret24 > 2 + ret34) {
                        d24 = d34;
                        ret24 = 2 + ret34;
                    }
                } else {
                    if (ret24 > 1 + ret34) {
                        d24 = d34;
                        ret24 = 1 + ret34;
                    }
                }
            }

        }
        if (ret61 != 10000) {
            if (movable52) {
                if (wet52) {
                    if (ret52 > 2 + ret61) {
                        d52 = d61;
                        ret52 = 2 + ret61;
                    }
                } else {
                    if (ret52 > 1 + ret61) {
                        d52 = d61;
                        ret52 = 1 + ret61;
                    }
                }
            }

            if (movable62) {
                if (wet62) {
                    if (ret62 > 2 + ret61) {
                        d62 = d61;
                        ret62 = 2 + ret61;
                    }
                } else {
                    if (ret62 > 1 + ret61) {
                        d62 = d61;
                        ret62 = 1 + ret61;
                    }
                }
            }

            if (movable53) {
                if (wet53) {
                    if (ret53 > 2 + ret61) {
                        d53 = d61;
                        ret53 = 2 + ret61;
                    }
                } else {
                    if (ret53 > 1 + ret61) {
                        d53 = d61;
                        ret53 = 1 + ret61;
                    }
                }
            }

            if (movable51) {
                if (wet51) {
                    if (ret51 > 2 + ret61) {
                        d51 = d61;
                        ret51 = 2 + ret61;
                    }
                } else {
                    if (ret51 > 1 + ret61) {
                        d51 = d61;
                        ret51 = 1 + ret61;
                    }
                }
            }

        }
        if (ret25 != 10000) {
            if (movable16) {
                if (wet16) {
                    if (ret16 > 2 + ret25) {
                        d16 = d25;
                        ret16 = 2 + ret25;
                    }
                } else {
                    if (ret16 > 1 + ret25) {
                        d16 = d25;
                        ret16 = 1 + ret25;
                    }
                }
            }

            if (movable26) {
                if (wet26) {
                    if (ret26 > 2 + ret25) {
                        d26 = d25;
                        ret26 = 2 + ret25;
                    }
                } else {
                    if (ret26 > 1 + ret25) {
                        d26 = d25;
                        ret26 = 1 + ret25;
                    }
                }
            }

            if (movable35) {
                if (wet35) {
                    if (ret35 > 2 + ret25) {
                        d35 = d25;
                        ret35 = 2 + ret25;
                    }
                } else {
                    if (ret35 > 1 + ret25) {
                        d35 = d25;
                        ret35 = 1 + ret25;
                    }
                }
            }

            if (movable15) {
                if (wet15) {
                    if (ret15 > 2 + ret25) {
                        d15 = d25;
                        ret15 = 2 + ret25;
                    }
                } else {
                    if (ret15 > 1 + ret25) {
                        d15 = d25;
                        ret15 = 1 + ret25;
                    }
                }
            }

        }
        if (ret70 != 10000) {
            if (movable61) {
                if (wet61) {
                    if (ret61 > 2 + ret70) {
                        d61 = d70;
                        ret61 = 2 + ret70;
                    }
                } else {
                    if (ret61 > 1 + ret70) {
                        d61 = d70;
                        ret61 = 1 + ret70;
                    }
                }
            }

            if (movable62) {
                if (wet62) {
                    if (ret62 > 2 + ret70) {
                        d62 = d70;
                        ret62 = 2 + ret70;
                    }
                } else {
                    if (ret62 > 1 + ret70) {
                        d62 = d70;
                        ret62 = 1 + ret70;
                    }
                }
            }

            if (movable60) {
                if (wet60) {
                    if (ret60 > 2 + ret70) {
                        d60 = d70;
                        ret60 = 2 + ret70;
                    }
                } else {
                    if (ret60 > 1 + ret70) {
                        d60 = d70;
                        ret60 = 1 + ret70;
                    }
                }
            }

        }
        if (ret16 != 10000) {
            if (movable26) {
                if (wet26) {
                    if (ret26 > 2 + ret16) {
                        d26 = d16;
                        ret26 = 2 + ret16;
                    }
                } else {
                    if (ret26 > 1 + ret16) {
                        d26 = d16;
                        ret26 = 1 + ret16;
                    }
                }
            }

            if (movable6) {
                if (wet6) {
                    if (ret6 > 2 + ret16) {
                        d6 = d16;
                        ret6 = 2 + ret16;
                    }
                } else {
                    if (ret6 > 1 + ret16) {
                        d6 = d16;
                        ret6 = 1 + ret16;
                    }
                }
            }

        }
        if (ret10 != 10000) {
            if (movable11) {
                if (wet11) {
                    if (ret11 > 2 + ret10) {
                        d11 = d10;
                        ret11 = 2 + ret10;
                    }
                } else {
                    if (ret11 > 1 + ret10) {
                        d11 = d10;
                        ret11 = 1 + ret10;
                    }
                }
            }

            if (movable20) {
                if (wet20) {
                    if (ret20 > 2 + ret10) {
                        d20 = d10;
                        ret20 = 2 + ret10;
                    }
                } else {
                    if (ret20 > 1 + ret10) {
                        d20 = d10;
                        ret20 = 1 + ret10;
                    }
                }
            }

            if (movable2) {
                if (wet2) {
                    if (ret2 > 2 + ret10) {
                        d2 = d10;
                        ret2 = 2 + ret10;
                    }
                } else {
                    if (ret2 > 1 + ret10) {
                        d2 = d10;
                        ret2 = 1 + ret10;
                    }
                }
            }

        }
        if (ret4 != 10000) {
            if (movable5) {
                if (wet5) {
                    if (ret5 > 2 + ret4) {
                        d5 = d4;
                        ret5 = 2 + ret4;
                    }
                } else {
                    if (ret5 > 1 + ret4) {
                        d5 = d4;
                        ret5 = 1 + ret4;
                    }
                }
            }

            if (movable14) {
                if (wet14) {
                    if (ret14 > 2 + ret4) {
                        d14 = d4;
                        ret14 = 2 + ret4;
                    }
                } else {
                    if (ret14 > 1 + ret4) {
                        d14 = d4;
                        ret14 = 1 + ret4;
                    }
                }
            }

        }
        if (ret5 != 10000) {
            if (movable6) {
                if (wet6) {
                    if (ret6 > 2 + ret5) {
                        d6 = d5;
                        ret6 = 2 + ret5;
                    }
                } else {
                    if (ret6 > 1 + ret5) {
                        d6 = d5;
                        ret6 = 1 + ret5;
                    }
                }
            }

            if (movable15) {
                if (wet15) {
                    if (ret15 > 2 + ret5) {
                        d15 = d5;
                        ret15 = 2 + ret5;
                    }
                } else {
                    if (ret15 > 1 + ret5) {
                        d15 = d5;
                        ret15 = 1 + ret5;
                    }
                }
            }

        }
        if (ret3 != 10000) {
            if (movable4) {
                if (wet4) {
                    if (ret4 > 2 + ret3) {
                        d4 = d3;
                        ret4 = 2 + ret3;
                    }
                } else {
                    if (ret4 > 1 + ret3) {
                        d4 = d3;
                        ret4 = 1 + ret3;
                    }
                }
            }

            if (movable13) {
                if (wet13) {
                    if (ret13 > 2 + ret3) {
                        d13 = d3;
                        ret13 = 2 + ret3;
                    }
                } else {
                    if (ret13 > 1 + ret3) {
                        d13 = d3;
                        ret13 = 1 + ret3;
                    }
                }
            }

        }
        if (ret6 != 10000) {
            if (movable16) {
                if (wet16) {
                    if (ret16 > 2 + ret6) {
                        d16 = d6;
                        ret16 = 2 + ret6;
                    }
                } else {
                    if (ret16 > 1 + ret6) {
                        d16 = d6;
                        ret16 = 1 + ret6;
                    }
                }
            }

        }
        if (ret2 != 10000) {
            if (movable3) {
                if (wet3) {
                    if (ret3 > 2 + ret2) {
                        d3 = d2;
                        ret3 = 2 + ret2;
                    }
                } else {
                    if (ret3 > 1 + ret2) {
                        d3 = d2;
                        ret3 = 1 + ret2;
                    }
                }
            }

            if (movable12) {
                if (wet12) {
                    if (ret12 > 2 + ret2) {
                        d12 = d2;
                        ret12 = 2 + ret2;
                    }
                } else {
                    if (ret12 > 1 + ret2) {
                        d12 = d2;
                        ret12 = 1 + ret2;
                    }
                }
            }

        }
        if (ret44 != 10000) {
            if (movable35) {
                if (wet35) {
                    if (ret35 > 2 + ret44) {
                        d35 = d44;
                        ret35 = 2 + ret44;
                    }
                } else {
                    if (ret35 > 1 + ret44) {
                        d35 = d44;
                        ret35 = 1 + ret44;
                    }
                }
            }

            if (movable34) {
                if (wet34) {
                    if (ret34 > 2 + ret44) {
                        d34 = d44;
                        ret34 = 2 + ret44;
                    }
                } else {
                    if (ret34 > 1 + ret44) {
                        d34 = d44;
                        ret34 = 1 + ret44;
                    }
                }
            }

        }
        if (ret53 != 10000) {
            if (movable44) {
                if (wet44) {
                    if (ret44 > 2 + ret53) {
                        d44 = d53;
                        ret44 = 2 + ret53;
                    }
                } else {
                    if (ret44 > 1 + ret53) {
                        d44 = d53;
                        ret44 = 1 + ret53;
                    }
                }
            }

            if (movable43) {
                if (wet43) {
                    if (ret43 > 2 + ret53) {
                        d43 = d53;
                        ret43 = 2 + ret53;
                    }
                } else {
                    if (ret43 > 1 + ret53) {
                        d43 = d53;
                        ret43 = 1 + ret53;
                    }
                }
            }

        }
        if (ret35 != 10000) {
            if (movable26) {
                if (wet26) {
                    if (ret26 > 2 + ret35) {
                        d26 = d35;
                        ret26 = 2 + ret35;
                    }
                } else {
                    if (ret26 > 1 + ret35) {
                        d26 = d35;
                        ret26 = 1 + ret35;
                    }
                }
            }

            if (movable25) {
                if (wet25) {
                    if (ret25 > 2 + ret35) {
                        d25 = d35;
                        ret25 = 2 + ret35;
                    }
                } else {
                    if (ret25 > 1 + ret35) {
                        d25 = d35;
                        ret25 = 1 + ret35;
                    }
                }
            }

        }
        if (ret62 != 10000) {
            if (movable53) {
                if (wet53) {
                    if (ret53 > 2 + ret62) {
                        d53 = d62;
                        ret53 = 2 + ret62;
                    }
                } else {
                    if (ret53 > 1 + ret62) {
                        d53 = d62;
                        ret53 = 1 + ret62;
                    }
                }
            }

            if (movable52) {
                if (wet52) {
                    if (ret52 > 2 + ret62) {
                        d52 = d62;
                        ret52 = 2 + ret62;
                    }
                } else {
                    if (ret52 > 1 + ret62) {
                        d52 = d62;
                        ret52 = 1 + ret62;
                    }
                }
            }

        }
        if (ret26 != 10000) {
            if (movable16) {
                if (wet16) {
                    if (ret16 > 2 + ret26) {
                        d16 = d26;
                        ret16 = 2 + ret26;
                    }
                } else {
                    if (ret16 > 1 + ret26) {
                        d16 = d26;
                        ret16 = 1 + ret26;
                    }
                }
            }

        }
        double initialDist = Math.sqrt(m40.distanceSquaredTo(target));
        Direction ans = Direction.CENTER;
        double cmax = 0;
        double dist30 = (initialDist - Math.sqrt(m30.distanceSquaredTo(target))) / (double) ret30;
        if (movable30 && dist30 > cmax) {
            cmax = dist30;
            ans = d30;
        }

        double dist31 = (initialDist - Math.sqrt(m31.distanceSquaredTo(target))) / (double) ret31;
        if (movable31 && dist31 > cmax) {
            cmax = dist31;
            ans = d31;
        }

        double dist32 = (initialDist - Math.sqrt(m32.distanceSquaredTo(target))) / (double) ret32;
        if (movable32 && dist32 > cmax) {
            cmax = dist32;
            ans = d32;
        }

        double dist41 = (initialDist - Math.sqrt(m41.distanceSquaredTo(target))) / (double) ret41;
        if (movable41 && dist41 > cmax) {
            cmax = dist41;
            ans = d41;
        }

        double dist50 = (initialDist - Math.sqrt(m50.distanceSquaredTo(target))) / (double) ret50;
        if (movable50 && dist50 > cmax) {
            cmax = dist50;
            ans = d50;
        }

        double dist20 = (initialDist - Math.sqrt(m20.distanceSquaredTo(target))) / (double) ret20;
        if (movable20 && dist20 > cmax) {
            cmax = dist20;
            ans = d20;
        }

        double dist21 = (initialDist - Math.sqrt(m21.distanceSquaredTo(target))) / (double) ret21;
        if (movable21 && dist21 > cmax) {
            cmax = dist21;
            ans = d21;
        }

        double dist22 = (initialDist - Math.sqrt(m22.distanceSquaredTo(target))) / (double) ret22;
        if (movable22 && dist22 > cmax) {
            cmax = dist22;
            ans = d22;
        }

        double dist23 = (initialDist - Math.sqrt(m23.distanceSquaredTo(target))) / (double) ret23;
        if (movable23 && dist23 > cmax) {
            cmax = dist23;
            ans = d23;
        }

        double dist24 = (initialDist - Math.sqrt(m24.distanceSquaredTo(target))) / (double) ret24;
        if (movable24 && dist24 > cmax) {
            cmax = dist24;
            ans = d24;
        }

        double dist33 = (initialDist - Math.sqrt(m33.distanceSquaredTo(target))) / (double) ret33;
        if (movable33 && dist33 > cmax) {
            cmax = dist33;
            ans = d33;
        }

        double dist42 = (initialDist - Math.sqrt(m42.distanceSquaredTo(target))) / (double) ret42;
        if (movable42 && dist42 > cmax) {
            cmax = dist42;
            ans = d42;
        }

        double dist51 = (initialDist - Math.sqrt(m51.distanceSquaredTo(target))) / (double) ret51;
        if (movable51 && dist51 > cmax) {
            cmax = dist51;
            ans = d51;
        }

        double dist60 = (initialDist - Math.sqrt(m60.distanceSquaredTo(target))) / (double) ret60;
        if (movable60 && dist60 > cmax) {
            cmax = dist60;
            ans = d60;
        }

        double dist10 = (initialDist - Math.sqrt(m10.distanceSquaredTo(target))) / (double) ret10;
        if (movable10 && dist10 > cmax) {
            cmax = dist10;
            ans = d10;
        }

        double dist11 = (initialDist - Math.sqrt(m11.distanceSquaredTo(target))) / (double) ret11;
        if (movable11 && dist11 > cmax) {
            cmax = dist11;
            ans = d11;
        }

        double dist12 = (initialDist - Math.sqrt(m12.distanceSquaredTo(target))) / (double) ret12;
        if (movable12 && dist12 > cmax) {
            cmax = dist12;
            ans = d12;
        }

        double dist13 = (initialDist - Math.sqrt(m13.distanceSquaredTo(target))) / (double) ret13;
        if (movable13 && dist13 > cmax) {
            cmax = dist13;
            ans = d13;
        }

        double dist14 = (initialDist - Math.sqrt(m14.distanceSquaredTo(target))) / (double) ret14;
        if (movable14 && dist14 > cmax) {
            cmax = dist14;
            ans = d14;
        }

        double dist15 = (initialDist - Math.sqrt(m15.distanceSquaredTo(target))) / (double) ret15;
        if (movable15 && dist15 > cmax) {
            cmax = dist15;
            ans = d15;
        }

        double dist16 = (initialDist - Math.sqrt(m16.distanceSquaredTo(target))) / (double) ret16;
        if (movable16 && dist16 > cmax) {
            cmax = dist16;
            ans = d16;
        }

        double dist25 = (initialDist - Math.sqrt(m25.distanceSquaredTo(target))) / (double) ret25;
        if (movable25 && dist25 > cmax) {
            cmax = dist25;
            ans = d25;
        }

        double dist34 = (initialDist - Math.sqrt(m34.distanceSquaredTo(target))) / (double) ret34;
        if (movable34 && dist34 > cmax) {
            cmax = dist34;
            ans = d34;
        }

        double dist43 = (initialDist - Math.sqrt(m43.distanceSquaredTo(target))) / (double) ret43;
        if (movable43 && dist43 > cmax) {
            cmax = dist43;
            ans = d43;
        }

        double dist52 = (initialDist - Math.sqrt(m52.distanceSquaredTo(target))) / (double) ret52;
        if (movable52 && dist52 > cmax) {
            cmax = dist52;
            ans = d52;
        }

        double dist61 = (initialDist - Math.sqrt(m61.distanceSquaredTo(target))) / (double) ret61;
        if (movable61 && dist61 > cmax) {
            cmax = dist61;
            ans = d61;
        }

        double dist70 = (initialDist - Math.sqrt(m70.distanceSquaredTo(target))) / (double) ret70;
        if (movable70 && dist70 > cmax) {
            cmax = dist70;
            ans = d70;
        }

        double dist2 = (initialDist - Math.sqrt(m2.distanceSquaredTo(target))) / (double) ret2;
        if (movable2 && dist2 > cmax) {
            cmax = dist2;
            ans = d2;
        }

        double dist3 = (initialDist - Math.sqrt(m3.distanceSquaredTo(target))) / (double) ret3;
        if (movable3 && dist3 > cmax) {
            cmax = dist3;
            ans = d3;
        }

        double dist4 = (initialDist - Math.sqrt(m4.distanceSquaredTo(target))) / (double) ret4;
        if (movable4 && dist4 > cmax) {
            cmax = dist4;
            ans = d4;
        }

        double dist5 = (initialDist - Math.sqrt(m5.distanceSquaredTo(target))) / (double) ret5;
        if (movable5 && dist5 > cmax) {
            cmax = dist5;
            ans = d5;
        }

        double dist6 = (initialDist - Math.sqrt(m6.distanceSquaredTo(target))) / (double) ret6;
        if (movable6 && dist6 > cmax) {
            cmax = dist6;
            ans = d6;
        }

        double dist26 = (initialDist - Math.sqrt(m26.distanceSquaredTo(target))) / (double) ret26;
        if (movable26 && dist26 > cmax) {
            cmax = dist26;
            ans = d26;
        }

        double dist35 = (initialDist - Math.sqrt(m35.distanceSquaredTo(target))) / (double) ret35;
        if (movable35 && dist35 > cmax) {
            cmax = dist35;
            ans = d35;
        }

        double dist44 = (initialDist - Math.sqrt(m44.distanceSquaredTo(target))) / (double) ret44;
        if (movable44 && dist44 > cmax) {
            cmax = dist44;
            ans = d44;
        }

        double dist53 = (initialDist - Math.sqrt(m53.distanceSquaredTo(target))) / (double) ret53;
        if (movable53 && dist53 > cmax) {
            cmax = dist53;
            ans = d53;
        }

        double dist62 = (initialDist - Math.sqrt(m62.distanceSquaredTo(target))) / (double) ret62;
        if (movable62 && dist62 > cmax) {
            cmax = dist62;
            ans = d62;
        }

        if (ans != null) {
            return ans;
        } else {
            return Direction.CENTER;
        }

    }
}