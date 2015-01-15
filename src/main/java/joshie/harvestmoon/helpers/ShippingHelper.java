package joshie.harvestmoon.helpers;

import static joshie.harvestmoon.HarvestMoon.handler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ShippingHelper {
    public static boolean addForShipping(EntityPlayer player, ItemStack stack) {
        if(!player.worldObj.isRemote) {
            return handler.getServer().getPlayerData(player).addForShipping(stack);
        } else return true;
    }
}
