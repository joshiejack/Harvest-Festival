package joshie.harvest.init;

import static joshie.harvest.calendar.Season.AUTUMN;
import static joshie.harvest.calendar.Season.SPRING;
import static joshie.harvest.calendar.Season.SUMMER;
import joshie.harvest.api.AnimalFoodType;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.crops.CropNull;
import joshie.harvest.crops.DropMelon;
import joshie.harvest.crops.DropPotato;
import joshie.harvest.crops.icons.IconHandlerBlock;
import joshie.harvest.crops.icons.IconHandlerCabbage;
import joshie.harvest.crops.icons.IconHandlerCorn;
import joshie.harvest.crops.icons.IconHandlerCucumber;
import joshie.harvest.crops.icons.IconHandlerEggplant;
import joshie.harvest.crops.icons.IconHandlerGrass;
import joshie.harvest.crops.icons.IconHandlerGreenPepper;
import joshie.harvest.crops.icons.IconHandlerNull;
import joshie.harvest.crops.icons.IconHandlerOnion;
import joshie.harvest.crops.icons.IconHandlerPineapple;
import joshie.harvest.crops.icons.IconHandlerSeedFood;
import joshie.harvest.crops.icons.IconHandlerSpinach;
import joshie.harvest.crops.icons.IconHandlerStrawberry;
import joshie.harvest.crops.icons.IconHandlerSweetPotato;
import joshie.harvest.crops.icons.IconHandlerTomato;
import joshie.harvest.crops.icons.IconHandlerTurnip;
import joshie.harvest.crops.icons.IconHandlerWheat;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

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

    public static void init() {
        null_crop = HFApi.CROPS.registerCrop(new CropNull().setCropIconHandler(new IconHandlerNull()));

        //Spring Crops
        turnip = HFApi.CROPS.registerCrop("turnip", 120, 60, 5, 0, 0, 0xFFFFFF, SPRING).setCropIconHandler(new IconHandlerTurnip());
        potato = HFApi.CROPS.registerCrop("potato", 150, 80, 8, 0, 0, 0xBE8D2B, SPRING).setDropHandler(new DropPotato()).setCropIconHandler(new IconHandlerSeedFood(Blocks.potatoes));
        cucumber = HFApi.CROPS.registerCrop("cucumber", 200, 60, 10, 5, 0, 0x36B313, SPRING).setAnimalFoodType(AnimalFoodType.FRUIT).setCropIconHandler(new IconHandlerCucumber());
        strawberry = HFApi.CROPS.registerCrop("strawberry", 150, 30, 9, 7, 3, 0xFF7BEA, SPRING).setAnimalFoodType(AnimalFoodType.FRUIT).setCropIconHandler(new IconHandlerStrawberry());
        cabbage = HFApi.CROPS.registerCrop("cabbage", 500, 250, 15, 0, 8, 0x8FFF40, SPRING).setCropIconHandler(new IconHandlerCabbage());

        //Summer Crops
        onion = HFApi.CROPS.registerCrop("onion", 150, 80, 8, 0, 0, 0XDCC307, SUMMER).setCropIconHandler(new IconHandlerOnion());
        tomato = HFApi.CROPS.registerCrop("tomato", 200, 60, 10, 7, 0, 0XE60820, SUMMER).setAnimalFoodType(AnimalFoodType.FRUIT).setCropIconHandler(new IconHandlerTomato());
        corn = HFApi.CROPS.registerCrop("corn", 300, 100, 15, 12, 0, 0XD4BD45, SUMMER).setCropIconHandler(new IconHandlerCorn());
        pumpkin = HFApi.CROPS.registerCrop("pumpkin", 500, 125, 15, 0, 3, 0XE09A39, SUMMER).setGrowsToSide(Blocks.pumpkin).setCropIconHandler(new IconHandlerBlock(Blocks.pumpkin));
        pineapple = HFApi.CROPS.registerCrop("pineapple", 1000, 500, 21, 5, 8, 0XD7CF00, SUMMER).setAnimalFoodType(AnimalFoodType.FRUIT).setCropIconHandler(new IconHandlerPineapple());

        //Autumn Crops
        eggplant = HFApi.CROPS.registerCrop("eggplant", 120, 80, 10, 7, 0, 0XA25CC4, AUTUMN).setCropIconHandler(new IconHandlerEggplant());
        spinach = HFApi.CROPS.registerCrop("spinach", 200, 80, 6, 0, 3, 0X90AE15, AUTUMN).setCropIconHandler(new IconHandlerSpinach());
        carrot = HFApi.CROPS.registerCrop("carrot", 300, 120, 8, 0, 0, 0XF8AC33, AUTUMN).setCropIconHandler(new IconHandlerSeedFood(Blocks.carrots));
        sweet_potato = HFApi.CROPS.registerCrop("potato_sweet", 300, 120, 6, 4, 0, 0XD82AAC, AUTUMN).setCropIconHandler(new IconHandlerSweetPotato());
        green_pepper = HFApi.CROPS.registerCrop("pepper_green", 150, 40, 8, 2, 8, 0x56D213, AUTUMN).setCropIconHandler(new IconHandlerGreenPepper());

        //All Seasons
        grass = HFApi.CROPS.registerCrop("grass", 500, 0, 11, 0, 0, 0x7AC958, SPRING, SUMMER, AUTUMN).setAnimalFoodType(AnimalFoodType.GRASS).setBecomesDouble(6).setHasAlternativeName().setRequiresSickle().setCropIconHandler(new IconHandlerGrass());
        wheat = HFApi.CROPS.registerCrop("wheat", 150, 100, 28, 0, 0, 0XEAC715, SPRING, SUMMER, AUTUMN).setAnimalFoodType(AnimalFoodType.GRASS).setRequiresSickle().setCropIconHandler(new IconHandlerWheat());
        watermelon = HFApi.CROPS.registerCrop("watermelon", 250, 25, 11, 0, 3, 0xFF3211, SUMMER).setAnimalFoodType(AnimalFoodType.FRUIT).setDropHandler(new DropMelon()).setGrowsToSide(Blocks.melon_block).setCropIconHandler(new IconHandlerBlock(Blocks.melon_block));
        if (HFConfig.vanilla.POTATO_OVERRIDE) potato.setItem(new ItemStack(Items.potato));
        if (HFConfig.vanilla.CARROT_OVERRIDE) carrot.setItem(new ItemStack(Items.carrot));
        if (HFConfig.vanilla.WHEAT_OVERRIDE) wheat.setItem(new ItemStack(Items.wheat));
        if (HFConfig.vanilla.PUMPKIN_OVERRIDE) pumpkin.setItem(new ItemStack(Blocks.pumpkin));
        if (HFConfig.vanilla.WATERMELON_OVERRIDE) watermelon.setItem(new ItemStack(Items.melon));
    }
}
