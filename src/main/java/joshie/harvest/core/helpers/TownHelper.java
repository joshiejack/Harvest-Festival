package joshie.harvest.core.helpers;

import java.util.UUID;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.player.PlayerTrackerServer;
import net.minecraft.entity.player.EntityPlayer;

public class TownHelper {
    public static WorldLocation getLocationFor(UUID owner_uuid, IBuilding home, String npc_location) {
        PlayerTrackerServer data = PlayerHelper.getData(owner_uuid);
        if (data != null) {
            return data.getCoordinatesFor(home, npc_location);
        } else return null;
    }
    
    public static boolean hasBuilding(EntityPlayer player, IBuilding building) {
        return hasBuilding(UUIDHelper.getPlayerUUID(player), building);
    }

    public static boolean hasBuilding(UUID owner_uuid, IBuilding building) {
        PlayerTrackerServer data = PlayerHelper.getData(owner_uuid);
        if (data != null) {
            return data.hasBuilding(building);
        } else return false;
    }
}
