package joshie.harvestmoon.core.helpers;

import joshie.harvestmoon.api.crops.ICropData;
import net.minecraft.entity.player.EntityPlayer;

public class TrackingHelper {
    public static void onHarvested(EntityPlayer player, ICropData data) {
        ServerHelper.getPlayerData(player).onHarvested(data);
    }
}
