package joshie.harvest.mining.loot;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.tools.HFTools;
import joshie.harvest.player.tracking.TrackingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.core.ITiered.ToolTier.BLESSED;

public class LootMythic extends LootChance {
    private static final ItemStack hoe = HFTools.HOE.getStack(BLESSED);
    private static final ItemStack sickle = HFTools.SICKLE.getStack(BLESSED);
    private static final ItemStack watering = HFTools.WATERING_CAN.getStack(BLESSED);
    private static final ItemStack axe = HFTools.AXE.getStack(BLESSED);
    private static final ItemStack hammer = HFTools.HAMMER.getStack(BLESSED);

    public LootMythic(ItemStack stack, double chance) {
        super(stack, chance);
    }

    @Override
    public boolean canPlayerObtain(EntityPlayer player) {
        TrackingData stats = HFTrackers.getPlayerTracker(player).getTracking();
        return stats.hasObtainedItem(hoe) && stats.hasObtainedItem(sickle) && stats.hasObtainedItem(watering) && stats.hasObtainedItem(axe) && stats.hasObtainedItem(hammer);
    }
}