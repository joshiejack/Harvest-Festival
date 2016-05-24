package joshie.harvest.mining.loot;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.items.HFItems;
import joshie.harvest.player.tracking.TrackingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.core.ITiered.ToolTier.BLESSED;

public class LootMythic extends LootChance {
    private static final ItemStack hoe = HFCrops.HOE.getStack(BLESSED);
    private static final ItemStack sickle = HFCrops.SICKLE.getStack(BLESSED);
    private static final ItemStack watering = HFCrops.WATERING_CAN.getStack(BLESSED);
    private static final ItemStack axe = HFItems.AXE.getStack(BLESSED);
    private static final ItemStack hammer = HFItems.HAMMER.getStack(BLESSED);

    public LootMythic(ItemStack stack, double chance) {
        super(stack, chance);
    }

    public boolean canPlayerObtain(EntityPlayer player) {
        TrackingData stats = HFTrackers.getPlayerTracker(player).getTracking();
        return stats.hasObtainedItem(hoe) && stats.hasObtainedItem(sickle) && stats.hasObtainedItem(watering) && stats.hasObtainedItem(axe) && stats.hasObtainedItem(hammer);
    }
}