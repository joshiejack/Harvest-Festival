package uk.joshiejack.harvestcore.database;

import net.minecraft.util.EnumActionResult;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.item.custom.ItemFertilizer;
import uk.joshiejack.harvestcore.registry.Fertilizer;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class FertilizerEvents {
    @SubscribeEvent
    public static void onBonemeal(BonemealEvent event) {
        if (ItemFertilizer.getBonemeal() != Fertilizer.NONE &&
                ItemFertilizer.applyFertilizer(event.getEntityPlayer(), event.getHand(), event.getPos()) == EnumActionResult.SUCCESS) {
            event.setResult(Event.Result.ALLOW); //Allow the stack to decrease but don't continue
        } //else event.setCanceled(true);
    }
}
