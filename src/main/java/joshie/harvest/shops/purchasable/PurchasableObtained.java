package joshie.harvest.shops.purchasable;

import joshie.harvest.core.HFTrackers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchasableObtained extends Purchasable {
    public PurchasableObtained(long cost, ItemStack stack) {
        super(cost, stack);
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return super.canList(world, player) && HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().hasObtainedItem(getDisplayStack());
    }
}