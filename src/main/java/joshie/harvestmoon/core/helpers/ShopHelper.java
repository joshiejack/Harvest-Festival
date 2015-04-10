package joshie.harvestmoon.core.helpers;

import joshie.harvestmoon.api.shops.IPurchaseable;
import joshie.harvestmoon.core.helpers.generic.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ShopHelper {
    /** This should only be ever called server ide **/
    public static boolean purchase(EntityPlayer player, IPurchaseable purchaseable, long cost) {
        long player_gold = PlayerHelper.getGold(player);
        if (player_gold - cost >= 0) {
            PlayerHelper.adjustGold(player, -cost);
            return purchaseable.onPurchased(player);
        }
        
        return false;
    }
}
