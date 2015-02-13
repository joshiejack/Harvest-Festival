package joshie.harvestmoon.init;

import static joshie.harvestmoon.helpers.generic.RegistryHelper.registerTiles;
import joshie.harvestmoon.blocks.BlockCrop;
import joshie.harvestmoon.blocks.BlockDirt;
import joshie.harvestmoon.blocks.BlockFlower;
import joshie.harvestmoon.blocks.BlockGeneral;
import joshie.harvestmoon.blocks.BlockSnowSheet;
import joshie.harvestmoon.blocks.BlockSoil;
import joshie.harvestmoon.blocks.BlockStone;
import joshie.harvestmoon.blocks.BlockWithered;
import joshie.harvestmoon.blocks.items.ItemBlockFarmland;
import joshie.harvestmoon.blocks.items.ItemBlockSnow;
import joshie.harvestmoon.blocks.tiles.TileFridge;
import joshie.harvestmoon.blocks.tiles.TileFryingPan;
import joshie.harvestmoon.blocks.tiles.TileKitchen;
import joshie.harvestmoon.blocks.tiles.TileMixer;
import joshie.harvestmoon.blocks.tiles.TileOven;
import joshie.harvestmoon.blocks.tiles.TilePot;
import joshie.harvestmoon.blocks.tiles.TileSteamer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.ExistingSubstitutionException;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;
import cpw.mods.fml.relauncher.Side;

public class HMBlocks {
    //Overriden Blocks
    public static Block snow;
    public static Block farmland;
    
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

        try {
            snow = new BlockSnowSheet().setHardness(0.1F).setStepSound(Block.soundTypeSnow).setBlockName("snow").setLightOpacity(0).setBlockTextureName("snow");
            farmland = new BlockSoil().setHardness(0.6F).setStepSound(Block.soundTypeGravel).setBlockName("farmland").setBlockTextureName("farmland");
            GameRegistry.addSubstitutionAlias("minecraft:snow_layer", Type.BLOCK, snow);
            GameRegistry.addSubstitutionAlias("minecraft:snow_layer", Type.ITEM, new ItemBlockSnow(snow, snow));
            GameRegistry.addSubstitutionAlias("minecraft:farmland", Type.BLOCK, farmland);
            GameRegistry.addSubstitutionAlias("minecraft:farmland", Type.ITEM, new ItemBlockFarmland(farmland));
        } catch (ExistingSubstitutionException e) {
            e.printStackTrace();
        }
    }
}
