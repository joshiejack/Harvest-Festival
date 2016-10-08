package joshie.harvest.core;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.Ore;
import joshie.harvest.core.block.BlockFlower;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.block.BlockGoddessWater;
import joshie.harvest.core.block.BlockStorage;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.helpers.RegistryHelper;
import joshie.harvest.core.item.ItemSizeable;
import joshie.harvest.core.render.SizeableDefinition;
import joshie.harvest.core.tile.TileShipping;
import joshie.harvest.core.util.HFLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
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
import net.minecraftforge.oredict.OreDictionary;

import static joshie.harvest.core.helpers.ConfigHelper.getBoolean;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.lib.LoadOrder.HFCORE;

@HFLoader(priority = HFCORE)
public class HFCore {
    public static final Fluid GODDESS = registerFluid(new Fluid("goddess_water", new ResourceLocation(MODID, "blocks/goddess_still"), new ResourceLocation(MODID, "blocks/goddess_flow")).setRarity(EnumRarity.RARE));
    public static final BlockGoddessWater GODDESS_WATER = new BlockGoddessWater(GODDESS).register("goddess_water");
    public static final BlockFlower FLOWERS = new BlockFlower().register("flowers");
    public static final BlockStorage STORAGE = new BlockStorage().register("storage");
    public static final ItemSizeable SIZEABLE = new ItemSizeable().register("sizeable");
    public static final AxisAlignedBB FENCE_COLLISION =  new AxisAlignedBB(0D, 0D, 0D, 1D, 1.5D, 1D);

    @SuppressWarnings("unchecked")
    public static void preInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(HarvestFestival.instance, new GuiHandler());
        LootFunctionManager.registerFunction(new SetEnum.Serializer());
        LootFunctionManager.registerFunction(new SetSizeable.Serializer());
        RegistryHelper.registerTiles(TileShipping.class);
        GODDESS.setBlock(GODDESS_WATER);

        //Register sellables
        OreDictionary.registerOre("flowerGoddess", FLOWERS.getStackFromEnum(FlowerType.GODDESS));
        HFApi.shipping.registerSellable(new ItemStack(Items.FISH, 1, 0), 20L);
        HFApi.shipping.registerSellable(new ItemStack(Items.FISH, 1, 1), 60L);
        HFApi.shipping.registerSellable(new ItemStack(Items.FISH, 1, 2), 100L);
        HFApi.shipping.registerSellable(new ItemStack(Items.FISH, 1, 3), 100L);
        HFApi.shipping.registerSellable(new ItemStack(Items.BREAD), 85L);
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

    public static void postInit() {
        BlockGoddessWater.VALID_ITEMS.register(Items.APPLE);
        BlockGoddessWater.VALID_ITEMS.register(Blocks.WATERLILY);
        BlockGoddessWater.VALID_ITEMS.register(Blocks.MELON_BLOCK);
        BlockGoddessWater.VALID_ITEMS.register(Blocks.TALLGRASS);
        BlockGoddessWater.VALID_ITEMS.register(Blocks.DOUBLE_PLANT);
        BlockGoddessWater.VALID_ITEMS.register(Blocks.RED_FLOWER);
        BlockGoddessWater.VALID_ITEMS.register(Blocks.YELLOW_FLOWER);
        BlockGoddessWater.VALID_ITEMS.register(Ore.of("treeLeaves"));
        BlockGoddessWater.VALID_ITEMS.register(Ore.of("treeSapling"));
        BlockGoddessWater.VALID_ITEMS.register(Ore.of("vine"));
        BlockGoddessWater.VALID_ITEMS.register(Ore.of("sugarcane"));
        BlockGoddessWater.VALID_ITEMS.register(Ore.of("blockCactus"));
    }

    private static Fluid registerFluid(Fluid fluid) {
        FluidRegistry.registerFluid(fluid);
        return fluid;
    }

    //Configure
    public static boolean DEBUG_MODE;
    public static boolean SLEEP_ANYTIME;
    public static boolean NO_TICK_OFFLINE;

    public static void configure() {
        DEBUG_MODE = getBoolean("Debug Mode", false, "Enabling this adds extra information to items, when you have f3 debug mode on");
        SLEEP_ANYTIME = getBoolean("Sleep any time of day", true);
        NO_TICK_OFFLINE = getBoolean("Server doesn't update time when no players online", false);
    }
}
