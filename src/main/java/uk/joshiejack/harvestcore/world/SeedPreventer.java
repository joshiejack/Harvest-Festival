package uk.joshiejack.harvestcore.world;

import uk.joshiejack.harvestcore.HarvestCore;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class SeedPreventer {
    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.RightClickBlock event) {
        //if (ItemSeeds.SEEDS.contains(event.getItemStack()) && event.getWorld().getTileEntity(event.getPos()) == null) event.setCanceled(true);
    }
}
