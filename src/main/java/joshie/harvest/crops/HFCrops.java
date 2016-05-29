package joshie.harvest.crops;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.core.helpers.SeedHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.base.BlockHFEnum;
import joshie.harvest.crops.blocks.BlockHFCrops;
import joshie.harvest.crops.blocks.BlockHFFarmland;
import joshie.harvest.crops.handlers.*;
import joshie.harvest.crops.items.*;
import joshie.harvest.items.ItemBaseTool;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.text.WordUtils;

import static joshie.harvest.api.animals.AnimalFoodType.FRUIT;
import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.core.HFTab.FARMING;

public class HFCrops {
    //Crops and Custom Farmland
    public static final BlockHFEnum CROPS = new BlockHFCrops().setUnlocalizedName("crops.block");
    public static final BlockHFEnum FARMLAND = new BlockHFFarmland().setUnlocalizedName("farmland");

    //Farming Tools
    public static final ItemBaseTool HOE = new ItemHoe().setUnlocalizedName("hoe");
    public static final ItemBaseTool SICKLE = new ItemSickle().setUnlocalizedName("sickle");
    public static final ItemBaseTool WATERING_CAN = new ItemWateringCan().setUnlocalizedName("wateringcan");

    //Seed Bag Item
    public static final Item SEEDS = new ItemHFSeeds().setUnlocalizedName("crops.seeds");

    //Dummy Crop
    public static final Crop NULL_CROP = new Crop().setStateHandler(new StateHandlerNull());

    //Spring Crops
    public static final ICrop TURNIP = registerCrop("turnip", 120, 60, 5, 0, 0, 0xFFFFFF, SPRING).setStateHandler(new StateHandlerTurnip());
    public static final ICrop POTATO = registerCrop("potato", 150, 80, 8, 0, 0, 0xBE8D2B, SPRING).setDropHandler(new DropHandlerPotato()).setStateHandler(new StateHandlerSeedFood(Blocks.POTATOES));
    public static final ICrop CUCUMBER = registerCrop("cucumber", 200, 60, 10, 5, 0, 0x36B313, SPRING).setAnimalFoodType(FRUIT).setStateHandler(new StateHandlerCucumber());
    public static final ICrop STRAWBERRY = registerCrop("strawberry", 150, 30, 9, 7, 3, 0xFF7BEA, SPRING).setAnimalFoodType(FRUIT).setStateHandler(new StateHandlerStrawberry());
    public static final ICrop CABBAGE = registerCrop("cabbage", 500, 250, 15, 0, 8, 0x8FFF40, SPRING).setStateHandler(new StateHandlerCabbage());

    //Summer Crops
    public static final ICrop TOMATO = registerCrop("onion", 150, 80, 8, 0, 0, 0XDCC307, SUMMER).setStateHandler(new StateHandlerOnion());
    public static final ICrop ONION = registerCrop("tomato", 200, 60, 10, 7, 0, 0XE60820, SUMMER).setAnimalFoodType(FRUIT).setStateHandler(new StateHandlerTomato());
    public static final ICrop CORN = registerCrop("corn", 300, 100, 15, 12, 0, 0XD4BD45, SUMMER).setStateHandler(new StateHandlerCorn());
    public static final ICrop PUMPKIN = registerCrop("pumpkin", 500, 125, 15, 0, 3, 0XE09A39, SUMMER).setGrowsToSide(Blocks.PUMPKIN).setStateHandler(new StateHandlerStem(Blocks.PUMPKIN));
    public static final ICrop PINEAPPLE = registerCrop("pineapple", 1000, 500, 21, 5, 8, 0XD7CF00, SUMMER).setAnimalFoodType(FRUIT).setStateHandler(new StateHandlerPineapple());
    public static final ICrop WATERMELON = registerCrop("watermelon", 250, 25, 11, 0, 3, 0xc92b3e, SUMMER).setAnimalFoodType(FRUIT).setDropHandler(new DropHandlerMelon()).setGrowsToSide(Blocks.MELON_BLOCK).setStateHandler(new StateHandlerStem(Blocks.MELON_BLOCK));

    //Autumn Crops
    public static final ICrop EGGPLANT = registerCrop("eggplant", 120, 80, 10, 7, 0, 0XA25CC4, AUTUMN).setStateHandler(new StateHandlerEggplant());
    public static final ICrop SPINACH = registerCrop("spinach", 200, 80, 6, 0, 3, 0X90AE15, AUTUMN).setStateHandler(new StateHandlerSpinach());
    public static final ICrop CARROT = registerCrop("carrot", 300, 120, 8, 0, 0, 0XF8AC33, AUTUMN).setStateHandler(new StateHandlerSeedFood(Blocks.CARROTS));
    public static final ICrop SWEET_POTATO = registerCrop("sweet_potato", 300, 120, 6, 4, 0, 0XD82AAC, AUTUMN).setStateHandler(new StateHandlerSweetPotato());
    public static final ICrop GREEN_PEPPER = registerCrop("green_pepper", 150, 40, 8, 2, 8, 0x56D213, AUTUMN).setStateHandler(new StateHandlerGreenPepper());
    public static final ICrop BEETROOT = registerCrop("beetroot", 250, 75, 8, 0, 0, 0x690000, AUTUMN).setStateHandler(new StateHandlerSeedFood(Blocks.BEETROOTS));

    //Year Long Crops
    public static final ICrop GRASS = registerCrop("grass", 500, 0, 11, 0, 0, 0x7AC958, SPRING, SUMMER, AUTUMN).setAnimalFoodType(AnimalFoodType.GRASS).setBecomesDouble(6).setHasAlternativeName().setRequiresSickle().setNoWaterRequirements().setStateHandler(new StateHandlerGrass());
    public static final ICrop WHEAT = registerCrop("wheat", 150, 100, 28, 0, 0, 0XEAC715, SPRING, SUMMER, AUTUMN).setAnimalFoodType(AnimalFoodType.GRASS).setRequiresSickle().setStateHandler(new StateHandlerWheat());

    //Nether Crop
    public static final ICrop NETHER_WART = registerCrop("nether_wart", 25000, 10, 4, 1, 5, 0x8B0000, NETHER).setStateHandler(new StateHandlerNetherWart()).setPlantType(EnumPlantType.Nether).setNoWaterRequirements().setSoilRequirements(SoilHandlers.SOUL_SAND).setDropHandler(new DropHandlerNetherWart());

    public static void preInit() {
        registerVanillaCrop(Items.WHEAT, WHEAT);
        registerVanillaCrop(Items.CARROT, CARROT);
        registerVanillaCrop(Items.POTATO, POTATO);
        registerVanillaCrop(Items.BEETROOT, BEETROOT);
        registerVanillaCrop(Items.MELON, WATERMELON);
        registerVanillaCrop(Blocks.PUMPKIN, PUMPKIN);
        registerVanillaCrop(Items.NETHER_WART, NETHER_WART);

        //Add a new crop item for things that do not have an item yet :D
        for (Crop crop : CropRegistry.REGISTRY.getValues()) {
            if (!crop.hasItemAssigned()) {
                crop.setItem(new ItemStack(new ItemCrop(crop).setUnlocalizedName("crop." + crop.getRegistryName().getResourcePath()), 1, 0));
            }

            //Register always in the ore dictionary
            ItemStack clone = crop.getCropStack().copy();
            clone.setItemDamage(OreDictionary.WILDCARD_VALUE);
            String name = "crop" + WordUtils.capitalizeFully(crop.getRegistryName().getResourcePath().replace("_", " ").replace(" ", ""));
            if (!isInDictionary(name, clone)) {
                OreDictionary.registerOre(name, clone);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        CropStateMapper mapper = new CropStateMapper();
        ModelLoader.setCustomStateMapper(CROPS, mapper);
        ModelLoader.setCustomStateMapper(FARMLAND, mapper);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                ICrop crop = SeedHelper.getCropFromSeed(stack);
                return crop != null ? crop.getColor() : -1;
            }
        }, SEEDS);
    }

    private static void registerVanillaCrop(Item item, ICrop crop) {
        HFApi.crops.registerCropProvider(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), crop);
        item.setCreativeTab(FARMING);
        crop.setItem(new ItemStack(item));
    }

    private static void registerVanillaCrop(Block block, ICrop crop) {
        HFApi.crops.registerCropProvider(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE), crop);
        block.setCreativeTab(FARMING);
        crop.setItem(new ItemStack(block));
    }

    private static ICrop registerCrop(String name, int cost, int sell, int stages, int regrow, int year, int color, Season... seasons) {
        return HFApi.crops.registerCrop(new ResourceLocation(HFModInfo.MODID, name), cost, sell, stages, regrow, year, color, seasons);
    }

    private static boolean isInDictionary(String name, ItemStack stack) {
        for (ItemStack check: OreDictionary.getOres(name)) {
            if (check.getItem() == stack.getItem() && (check.getItemDamage() == OreDictionary.WILDCARD_VALUE || check.getItemDamage() == stack.getItemDamage())) {
                return true;
            }
        }

        return false;
    }
}