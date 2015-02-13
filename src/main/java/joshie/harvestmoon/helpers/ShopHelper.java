package joshie.harvestmoon.helpers;

import joshie.harvestmoon.helpers.generic.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ShopHelper {
    /** This should only be ever called server ide **/
    public static void purchase(EntityPlayer player, ItemStack[] products, long cost) {
        long player_gold = PlayerHelper.getGold(player);
        if (player_gold - cost >= 0) {
            PlayerHelper.adjustGold(player, -cost);
            for (ItemStack product : products) {
                ItemHelper.addToPlayerInventory(player, product);
            }
        }
    }
}
