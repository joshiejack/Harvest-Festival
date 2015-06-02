package joshie.harvest.init;

import static joshie.harvest.core.helpers.generic.RegistryHelper.registerTiles;
import static net.minecraft.block.Block.soundTypeGrass;
import static net.minecraft.block.Block.soundTypeGravel;
import static net.minecraft.block.Block.soundTypeMetal;
import static net.minecraft.block.Block.soundTypePiston;
import static net.minecraft.block.Block.soundTypeWood;
import joshie.harvest.blocks.BlockCookware;
import joshie.harvest.blocks.BlockCrop;
import joshie.harvest.blocks.BlockDirt;
import joshie.harvest.blocks.BlockFlower;
import joshie.harvest.blocks.BlockGoddessWater;
import joshie.harvest.blocks.BlockPreview;
import joshie.harvest.blocks.BlockStone;
import joshie.harvest.blocks.BlockWood;
import joshie.harvest.blocks.tiles.TileCooking;
import joshie.harvest.blocks.tiles.TileFridge;
import joshie.harvest.blocks.tiles.TileFryingPan;
import joshie.harvest.blocks.tiles.TileKitchen;
import joshie.harvest.blocks.tiles.TileMarker;
import joshie.harvest.blocks.tiles.TileMixer;
import joshie.harvest.blocks.tiles.TileOven;
import joshie.harvest.blocks.tiles.TilePot;
import joshie.harvest.blocks.tiles.TileSteamer;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class HFBlocks {
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

        registerTiles(HFModInfo.CAPNAME, TileCooking.class, TileFridge.class, TileFryingPan.class, TileKitchen.class, TileMarker.class, 
                            TileMixer.class, TileOven.class, TilePot.class, TileSteamer.class);
    }
}
