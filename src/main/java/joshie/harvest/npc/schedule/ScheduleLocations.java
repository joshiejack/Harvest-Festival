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
    static final BuildingLocation CHURCHINSIDE = new BuildingLocation(CHURCH, THOMAS);
}
