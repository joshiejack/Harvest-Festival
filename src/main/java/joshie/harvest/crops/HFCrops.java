package joshie.harvest.crops;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.GrowthHandler;
import joshie.harvest.api.crops.WateringHandler;
import joshie.harvest.core.base.render.MeshIdentical;
import joshie.harvest.core.helpers.RegistryHelper;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.crops.block.BlockFruit;
import joshie.harvest.crops.block.BlockHFCrops;
import joshie.harvest.crops.block.BlockLeavesTree;
import joshie.harvest.crops.block.BlockSprinkler;
import joshie.harvest.crops.handlers.growth.GrowthHandlerNether;
import joshie.harvest.crops.handlers.growth.GrowthHandlerSide;
import joshie.harvest.crops.handlers.rules.SpecialRulesQuest;
import joshie.harvest.crops.handlers.rules.SpecialRulesRanch;
import joshie.harvest.crops.item.ItemCrop;
import joshie.harvest.crops.item.ItemCrop.Crops;
import joshie.harvest.crops.item.ItemHFSeeds;
import joshie.harvest.crops.loot.SetCropType;
import joshie.harvest.crops.tile.TileCrop;
import joshie.harvest.crops.tile.TileSprinkler;
import joshie.harvest.crops.tile.TileSprinklerOld;
import joshie.harvest.crops.tile.TileWithered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;

import static joshie.harvest.api.animals.AnimalFoodType.FRUIT;
import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.core.handlers.DisableHandler.BLACKLIST;
import static joshie.harvest.core.helpers.ConfigHelper.getBoolean;
import static joshie.harvest.core.helpers.ConfigHelper.getInteger;
import static joshie.harvest.core.helpers.RegistryHelper.*;
import static joshie.harvest.core.lib.LoadOrder.HFCROPS;
import static net.minecraft.init.Items.*;

@HFLoader(priority = HFCROPS)
public class HFCrops {
    //Crops and Trees
    public static final BlockHFCrops CROPS = new BlockHFCrops().register("crops_block");
    public static final BlockSprinkler SPRINKLER = new BlockSprinkler().register("sprinkler");
    public static final BlockFruit FRUITS = new BlockFruit().register("fruit");
    public static final BlockLeavesTree LEAVES = new BlockLeavesTree().register("leaves");
    public static final GrowthHandler SOUL_SAND = new GrowthHandlerNether();

    //Seed Bag Item
    public static final ItemHFSeeds SEEDS = new ItemHFSeeds().register("crops_seeds");
    public static final ItemCrop CROP = new ItemCrop().register("crops");

    //Spring Crops
    public static final Crop TURNIP = registerCrop("turnip").setItem(getCropStack(Crops.TURNIP)).setValue(120, 40).setStages(2, 4, 5).setSeedColours(0xFFFFFF);
    public static final Crop POTATO = registerCrop("potato").setItem(Items.POTATO).setValue(150, 80).setStages(Blocks.POTATOES, 1, 2, 3, 4, 5, 6, 7, 8).setSeedColours(0xBE8D2B);
    public static final Crop CUCUMBER = registerCrop("cucumber").setItem(getCropStack(Crops.CUCUMBER)).setValue(300, 40).setUnlocked(2).setStages(4, 7, 9, 10).setRegrow(7).setSeedColours(0x36B313).setAnimalFoodType(FRUIT);
    public static final Crop STRAWBERRY = registerCrop("strawberry").setItem(getCropStack(Crops.STRAWBERRY)).setValue(200, 30).setStages(3, 6, 8, 9).setRegrow(7).setSeedColours(0xFF7BEA)
                                            .setAnimalFoodType(FRUIT).setPurchaseRules(new SpecialRulesQuest("strawberry"));
    public static final Crop CABBAGE = registerCrop("cabbage").setItem(getCropStack(Crops.CABBAGE)).setValue(500, 260).setStages(4, 9, 14, 15).setSeedColours(0x8FFF40)
                                            .setPurchaseRules(new SpecialRulesQuest("cabbage"));

    private static final SpecialRulesQuest TOWN_PROGRESS = new SpecialRulesQuest("progress");
    //Summer Crops
    public static final Crop ONION = registerCrop("onion").setItem(getCropStack(Crops.ONION)).setValue(150, 80).setStages(3, 7, 8).setSeedColours(0XDCC307).setSeasons(SUMMER);
    public static final Crop TOMATO = registerCrop("tomato").setItem(getCropStack(Crops.TOMATO)).setValue(650, 50).setUnlocked(2).setStages(2, 4, 6, 9, 10).setRegrow(7).setSeedColours(0XE60820).setSeasons(SUMMER).setAnimalFoodType(FRUIT);
    public static final Crop PUMPKIN = registerCrop("pumpkin").setItem(new ItemStack(Blocks.PUMPKIN)).setValue(350, 140).setStages(15).setSeedColours(0XE09A39)
                                        .setSeasons(SUMMER).setIngredient(2, 0.3F).setGrowthHandler(new GrowthHandlerSide(Blocks.PUMPKIN));
    public static final Crop PINEAPPLE = registerCrop("pineapple").setItem(getCropStack(Crops.PINEAPPLE)).setValue(500, 270).setStages(5, 10, 15, 20, 21).setRegrow(16).setSeedColours(0XD7CF00)
                                        .setSeasons(SUMMER).setAnimalFoodType(FRUIT).setPurchaseRules(new SpecialRulesQuest("pineapple"));
    public static final Crop WATERMELON = registerCrop("watermelon").setItem(Items.MELON).setValue(400, 20).setStages(11).setSeedColours(0xc92b3e)
                                        .setSeasons(SUMMER).setAnimalFoodType(FRUIT).setGrowthHandler(new GrowthHandlerSide(Blocks.MELON_BLOCK)).setPurchaseRules(TOWN_PROGRESS);
    //Summer Trees
    private static final SpecialRulesQuest TREES1 = new SpecialRulesQuest("trees1");
    private static final SpecialRulesQuest TREES2 = new SpecialRulesQuest("trees2");
    public static final Crop BANANA = registerTree("banana").setStageLength(9, 9, 14, 23).setFruitRegrow(4).setItem(getCropStack(Crops.BANANA)).setValue(2500, 300).setSeedColours(0xFFEF6A)
                                        .setSeasons(SUMMER).setPurchaseRules(TREES2);
    public static final Crop ORANGE = registerTree("orange").setStageLength(7, 16, 12, 14).setItem(getCropStack(Crops.ORANGE)).setValue(2800, 200).setSeedColours(0xEDB325)
                                        .setSeasons(SUMMER).setPurchaseRules(TREES1);
    public static final Crop PEACH = registerTree("peach").setStageLength(5, 10, 10, 11).setItem(getCropStack(Crops.PEACH)).setValue(3000, 250).setSeedColours(0xFFB0A5)
                                        .setSeasons(SUMMER).setPurchaseRules(TREES2);

    //Autumn Crops
    public static final Crop EGGPLANT = registerCrop("eggplant").setItem(getCropStack(Crops.EGGPLANT)).setValue(320, 50).setUnlocked(2).setStages(3, 6, 9, 10).setRegrow(7).setSeedColours(0XA25CC4).setSeasons(AUTUMN);
    public static final Crop SPINACH = registerCrop("spinach").setItem(getCropStack(Crops.SPINACH)).setValue(200, 70).setStages(2, 5, 6).setSeedColours(0X90AE15).setSeasons(AUTUMN);
    public static final Crop CARROT = registerCrop("carrot").setItem(Items.CARROT).setValue(300, 120).setStages(Blocks.CARROTS, 1, 2, 3, 4, 5, 6, 7, 8).setSeedColours(0XF8AC33).setSeasons(AUTUMN);
    public static final Crop SWEET_POTATO = registerCrop("sweet_potato").setItem(getCropStack(Crops.SWEET_POTATO)).setValue(250, 40).setStages(3, 5, 6).setRegrow(4).setSeedColours(0XD82AAC)
                                            .setSeasons(AUTUMN).setPurchaseRules(new SpecialRulesQuest("sweetpotato"));
    public static final Crop GREEN_PEPPER = registerCrop("green_pepper").setItem(getCropStack(Crops.GREEN_PEPPER)).setValue(300, 50).setStages(1, 3, 4, 7, 8).setRegrow(5).setSeedColours(0x56D213)
                                            .setSeasons(AUTUMN).setPurchaseRules(new SpecialRulesQuest("greenpepper"));
    public static final Crop BEETROOT = registerCrop("beetroot").setItem(Items.BEETROOT).setValue(250, 160).setStages(Blocks.BEETROOTS, 3, 7, 10, 11).setSeedColours(0x690000)
                                            .setSeasons(AUTUMN).setPurchaseRules(TOWN_PROGRESS);

    //Autumn Trees
    public static final Crop APPLE = registerTree("apple").setStageLength(5, 10, 10, 11).setItem(new ItemStack(Items.APPLE)).setValue(1500, 100).setSeedColours(0xE73921)
                                        .setSeasons(AUTUMN).setPurchaseRules(TREES1);
    public static final Crop GRAPE = registerTree("grape").setStageLength(7, 16, 12, 14).setItem(getCropStack(Crops.GRAPE)).setValue(2700, 200).setSeedColours(0xD58EF8)
                                        .setSeasons(AUTUMN).setPurchaseRules(TREES2);

    //Year Long Crops
    public static final Crop GRASS = registerCrop("grass").setItem(getCropStack(Crops.GRASS)).setValue(500, 1).setStages(11).setRegrow(1).setSeedColours(0x7AC958).setSeasons(SPRING, SUMMER, AUTUMN)
                                        .setAnimalFoodType(AnimalFoodType.GRASS).setBecomesDouble(6).setHasAlternativeName().setRequiresSickle(6).setNoWaterRequirements().setPurchaseRules(new SpecialRulesRanch());
    public static final Crop WHEAT = registerCrop("wheat").setItem(Items.WHEAT).setValue(150, 120).setStages(Blocks.WHEAT, 2, 5, 9, 12, 15, 20, 27, 28).setSeedColours(0XEAC715).setSeasons(SPRING, SUMMER, AUTUMN)
                                        .setIngredient(1, 0.1F).setAnimalFoodType(AnimalFoodType.GRASS).setRequiresSickle(0);
    public static final Crop CORN = registerCrop("corn").setItem(getCropStack(Crops.CORN)).setValue(200, 30).setStages(3, 8, 12, 14, 15).setRegrow(12).setSeedColours(0XD4BD45)
                                        .setSeasons(SUMMER, AUTUMN).setPurchaseRules(new SpecialRulesQuest("corn"));
    public static final Crop NETHER_WART = registerCrop("nether_wart").setItem(Items.NETHER_WART).setValue(4000, 20).setStages(Blocks.NETHER_WART, 1, 2, 3, 4).setRegrow(1).setSeedColours(0x8B0000)
                                            .setPlantType(EnumPlantType.Nether).setNoWaterRequirements().setGrowthHandler(SOUL_SAND).setPurchaseRules(new SpecialRulesQuest("netherwart"));
    //Tutorial Crop
    public static final Crop TUTORIAL = registerCrop("tutorial_turnip").setItem(getCropStack(Crops.TUTORIAL_TURNIP)).setValue(0, 1).setStages(1, 2, 3).setSeedColours(0xACA262).setSeasons(SPRING, SUMMER, AUTUMN, WINTER);

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
        RegistryHelper.registerTiles(TileWithered.class, TileCrop.class, TileSprinkler.class, TileSprinklerOld.class);
        if (DISABLE_VANILLA_MOISTURE) Blocks.FARMLAND.setTickRandomly(false);
        if (DISABLE_VANILLA_WHEAT_SEEDS || DISABLE_VANILLA_SEEDS) {
            BLACKLIST.register(MELON_SEEDS);
            BLACKLIST.register(PUMPKIN_SEEDS);
            BLACKLIST.register(BEETROOT_SEEDS);
            BLACKLIST.register(WHEAT_SEEDS);
            BLACKLIST.register(CARROT);
            BLACKLIST.register(POTATO);
        }

        //Register everything in the ore dictionary
        for (Crop crop : Crop.REGISTRY.getValues()) {
            if (crop != Crop.NULL_CROP) {
                //Register always in the ore dictionary
                ItemStack clone = crop.getCropStack(1);
                String name = "crop" + WordUtils.capitalizeFully(crop.getRegistryName().getResourcePath(), '_').replace("_", "");
                RegistryHelper.registerOreIfNotExists(name, clone);
                HFApi.crops.registerCropProvider(clone, crop);
            }
        }
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
        for (Crop crop : Crop.REGISTRY) {
            if (crop != Crop.NULL_CROP && crop.skipLoadingRender()) {
                colors.registerBlockColorHandler(coloring, ((IBlockState)crop.getStateHandler().getValidStates().get(0)).getBlock());
            }
        }
    }

    public static ItemStack getCropStack(Crops crop) {
        return CROP.getStackFromEnum(crop);
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
        SPRINKLER_DRAIN_RATE = getInteger("Sprinkler's daily water consumption", 0, "This number NEEDs to be a factor of 1000, Otherwise you'll have trouble refilling the sprinkler manually. Acceptable values are: 1, 2, 4, 5, 8, 10, 20, 25, 40, 50, 100, 125, 200, 250, 500, 1000");
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