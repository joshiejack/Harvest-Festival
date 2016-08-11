package joshie.harvest.npc.schedule;

import joshie.harvest.api.buildings.BuildingLocation;

import static joshie.harvest.buildings.HFBuildings.*;
import static joshie.harvest.town.TownData.*;

public class ScheduleLocations {
    static final BuildingLocation POND = new BuildingLocation(FISHING_HOLE, FISHING_POND);
    static final BuildingLocation GODDESS = new BuildingLocation(GODDESS_POND, GODDESS_HOME);
    static final BuildingLocation BARNBUILDING = new BuildingLocation(BARN, JIM_HOME);
    static final BuildingLocation POULTRYBUILDING = new BuildingLocation(POULTRY_FARM, ASHLEE_HOME);
    static final BuildingLocation TOWNHALLLEFT = new BuildingLocation(TOWNHALL, TOWNHALL_LEFT_WING);
    static final BuildingLocation TOWNHALLRIGHT = new BuildingLocation(TOWNHALL, TOWNHALL_RIGHT_WING);
    static final BuildingLocation TOWNHALLENTRANCE = new BuildingLocation(TOWNHALL, TOWNHALL_ENTRANCE);
    static final BuildingLocation TOWNHALLSTAGE = new BuildingLocation(TOWNHALL, TOWNHALL_CENTRE);
    static final BuildingLocation CHURCHINSIDE = new BuildingLocation(CHURCH, THOMAS);
    static final BuildingLocation GENERALFRONT = new BuildingLocation(SUPERMARKET, MARKET_STOREFRONT);
    static final BuildingLocation CARPENTERFRONT = new BuildingLocation(CARPENTER, CARPENTER_DOOR);
    static final BuildingLocation CARPENTERDOWN = new BuildingLocation(CARPENTER, CARPENTER_DOWNSTAIRS);
    static final BuildingLocation CARPENTERUP = new BuildingLocation(CARPENTER, JADE_HOME);
    static final BuildingLocation CHURCHFRONT = new BuildingLocation(CHURCH, CHURCH_FRONT);
    static final BuildingLocation MINEENTRANCE = new BuildingLocation(MINING_HILL, MINE_ENTRANCE);
    static final BuildingLocation MINEHUTENTRANCE = new BuildingLocation(MINING_HUT, MINER_FRONT);
    static final BuildingLocation CAFEFRONT = new BuildingLocation(CAFE, CAFE_FRONT);
    static final BuildingLocation BLACKSMITHFRONT = new BuildingLocation(BLACKSMITH, BLACKSMITH_FRONT);
}
