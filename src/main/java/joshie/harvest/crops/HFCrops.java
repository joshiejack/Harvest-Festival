package joshie.harvest.crops;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.crops.*;
import joshie.harvest.core.base.render.MeshIdentical;
import joshie.harvest.core.handlers.DisableHandler;
import joshie.harvest.core.helpers.RegistryHelper;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.crops.block.BlockHFCrops;
import joshie.harvest.crops.block.BlockSprinkler;
import joshie.harvest.crops.handlers.growth.GrowthHandlerNether;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.text.WordUtils;

import static joshie.harvest.api.animals.AnimalFoodType.FRUIT;
import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.core.HFTab.FARMING;
import static joshie.harvest.core.handlers.DisableHandler.BLACKLIST;
import static joshie.harvest.core.helpers.ConfigHelper.getBoolean;
import static joshie.harvest.core.helpers.ConfigHelper.getInteger;
import static joshie.harvest.core.lib.HFModInfo.*;
import static joshie.harvest.core.lib.LoadOrder.HFCROPS;
import static net.minecraft.init.Items.*;

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
    public static final Crop TURNIP = registerCrop("turnip").setItem(getCropStack(ItemCrop.Crop.TURNIP)).setGoldValues(120, 60).setStages(5).setSeedColours(0xFFFFFF);
    public static final Crop POTATO = registerCrop("potato").setItem(Items.POTATO).setGoldValues(150, 80).setStages(8).setSeedColours(0xBE8D2B);
    public static final Crop CUCUMBER = registerCrop("cucumber").setItem(getCropStack(ItemCrop.Crop.CUCUMBER)).setGoldValues(100, 60).setStages(10).setRegrowStage(5).setSeedColours(0x36B313)
                                            .setAnimalFoodType(FRUIT);
    public static final Crop STRAWBERRY = registerCrop("strawberry").setItem(getCropStack(ItemCrop.Crop.STRAWBERRY)).setGoldValues(150, 30).setStages(9).setRegrowStage(7).setYearUnlocked(1).setSeedColours(0xFF7BEA)
                                            .setAnimalFoodType(FRUIT);
    public static final Crop CABBAGE = registerCrop("cabbage").setItem(getCropStack(ItemCrop.Crop.CABBAGE)).setGoldValues(500, 250).setStages(15).setYearUnlocked(3).setSeedColours(0x8FFF40);

    //Summer Crops
    public static final Crop ONION = registerCrop("onion").setItem(getCropStack(ItemCrop.Crop.ONION)).setGoldValues(150, 80).setStages(8).setSeedColours(0XDCC307).setSeasons(SUMMER);
    public static final Crop TOMATO = registerCrop("tomato").setItem(getCropStack(ItemCrop.Crop.TOMATO)).setGoldValues(200, 60).setStages(10).setRegrowStage(7).setSeedColours(0XE60820).setSeasons(SUMMER)
                                        .setAnimalFoodType(FRUIT);
    public static final Crop CORN = registerCrop("corn").setItem(getCropStack(ItemCrop.Crop.CORN)).setGoldValues(300, 100).setStages(15).setRegrowStage(12).setSeedColours(0XD4BD45).setSeasons(SUMMER);
    public static final Crop PUMPKIN = registerCrop("pumpkin").setItem(new ItemStack(Blocks.PUMPKIN)).setGoldValues(500, 125).setStages(15).setYearUnlocked(1).setSeedColours(0XE09A39).setSeasons(SUMMER)
                                        .setIngredient(2, 0.3F).setGrowsToSide(Blocks.PUMPKIN);
    public static final Crop PINEAPPLE = registerCrop("pineapple").setItem(getCropStack(ItemCrop.Crop.PINEAPPLE)).setGoldValues(1000, 500).setStages(21).setRegrowStage(5).setYearUnlocked(3).setSeedColours(0XD7CF00).setSeasons(SUMMER)
                                        .setAnimalFoodType(FRUIT);
    public static final Crop WATERMELON = registerCrop("watermelon").setItem(Items.MELON).setGoldValues(250, 25).setStages(11).setYearUnlocked(1).setSeedColours(0xc92b3e).setSeasons(SUMMER)
                                        .setAnimalFoodType(FRUIT).setGrowsToSide(Blocks.MELON_BLOCK);

    //Autumn Crops
    public static final Crop EGGPLANT = registerCrop("eggplant").setItem(getCropStack(ItemCrop.Crop.EGGPLANT)).setGoldValues(120, 80).setStages(10).setRegrowStage(7).setSeedColours(0XA25CC4).setSeasons(AUTUMN);
    public static final Crop SPINACH = registerCrop("spinach").setItem(getCropStack(ItemCrop.Crop.SPINACH)).setGoldValues(200, 80).setStages(6).setYearUnlocked(1).setSeedColours(0X90AE15).setSeasons(AUTUMN);
    public static final Crop CARROT = registerCrop("carrot").setItem(Items.CARROT).setGoldValues(300, 120).setStages(8).setSeedColours(0XF8AC33).setSeasons(AUTUMN);
    public static final Crop SWEET_POTATO = registerCrop("sweet_potato").setItem(getCropStack(ItemCrop.Crop.SWEET_POTATO)).setGoldValues(300, 120).setStages(6).setRegrowStage(4).setSeedColours(0XD82AAC).setSeasons(AUTUMN);
    public static final Crop GREEN_PEPPER = registerCrop("green_pepper").setItem(getCropStack(ItemCrop.Crop.GREEN_PEPPER)).setGoldValues(150, 40).setStages(8).setRegrowStage(2).setYearUnlocked(3).setSeedColours(0x56D213).setSeasons(AUTUMN);
    public static final Crop BEETROOT = registerCrop("beetroot").setItem(Items.BEETROOT).setGoldValues(250, 75).setStages(8).setSeedColours(0x690000).setSeasons(AUTUMN);

    //Year Long Crops
    public static final Crop GRASS = registerCrop("grass").setItem(getCropStack(ItemCrop.Crop.GRASS)).setGoldValues(500, 1).setStages(11).setRegrowStage(1).setSeedColours(0x7AC958).setSeasons(SPRING, SUMMER, AUTUMN)
                                        .setWitheredColor(0x7a5230).setAnimalFoodType(AnimalFoodType.GRASS).setBecomesDouble(6).setHasAlternativeName().setRequiresSickle(6).setNoWaterRequirements();
    public static final Crop WHEAT = registerCrop("wheat").setItem(Items.WHEAT).setGoldValues(150, 100).setStages(28).setSeedColours(0XEAC715).setSeasons(SPRING, SUMMER, AUTUMN)
                                        .setIngredient(1, 0.1F).setAnimalFoodType(AnimalFoodType.GRASS).setRequiresSickle(0);
    //Nether Crops
    public static final Crop NETHER_WART = registerCrop("nether_wart").setItem(Items.NETHER_WART).setGoldValues(25000, 10).setStages(4).setRegrowStage(1).setYearUnlocked(1).setSeedColours(0x8B0000)
                                            .setPlantType(EnumPlantType.Nether).setNoWaterRequirements().setGrowthHandler(SOUL_SAND);
    //Tutorial Crop
    public static final Crop TUTORIAL = registerCrop("tutorial_turnip").setItem(getCropStack(ItemCrop.Crop.TUTORIAL_TURNIP)).setGoldValues(0, 1).setStages(3).setSeedColours(0xACA262).setSeasons(SPRING, SUMMER, AUTUMN, WINTER);

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
                if (!isInDictionary(name, clone)) {
                    OreDictionary.registerOre(name, clone);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomMeshDefinition(SEEDS, new MeshIdentical(SEEDS));
        ModelLoader.setCustomStateMapper(CROPS, new CropStateMapper());
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

    private static ItemStack getCropStack(ItemCrop.Crop crop) {
        return CROP.getStackFromEnum(crop);
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