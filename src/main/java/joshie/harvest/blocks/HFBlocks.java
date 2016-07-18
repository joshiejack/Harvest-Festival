package joshie.harvest.blocks;

import joshie.harvest.blocks.BlockFlower.FlowerType;
import joshie.harvest.blocks.tiles.TileShipping;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class HFBlocks {
    //Fluid
    public static final Fluid GODDESS = registerFluid(new Fluid("goddess_water", new ResourceLocation(MODID, "blocks/goddess_still"), new ResourceLocation(MODID, "blocks/goddess_flow")).setRarity(EnumRarity.RARE));
    public static final BlockGoddessWater GODDESS_WATER = new BlockGoddessWater(GODDESS).register("goddess_water");
    public static final BlockFlower FLOWERS = new BlockFlower().register("flowers");
    public static final BlockStorage STORAGE = new BlockStorage().register("storage");
    public static final BlockRock ROCK = new BlockRock().register("rock");
    public static final BlockWood WOOD = new BlockWood().register("wood");

    public static void preInit() {
        GODDESS.setBlock(GODDESS_WATER);
        RegistryHelper.registerTiles(TileShipping.class);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
            public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
                FlowerType type = FLOWERS.getEnumFromState(state);
                if (!type.isColored()) return -1;
                return worldIn != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic();
            }
        }, FLOWERS);

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                return FLOWERS.getEnumFromMeta(stack.getItemDamage()).isColored() ? ColorizerFoliage.getFoliageColorBasic() : -1;
            }
        }, FLOWERS);
    }

    @SideOnly(Side.CLIENT)
    public static void registerFluidBlockRendering(Block block, String name) {
        final ModelResourceLocation fluidLocation = new ModelResourceLocation(MODID + ":fluids", name);

        ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return fluidLocation;
            }
        });
    }

    private static Fluid registerFluid(Fluid fluid) {
        FluidRegistry.registerFluid(fluid);
        return fluid;
    }
}