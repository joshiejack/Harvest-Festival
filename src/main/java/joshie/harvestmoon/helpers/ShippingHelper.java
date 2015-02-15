package joshie.harvestmoon.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ShippingHelper {
    public static boolean addForShipping(EntityPlayer player, ItemStack stack) {
        if (!player.worldObj.isRemote) {
            return ServerHelper.getPlayerData(player).addForShipping(stack);
        } else return true;
    }
}
