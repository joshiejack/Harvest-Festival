package joshie.harvestmoon.init;

import static joshie.harvestmoon.calendar.Season.AUTUMN;
import static joshie.harvestmoon.calendar.Season.SPRING;
import static joshie.harvestmoon.calendar.Season.SUMMER;
import joshie.harvestmoon.api.AnimalFoodType;
import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.crops.CropNull;
import joshie.harvestmoon.crops.DropMelon;
import joshie.harvestmoon.crops.DropPotato;
import joshie.harvestmoon.crops.icons.IconHandlerBlock;
import joshie.harvestmoon.crops.icons.IconHandlerCabbage;
import joshie.harvestmoon.crops.icons.IconHandlerCorn;
import joshie.harvestmoon.crops.icons.IconHandlerCucumber;
import joshie.harvestmoon.crops.icons.IconHandlerEggplant;
import joshie.harvestmoon.crops.icons.IconHandlerGrass;
import joshie.harvestmoon.crops.icons.IconHandlerGreenPepper;
import joshie.harvestmoon.crops.icons.IconHandlerNull;
import joshie.harvestmoon.crops.icons.IconHandlerOnion;
import joshie.harvestmoon.crops.icons.IconHandlerPineapple;
import joshie.harvestmoon.crops.icons.IconHandlerSeedFood;
import joshie.harvestmoon.crops.icons.IconHandlerSpinach;
import joshie.harvestmoon.crops.icons.IconHandlerStrawberry;
import joshie.harvestmoon.crops.icons.IconHandlerSweetPotato;
import joshie.harvestmoon.crops.icons.IconHandlerTomato;
import joshie.harvestmoon.crops.icons.IconHandlerTurnip;
import joshie.harvestmoon.crops.icons.IconHandlerWheat;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class HMCrops {
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

    //TODO: Add Vanilla Melon

    public static void init() {
        null_crop = HMApi.CROPS.registerCrop(new CropNull().setCropIconHandler(new IconHandlerNull()));

        //Spring Crops
        turnip = HMApi.CROPS.registerCrop("turnip", 120, 60, 5, 0, 0, 0xEDE1B5, SPRING).setCropIconHandler(new IconHandlerTurnip());
        potato = HMApi.CROPS.registerCrop("potato", 150, 80, 8, 0, 0, 0x8D782A, SPRING).setDropHandler(new DropPotato()).setCropIconHandler(new IconHandlerSeedFood(Blocks.potatoes));
        cucumber = HMApi.CROPS.registerCrop("cucumber", 200, 60, 10, 5, 0, 0x137B27, SPRING).setAnimalFoodType(AnimalFoodType.FRUIT).setCropIconHandler(new IconHandlerCucumber());
        strawberry = HMApi.CROPS.registerCrop("strawberry", 150, 30, 9, 7, 3, 0xff7bea, SPRING).setAnimalFoodType(AnimalFoodType.FRUIT).setCropIconHandler(new IconHandlerStrawberry());
        cabbage = HMApi.CROPS.registerCrop("cabbage", 500, 250, 15, 0, 8, 0x8DF146, SPRING).setCropIconHandler(new IconHandlerCabbage());

        //Summer Crops
        tomato = HMApi.CROPS.registerCrop("tomato", 200, 60, 10, 7, 0, 0xF23B0C, SUMMER).setAnimalFoodType(AnimalFoodType.FRUIT).setCropIconHandler(new IconHandlerTomato());
        onion = HMApi.CROPS.registerCrop("onion", 150, 80, 8, 0, 0, 0xF3B073, SUMMER).setCropIconHandler(new IconHandlerOnion());
        corn = HMApi.CROPS.registerCrop("corn", 300, 100, 15, 12, 0, 0xF8E048, SUMMER).setCropIconHandler(new IconHandlerCorn());
        pumpkin = HMApi.CROPS.registerCrop("pumpkin", 500, 125, 15, 0, 3, 0x54971E, SUMMER).setGrowsToSide(Blocks.pumpkin).setCropIconHandler(new IconHandlerBlock(Blocks.pumpkin));
        pineapple = HMApi.CROPS.registerCrop("pineapple", 1000, 500, 21, 5, 8, 0xEECD33, SUMMER).setAnimalFoodType(AnimalFoodType.FRUIT).setCropIconHandler(new IconHandlerPineapple());

        //Autumn Crops
        eggplant = HMApi.CROPS.registerCrop("eggplant", 120, 80, 10, 7, 0, 0x9F61C8, AUTUMN).setCropIconHandler(new IconHandlerEggplant());
        carrot = HMApi.CROPS.registerCrop("carrot", 300, 120, 8, 0, 0, 0xF79316, AUTUMN).setCropIconHandler(new IconHandlerSeedFood(Blocks.carrots));
        sweet_potato = HMApi.CROPS.registerCrop("potato_sweet", 300, 120, 6, 4, 0, 0xCD1A8B, AUTUMN).setCropIconHandler(new IconHandlerSweetPotato());
        spinach = HMApi.CROPS.registerCrop("spinach", 200, 80, 6, 0, 3, 0x30B028, AUTUMN).setCropIconHandler(new IconHandlerSpinach());
        green_pepper = HMApi.CROPS.registerCrop("pepper_green", 150, 40, 8, 2, 8, 0x1F5F12, AUTUMN).setCropIconHandler(new IconHandlerGreenPepper());

        //All Seasons
        grass = HMApi.CROPS.registerCrop("grass", 500, 0, 11, 0, 0, 0x006633, SPRING, SUMMER, AUTUMN).setAnimalFoodType(AnimalFoodType.GRASS).setBecomesDouble(6).setIsStatic().setHasAlternativeName().setRequiresSickle().setCropIconHandler(new IconHandlerGrass());
        wheat = HMApi.CROPS.registerCrop("wheat", 150, 100, 28, 0, 0, 0x8C8C00, SPRING, SUMMER, AUTUMN).setAnimalFoodType(AnimalFoodType.GRASS).setRequiresSickle().setCropIconHandler(new IconHandlerWheat());
        watermelon = HMApi.CROPS.registerCrop("watermelon", 250, 25, 11, 0, 3, 0xFF73FF, SUMMER).setAnimalFoodType(AnimalFoodType.FRUIT).setDropHandler(new DropMelon()).setGrowsToSide(Blocks.melon_block).setCropIconHandler(new IconHandlerBlock(Blocks.melon_block));
        if (HMConfiguration.vanilla.POTATO_OVERRIDE) potato.setItem(Items.potato);
        if (HMConfiguration.vanilla.CARROT_OVERRIDE) carrot.setItem(Items.carrot);
        if (HMConfiguration.vanilla.WHEAT_OVERRIDE) wheat.setItem(Items.wheat);
        if (HMConfiguration.vanilla.PUMPKIN_OVERRIDE) pumpkin.setItem(Item.getItemFromBlock(Blocks.pumpkin));
        if (HMConfiguration.vanilla.WATERMELON_OVERRIDE) watermelon.setItem(Items.melon);
    }
}
