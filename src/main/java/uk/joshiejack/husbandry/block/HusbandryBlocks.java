package uk.joshiejack.husbandry.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;

import static uk.joshiejack.husbandry.Husbandry.MODID;


@SuppressWarnings("unused")
@GameRegistry.ObjectHolder(MODID)
@Mod.EventBusSubscriber(modid = MODID)
public class HusbandryBlocks {
    public static final BlockDoubleMachine DOUBLE_MACHINE = null;
    public static final BlockMachine MACHINE = null;
    public static final BlockTray TRAY = null;
    public static final BlockTrough TROUGH = null;
    public static final BlockIncubator INCUBATOR = null;
    public static final BlockProduct PRODUCT = null;

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(new BlockDoubleMachine(), new BlockMachine(), new BlockTray(),
                                        new BlockTrough(), new BlockIncubator(), new BlockProduct());
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(RegistryHelper.getItemBlocks(DOUBLE_MACHINE, MACHINE, TRAY, TROUGH, INCUBATOR, PRODUCT));
    }
}
