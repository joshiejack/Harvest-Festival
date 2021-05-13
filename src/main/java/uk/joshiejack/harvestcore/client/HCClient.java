package uk.joshiejack.harvestcore.client;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.item.HCItems;
import uk.joshiejack.harvestcore.item.ItemSeedlings;
import uk.joshiejack.harvestcore.item.ItemSeeds;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("unused, ConstantConditions")
@Mod.EventBusSubscriber(modid = HarvestCore.MODID, value = Side.CLIENT)
public class HCClient {
    @SubscribeEvent
    public static void init(ColorHandlerEvent.Item event) {
        event.getItemColors().registerItemColorHandler((stack, tintIndex) -> {
            ItemSeeds.CropData seeds = HCItems.SEED_BAG.getSeeds(stack);
            return seeds != null ? seeds.getColor() : -1;
        }, HCItems.SEED_BAG);

        event.getItemColors().registerItemColorHandler(((stack, tintIndex) -> {
            ItemSeedlings.TreeData sapling = HCItems.SEEDLING_BAG.getSapling(stack);
            return sapling != null && tintIndex == 1 ? sapling.getColor() : -1;
        }), HCItems.SEEDLING_BAG);
    }
}
