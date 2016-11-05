package joshie.harvest.shops.purchasable;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.HFTrackers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchasableWeekend extends Purchasable {
    private final ItemStack[] required;

    public PurchasableWeekend(long cost, ItemStack stack, ItemStack... required) {
        super(cost, stack);
        this.required = required;
    }

    private boolean hasRequiredItem(EntityPlayer player) {
        if (required == null || required.length == 0) return true;
        else {
            for (ItemStack stack: required) {
                if (!HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().hasObtainedItem(stack)) return false;
            }

            return true;
        }
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player, int amount) {
        return amount == 1 && HFApi.calendar.getDate(world).getWeekday().isWeekend() && hasRequiredItem(player);
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().addAsObtained(stacks[0]);
        super.onPurchased(player);
    }
}
