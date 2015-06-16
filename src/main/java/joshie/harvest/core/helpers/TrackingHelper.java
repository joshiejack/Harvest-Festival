package joshie.harvest.core.helpers;

import joshie.harvest.api.crops.ICropData;
import joshie.harvest.core.handlers.DataHelper;
import net.minecraft.entity.player.EntityPlayer;

public class TrackingHelper {
    public static void onHarvested(EntityPlayer player, ICropData data) {
        DataHelper.getPlayerTracker(player).getTracking().onHarvested(data);
    }
}
