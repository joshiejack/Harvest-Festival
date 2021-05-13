package uk.joshiejack.horticulture.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.joshiejack.horticulture.item.ItemCrop;
import uk.joshiejack.penguinlib.block.base.BlockBaseCrop;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;

import static uk.joshiejack.horticulture.Horticulture.MODID;

@SuppressWarnings("unused")
@GameRegistry.ObjectHolder(MODID)
@Mod.EventBusSubscriber(modid = MODID)
public class HorticultureBlocks {
    public static final BlockBaseCrop TURNIP = null;
    public static final BlockBaseCrop ONION = null;
    public static final BlockBaseCrop SPINACH = null;
    public static final BlockBaseCrop SWEET_POTATO = null;
    public static final BlockBaseCrop CABBAGE = null;
    public static final BlockBaseCrop CUCUMBER = null;
    public static final BlockBaseCrop EGGPLANT = null;
    public static final BlockBaseCrop STRAWBERRY = null;
    public static final BlockBaseCrop CORN = null;
    public static final BlockBaseCrop GREEN_PEPPER = null;
    public static final BlockBaseCrop PINEAPPLE = null;
    public static final BlockBaseCrop TOMATO = null;
    public static final BlockTrellis.BlockTrellisNS GRAPE_NS = null;
    public static final BlockTrellis.BlockTrellisEW GRAPE_EW = null;
    public static final BlockSapling SAPLING = null;
    public static final BlockLeavesTemperate LEAVES_TEMPERATE = null;
    public static final BlockLeavesTropical LEAVES_TROPICAL = null;
    public static final BlockFruit FRUIT = null;
    public static final BlockSprinkler SPRINKLER = null;
    public static final BlockMachine MACHINE = null;
    public static final BlockStump STUMP = null;

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry()
                .registerAll(new BlockCrop(ItemCrop.Crops.TURNIP, 3), new BlockCrop(ItemCrop.Crops.ONION, 3), new BlockCrop(ItemCrop.Crops.SPINACH, 3),
                        new BlockCrop(ItemCrop.Crops.SWEET_POTATO, 3, 0), new BlockCrop(ItemCrop.Crops.CABBAGE, 4), new BlockCrop(ItemCrop.Crops.CUCUMBER, 4, 2),
                        new BlockCrop(ItemCrop.Crops.EGGPLANT, 4, 1), new BlockCrop(ItemCrop.Crops.STRAWBERRY, 4, 2), new BlockCrop(ItemCrop.Crops.CORN, 5, 2),
                        new BlockCrop(ItemCrop.Crops.GREEN_PEPPER, 5, 3), new BlockCrop(ItemCrop.Crops.PINEAPPLE, 5, 1), new BlockCrop(ItemCrop.Crops.TOMATO, 5, 2),
                        new BlockTrellis.BlockTrellisNS(ItemCrop.Crops.GRAPE, 4, 2), new BlockTrellis.BlockTrellisEW(ItemCrop.Crops.GRAPE, 4, 2), new BlockSapling(),
                        new BlockLeavesTemperate(), new BlockLeavesTropical(), new BlockFruit(), new BlockSprinkler(), new BlockMachine(), new BlockStump()
                );
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(RegistryHelper.getItemBlocks(SAPLING, LEAVES_TEMPERATE, LEAVES_TROPICAL, FRUIT, SPRINKLER, MACHINE, STUMP));
    }
}
