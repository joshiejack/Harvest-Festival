package joshie.harvest.blocks;

import joshie.harvest.blocks.tiles.*;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;

import static joshie.harvest.core.helpers.generic.RegistryHelper.registerTiles;

public class HFBlocks {
    //Cooking & Farming
    public static Block COOKWARE;
    public static Block CROPS;
    public static Block FLOWERS;
    //Mine
    public static Block STONE;
    public static Block DIRT;
    //Misc
    public static Block WOOD_MACHINES;
    public static Block PREVIEW;
    public static Block GODDESS_WATER;
    public static Fluid GODDESS;

    public static void preInit() {

        //Cooking & Farming
        COOKWARE = new BlockCookware().setSoundType(soundTypeMetal).setUnlocalizedName("cookware");
        CROPS = new BlockCrop().setSoundType(soundTypeGrass).setUnlocalizedName("crops.block");
        FLOWERS = new BlockFlower().setSoundType(soundTypeGrass).setUnlocalizedName("flowers.block");
        //Mine
        STONE = new BlockStone().setSoundType(soundTypePiston).setUnlocalizedName("stone");
        DIRT = new BlockDirt("hf", "ctm/dirt").setSoundType(soundTypeGravel).setUnlocalizedName("dirt");
        //Misc
        WOOD_MACHINES = new BlockWood().setSoundType(soundTypeWood).setUnlocalizedName("general.block");
        PREVIEW = new BlockPreview().setUnlocalizedName("preview");
        GODDESS = new Fluid("hf_goddess_water").setRarity(EnumRarity.RARE);
        FluidRegistry.registerFluid(GODDESS);
        GODDESS_WATER = new BlockGoddessWater(GODDESS).setUnlocalizedName("goddess.water");
        GODDESS.setBlock(GODDESS_WATER);

        registerTiles(HFModInfo.CAPNAME, TileCooking.class, TileFridge.class, TileFryingPan.class, TileCounter.class, TileMarker.class,
                TileMixer.class, TileOven.class, TilePot.class);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {

    }

    @SideOnly(Side.CLIENT)
    private static void registerRenders(Block b) {

    }

    @SideOnly(Side.CLIENT)
    private static String sanitizeGeneral(String name) {
        name = name.replace(".", " ");
        name = WordUtils.capitalize(name);
        return name.replace(" ", "");
    }
}