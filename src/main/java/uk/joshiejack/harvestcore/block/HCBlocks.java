package uk.joshiejack.harvestcore.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.HCConfig;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.seasons.Season;

@GameRegistry.ObjectHolder(HarvestCore.MODID)
@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class HCBlocks {
    public static final BlockMailbox MAILBOX = null;
    public static final BlockElevator ELEVATOR = null;
    public static final BlockMachine MACHINE = null;

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        final IForgeRegistry<Block> registry = event.getRegistry();
        if (HCConfig.enableSpringWildernessCrops) registry.register(new BlockWildCrop(Season.SPRING));
        if (HCConfig.enableSummerWildernessCrops) registry.register(new BlockWildCrop(Season.SUMMER));
        if (HCConfig.enableAutumnWildernessCrops) registry.register(new BlockWildCrop(Season.AUTUMN));
        if (HCConfig.enableWinterWildernessCrops) registry.register(new BlockWildCrop(Season.WINTER));
        if (HCConfig.enableWetWildernessCrops) registry.register(new BlockWildCrop(Season.WET));
        if (HCConfig.enableDryWildernessCrops) registry.register(new BlockWildCrop(Season.DRY));
        registry.registerAll(new BlockMailbox(), new BlockElevator(), new BlockMachine());
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(RegistryHelper.getItemBlocks(MAILBOX, ELEVATOR, MACHINE));
    }
}
