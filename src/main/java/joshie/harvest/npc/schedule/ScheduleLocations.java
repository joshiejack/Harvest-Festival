package joshie.harvest.npc.schedule;

import joshie.harvest.api.buildings.BuildingLocation;

import static joshie.harvest.buildings.HFBuildings.*;
import static joshie.harvest.town.TownData.*;

public class ScheduleLocations {
    static final BuildingLocation POND = new BuildingLocation(FISHING_HOLE, FISHING_POND).withDistance(16D).withTime(1500L);
    static final BuildingLocation GODDESS = new BuildingLocation(GODDESS_POND, GODDESS_HOME).withDistance(24D).withTime(1500L);
    static final BuildingLocation BARNBUILDING = new BuildingLocation(BARN, JIM_HOME).withDistance(8D);
    static final BuildingLocation POULTRYBUILDING = new BuildingLocation(POULTRY_FARM, ASHLEE_HOME).withDistance(8D);
    static final BuildingLocation TOWNHALLLEFT = new BuildingLocation(TOWNHALL, TOWNHALL_LEFT_WING).withDistance(10D);
    static final BuildingLocation TOWNHALLRIGHT = new BuildingLocation(TOWNHALL, TOWNHALL_RIGHT_WING).withDistance(10D);
    static final BuildingLocation TOWNHALLENTRANCE = new BuildingLocation(TOWNHALL, TOWNHALL_ENTRANCE).withDistance(10D);
    static final BuildingLocation TOWNHALLSTAGE = new BuildingLocation(TOWNHALL, TOWNHALL_CENTRE);
    static final BuildingLocation CHURCHINSIDE = new BuildingLocation(CHURCH, THOMAS).withDistance(10D).withTime(1000L);
    static final BuildingLocation GENERALFRONT = new BuildingLocation(SUPERMARKET, MARKET_STOREFRONT).withDistance(16D).withTime(1500L);
    static final BuildingLocation CARPENTERFRONT = new BuildingLocation(CARPENTER, CARPENTER_DOOR).withDistance(24D).withTime(1500L);
    static final BuildingLocation CARPENTERDOWN = new BuildingLocation(CARPENTER, CARPENTER_DOWNSTAIRS).withDistance(3D);
    static final BuildingLocation CARPENTERUP = new BuildingLocation(CARPENTER, JADE_HOME).withDistance(3D);
    static final BuildingLocation CHURCHFRONT = new BuildingLocation(CHURCH, CHURCH_FRONT).withDistance(24D).withTime(1500L);
    static final BuildingLocation MINEENTRANCE = new BuildingLocation(MINING_HILL, MINE_ENTRANCE).withDistance(24D).withTime(1500L);
    static final BuildingLocation MINEHUTENTRANCE = new BuildingLocation(MINING_HUT, MINER_FRONT).withDistance(16D).withTime(1500L);
    static final BuildingLocation CAFEFRONT = new BuildingLocation(CAFE, CAFE_FRONT).withDistance(16D).withTime(1500L);
    static final BuildingLocation BLACKSMITHFRONT = new BuildingLocation(BLACKSMITH, BLACKSMITH_FRONT).withDistance(16D).withTime(1500L);
}
