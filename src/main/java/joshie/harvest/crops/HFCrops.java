package joshie.harvest.crops;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.crops.handlers.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import static joshie.harvest.api.animals.AnimalFoodType.FRUIT;
import static joshie.harvest.api.animals.AnimalFoodType.GRASS;
import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.core.HFTab.FARMING;

public class HFCrops {
    public static ICrop null_crop; //Dummy crop

    public static ICrop turnip;
    public static ICrop potato;
    public static ICrop cucumber;
    public static ICrop strawberry;
    public static ICrop cabbage;
    public static ICrop tomato;
    public static ICrop onion;
    public static ICrop corn;
    public static ICrop pumpkin;
    public static ICrop pineapple;
    public static ICrop eggplant;
    public static ICrop carrot;
    public static ICrop sweet_potato;
    public static ICrop spinach;
    public static ICrop green_pepper;
    public static ICrop grass;
    public static ICrop wheat;
    public static ICrop watermelon;
    public static ICrop beetroot;

    public static void preInit() {
        null_crop = new Crop().setStateHandler(new StateHandlerNull());

        //Spring Crops
        turnip = registerCrop("turnip", 120, 60, 5, 0, 0, 0xFFFFFF, SPRING).setStateHandler(new StateHandlerTurnip());
        potato = registerCrop("potato", 150, 80, 8, 0, 0, 0xBE8D2B, SPRING).setDropHandler(new DropHandlerPotato()).setStateHandler(new StateHandlerSeedFood(Blocks.POTATOES));
        cucumber = registerCrop("cucumber", 200, 60, 10, 5, 0, 0x36B313, SPRING).setAnimalFoodType(FRUIT).setStateHandler(new StateHandlerCucumber());
        strawberry = registerCrop("strawberry", 150, 30, 9, 7, 3, 0xFF7BEA, SPRING).setAnimalFoodType(FRUIT).setStateHandler(new StateHandlerStrawberry());
        cabbage = registerCrop("cabbage", 500, 250, 15, 0, 8, 0x8FFF40, SPRING).setStateHandler(new StateHandlerCabbage());

        //Summer Crops
        onion = registerCrop("onion", 150, 80, 8, 0, 0, 0XDCC307, SUMMER).setStateHandler(new StateHandlerOnion());
        tomato = registerCrop("tomato", 200, 60, 10, 7, 0, 0XE60820, SUMMER).setAnimalFoodType(FRUIT).setStateHandler(new StateHandlerTomato());
        corn = registerCrop("corn", 300, 100, 15, 12, 0, 0XD4BD45, SUMMER).setStateHandler(new StateHandlerCorn());
        pumpkin = registerCrop("pumpkin", 500, 125, 15, 0, 3, 0XE09A39, SUMMER).setGrowsToSide(Blocks.PUMPKIN).setStateHandler(new StateHandlerStem(Blocks.PUMPKIN));
        pineapple = registerCrop("pineapple", 1000, 500, 21, 5, 8, 0XD7CF00, SUMMER).setAnimalFoodType(FRUIT).setStateHandler(new StateHandlerPineapple());

        //Autumn Crops
        eggplant = registerCrop("eggplant", 120, 80, 10, 7, 0, 0XA25CC4, AUTUMN).setStateHandler(new StateHandlerEggplant());
        spinach = registerCrop("spinach", 200, 80, 6, 0, 3, 0X90AE15, AUTUMN).setStateHandler(new StateHandlerSpinach());
        carrot = registerCrop("carrot", 300, 120, 8, 0, 0, 0XF8AC33, AUTUMN).setStateHandler(new StateHandlerSeedFood(Blocks.CARROTS));
        sweet_potato = registerCrop("sweet_potato", 300, 120, 6, 4, 0, 0XD82AAC, AUTUMN).setStateHandler(new StateHandlerSweetPotato());
        green_pepper = registerCrop("green_pepper", 150, 40, 8, 2, 8, 0x56D213, AUTUMN).setStateHandler(new StateHandlerGreenPepper());

        //All Seasons
        grass = registerCrop("grass", 500, 0, 11, 0, 0, 0x7AC958, SPRING, SUMMER, AUTUMN).setAnimalFoodType(GRASS).setBecomesDouble(6).setHasAlternativeName().setRequiresSickle().setNoWaterRequirements().setStateHandler(new StateHandlerGrass());
        wheat = registerCrop("wheat", 150, 100, 28, 0, 0, 0XEAC715, SPRING, SUMMER, AUTUMN).setAnimalFoodType(GRASS).setRequiresSickle().setStateHandler(new StateHandlerWheat());

        //Vanilla additions
        watermelon = registerCrop("watermelon", 250, 25, 11, 0, 3, 0xc92b3e, SUMMER).setAnimalFoodType(FRUIT).setDropHandler(new DropHandlerMelon()).setGrowsToSide(Blocks.MELON_BLOCK).setStateHandler(new StateHandlerStem(Blocks.MELON_BLOCK));
        beetroot = registerCrop("beetroot", 250, 75, 8, 0, 0, 0x690000, AUTUMN).setStateHandler(new StateHandlerSeedFood(Blocks.BEETROOTS));

        registerVanillaCrop(Items.WHEAT, wheat);
        registerVanillaCrop(Items.CARROT, carrot);
        registerVanillaCrop(Items.POTATO, potato);
        registerVanillaCrop(Items.BEETROOT, beetroot);
        registerVanillaCrop(Items.MELON, watermelon);
        registerVanillaCrop(Blocks.PUMPKIN, pumpkin);
    }

    private static void registerVanillaCrop(Item item, ICrop crop) {
        HFApi.CROPS.registerCropProvider(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), crop);
        item.setCreativeTab(FARMING);
        crop.setItem(new ItemStack(item));
    }

    private static void registerVanillaCrop(Block block, ICrop crop) {
        HFApi.CROPS.registerCropProvider(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE), crop);
        block.setCreativeTab(FARMING);
        crop.setItem(new ItemStack(block));
    }

    private static ICrop registerCrop(String name, int cost, int sell, int stages, int regrow, int year, int color, Season... seasons) {
        return HFApi.CROPS.registerCrop(new ResourceLocation(HFModInfo.MODID, name), cost, sell, stages, regrow, year, color, seasons);
    }
}