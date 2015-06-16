package joshie.harvest.core.helpers;

import java.util.UUID;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.core.handlers.HFTracker;
import net.minecraft.entity.player.EntityPlayer;

public class TownHelper {
    public static WorldLocation getLocationFor(UUID owner_uuid, IBuilding home, String npc_location) {
        return HFTracker.getPlayerTracker(owner_uuid).getTown().getCoordinatesFor(home, npc_location);
    }
    
    public static boolean hasBuilding(EntityPlayer player, IBuilding building) {
        return hasBuilding(UUIDHelper.getPlayerUUID(player), building);
    }

    public static boolean hasBuilding(UUID owner_uuid, IBuilding building) {
        return HFTracker.getPlayerTracker(owner_uuid).getTown().hasBuilding(building);
    }
}
