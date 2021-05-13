package uk.joshiejack.harvestcore.world.mine.dimension;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.world.mine.Mine;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class MineProtector {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (Mine.BY_ID.containsKey(event.getEntityPlayer().world.provider.getDimension()) && event.getPos().getY() %6 == 1) event.setNewSpeed(0F);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDetonate(ExplosionEvent.Detonate event) {
        if (Mine.BY_ID.containsKey(event.getWorld().provider.getDimension())) {
            event.getAffectedBlocks().removeIf(e -> e.getY() % 6 == 1);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onHarvestBlock(BlockEvent.HarvestDropsEvent event) {
        if (Mine.BY_ID.containsKey(event.getWorld().provider.getDimension()) && event.getPos().getY() %6 == 1) event.getDrops().clear();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        if (Mine.BY_ID.containsKey(event.getWorld().provider.getDimension()) && event.getPos().getY() %6 == 1) event.setCanceled(true);
    }
}
