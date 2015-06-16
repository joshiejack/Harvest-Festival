package joshie.harvest.mining;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.core.handlers.DataHelper;
import joshie.harvest.core.util.SafeStack;
import joshie.harvest.init.HFItems;
import joshie.harvest.player.TrackingStats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class LootMythic extends LootChance {
    private static final SafeStack hoe = new SafeStack(new ItemStack(HFItems.hoe, 1, ToolTier.BLESSED.ordinal()));
    private static final SafeStack sickle = new SafeStack(new ItemStack(HFItems.sickle, 1, ToolTier.BLESSED.ordinal()));
    private static final SafeStack watering = new SafeStack(new ItemStack(HFItems.wateringcan, 1, ToolTier.BLESSED.ordinal()));
    
    public LootMythic(ItemStack stack, double chance) {
        super(stack, chance);
    }

    public boolean canPlayerObtain(EntityPlayer player) {
        TrackingStats stats = DataHelper.getPlayerTracker(player).getTracking();
        return stats.hasObtainedItem(hoe) && stats.hasObtainedItem(sickle) && stats.hasObtainedItem(watering);
    }
}
