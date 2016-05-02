package joshie.harvest.blocks;

import joshie.harvest.blocks.tiles.*;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.base.BlockHFBase;
import joshie.harvest.core.util.base.BlockHFBaseMeta;
import joshie.harvest.crops.CropStateMapper;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HFBlocks {
    //Fluid
    public static final Fluid GODDESS = registerFluid(new Fluid("hf_goddess_water", new ResourceLocation("blocks/hf_goddess_water_still"), new ResourceLocation("blocks/hf_goddess_water_flow")).setRarity(EnumRarity.RARE));
    public static final BlockFluidClassic GODDESS_WATER = new BlockGoddessWater(GODDESS).setUnlocalizedName("goddess.water");

    //Cooking & Farming
    public static final BlockHFBaseMeta COOKWARE = new BlockCookware().setUnlocalizedName("cookware");
    public static final BlockHFBaseMeta CROPS = new BlockCrop().setUnlocalizedName("crops.block");
    public static final BlockHFBase FLOWERS = new BlockFlower().setUnlocalizedName("flowers.block");
    //Mine
    public static BlockHFBaseMeta STONE = new BlockStone().setUnlocalizedName("stone");
    public static BlockHFBaseMeta DIRT = new BlockDirt().setUnlocalizedName("dirt");
    //Misc
    public static BlockHFBaseMeta WOOD_MACHINES = new BlockWood().setUnlocalizedName("general.block");
    public static BlockHFBaseMeta PREVIEW = new BlockPreview().setUnlocalizedName("preview");

    public static void preInit() {
        GODDESS.setBlock(GODDESS_WATER);

        RegistryHelper.registerTiles(HFModInfo.MODID, TileCooking.class, TileFridge.class, TileFryingPan.class, TileCounter.class, TileMarker.class,
                TileMixer.class, TileOven.class, TilePot.class);
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomStateMapper(CROPS, new CropStateMapper());
    }

    private static Fluid registerFluid(Fluid fluid) {
        FluidRegistry.registerFluid(fluid);
        return fluid;
    }
}