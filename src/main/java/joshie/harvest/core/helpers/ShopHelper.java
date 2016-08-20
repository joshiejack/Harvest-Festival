package joshie.harvest.core.helpers;

import joshie.harvest.api.shops.IPurchaseable;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.stats.StatDataServer;
import net.minecraft.entity.player.EntityPlayerMP;

public class ShopHelper {
    /** This should only be ever called server ide **/
    public static boolean purchase(EntityPlayerMP player, IPurchaseable purchaseable, long cost) {
        StatDataServer stats = HFTrackers.<PlayerTrackerServer>getPlayerTracker(player).getStats();
        if (stats.getGold() - cost >= 0) {
            stats.addGold(player, -cost);
            return purchaseable.onPurchased(player);
        }
        
        return false;
    }
}
