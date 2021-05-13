package uk.joshiejack.settlements.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;

@GameRegistry.ObjectHolder(Settlements.MODID)
@Mod.EventBusSubscriber(modid = Settlements.MODID)
public class AdventureBlocks {
    public static final BlockQuestBoard QUEST_BOARD = null;

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new BlockQuestBoard());
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(RegistryHelper.getItemBlocks(QUEST_BOARD));
    }
}
