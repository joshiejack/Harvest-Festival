package joshie.harvest.shops.purchasable;

import joshie.harvest.core.HFTrackers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class PurchasableObtained extends Purchasable {
    public PurchasableObtained(long cost, @Nonnull ItemStack stack) {
        super(cost, stack);
    }

    @Override
    public boolean canList(@Nonnull World world, @Nonnull EntityPlayer player) {
        return super.canList(world, player) && HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().hasObtainedItem(getDisplayStack());
    }
}