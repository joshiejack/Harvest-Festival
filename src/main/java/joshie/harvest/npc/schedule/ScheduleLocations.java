package joshie.harvest.npc.schedule;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.town.TownData;

import static joshie.harvest.buildings.HFBuildings.*;
import static joshie.harvest.town.TownData.*;

public class ScheduleLocations {
    public static final BuildingLocation POND = new BuildingLocation(FISHING_HOLE, FISHING_POND).withDistance(16D).withTime(1500L);
    static final BuildingLocation PONDBACK = new BuildingLocation(FISHING_HOLE, FISHING_POND_BACK).withDistance(20D).withTime(1500L);
    static final BuildingLocation PONDLEFT = new BuildingLocation(FISHING_HOLE, FISHING_POND_LEFT).withDistance(20D).withTime(1500L);
    static final BuildingLocation PONDRIGHT = new BuildingLocation(FISHING_HOLE, FISHING_POND_RIGHT).withDistance(20D).withTime(1500L);
    static final BuildingLocation GODDESS = new BuildingLocation(GODDESS_POND, GODDESS_HOME).withDistance(24D).withTime(1500L);
    static final BuildingLocation GODDESSBACKRIGHT = new BuildingLocation(GODDESS_POND, GODDESS_BACK_RIGHT).withDistance(24D).withTime(1500L);
    static final BuildingLocation GODDESSFRONT = new BuildingLocation(GODDESS_POND, GODDESS_HOME).withDistance(24D).withTime(1500L);
    static final BuildingLocation GODDESSFRONTLEFT = new BuildingLocation(GODDESS_POND, GODDESS_FRONT_LEFT).withDistance(24D).withTime(1500L);
    static final BuildingLocation GODDESSFRONTRIGHT = new BuildingLocation(GODDESS_POND, GODDESS_FRONT_RIGHT).withDistance(24D).withTime(1500L);
    static final BuildingLocation BARNBUILDING = new BuildingLocation(BARN, JIM_HOME).withDistance(8D);
    static final BuildingLocation BARNLEFT = new BuildingLocation(BARN, BARN_LEFT).withDistance(8D);
    static final BuildingLocation BARNDOOR = new BuildingLocation(BARN, BARN_DOOR).withDistance(20D);
    static final BuildingLocation BARNRIGHT = new BuildingLocation(BARN, BARN_RIGHT).withDistance(20D);
    static final BuildingLocation POULTRYBUILDING = new BuildingLocation(POULTRY_FARM, ASHLEE_HOME).withDistance(8D);
    static final BuildingLocation POULTRYDOOR = new BuildingLocation(POULTRY_FARM, POULTRY_DOOR).withDistance(20D);
    static final BuildingLocation TOWNHALLLEFT = new BuildingLocation(TOWNHALL, TOWNHALL_LEFT_WING).withDistance(10D);
    static final BuildingLocation TOWNHALLRIGHT = new BuildingLocation(TOWNHALL, TOWNHALL_RIGHT_WING).withDistance(10D);
    static final BuildingLocation TOWNHALLENTRANCE = new BuildingLocation(TOWNHALL, TOWNHALL_ENTRANCE).withDistance(10D);
    static final BuildingLocation TOWNHALLSTAGE = new BuildingLocation(TOWNHALL, JAMIE_HOME);
    static final BuildingLocation TOWNHALLTEEN = new BuildingLocation(TOWNHALL, TOWNHALL_TEEN_BEDROOM);
    static final BuildingLocation CHURCHINSIDE = new BuildingLocation(CHURCH, THOMAS).withDistance(10D).withTime(1000L);
    static final BuildingLocation CHURCHPEWFRONTLEFT = new BuildingLocation(CHURCH, CHURCH_PEW_FRONT_LEFT).withDistance(3D).withTime(1000L);
    static final BuildingLocation CHURCHPEWFRONTRIGHT = new BuildingLocation(CHURCH, CHURCH_PEW_FRONT_RIGHT).withDistance(3D).withTime(1000L);
    static final BuildingLocation CHURCHPEWCENTRE = new BuildingLocation(CHURCH, CHURCH_PEW_CENTRE).withDistance(3D).withTime(1000L);
    static final BuildingLocation CHURCHPEWBACKLEFT = new BuildingLocation(CHURCH, CHURCH_PEW_BACK_LEFT).withDistance(3D).withTime(1000L);
    static final BuildingLocation CHURCHPEWBACKTRIGHT = new BuildingLocation(CHURCH, CHURCH_PEW_BACK_RIGHT).withDistance(3D).withTime(1000L);
    static final BuildingLocation CHURCH_RIGHT = new BuildingLocation(CHURCH, TownData.CHURCH_RIGHT).withDistance(20D).withTime(1000L);
    static final BuildingLocation GENERALFRONT = new BuildingLocation(SUPERMARKET, MARKET_STOREFRONT).withDistance(16D).withTime(1500L);
    static final BuildingLocation GENERALTABLES = new BuildingLocation(SUPERMARKET, MARKET_TABLES).withDistance(16D).withTime(1500L);
    static final BuildingLocation GENERALGARDEN = new BuildingLocation(SUPERMARKET, MARKET_GARDEN).withDistance(16D).withTime(1500L);
    static final BuildingLocation GENERALBACKYARD = new BuildingLocation(SUPERMARKET, MARKET_BACKYARD).withDistance(24D).withTime(1500L);
    static final BuildingLocation GENERALBASEMENT = new BuildingLocation(SUPERMARKET, MARKET_BASEMENT).withDistance(16D).withTime(1500L);
    static final BuildingLocation CARPENTERFRONT = new BuildingLocation(CARPENTER, CARPENTER_DOOR).withDistance(24D).withTime(1500L);
    static final BuildingLocation CARPENTERDOWN = new BuildingLocation(CARPENTER, CARPENTER_DOWNSTAIRS).withDistance(3D);
    static final BuildingLocation CARPENTERUP = new BuildingLocation(CARPENTER, JADE_HOME).withDistance(3D);
    static final BuildingLocation CHURCHFRONT = new BuildingLocation(CHURCH, CHURCH_FRONT).withDistance(24D).withTime(1500L);
    static final BuildingLocation MINEENTRANCE = new BuildingLocation(MINING_HILL, MINE_ENTRANCE).withDistance(24D).withTime(1500L);
    static final BuildingLocation MINEHUTENTRANCE = new BuildingLocation(MINING_HUT, MINER_FRONT).withDistance(16D).withTime(1500L);
    static final BuildingLocation CAFEFRONT = new BuildingLocation(CAFE, CAFE_FRONT).withDistance(16D).withTime(1500L);
    static final BuildingLocation CAFECUSTOMER = new BuildingLocation(CAFE, CAFE_CUSTOMER).withDistance(16D).withTime(1500L);
    static final BuildingLocation CAFEBALCONY = new BuildingLocation(CAFE, LIARA_HOME).withDistance(16D).withTime(1500L);
    static final BuildingLocation CAFEKITCHEN = new BuildingLocation(CAFE, KATLIN_HOME).withDistance(16D).withTime(1500L);
    static final BuildingLocation BLACKSMITHFRONT = new BuildingLocation(BLACKSMITH, BLACKSMITH_FRONT).withDistance(16D).withTime(1500L);
    static final BuildingLocation BLACKSMITHDOOR = new BuildingLocation(BLACKSMITH, BLACKSMITH_DOOR).withDistance(20D).withTime(1500L);
}
