package joshie.harvest.crops;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.GrowthHandler;
import joshie.harvest.api.crops.WateringHandler;
import joshie.harvest.core.base.render.MeshIdentical;
import joshie.harvest.core.helpers.RegistryHelper;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.crops.block.*;
import joshie.harvest.crops.handlers.growth.GrowthHandlerNether;
import joshie.harvest.crops.handlers.growth.GrowthHandlerSide;
import joshie.harvest.crops.handlers.rules.SpecialRulesRanch;
import joshie.harvest.crops.handlers.rules.SpecialRulesYear;
import joshie.harvest.crops.item.ItemCrop;
import joshie.harvest.crops.item.ItemCrop.Crops;
import joshie.harvest.crops.item.ItemHFSeeds;
import joshie.harvest.crops.loot.SetCropType;
import joshie.harvest.crops.tile.*;
import joshie.harvest.shops.rules.SpecialRulesQuest;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;

import static joshie.harvest.api.animals.AnimalFoodType.FRUIT;
import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.core.handlers.DisableHandler.SEEDS_BLACKLIST;
import static joshie.harvest.core.helpers.ConfigHelper.getBoolean;
import static joshie.harvest.core.helpers.ConfigHelper.getInteger;
import static joshie.harvest.core.helpers.RegistryHelper.*;
import static joshie.harvest.core.lib.LoadOrder.HFCROPS;

@HFLoader(priority = HFCROPS)
public class HFCrops {
    //Crops and Trees
    public static final BlockHFCrops CROPS = new BlockHFCrops().register("crops_block");
    public static final BlockSprinkler SPRINKLER = new BlockSprinkler().register("sprinkler");
    public static final BlockFruit FRUITS = new BlockFruit().register("fruit");
    public static final BlockLeavesFruit LEAVES_FRUIT = new BlockLeavesFruit().register("leaves_fruit");
    public static final BlockLeavesTropical LEAVES_TROPICAL = new BlockLeavesTropical().register("leaves_tropical");
    public static final GrowthHandler SOUL_SAND = new GrowthHandlerNether();

    //Seed Bag Item
    public static final ItemHFSeeds SEEDS = new ItemHFSeeds().register("crops_seeds");
    public static final ItemCrop CROP = new ItemCrop().register("crops");
    private static final ISpecialRules YEAR2 = new SpecialRulesYear(1);

    //Spring Crops
    public static final Crop TURNIP = registerCrop("turnip").setItem(getCropStack(Crops.TURNIP)).setValue(300, 60).setStages(2, 4, 5).setSeedColours(0xFFFFFF);
    public static final Crop POTATO = registerCrop("potato").setItem(Items.POTATO).setValue(450, 100).setStages(Blocks.POTATOES, 1, 2, 3, 4, 5, 6, 7, 8).setSeedColours(0xBE8D2B);
    public static final Crop CUCUMBER = registerCrop("cucumber").setItem(getCropStack(Crops.CUCUMBER)).setValue(900, 50).setStages(4, 7, 9, 10).setRegrow(7).setSeedColours(0x36B313)
                                            .setAnimalFoodType(FRUIT).setPurchaseRules(YEAR2);
    public static final Crop STRAWBERRY = registerCrop("strawberry").setItem(getCropStack(Crops.STRAWBERRY)).setValue(800, 40).setStages(3, 6, 8, 9).setRegrow(7).setSeedColours(0xFF7BEA)
                                            .setAnimalFoodType(FRUIT).setPurchaseRules(new SpecialRulesQuest("strawberry"));
    public static final Crop CABBAGE = registerCrop("cabbage").setItem(getCropStack(Crops.CABBAGE)).setValue(1500, 460).setStages(4, 9, 14, 15).setSeedColours(0x8FFF40)
                                            .setPurchaseRules(new SpecialRulesQuest("cabbage"));

    private static final SpecialRulesQuest TOWN_PROGRESS = new SpecialRulesQuest("progress");
    //Summer Crops
    public static final Crop ONION = registerCrop("onion").setItem(getCropStack(Crops.ONION)).setValue(350, 120).setStages(3, 7, 8).setSeedColours(0XDCC307).setSeasons(SUMMER);
    public static final Crop TOMATO = registerCrop("tomato").setItem(getCropStack(Crops.TOMATO)).setValue(950, 80).setStages(2, 4, 6, 9, 10).setRegrow(7).setSeedColours(0XE60820)
                                        .setSeasons(SUMMER).setAnimalFoodType(FRUIT).setPurchaseRules(YEAR2);
    public static final Crop PUMPKIN = registerCrop("pumpkin").setItem(new ItemStack(Blocks.PUMPKIN)).setValue(750, 300).setStages(15).setSeedColours(0XE09A39).setSeasons(SUMMER).setIngredient(2, 0.3F);
    public static final Crop PINEAPPLE = registerCrop("pineapple").setItem(getCropStack(Crops.PINEAPPLE)).setValue(2000, 420).setStages(5, 10, 15, 20, 21).setRegrow(16).setSeedColours(0XD7CF00)
                                        .setSeasons(SUMMER).setAnimalFoodType(FRUIT).setPurchaseRules(new SpecialRulesQuest("pineapple"));
    public static final Crop WATERMELON = registerCrop("watermelon").setItem(Items.MELON).setValue(550, 30).setStages(11).setSeedColours(0xc92b3e)
                                        .setSeasons(SUMMER).setAnimalFoodType(FRUIT).setGrowthHandler(new GrowthHandlerSide(Blocks.MELON_BLOCK)).setPurchaseRules(TOWN_PROGRESS);
    //Summer Trees
    private static final SpecialRulesQuest TREES1 = new SpecialRulesQuest("trees1");
    private static final SpecialRulesQuest TREES2 = new SpecialRulesQuest("trees2");
    public static final Crop BANANA = registerTree("banana").setLogs(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, EnumType.JUNGLE)).setStageLength(15, 15, 25).setFruitRegrow(4)
                                        .setItem(getCropStack(Crops.BANANA)).setValue(2500, 300).setSeedColours(0xFFEF6A)
                                        .setSeasons(SUMMER).setPurchaseRules(TREES2).setAnimalFoodType(AnimalFoodType.FRUIT);
    public static final Crop ORANGE = registerTree("orange").setStageLength(12, 22, 15).setItem(getCropStack(Crops.ORANGE)).setValue(2800, 200).setSeedColours(0xEDB325)
                                        .setSeasons(SUMMER).setPurchaseRules(TREES1).setAnimalFoodType(AnimalFoodType.FRUIT);
    public static final Crop PEACH = registerTree("peach").setStageLength(6, 15, 15).setItem(getCropStack(Crops.PEACH)).setValue(3000, 250).setSeedColours(0xFFB0A5)
                                        .setSeasons(SUMMER).setPurchaseRules(TREES2).setAnimalFoodType(AnimalFoodType.FRUIT);

    //Autumn Crops
    public static final Crop EGGPLANT = registerCrop("eggplant").setItem(getCropStack(Crops.EGGPLANT)).setValue(750, 80).setStages(3, 6, 9, 10).setRegrow(7).setSeedColours(0XA25CC4)
                                        .setSeasons(AUTUMN).setPurchaseRules(YEAR2);
    public static final Crop SPINACH = registerCrop("spinach").setItem(getCropStack(Crops.SPINACH)).setValue(350, 100).setStages(2, 5, 6).setSeedColours(0X90AE15).setSeasons(AUTUMN);
    public static final Crop CARROT = registerCrop("carrot").setItem(Items.CARROT).setValue(500, 180).setStages(Blocks.CARROTS, 1, 2, 3, 4, 5, 6, 7, 8).setSeedColours(0XF8AC33).setSeasons(AUTUMN);
    public static final Crop SWEET_POTATO = registerCrop("sweet_potato").setItem(getCropStack(Crops.SWEET_POTATO)).setValue(1000, 60).setStages(3, 5, 6).setRegrow(4).setSeedColours(0XD82AAC)
                                            .setSeasons(AUTUMN).setPurchaseRules(new SpecialRulesQuest("sweetpotato"));
    public static final Crop GREEN_PEPPER = registerCrop("green_pepper").setItem(getCropStack(Crops.GREEN_PEPPER)).setValue(900, 80).setStages(1, 3, 4, 7, 8).setRegrow(5).setSeedColours(0x56D213)
                                            .setSeasons(AUTUMN).setPurchaseRules(new SpecialRulesQuest("greenpepper"));
    public static final Crop BEETROOT = registerCrop("beetroot").setItem(Items.BEETROOT).setValue(550, 330).setStages(Blocks.BEETROOTS, 3, 7, 10, 11).setSeedColours(0x690000)
                                            .setSeasons(AUTUMN).setPurchaseRules(TOWN_PROGRESS);

    //Autumn Trees
    public static final Crop APPLE = registerTree("apple").setStageLength(6, 15, 15).setItem(new ItemStack(Items.APPLE)).setValue(1500, 100).setSeedColours(0xE73921)
                                        .setSeasons(AUTUMN).setPurchaseRules(TREES1).setAnimalFoodType(AnimalFoodType.FRUIT);
    public static final Crop GRAPE = registerTree("grape").setStageLength(12, 22, 15).setItem(getCropStack(Crops.GRAPE)).setValue(2700, 200).setSeedColours(0xD58EF8)
                                        .setSeasons(AUTUMN).setPurchaseRules(TREES2).setAnimalFoodType(AnimalFoodType.FRUIT);

    //Year Long Crops
    public static final Crop GRASS = registerCrop("grass").setItem(getCropStack(Crops.GRASS)).setValue(500, 1).setStages(11).setRegrow(1).setSeedColours(0x7AC958).setSeasons(SPRING, SUMMER, AUTUMN)
                                        .setAnimalFoodType(AnimalFoodType.GRASS).setBecomesDouble(6).setHasAlternativeName().setRequiresSickle(6).setNoWaterRequirements().setPurchaseRules(new SpecialRulesRanch());
    public static final Crop WHEAT = registerCrop("wheat").setItem(Items.WHEAT).setValue(150, 100).setStages(Blocks.WHEAT, 2, 5, 9, 12, 16, 22, 27, 28).setSeedColours(0XEAC715).setSeasons(SPRING, SUMMER, AUTUMN)
                                        .setIngredient(1, 0.1F).setAnimalFoodType(AnimalFoodType.GRASS).setRequiresSickle(0);
    public static final Crop CORN = registerCrop("corn").setItem(getCropStack(Crops.CORN)).setValue(1200, 50).setStages(3, 8, 12, 14, 15).setRegrow(12).setSeedColours(0XD4BD45)
                                        .setSeasons(SUMMER, AUTUMN).setPurchaseRules(new SpecialRulesQuest("corn"));
    //TODO: Readd properly in 0.7
    private static final Crop NETHER_WART = registerCrop("nether_wart").setItem(Items.NETHER_WART).setValue(0, 10).setStages(Blocks.NETHER_WART, 1, 2, 3, 4).setRegrow(1).setSeedColours(0x8B0000)
                                            .setPlantType(EnumPlantType.Nether).setNoWaterRequirements().setGrowthHandler(SOUL_SAND).setPurchaseRules(new SpecialRulesQuest("netherwart")).setSkipRender();
    //Tutorial Crop
    public static final Crop TUTORIAL = registerCrop("tutorial_turnip").setItem(getCropStack(Crops.TUTORIAL_TURNIP)).setValue(0, 10).setStages(1, 2, 3).setSeedColours(0xACA262).setSeasons(SPRING, SUMMER, AUTUMN, WINTER);

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
        HFApi.crops.registerCropProvider(new ItemStack(Items.NETHER_WART), NETHER_WART);
        //registerVanillaCrop(Blocks.NETHER_WART, Items.NETHER_WART, NETHER_WART);
        HFApi.crops.registerWateringHandler(new WateringHandler());
        HFApi.shipping.registerSellable(new ItemStack(Items.POISONOUS_POTATO), 1L);
        RegistryHelper.registerTiles(TileWithered.class, TileCrop.class, TileSprinkler.class, TileSprinklerOld.class, TileFruit.class);
        if (DISABLE_VANILLA_MOISTURE) Blocks.FARMLAND.setTickRandomly(false);
        if (DISABLE_VANILLA_WHEAT_SEEDS || DISABLE_VANILLA_SEEDS) {
            SEEDS_BLACKLIST.register(Items.MELON_SEEDS);
            SEEDS_BLACKLIST.register(Items.PUMPKIN_SEEDS);
            SEEDS_BLACKLIST.register(Items.BEETROOT_SEEDS);
            SEEDS_BLACKLIST.register(Items.WHEAT_SEEDS);
            SEEDS_BLACKLIST.register(Items.CARROT);
            SEEDS_BLACKLIST.register(Items.POTATO);
        }

        //Register everything in the ore dictionary
        //Register always in the ore dictionary
        Crop.REGISTRY.values().stream().filter(crop -> crop != Crop.NULL_CROP).forEachOrdered(crop -> {
            //Register always in the ore dictionary
            ItemStack clone = crop.getCropStack(1);
            String name = "crop" + WordUtils.capitalizeFully(crop.getResource().getResourcePath(), '_').replace("_", "");
            RegistryHelper.registerOreIfNotExists(name, clone);
            HFApi.crops.registerCropProvider(clone, crop);
            HFApi.shipping.registerSellable(clone, crop.getSellValue());
        });

        registerOreIfNotExists("cropMelon", new ItemStack(Items.MELON));
        registerOreIfNotExists("cropMelon", new ItemStack(Blocks.MELON_BLOCK));
        registerOreIfNotExists("cropWatermelon", new ItemStack(Blocks.MELON_BLOCK));
        HFApi.crops.registerFarmlandToDirtMapping(Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 0), Blocks.DIRT.getDefaultState());
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomMeshDefinition(SEEDS, new MeshIdentical(SEEDS));
        ModelLoader.setCustomStateMapper(CROPS, new CropStateMapper());
    }

    public static void init() {
        HFApi.npc.getGifts().addToBlacklist(SEEDS);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
            Crop crop = HFCrops.SEEDS.getCropFromStack(stack);
            return crop != null ? crop.getColor() : -1;
        }, SEEDS);

        IBlockColor block = (state, worldIn, pos, tintIndex) -> tintIndex == 0 ? (worldIn != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic()) : -1;
        IItemColor item = (stack, index) -> ColorizerFoliage.getFoliageColorBasic();
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(block, LEAVES_FRUIT);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(block, LEAVES_TROPICAL);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(item, Item.getItemFromBlock(LEAVES_FRUIT));
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(item, Item.getItemFromBlock(LEAVES_TROPICAL));
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public static void postInitClient() {
        BlockColors colors = Minecraft.getMinecraft().getBlockColors();
        IBlockColor coloring = (state, world, pos, tintIndex) -> {
            if (world != null && pos != null) {
                IBlockState actual = world.getBlockState(pos);
                if (actual.getBlock() == HFCrops.CROPS) {
                    CropData data = CropHelper.getCropDataAt(world, pos);
                    if (data != null) {
                        Season season = CropHelper.getSeasonAt(world, pos);
                        return data.getCrop().getStateHandler().getColor(world, pos, state, season, data.getCrop(), BlockHFCrops.isWithered(actual));
                    }
                }
            }

            return -1;
        };

        //Register all the normal blocks as using the tint index
        colors.registerBlockColorHandler(coloring, CROPS);
        Crop.REGISTRY.values().stream().filter(crop -> crop != Crop.NULL_CROP && crop.skipLoadingRender())
                .forEachOrdered(crop -> colors.registerBlockColorHandler(coloring, ((IBlockState) crop.getStateHandler().getValidStates().get(0)).getBlock()));
    }

    public static ItemStack getCropStack(Crops crop) {
        return CROP.getStackFromEnum(crop);
    }

    //Configure
    public static boolean SEASONAL_BONEMEAL;
    public static boolean ENABLE_BONEMEAL;
    public static boolean GROWS_DAILY;
    public static boolean DISABLE_VANILLA_HOE;
    public static boolean DISABLE_VANILLA_SEEDS;
    public static boolean DISABLE_VANILLA_GROWTH;
    public static boolean DISABLE_VANILLA_DROPS;
    public static boolean DISABLE_VANILLA_WHEAT_SEEDS;
    public static boolean DISABLE_VANILLA_MOISTURE;
    public static int SPRINKLER_DRAIN_RATE;
    private static boolean CROPS_DIE_CLIENT;
    private static boolean CROPS_DIE_SERVER;
    public static boolean CROPS_SHOULD_DIE;

    public static void configure() {
        GROWS_DAILY = getBoolean("Crops grow daily", true, "This setting when set to true, will make crops grow based on day by day instead of based on random ticks");
        ENABLE_BONEMEAL = getBoolean("Enable bonemeal", false, "Enabling this will allow you to use bonemeal on plants to grow them.");
        SEASONAL_BONEMEAL = getBoolean("Seasonal bonemeal", true, "If you have bonemeal enabled, with this setting active, bonemeal will only work when the crop is in season");
        DISABLE_VANILLA_SEEDS = getBoolean("Disable vanilla seeds", true, "If this is true, vanilla seeds will not plant their crops");
        DISABLE_VANILLA_GROWTH = getBoolean("Disable vanilla growth", true, "If this is true, vanilla crops will not grow");
        DISABLE_VANILLA_DROPS = getBoolean("Disable vanilla drops", true, "If this is true, vanilla crops will not drop their items");
        DISABLE_VANILLA_WHEAT_SEEDS = getBoolean("Disable seed drops from grass", true, "If this is true, grass will not drop wheat seeds");
        DISABLE_VANILLA_HOE = getBoolean("Disable vanilla hoes", false, "If this is true, vanilla hoes will not till dirt");
        DISABLE_VANILLA_MOISTURE = getBoolean("Disable vanilla moisture", true, "If this is set to true then farmland will not automatically become wet, and must be watered, it will also not automatically revert to dirt. (Basically disables random ticks for farmland)");
        SPRINKLER_DRAIN_RATE = getInteger("Sprinkler's daily water consumption", 0, "This number NEEDs to be a factor of 1000, Otherwise you'll have trouble refilling the sprinkler manually. Acceptable values are: 1, 2, 4, 5, 8, 10, 20, 25, 40, 50, 100, 125, 200, 250, 500, 1000");
        CROPS_DIE_CLIENT = getBoolean("Integrated Server > Crops die when not having been watered", true);
        CROPS_DIE_SERVER = getBoolean("Dedicated Server > Crops die when not having been watered", false);
    }

    @SuppressWarnings("unused")
    public static void onServerStarting() {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server.isDedicatedServer()) {
            CROPS_SHOULD_DIE = HFCrops.CROPS_DIE_SERVER;
        } else CROPS_SHOULD_DIE = HFCrops.CROPS_DIE_CLIENT;
    }
}