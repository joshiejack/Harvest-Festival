package uk.joshiejack.furniture;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import uk.joshiejack.furniture.block.BlockOldLadder;
import uk.joshiejack.furniture.block.FurnitureBlocks;
import uk.joshiejack.furniture.network.FurnitureGuiHandler;
import uk.joshiejack.penguinlib.creativetab.PenguinTab;

@Mod(modid = Furniture.MODID, name = "Furniture", version = "@FURNITURE_VERSION@", dependencies = "required-after:penguinlib")
public class Furniture {
    public static final String MODID = "furniture";
    @SuppressWarnings("ConstantConditions")
    public static final CreativeTabs INSTANCE = new PenguinTab(MODID, () -> FurnitureBlocks.LADDER.getStackFromEnum(BlockOldLadder.Ladder.OAK, 1));

    @Mod.Instance(Furniture.MODID)
    public static Furniture instance;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new FurnitureGuiHandler());
    }
}
