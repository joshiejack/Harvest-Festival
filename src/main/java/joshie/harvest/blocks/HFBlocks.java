package joshie.harvest.blocks;

import joshie.harvest.blocks.tiles.*;
import joshie.harvest.buildings.render.PreviewRender;
import joshie.harvest.cooking.render.SpecialRendererCounter;
import joshie.harvest.cooking.render.SpecialRendererFryingPan;
import joshie.harvest.cooking.render.SpecialRendererMixer;
import joshie.harvest.cooking.render.SpecialRendererPot;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.base.BlockHFBase;
import joshie.harvest.core.util.base.BlockHFBaseEnum;
import joshie.harvest.crops.CropStateMapper;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HFBlocks {
    //Fluid
    public static final Fluid GODDESS = registerFluid(new Fluid("hf_goddess_water", new ResourceLocation("blocks/hf_goddess_water_still"), new ResourceLocation("blocks/hf_goddess_water_flow")).setRarity(EnumRarity.RARE));
    public static final BlockFluidClassic GODDESS_WATER = new BlockGoddessWater(GODDESS).setUnlocalizedName("goddess.water");

    //Cooking & Farming
    public static final BlockHFBaseEnum COOKWARE = new BlockCookware().setUnlocalizedName("cookware");
    public static final BlockHFBaseEnum FARMLAND = new BlockFarmland().setUnlocalizedName("farmland");
    public static final BlockHFBaseEnum CROPS = new BlockCrop().setUnlocalizedName("crops.block");
    public static final BlockHFBase FLOWERS = new BlockFlower().setUnlocalizedName("flowers.block");
    //Mine
    public static BlockHFBaseEnum STONE = new BlockStone().setUnlocalizedName("stone");
    public static BlockHFBaseEnum DIRT = new BlockDirt().setUnlocalizedName("dirt");
    //Misc
    public static BlockHFBaseEnum WOOD_MACHINES = new BlockWood().setUnlocalizedName("woodware");
    public static BlockPreview PREVIEW = (BlockPreview) new BlockPreview().setUnlocalizedName("preview");

    public static void preInit() {
        GODDESS.setBlock(GODDESS_WATER);
        RegistryHelper.registerTiles(HFModInfo.MODID, TileCooking.class, TileFridge.class, TileFryingPan.class, TileCounter.class, TileMarker.class,
                TileMixer.class, TileOven.class, TilePot.class);
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomStateMapper(CROPS, new CropStateMapper());
        ModelLoader.setCustomStateMapper(FARMLAND, new CropStateMapper());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMarker.class, new PreviewRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFryingPan.class, new SpecialRendererFryingPan());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePot.class, new SpecialRendererPot());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCounter.class, new SpecialRendererCounter());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMixer.class, new SpecialRendererMixer());
    }

    private static Fluid registerFluid(Fluid fluid) {
        FluidRegistry.registerFluid(fluid);
        return fluid;
    }
}