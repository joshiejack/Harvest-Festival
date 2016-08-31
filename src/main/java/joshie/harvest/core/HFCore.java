package joshie.harvest.core;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.core.block.BlockFlower;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.block.BlockGoddessWater;
import joshie.harvest.core.block.BlockStorage;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import joshie.harvest.core.item.ItemSizeable;
import joshie.harvest.core.render.SizeableDefinition;
import joshie.harvest.core.tile.TileShipping;
import joshie.harvest.core.util.HFLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.lib.LoadOrder.HFCORE;

@HFLoader(priority = HFCORE)
public class HFCore {
    public static final Fluid GODDESS = registerFluid(new Fluid("goddess_water", new ResourceLocation(MODID, "blocks/goddess_still"), new ResourceLocation(MODID, "blocks/goddess_flow")).setRarity(EnumRarity.RARE));
    public static final BlockGoddessWater GODDESS_WATER = new BlockGoddessWater(GODDESS).register("goddess_water");
    public static final BlockFlower FLOWERS = new BlockFlower().register("flowers");
    public static final BlockStorage STORAGE = new BlockStorage().register("storage");
    public static final ItemSizeable SIZEABLE = new ItemSizeable().register("sizeable");

    public static void preInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(HarvestFestival.instance, new GuiHandler());
        LootFunctionManager.registerFunction(new SetEnum.Serializer());
        LootFunctionManager.registerFunction(new SetSizeable.Serializer());
        RegistryHelper.registerTiles(TileShipping.class);
        GODDESS.setBlock(GODDESS_WATER);

        //Register sellables
        HFApi.shipping.registerSellable(new ItemStack(Items.FISH, 1, 0), 50L);
        HFApi.shipping.registerSellable(new ItemStack(Items.FISH, 1, 1), 120L);
        HFApi.shipping.registerSellable(new ItemStack(Items.FISH, 1, 2), 200L);
        HFApi.shipping.registerSellable(new ItemStack(Items.FISH, 1, 3), 200L);
        HFApi.shipping.registerSellable(new ItemStack(Items.APPLE), 100L);
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        RegistryHelper.registerFluidBlockRendering(GODDESS_WATER, "goddess_water");
        ModelLoader.setCustomMeshDefinition(SIZEABLE, SizeableDefinition.INSTANCE);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
            public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
                FlowerType type = HFCore.FLOWERS.getEnumFromState(state);
                if (!type.isColored()) return -1;
                return worldIn != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic();
            }
        }, HFCore.FLOWERS);

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                return HFCore.FLOWERS.getEnumFromMeta(stack.getItemDamage()).isColored() ? ColorizerFoliage.getFoliageColorBasic() : -1;
            }
        }, HFCore.FLOWERS);

        //Register the models
        SizeableDefinition.INSTANCE.registerEverything();
    }

    private static Fluid registerFluid(Fluid fluid) {
        FluidRegistry.registerFluid(fluid);
        return fluid;
    }

    //Configure
    public static boolean DEBUG_MODE = true;
}
