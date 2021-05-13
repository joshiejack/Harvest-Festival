package uk.joshiejack.furniture.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.joshiejack.furniture.Furniture;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;

@GameRegistry.ObjectHolder(Furniture.MODID)
@Mod.EventBusSubscriber(modid = Furniture.MODID)
public class FurnitureBlocks {
    //public static final BlockCrib CRIB = null;
   // public static final BlockDoubleBed DOUBLE_BED = null;
    //public static final BlockChildBed CHILD_BED = null;
    public static final BlockOldLadder LADDER = null;
    public static final BlockNewLadder LADDER2 = null;
    public static final BlockTelevision TELEVISION = null;

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(new BlockOldLadder(), new BlockNewLadder(), new BlockTelevision());
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(RegistryHelper.getItemBlocks(LADDER, LADDER2, TELEVISION));
    }
}
