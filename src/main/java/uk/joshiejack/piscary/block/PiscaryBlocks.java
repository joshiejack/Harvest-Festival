package uk.joshiejack.piscary.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;

import static uk.joshiejack.piscary.Piscary.MODID;

@SuppressWarnings("unused")
@GameRegistry.ObjectHolder(MODID)
@Mod.EventBusSubscriber(modid = MODID)
public class PiscaryBlocks {
    public static final BlockTrap FISH_TRAP = null;
    public static final BlockHatchery HATCHERY = null;
    public static final BlockMachine MACHINE = null;

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(new BlockTrap(), new BlockHatchery(), new BlockMachine());
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(RegistryHelper.getItemBlocks(FISH_TRAP, HATCHERY, MACHINE));
    }
}
