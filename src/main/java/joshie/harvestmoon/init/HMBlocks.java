package joshie.harvestmoon.init;

import static joshie.lib.helpers.RegistryHelper.registerTiles;
import static net.minecraft.block.Block.soundTypeGravel;
import joshie.harvestmoon.blocks.BlockCrop;
import joshie.harvestmoon.blocks.BlockGeneral;
import joshie.harvestmoon.blocks.BlockSoil;
import joshie.harvestmoon.blocks.BlockWithered;
import joshie.harvestmoon.blocks.TileFridge;
import net.minecraft.block.Block;

public class HMBlocks {
    public static Block crops;
    public static Block shipping;
    public static Block soil;
    public static Block withered;

    public static void init() {
        crops = new BlockCrop().setBlockName("crops.block");
        shipping = new BlockGeneral().setBlockName("general.block");
        soil = new BlockSoil().setHardness(0.6F).setStepSound(soundTypeGravel).setBlockName("farmland").setBlockTextureName("farmland");
        withered = new BlockWithered().setBlockName("crops.withered");

        registerTiles("HM", TileFridge.class);
    }
}
