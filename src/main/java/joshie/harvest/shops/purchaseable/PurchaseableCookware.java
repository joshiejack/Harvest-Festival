package joshie.harvest.shops.purchaseable;

import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchaseableCookware extends Purchaseable {
    private ItemStack required;

    public PurchaseableCookware(ItemStack stack, long cost, ItemStack required) {
        super(cost, new ItemStack[] { stack });
        this.required = required;
    }

    private boolean hasRequiredItem(EntityPlayer player) {
        return required == null || HFTrackers.getPlayerTracker(player).getTracking().hasObtainedItem(required);
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
