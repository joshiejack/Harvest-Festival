package joshie.harvest.core;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.core.block.*;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.entity.EntityBasket;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.RegistryHelper;
import joshie.harvest.core.lib.EntityIDs;
import joshie.harvest.core.loot.SetEnum;
import joshie.harvest.core.loot.SetSizeable;
import joshie.harvest.core.render.RenderBasket;
import joshie.harvest.core.render.SpecialRendererBasket;
import joshie.harvest.core.render.SpecialRendererMailbox;
import joshie.harvest.core.render.SpecialRendererPlate;
import joshie.harvest.core.tile.*;
import joshie.harvest.core.util.annotations.HFLoader;
import net.minecraft.block.BlockFlower.EnumFlowerColor;
import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nonnull;

import static joshie.harvest.core.helpers.ConfigHelper.getBoolean;
import static joshie.harvest.core.helpers.ConfigHelper.getInteger;
import static joshie.harvest.core.helpers.RegistryHelper.registerSounds;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.lib.LoadOrder.HFCORE;
import static net.minecraft.block.BlockDoublePlant.EnumPlantType.*;
import static net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity;

@HFLoader(priority = HFCORE)
@SuppressWarnings("unused")
public class HFCore {
    public static final Fluid GODDESS = registerFluid(new Fluid("goddess_water", new ResourceLocation(MODID, "blocks/goddess_still"), new ResourceLocation(MODID, "blocks/goddess_flow")).setRarity(EnumRarity.RARE));
    public static final BlockGoddessWater GODDESS_WATER = new BlockGoddessWater(GODDESS).register("goddess_water");
    public static final BlockFlower FLOWERS = new BlockFlower().register("flowers");
    public static final BlockStorage STORAGE = new BlockStorage().register("storage");
    public static final BlockStand STAND = new BlockStand().register("stand");
    public static final BlockDecorative DECORATIVE = new BlockDecorative().register("decorative");
    public static final AxisAlignedBB FENCE_COLLISION =  new AxisAlignedBB(0D, 0D, 0D, 1D, 1.5D, 1D);

    @SuppressWarnings("unchecked")
    public static void preInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(HarvestFestival.instance, new GuiHandler());
        LootFunctionManager.registerFunction(new SetEnum.Serializer());
        LootFunctionManager.registerFunction(new SetSizeable.Serializer());
        RegistryHelper.registerTiles(TileShipping.class, TileMailbox.class, TilePlate.class, TileBasket.class, TileFestivalPot.class);
        registerModEntity(new ResourceLocation(MODID, "basket"), EntityBasket.class, "basket", EntityIDs.BASKET, HarvestFestival.instance, 150, 3, true);
        registerSounds("kerching");
        GODDESS.setBlock(GODDESS_WATER);

        //Register Flowers
        registerIfNotRegistered("flowerPinkcat", FLOWERS.getStackFromEnum(FlowerType.PINKCAT));
        registerIfNotRegistered("flowerToy", FLOWERS.getStackFromEnum(FlowerType.TOY));
        registerIfNotRegistered("flowerMagicBlue", FLOWERS.getStackFromEnum(FlowerType.BLUE_MAGICGRASS));
        registerIfNotRegistered("flowerMagicRed", FLOWERS.getStackFromEnum(FlowerType.RED_MAGICGRASS));
        registerIfNotRegistered("flowerMoondrop", FLOWERS.getStackFromEnum(FlowerType.MOONDROP));
        registerIfNotRegistered("flowerSunflower", new ItemStack(Blocks.DOUBLE_PLANT, 1, SUNFLOWER.getMeta()));
        registerIfNotRegistered("flowerLilac", new ItemStack(Blocks.DOUBLE_PLANT, 1, SYRINGA.getMeta()));
        registerIfNotRegistered("flowerRose", new ItemStack(Blocks.DOUBLE_PLANT, 1, ROSE.getMeta()));
        registerIfNotRegistered("flowerPeony", new ItemStack(Blocks.DOUBLE_PLANT, 1, PAEONIA.getMeta()));
        registerIfNotRegistered("flowerDandelion", new ItemStack(Blocks.YELLOW_FLOWER));
        for (EnumFlowerType type: getTypes(EnumFlowerColor.RED)) {
            registerIfNotRegistered("flower" + WordUtils.capitalize(type.getUnlocalizedName()), new ItemStack(Blocks.RED_FLOWER, 1, type.getMeta()));
        }
    }

    private static void registerIfNotRegistered(String string, @Nonnull ItemStack stack) {
        if (!InventoryHelper.isOreName(stack, string)) {
            OreDictionary.registerOre(string, stack);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        RegistryHelper.registerFluidBlockRendering(GODDESS_WATER, "goddess_water");
        RenderingRegistry.registerEntityRenderingHandler(EntityBasket.class, RenderBasket::new);
    }

    public static void init() {
        HFApi.npc.getGifts().addToBlacklist(Items.BUCKET, Items.LAVA_BUCKET, Items.WATER_BUCKET, Items.FLINT_AND_STEEL, Items.BOW, Items.ARROW, Items.MINECART, Items.CHEST_MINECART, Items.FURNACE_MINECART,
                Items.BOAT, Items.ACACIA_BOAT, Items.BIRCH_BOAT, Items.DARK_OAK_BOAT, Items.JUNGLE_BOAT, Items.SPRUCE_BOAT, Items.FISHING_ROD, Items.SHEARS, Items.SPAWN_EGG, Items.TNT_MINECART,
                Items.DIAMOND_HORSE_ARMOR, Items.GOLDEN_HORSE_ARMOR, Items.IRON_HORSE_ARMOR, Items.SPECTRAL_ARROW, Items.TIPPED_ARROW);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileBasket.class, new SpecialRendererBasket());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePlate.class, new SpecialRendererPlate());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFestivalPot.class, new SpecialRendererFestivalPot());
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
                FlowerType type = HFCore.FLOWERS.getEnumFromState(state);
                if (!type.isColored()) return -1;
                return worldIn != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic();
        }, HFCore.FLOWERS);

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) ->
                HFCore.FLOWERS.getEnumFromMeta(stack.getItemDamage()).isColored() ? ColorizerFoliage.getFoliageColorBasic() : -1 , HFCore.FLOWERS);

        ClientRegistry.bindTileEntitySpecialRenderer(TileMailbox.class, new SpecialRendererMailbox());
    }

    private static Fluid registerFluid(Fluid fluid) {
        FluidRegistry.registerFluid(fluid);
        return fluid;
    }

    private static EnumFlowerType[] getTypes(EnumFlowerColor flowerColor) {
        EnumFlowerType[][] TYPES_FOR_BLOCK = ReflectionHelper.getPrivateValue(EnumFlowerType.class, null, "TYPES_FOR_BLOCK", "field_176981_k");
        return TYPES_FOR_BLOCK[flowerColor.ordinal()];
    }

    //Configure
    public static boolean DEBUG_MODE;
    public static boolean SLEEP_ANYTIME;
    public static boolean SLEEP_ONLY_AT_NIGHT;
    public static boolean NO_TICK_OFFLINE;
    public static boolean DISPLAY_SHIPPED_LIST;
    public static int DISPLAY_SHIPPED_TICKS_ON_SCREEN;
    public static int MOBS_ONLY_SPAWN_UNDERGROUND_IN_OVERWORLD;

    public static void configure() {
        DEBUG_MODE = getBoolean("Debug Mode", false, "Enabling this adds extra information to items, when you have f3 debug mode on");
        SLEEP_ANYTIME = getBoolean("Sleep any time of day", true);
        SLEEP_ONLY_AT_NIGHT = getBoolean("Disable sleep between 6am and sunset", false);
        NO_TICK_OFFLINE = getBoolean("Server doesn't update time when no players online", false);
        DISPLAY_SHIPPED_LIST = getBoolean("Shipped items list > Enabled", true, "Will display a list of items and how much they were sold for when they day changes. Needs to be enabled on the client and the server to work");
        DISPLAY_SHIPPED_TICKS_ON_SCREEN = getInteger("Shipped items list > Ticks Displayed", 500, "This is the number of ticks the list will stay on the screen for, before scrolling off");
        MOBS_ONLY_SPAWN_UNDERGROUND_IN_OVERWORLD = getInteger("Mobs in overworld only spawn under Y value", 60, "Set to 0, or less for no mob spawns. Set to 256 or greater to disable.");
    }
}
