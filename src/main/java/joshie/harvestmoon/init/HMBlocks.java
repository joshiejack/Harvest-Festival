package joshie.harvestmoon.init;

import static net.minecraft.block.Block.*;
import static joshie.harvestmoon.helpers.generic.RegistryHelper.registerTiles;
import joshie.harvestmoon.blocks.BlockCrop;
import joshie.harvestmoon.blocks.BlockDirt;
import joshie.harvestmoon.blocks.BlockFlower;
import joshie.harvestmoon.blocks.BlockGeneral;
import joshie.harvestmoon.blocks.BlockStone;
import joshie.harvestmoon.blocks.BlockWithered;
import joshie.harvestmoon.blocks.tiles.TileCooking;
import joshie.harvestmoon.blocks.tiles.TileFridge;
import joshie.harvestmoon.blocks.tiles.TileFryingPan;
import joshie.harvestmoon.blocks.tiles.TileKitchen;
import joshie.harvestmoon.blocks.tiles.TileMixer;
import joshie.harvestmoon.blocks.tiles.TileOven;
import joshie.harvestmoon.blocks.tiles.TilePot;
import joshie.harvestmoon.blocks.tiles.TileRuralChest;
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
        crops = new BlockCrop().setStepSound(soundTypeGrass).setBlockName("crops.block");
        dirt = new BlockDirt().setStepSound(soundTypeGravel).setBlockName("dirt");
        flowers = new BlockFlower().setStepSound(soundTypeGrass).setBlockName("flowers.block");
        tiles = new BlockGeneral().setStepSound(soundTypeMetal).setBlockName("general.block");
        stone = new BlockStone().setStepSound(soundTypePiston).setBlockName("stone");
        withered = new BlockWithered().setStepSound(soundTypeGrass).setBlockName("crops.withered");

        registerTiles("HM", TileCooking.class, TileFridge.class, TileFryingPan.class, TileKitchen.class, 
                            TileMixer.class, TileOven.class, TilePot.class, TileRuralChest.class, TileSteamer.class);
    }
}
