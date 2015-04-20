package joshie.harvestmoon.core.helpers;

import java.util.UUID;

import joshie.harvestmoon.api.WorldLocation;
import joshie.harvestmoon.api.buildings.IBuildingGroup;
import joshie.harvestmoon.player.PlayerDataServer;

public class TownHelper {
    public static WorldLocation getLocationFor(UUID owner_uuid, IBuildingGroup home, String npc_location) {
        PlayerDataServer data = PlayerHelper.getData(owner_uuid);
        if (data != null) {
            return data.getCoordinatesFor(home, npc_location);
        } else return null;
    }
}
