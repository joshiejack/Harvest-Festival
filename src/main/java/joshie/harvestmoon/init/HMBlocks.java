package joshie.harvestmoon.init;

import static joshie.harvestmoon.core.helpers.generic.RegistryHelper.registerTiles;
import static net.minecraft.block.Block.soundTypeGrass;
import static net.minecraft.block.Block.soundTypeGravel;
import static net.minecraft.block.Block.soundTypeMetal;
import static net.minecraft.block.Block.soundTypePiston;
import static net.minecraft.block.Block.soundTypeWood;
import joshie.harvestmoon.blocks.BlockCookware;
import joshie.harvestmoon.blocks.BlockCrop;
import joshie.harvestmoon.blocks.BlockDirt;
import joshie.harvestmoon.blocks.BlockFlower;
import joshie.harvestmoon.blocks.BlockGoddessWater;
import joshie.harvestmoon.blocks.BlockPreview;
import joshie.harvestmoon.blocks.BlockStone;
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
import joshie.harvestmoon.core.lib.HMModInfo;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class HMBlocks {
    public static Block cookware;
    public static Block crops;
    public static Block dirt;
    public static Block flowers;
    public static Block preview;
    public static Block stone;
    public static Block woodmachines;
    public static Block goddessWater;
    
    public static Fluid goddess;

    public static void init() {
        crops = new BlockCrop().setStepSound(soundTypeGrass).setBlockName("crops.block");
        dirt = new BlockDirt().setStepSound(soundTypeGravel).setBlockName("dirt");
        flowers = new BlockFlower().setStepSound(soundTypeGrass).setBlockName("flowers.block");
        cookware = new BlockCookware().setStepSound(soundTypeMetal).setBlockName("cookware");
        woodmachines = new BlockWood().setStepSound(soundTypeWood).setBlockName("general.block");
        preview = new BlockPreview().setBlockName("preview");
        stone = new BlockStone().setStepSound(soundTypePiston).setBlockName("stone");
        goddess = new Fluid("hm_goddess_water").setRarity(EnumRarity.rare);
        FluidRegistry.registerFluid(goddess);
        goddessWater = new BlockGoddessWater(goddess).setBlockName("goddess.water");
        goddess.setBlock(goddessWater);

        registerTiles(HMModInfo.CAPNAME, TileCooking.class, TileFridge.class, TileFryingPan.class, TileKitchen.class, TileMarker.class, 
                            TileMixer.class, TileOven.class, TilePot.class, TileRuralChest.class, TileSteamer.class);
    }
}
