package joshie.harvest.core.helpers;

import joshie.harvest.core.handlers.DataHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ShippingHelper {
    public static boolean addForShipping(EntityPlayer player, ItemStack stack) {
        return DataHelper.getPlayerTracker(player).getShipping().addForShipping(stack);
    }
}
