package joshie.harvest.core.helpers;

import joshie.harvest.api.crops.ICropData;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.entity.player.EntityPlayer;

public class TrackingHelper {
    public static void onHarvested(EntityPlayer player, ICropData data) {
        HFTrackers.getPlayerTracker(player).getTracking().onHarvested(data);
    }
}
