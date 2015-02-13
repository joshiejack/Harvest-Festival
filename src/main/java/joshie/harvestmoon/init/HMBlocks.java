package joshie.harvestmoon.init;

import static joshie.harvestmoon.helpers.generic.RegistryHelper.registerTiles;
import joshie.harvestmoon.blocks.BlockCrop;
import joshie.harvestmoon.blocks.BlockDirt;
import joshie.harvestmoon.blocks.BlockFlower;
import joshie.harvestmoon.blocks.BlockGeneral;
import joshie.harvestmoon.blocks.BlockStone;
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
    public static Block dirt;
    public static Block flowers;
    public static Block stone;
    public static Block tiles;
    public static Block withered;

    public static void init() {
        crops = new BlockCrop().setBlockName("crops.block");
        dirt = new BlockDirt().setBlockName("dirt");
        flowers = new BlockFlower().setBlockName("flowers.block");
        tiles = new BlockGeneral().setBlockName("general.block");
        stone = new BlockStone().setBlockName("stone");
        withered = new BlockWithered().setBlockName("crops.withered");

        registerTiles("HM", TileFridge.class, TileFryingPan.class, TileKitchen.class, TileMixer.class, TileOven.class, TilePot.class, TileSteamer.class);
    }
}
