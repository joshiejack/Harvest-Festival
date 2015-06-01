package joshie.harvest.core.helpers;

import joshie.harvest.api.crops.ICropData;
import net.minecraft.entity.player.EntityPlayer;

public class TrackingHelper {
    public static void onHarvested(EntityPlayer player, ICropData data) {
        ServerHelper.getPlayerData(player).onHarvested(data);
    }
}
