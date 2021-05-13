package uk.joshiejack.gastronomy.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.joshiejack.gastronomy.fluid.GastronomyFluids;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import static uk.joshiejack.gastronomy.Gastronomy.MODID;

@SuppressWarnings("unused")
@GameRegistry.ObjectHolder(MODID)
@Mod.EventBusSubscriber(modid = MODID)
public class GastronomyBlocks {
    public static final BlockFridge STORAGE = null;
    public static final BlockCookware COOKWARE = null;
    public static final BlockNature NATURE = null;
    public static final BlockCupboard CUPBOARD = null;

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        GastronomyFluids.load(); // Yes boys
        event.getRegistry().registerAll(new BlockFridge(), new BlockCookware(), new BlockNature(), new BlockCupboard());
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(RegistryHelper.getItemBlocks(STORAGE, COOKWARE, NATURE, CUPBOARD));
    }

    @SuppressWarnings("unchecked")
    public static void init() {
        StackHelper.registerSynonym(NATURE);
    }
}
