package harvestmoon.init;

import static net.minecraft.block.Block.soundTypeGravel;
import harvestmoon.blocks.BlockCrop;
import harvestmoon.blocks.BlockGeneral;
import harvestmoon.blocks.BlockSoil;
import harvestmoon.blocks.BlockWithered;
import harvestmoon.blocks.TileFridge;
import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;

public class HMBlocks {
    public static Block crops;
    public static Block shipping;
    public static Block soil;
    public static Block withered;

    public static void init() {
        crops = new BlockCrop().setBlockName("crops.block").register();
        shipping = new BlockGeneral().setBlockName("general").register();
        soil = new BlockSoil().setHardness(0.6F).setStepSound(soundTypeGravel).setBlockName("farmland").setBlockTextureName("farmland");
        withered = new BlockWithered().setBlockName("crops.withered").register();

        GameRegistry.registerBlock(soil, "soil");
        GameRegistry.registerTileEntity(TileFridge.class, "HMFridge");
    }
}
