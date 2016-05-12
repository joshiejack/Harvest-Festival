package joshie.harvest.mining.loot;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.items.HFItems;
import joshie.harvest.player.tracking.TrackingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.core.ITiered.ToolTier.MYSTRIL;

public class LootCursed extends LootChance {
    private static final ItemStack hoe = HFItems.HOE.getStack(MYSTRIL);
    private static final ItemStack sickle = HFItems.SICKLE.getStack(MYSTRIL);
    private static final ItemStack watering = HFItems.WATERING_CAN.getStack(MYSTRIL);
    private static final ItemStack axe = HFItems.AXE.getStack(MYSTRIL);
    private static final ItemStack hammer = HFItems.HAMMER.getStack(MYSTRIL);

    public LootCursed(ItemStack stack, double chance) {
        super(stack, chance);
    }

    public boolean canPlayerObtain(EntityPlayer player) {
        TrackingData stats = HFTrackers.getPlayerTracker(player).getTracking();
        return stats.hasObtainedItem(hoe) && stats.hasObtainedItem(sickle) && stats.hasObtainedItem(watering) && stats.hasObtainedItem(axe) && stats.hasObtainedItem(hammer);
    }
}