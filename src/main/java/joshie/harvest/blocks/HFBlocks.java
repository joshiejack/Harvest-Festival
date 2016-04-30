package joshie.harvest.blocks;

import joshie.harvest.blocks.tiles.*;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;

public class HFBlocks {
    //Cooking & Farming
    public static BlockCookware COOKWARE;
    public static BlockCrop CROPS;
    public static BlockFlower FLOWERS;
    //Mine
    public static BlockStone STONE;
    public static BlockDirt DIRT;
    //Misc
    public static BlockWood WOOD_MACHINES;
    public static BlockPreview PREVIEW;
    public static BlockGoddessWater GODDESS_WATER;
    public static Fluid GODDESS;

    public static void preInit() {

        //Cooking & Farming
        COOKWARE = (BlockCookware) new BlockCookware().setUnlocalizedName("cookware");
        CROPS = (BlockCrop) new BlockCrop().setUnlocalizedName("crops.block");
        FLOWERS = (BlockFlower) new BlockFlower().setUnlocalizedName("flowers.block");
        //Mine
        STONE = (BlockStone) new BlockStone().setUnlocalizedName("stone");
        DIRT = (BlockDirt) new BlockDirt().setUnlocalizedName("dirt");
        //Misc
        WOOD_MACHINES = (BlockWood) new BlockWood().setUnlocalizedName("general.block");
        PREVIEW = (BlockPreview) new BlockPreview().setUnlocalizedName("preview");
        GODDESS = new Fluid("hf_goddess_water", new ResourceLocation("blocks/hf_goddess_water_still"), new ResourceLocation("blocks/hf_goddess_water_flow")).setRarity(EnumRarity.RARE);
        FluidRegistry.registerFluid(GODDESS);
        GODDESS_WATER = new BlockGoddessWater(GODDESS).setUnlocalizedName("goddess.water");
        GODDESS.setBlock(GODDESS_WATER);

        RegistryHelper.registerTiles(HFModInfo.CAPNAME, TileCooking.class, TileFridge.class, TileFryingPan.class, TileCounter.class, TileMarker.class,
                TileMixer.class, TileOven.class, TilePot.class);
    }

    @SideOnly(Side.CLIENT)
    private static String sanitizeGeneral(String name) {
        name = name.replace(".", " ");
        name = WordUtils.capitalize(name);
        return name.replace(" ", "");
    }
}