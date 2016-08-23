package joshie.harvest.shops.purchaseable;

import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchaseableWeekend extends Purchaseable {
    private ItemStack[] required;

    public PurchaseableWeekend(long cost, ItemStack stack, ItemStack... required) {
        super(cost, stack);
        this.required = required;
    }

    private boolean hasRequiredItem(EntityPlayer player) {
        if (required == null || required.length == 0) return true;
        else {
            for (ItemStack stack: required) {
                if (!HFTrackers.getPlayerTracker(player).getTracking().hasObtainedItem(stack)) return false;
            }

            return true;
        }
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        Weekday weekday = HFTrackers.getCalendar(world).getDate().getWeekday();
        return (weekday == Weekday.SATURDAY || weekday == Weekday.SUNDAY) && hasRequiredItem(player);
    }

    @Override
    public boolean onPurchased(EntityPlayer player) {
        HFTrackers.getPlayerTracker(player).getTracking().addAsObtained(stacks[0]);
        return super.onPurchased(player);
    }
}
