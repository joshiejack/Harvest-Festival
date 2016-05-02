package joshie.harvest.crops;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.core.config.HFConfig;
import joshie.harvest.crops.handlers.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.animals.AnimalFoodType.FRUIT;
import static joshie.harvest.api.animals.AnimalFoodType.GRASS;
import static joshie.harvest.api.calendar.Season.*;
import static net.minecraft.init.Blocks.PUMPKIN;

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
        null_crop = HFApi.CROPS.registerCrop(new Crop().setStateHandler(new StateHandlerNull()));

        //Spring Crops
        turnip = HFApi.CROPS.registerCrop("turnip", 120, 60, 5, 0, 0, 0xFFFFFF, SPRING).setStateHandler(new StateHandlerTurnip());
        potato = HFApi.CROPS.registerCrop("potato", 150, 80, 8, 0, 0, 0xBE8D2B, SPRING).setDropHandler(new DropHandlerPotato()).setStateHandler(new StateHandlerSeedFood(Blocks.POTATOES));
        cucumber = HFApi.CROPS.registerCrop("cucumber", 200, 60, 10, 5, 0, 0x36B313, SPRING).setAnimalFoodType(FRUIT).setStateHandler(new StateHandlerCucumber());
        strawberry = HFApi.CROPS.registerCrop("strawberry", 150, 30, 9, 7, 3, 0xFF7BEA, SPRING).setAnimalFoodType(FRUIT).setStateHandler(new StateHandlerStrawberry());
        cabbage = HFApi.CROPS.registerCrop("cabbage", 500, 250, 15, 0, 8, 0x8FFF40, SPRING).setStateHandler(new StateHandlerCabbage());

        //Summer Crops
        onion = HFApi.CROPS.registerCrop("onion", 150, 80, 8, 0, 0, 0XDCC307, SUMMER).setStateHandler(new StateHandlerOnion());
        tomato = HFApi.CROPS.registerCrop("tomato", 200, 60, 10, 7, 0, 0XE60820, SUMMER).setAnimalFoodType(FRUIT).setStateHandler(new StateHandlerTomato());
        corn = HFApi.CROPS.registerCrop("corn", 300, 100, 15, 12, 0, 0XD4BD45, SUMMER).setStateHandler(new StateHandlerCorn());
        pumpkin = HFApi.CROPS.registerCrop("pumpkin", 500, 125, 15, 0, 3, 0XE09A39, SUMMER).setGrowsToSide(Blocks.PUMPKIN).setStateHandler(new StateHandlerStem(Blocks.PUMPKIN));
        pineapple = HFApi.CROPS.registerCrop("pineapple", 1000, 500, 21, 5, 8, 0XD7CF00, SUMMER).setAnimalFoodType(FRUIT).setStateHandler(new StateHandlerPineapple());

        //Autumn Crops
        eggplant = HFApi.CROPS.registerCrop("eggplant", 120, 80, 10, 7, 0, 0XA25CC4, AUTUMN).setStateHandler(new StateHandlerEggplant());
        spinach = HFApi.CROPS.registerCrop("spinach", 200, 80, 6, 0, 3, 0X90AE15, AUTUMN).setStateHandler(new StateHandlerSpinach());
        carrot = HFApi.CROPS.registerCrop("carrot", 300, 120, 8, 0, 0, 0XF8AC33, AUTUMN).setStateHandler(new StateHandlerSeedFood(Blocks.CARROTS));
        sweet_potato = HFApi.CROPS.registerCrop("potato_sweet", 300, 120, 6, 4, 0, 0XD82AAC, AUTUMN).setStateHandler(new StateHandlerSweetPotato());
        green_pepper = HFApi.CROPS.registerCrop("pepper_green", 150, 40, 8, 2, 8, 0x56D213, AUTUMN).setStateHandler(new StateHandlerGreenPepper());

        //All Seasons
        grass = HFApi.CROPS.registerCrop("grass", 500, 0, 11, 0, 0, 0x7AC958, SPRING, SUMMER, AUTUMN).setAnimalFoodType(GRASS).setBecomesDouble(6).setHasAlternativeName().setRequiresSickle().setNoWaterRequirements().setStateHandler(new StateHandlerGrass());
        wheat = HFApi.CROPS.registerCrop("wheat", 150, 100, 28, 0, 0, 0XEAC715, SPRING, SUMMER, AUTUMN).setAnimalFoodType(GRASS).setRequiresSickle().setStateHandler(new StateHandlerWheat());

        //Vanilla additions
        watermelon = HFApi.CROPS.registerCrop("watermelon", 250, 25, 11, 0, 3, 0xc92b3e, SUMMER).setAnimalFoodType(FRUIT).setDropHandler(new DropHandlerMelon()).setGrowsToSide(Blocks.MELON_BLOCK).setStateHandler(new StateHandlerStem(Blocks.MELON_BLOCK));
        beetroot = HFApi.CROPS.registerCrop("beetroot", 250, 75, 8, 0, 0, 0x690000, AUTUMN).setStateHandler(new StateHandlerSeedFood(Blocks.BEETROOTS));
        if (HFConfig.asm.POTATO_OVERRIDE) potato.setItem(new ItemStack(Items.POTATO));
        if (HFConfig.asm.CARROT_OVERRIDE) carrot.setItem(new ItemStack(Items.CARROT));
        if (HFConfig.asm.WHEAT_OVERRIDE) wheat.setItem(new ItemStack(Items.WHEAT));
        if (HFConfig.asm.PUMPKIN_OVERRIDE) pumpkin.setItem(new ItemStack(PUMPKIN));
        if (HFConfig.asm.WATERMELON_OVERRIDE) watermelon.setItem(new ItemStack(Items.MELON));
    }
}