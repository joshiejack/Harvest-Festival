package joshie.harvestmoon.init;

import static joshie.harvestmoon.helpers.generic.RegistryHelper.registerTiles;
import static net.minecraft.block.Block.soundTypeGrass;
import static net.minecraft.block.Block.soundTypeGravel;
import static net.minecraft.block.Block.soundTypeMetal;
import static net.minecraft.block.Block.soundTypePiston;
import static net.minecraft.block.Block.soundTypeWood;
import joshie.harvestmoon.blocks.BlockCookware;
import joshie.harvestmoon.blocks.BlockCrop;
import joshie.harvestmoon.blocks.BlockDirt;
import joshie.harvestmoon.blocks.BlockFlower;
import joshie.harvestmoon.blocks.BlockPreview;
import joshie.harvestmoon.blocks.BlockStone;
import joshie.harvestmoon.blocks.BlockWithered;
import joshie.harvestmoon.blocks.BlockWood;
import joshie.harvestmoon.blocks.tiles.TileCooking;
import joshie.harvestmoon.blocks.tiles.TileFridge;
import joshie.harvestmoon.blocks.tiles.TileFryingPan;
import joshie.harvestmoon.blocks.tiles.TileKitchen;
import joshie.harvestmoon.blocks.tiles.TileMarker;
import joshie.harvestmoon.blocks.tiles.TileMixer;
import joshie.harvestmoon.blocks.tiles.TileOven;
import joshie.harvestmoon.blocks.tiles.TilePot;
import joshie.harvestmoon.blocks.tiles.TileRuralChest;
import joshie.harvestmoon.blocks.tiles.TileSteamer;
import net.minecraft.block.Block;

public class HMBlocks {
    public static Block cookware;
    public static Block crops;
    public static Block dirt;
    public static Block flowers;
    public static Block preview;
    public static Block stone;
    public static Block withered;
    public static Block woodmachines;

    public static void init() {
        crops = new BlockCrop().setStepSound(soundTypeGrass).setBlockName("crops.block");
        dirt = new BlockDirt().setStepSound(soundTypeGravel).setBlockName("dirt");
        flowers = new BlockFlower().setStepSound(soundTypeGrass).setBlockName("flowers.block");
        cookware = new BlockCookware().setStepSound(soundTypeMetal).setBlockName("cookware");
        woodmachines = new BlockWood().setStepSound(soundTypeWood).setBlockName("general.block");
        preview = new BlockPreview().setBlockName("preview");
        stone = new BlockStone().setStepSound(soundTypePiston).setBlockName("stone");
        withered = new BlockWithered().setStepSound(soundTypeGrass).setBlockName("crops.withered");

        registerTiles("HM", TileCooking.class, TileFridge.class, TileFryingPan.class, TileKitchen.class, TileMarker.class, 
                            TileMixer.class, TileOven.class, TilePot.class, TileRuralChest.class, TileSteamer.class);
    }
}
