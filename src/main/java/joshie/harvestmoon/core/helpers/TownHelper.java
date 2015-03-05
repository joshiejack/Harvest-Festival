package joshie.harvestmoon.core.helpers;

import java.util.UUID;

import joshie.harvestmoon.api.WorldLocation;
import joshie.harvestmoon.buildings.BuildingGroup;
import joshie.harvestmoon.player.PlayerDataServer;
import net.minecraft.entity.player.EntityPlayer;

public class TownHelper {
    public static WorldLocation getLocationFor(EntityPlayer player, BuildingGroup building, String npc_location) {
        return PlayerHelper.getData(player).getCoordinatesFor(building, npc_location);
    }

    /** CAN AND WILL RETURN NULL, IF THE UUID COULD NOT BE FOUND **/
    public static WorldLocation getLocationFor(UUID owner, BuildingGroup building, String npc_location) {
        PlayerDataServer data = PlayerHelper.getData(owner);
        if (data != null) {
            return data.getCoordinatesFor(building, npc_location);
        } else return null;
    }
}
