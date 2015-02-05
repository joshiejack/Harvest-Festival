package joshie.harvestmoon.init;

import static joshie.lib.helpers.RegistryHelper.registerTiles;
import static net.minecraft.block.Block.soundTypeGravel;
import joshie.harvestmoon.blocks.BlockCrop;
import joshie.harvestmoon.blocks.BlockGeneral;
import joshie.harvestmoon.blocks.BlockSoil;
import joshie.harvestmoon.blocks.BlockWithered;
import joshie.harvestmoon.blocks.tiles.TileFridge;
import joshie.harvestmoon.blocks.tiles.TileFryingPan;
import joshie.harvestmoon.blocks.tiles.TileKitchen;
import joshie.harvestmoon.blocks.tiles.TileMixer;
import joshie.harvestmoon.blocks.tiles.TileOven;
import joshie.harvestmoon.blocks.tiles.TilePot;
import joshie.harvestmoon.blocks.tiles.TileSteamer;
import net.minecraft.block.Block;

public class HMBlocks {
    public static Block crops;
    public static Block tiles;
    public static Block soil;
    public static Block withered;

    public static void init() {
        crops = new BlockCrop().setBlockName("crops.block");
        tiles = new BlockGeneral().setBlockName("general.block");
        soil = new BlockSoil().setHardness(0.6F).setStepSound(soundTypeGravel).setBlockName("farmland").setBlockTextureName("farmland");
        withered = new BlockWithered().setBlockName("crops.withered");

        registerTiles("HM", TileFridge.class, TileFryingPan.class, TileKitchen.class, TileMixer.class, TileOven.class, TilePot.class, TileSteamer.class);
    }
}
