package joshie.harvest.crops;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.crops.*;
import joshie.harvest.core.base.render.FMLDefinition;
import joshie.harvest.core.base.render.MeshIdentical;
import joshie.harvest.core.handlers.DisableHandler;
import joshie.harvest.core.helpers.RegistryHelper;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.crops.block.BlockHFCrops;
import joshie.harvest.crops.block.BlockSprinkler;
import joshie.harvest.crops.handlers.drop.DropHandlerGrass;
import joshie.harvest.crops.handlers.drop.DropHandlerMelon;
import joshie.harvest.crops.handlers.drop.DropHandlerNetherWart;
import joshie.harvest.crops.handlers.growth.GrowthHandlerNether;
import joshie.harvest.crops.handlers.state.*;
import joshie.harvest.crops.item.ItemCrop;
import joshie.harvest.crops.item.ItemHFSeeds;
import joshie.harvest.crops.tile.TileCrop;
import joshie.harvest.crops.tile.TileSprinkler;
import joshie.harvest.crops.tile.TileWithered;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Iterator;
import java.util.List;

import static joshie.harvest.api.animals.AnimalFoodType.FRUIT;
import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.core.HFTab.FARMING;
import static joshie.harvest.core.helpers.ConfigHelper.getBoolean;
import static joshie.harvest.core.helpers.ConfigHelper.getInteger;
import static joshie.harvest.core.lib.HFModInfo.*;
import static joshie.harvest.core.lib.LoadOrder.HFCROPS;
import static net.minecraft.init.Blocks.POTATOES;

@HFLoader(priority = HFCROPS)
public class HFCrops {
    //Crops and Custom Farmland
    public static final BlockHFCrops CROPS = new BlockHFCrops().register("crops_block");
    public static final BlockSprinkler SPRINKLER = new BlockSprinkler().register("sprinkler");
    public static final GrowthHandler SOUL_SAND = new GrowthHandlerNether();

    //Seed Bag Item
    public static final ItemHFSeeds SEEDS = new ItemHFSeeds().register("crops_seeds");
    public static final ItemCrop CROP = new ItemCrop().register("crops");

    //Spring Crops
    public static final Crop TURNIP = registerCrop("turnip").setGoldValues(120, 60).setStages(5).setSeedColours(0xFFFFFF).setFoodStats(1, 0.4F);
    public static final Crop POTATO = registerCrop("potato").setGoldValues(150, 80).setStages(8).setSeedColours(0xBE8D2B).setItem(Items.POTATO).setStateHandler(new StateHandlerSeedFood(POTATOES));
    public static final Crop CUCUMBER = registerCrop("cucumber").setGoldValues(100, 60).setStages(10).setRegrowStage(5).setSeedColours(0x36B313).setFoodStats(2, 0.25F).setAnimalFoodType(FRUIT);
    public static final Crop STRAWBERRY = registerCrop("strawberry").setGoldValues(150, 30).setStages(9).setRegrowStage(7).setYearUnlocked(1).setSeedColours(0xFF7BEA).setFoodStats(3, 0.8F).setAnimalFoodType(FRUIT);
    public static final Crop CABBAGE = registerCrop("cabbage").setGoldValues(500, 250).setStages(15).setYearUnlocked(3).setSeedColours(0x8FFF40).setFoodStats(1, 0.5F);

    //Summer Crops
    public static final Crop ONION = registerCrop("onion", 150L, 80L, 8, 0, 0, 0XDCC307, SUMMER).setFoodStats(1, 0.4F).setStateHandler(new StateHandlerOnion());
    public static final Crop TOMATO = registerCrop("tomato", 200L, 60L, 10, 7, 0, 0XE60820, SUMMER).setFoodStats(3, 0.5F).setAnimalFoodType(FRUIT).setStateHandler(new StateHandlerTomato());
    public static final Crop CORN = registerCrop("corn", 300L, 100L, 15, 12, 0, 0XD4BD45, SUMMER).setFoodStats(2, 0.3F).setStateHandler(new StateHandlerCorn());
    public static final Crop PUMPKIN = registerCrop("pumpkin", 500L, 125L, 15, 0, 1, 0XE09A39, SUMMER).setIngredient(new Ingredient("pumpkin", 2, 0.3F)).setItem(new ItemStack(Blocks.PUMPKIN)).setGrowsToSide(Blocks.PUMPKIN).setStateHandler(new StateHandlerPumpkin());
    public static final Crop PINEAPPLE = registerCrop("pineapple", 1000L, 500L, 21, 5, 3, 0XD7CF00, SUMMER).setFoodStats(2, 1.34F).setAnimalFoodType(FRUIT).setStateHandler(new StateHandlerPineapple());
    public static final Crop WATERMELON = registerCrop("watermelon", 250L, 25L, 11, 0, 1, 0xc92b3e, SUMMER).setItem(new ItemStack(Items.MELON)).setAnimalFoodType(FRUIT).setDropHandler(new DropHandlerMelon()).setGrowsToSide(Blocks.MELON_BLOCK).setStateHandler(new StateHandlerMelon());

    //Autumn Crops
    public static final Crop EGGPLANT = registerCrop("eggplant", 120L, 80L, 10, 7, 0, 0XA25CC4, AUTUMN).setFoodStats(3, 1.1F).setStateHandler(new StateHandlerEggplant());
    public static final Crop SPINACH = registerCrop("spinach", 200L, 80L, 6, 0, 1, 0X90AE15, AUTUMN).setFoodStats(2, 1.0F).setStateHandler(new StateHandlerSpinach());
    public static final Crop CARROT = registerCrop("carrot", 300L, 120L, 8, 0, 0, 0XF8AC33, AUTUMN).setItem(new ItemStack(Items.CARROT)).setStateHandler(new StateHandlerSeedFood(Blocks.CARROTS));
    public static final Crop SWEET_POTATO = registerCrop("sweet_potato", 300L, 120L, 6, 4, 0, 0XD82AAC, AUTUMN).setFoodStats(2, 0.35F).setStateHandler(new StateHandlerSweetPotato());
    public static final Crop GREEN_PEPPER = registerCrop("green_pepper", 150L, 40L, 8, 2, 3, 0x56D213, AUTUMN).setFoodStats(2, 0.5F).setStateHandler(new StateHandlerGreenPepper());
    public static final Crop BEETROOT = registerCrop("beetroot", 250L, 75L, 8, 0, 0, 0x690000, AUTUMN).setItem(new ItemStack(Items.BEETROOT)).setStateHandler(new StateHandlerBeetroot());

    //Year Long Crops
    public static final Crop GRASS = registerCrop("grass", 500L, 1L, 11, 1, 0, 0x7AC958, SPRING, SUMMER, AUTUMN).setWitheredColor(0x7a5230).setAnimalFoodType(AnimalFoodType.GRASS).setDropHandler(new DropHandlerGrass()).setBecomesDouble(6).setHasAlternativeName().setRequiresSickle(6).setNoWaterRequirements().setStateHandler(new StateHandlerGrass());
    public static final Crop WHEAT = registerCrop("wheat", 150L, 100L, 28, 0, 0, 0XEAC715, SPRING, SUMMER, AUTUMN).setIngredient(new Ingredient("wheat", 1, 0.1F)).setItem(new ItemStack(Items.WHEAT)).setAnimalFoodType(AnimalFoodType.GRASS).setRequiresSickle(0).setStateHandler(new StateHandlerWheat());

    //Nether Crops
    public static final Crop NETHER_WART = registerCrop("nether_wart", 25000L, 10L, 4, 1, 1, 0x8B0000).setItem(new ItemStack(Items.NETHER_WART)).setStateHandler(new StateHandlerNetherWart()).setPlantType(EnumPlantType.Nether).setNoWaterRequirements().setGrowthHandler(SOUL_SAND).setDropHandler(new DropHandlerNetherWart());

    //Tutorial Crop
    public static final Crop TUTORIAL = registerCrop("tutorial_turnip", 0L, 1L, 3, 0, 0, 0xACA262, SPRING, SUMMER, AUTUMN, WINTER).setFoodStats(1, 0.1F);

    @SuppressWarnings("unchecked")
    public static void preInit() {
        //Register the crop serializer
        LootFunctionManager.registerFunction(new SetCropType.Serializer());
        registerVanillaCrop(Blocks.WHEAT, Items.WHEAT, WHEAT);
        registerVanillaCrop(Blocks.CARROTS, Items.CARROT, CARROT);
        registerVanillaCrop(Blocks.POTATOES, Items.POTATO, POTATO);
        registerVanillaCrop(Blocks.BEETROOTS, Items.BEETROOT, BEETROOT);
        registerVanillaCrop(Blocks.MELON_STEM, Items.MELON, WATERMELON);
        registerVanillaCrop(Blocks.PUMPKIN_STEM, Blocks.PUMPKIN, PUMPKIN);
        registerVanillaCrop(Blocks.NETHER_WART, Items.NETHER_WART, NETHER_WART);
        HFApi.crops.registerWateringHandler(new WateringHandler());
        HFApi.shipping.registerSellable(new ItemStack(Items.POISONOUS_POTATO), 1L);
        RegistryHelper.registerTiles(TileWithered.class, TileCrop.class, TileSprinkler.class);
        if (DISABLE_VANILLA_WHEAT_SEEDS) RegistryHelper.removeSeed(new ItemStack(Items.WHEAT_SEEDS));
        if (DISABLE_VANILLA_MOISTURE) Blocks.FARMLAND.setTickRandomly(false);
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomMeshDefinition(SEEDS, new MeshIdentical(SEEDS));
        ModelLoader.setCustomStateMapper(CROPS, new CropStateMapper());
        ModelLoader.setCustomMeshDefinition(CROP, new FMLDefinition<Crop>(CROP, "crops", Crop.REGISTRY) {
            @Override
            public boolean shouldSkip(Crop crop) {
                return super.shouldSkip(crop) || crop.getCropStack(1).getItem() != CROP || crop.skipLoadingRender();
            }
        });
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
            Crop crop = HFCrops.SEEDS.getCropFromStack(stack);
            return crop != null ? crop.getColor() : -1;
        }, SEEDS);

        BlockColors colors = Minecraft.getMinecraft().getBlockColors();
        IBlockColor coloring = (state, world, pos, tintIndex) -> {
            if (world != null && pos != null) {
                IBlockState actual = world.getBlockState(pos);
                if (actual.getBlock() == HFCrops.CROPS) {
                    if (BlockHFCrops.isWithered(actual)) {
                        CropData data = CropHelper.getCropDataAt(world, pos);
                        return data != null ? data.getCrop().getWitheredColor() : 0xA64DFF;
                    }
                }
            }

            return -1;
        };

        //Register all the normal blocks as using the tint index
        colors.registerBlockColorHandler(coloring, CROPS);
        for (Crop crop : Crop.REGISTRY) {
            if (crop != Crop.NULL_CROP && crop.skipLoadingRender()) {
                colors.registerBlockColorHandler(coloring, crop.getStateHandler().getValidStates().get(0).getBlock());
            }
        }

        //Register the models
        FMLDefinition.getDefinition("crops").registerEverything();
    }

    private static List<List<ItemStack>> idToStack = ReflectionHelper.getPrivateValue(OreDictionary.class, null, "idToStack");

    public static void remap() {
        //Remove my existing entries
        for (int i = 0; i < idToStack.size(); i++) {
            Iterator<ItemStack> it = idToStack.get(i).iterator();
            while (it.hasNext()) {
                if (it.next().getItem() == CROP) {
                    it.remove();
                }
            }
        }

        OreDictionary.rebakeMap();

        //Register everything
        for (Crop crop : Crop.REGISTRY.getValues()) {
            if (crop != Crop.NULL_CROP) {
                //Register always in the ore dictionary
                ItemStack clone = crop.getCropStack(1);
                String name = "crop" + WordUtils.capitalizeFully(crop.getRegistryName().getResourcePath(), '_').replace("_", "");
                if (!isInDictionary(name, clone)) {
                    OreDictionary.registerOre(name, clone);
                }
            }
        }
    }

    private static void registerVanillaCrop(Block cropBlock, Item item, Crop crop) {
        HFApi.crops.registerCropProvider(new ItemStack(item), crop);
        crop.setSkipRender();
        item.setCreativeTab(FARMING);
        if (DISABLE_VANILLA_GROWTH || DISABLE_VANILLA_DROPS) DisableHandler.CROPS.add(cropBlock);
        if (DISABLE_VANILLA_GROWTH) {
            cropBlock.setTickRandomly(false);
        }
    }

    private static void registerVanillaCrop(Block cropBlock, Block block, Crop crop) {
        HFApi.crops.registerCropProvider(new ItemStack(block), crop);
        crop.setSkipRender();
        block.setCreativeTab(FARMING);
        if (DISABLE_VANILLA_GROWTH || DISABLE_VANILLA_DROPS) DisableHandler.CROPS.add(cropBlock);
        if (DISABLE_VANILLA_GROWTH) {
            cropBlock.setTickRandomly(false);
        }
    }

    private static Crop registerCrop(String name) {
        Crop crop = new Crop(new ResourceLocation(MODID, name));

        //Atempt to add a state handler
        try {
            crop.setStateHandler((IStateHandler) Class.forName(CROPSTATES + WordUtils.capitalizeFully(name.replace("_", " ")).replace(" ", "")).newInstance());
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {/**/}

        //Atempt to add a drop handler
        try {
            crop.setDropHandler((DropHandler) Class.forName(DROPHANDLERS + WordUtils.capitalizeFully(name.replace("_", " ")).replace(" ", "")).newInstance());
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {/**/}

        return crop;
    }

    private static Crop registerCrop(String name, long cost, long sell, int stages, int regrow, int year, int color, Season... seasons) {
        return new Crop(new ResourceLocation(MODID, name), cost, sell, stages, color, seasons).setRegrowStage(regrow).setYearUnlocked(year);
    }

    private static boolean isInDictionary(String name, ItemStack stack) {
        for (ItemStack check: OreDictionary.getOres(name)) {
            if (check.getItem() == stack.getItem() && (check.getItemDamage() == OreDictionary.WILDCARD_VALUE || check.getItemDamage() == stack.getItemDamage())) {
                return true;
            }
        }

        return false;
    }

    //Configure
    public static boolean SEASONAL_BONEMEAL;
    public static boolean ENABLE_BONEMEAL;
    public static boolean ALWAYS_GROW;
    public static boolean DISABLE_VANILLA_HOE;
    public static boolean DISABLE_VANILLA_SEEDS;
    public static boolean DISABLE_VANILLA_GROWTH;
    public static boolean DISABLE_VANILLA_DROPS;
    public static boolean DISABLE_VANILLA_WHEAT_SEEDS;
    public static boolean DISABLE_VANILLA_MOISTURE;
    public static int SPRINKLER_DRAIN_RATE;
    public static boolean VALIDATE_FARMLAND;
    private static boolean CROPS_DIE_CLIENT;
    private static boolean CROPS_DIE_SERVER;
    public static boolean CROPS_SHOULD_DIE;

    public static void configure() {
        ALWAYS_GROW = getBoolean("Crops always grow", false, "This setting when set to true, will make crops grow based on random tick instead of day by day, Take note that this also affects the number of seeds a crop bag will plant. It will only plant 3 seeds instead of a 3x3");
        ENABLE_BONEMEAL = getBoolean("Enable bonemeal", false, "Enabling this will allow you to use bonemeal on plants to grow them.");
        SEASONAL_BONEMEAL = getBoolean("Seasonal bonemeal", true, "If you have bonemeal enabled, with this setting active, bonemeal will only work when the crop is in season");
        DISABLE_VANILLA_SEEDS = getBoolean("Disable vanilla seeds", true, "If this is true, vanilla seeds will not plant their crops");
        DISABLE_VANILLA_GROWTH = getBoolean("Disable vanilla growth", true, "If this is true, vanilla crops will not grow");
        DISABLE_VANILLA_DROPS = getBoolean("Disable vanilla drops", true, "If this is true, vanilla crops will not drop their items");
        DISABLE_VANILLA_WHEAT_SEEDS = getBoolean("Disable seed drops from grass", true, "If this is true, grass will not drop wheat seeds");
        DISABLE_VANILLA_HOE = getBoolean("Disable vanilla hoes", false, "If this is true, vanilla hoes will not till dirt");
        DISABLE_VANILLA_MOISTURE = getBoolean("Disable vanilla moisture", true, "If this is set to true then farmland will not automatically become wet, and must be watered, it will also not automatically revert to dirt. (Basically disables random ticks for farmland)");
        SPRINKLER_DRAIN_RATE = getInteger("Sprinkler's daily consumption", 250, "This number NEEDs to be a factor of 1000, Otherwise you'll have trouble refilling the sprinkler manually. Acceptable values are: 1, 2, 4, 5, 8, 10, 20, 25, 40, 50, 100, 125, 200, 250, 500, 1000");
        VALIDATE_FARMLAND = getBoolean("Check for farmland on chunk load", true, "Disable this if you think it will help...");
        CROPS_DIE_CLIENT = getBoolean("Integrated Server > Crops die when not having been watered", true);
        CROPS_DIE_SERVER = getBoolean("Dedicated Server > Crops die when not having been watered", false);
    }

    public static void onServerStarting() {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server.isDedicatedServer()) {
            CROPS_SHOULD_DIE = HFCrops.CROPS_DIE_SERVER;
        } else CROPS_SHOULD_DIE = HFCrops.CROPS_DIE_CLIENT;
    }
}