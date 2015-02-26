package joshie.harvestmoon.init;

import static joshie.harvestmoon.calendar.Season.AUTUMN;
import static joshie.harvestmoon.calendar.Season.SPRING;
import static joshie.harvestmoon.calendar.Season.SUMMER;
import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.crops.CropSeedFood;
import joshie.harvestmoon.crops.CropWheat;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class HMCrops {
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

    public static void init() {
        //Spring Crops
        turnip = HMApi.CROPS.registerCrop("turnip", SPRING, 120, 60, 5, 0, 0, 0xEDE1B5);
        potato =  HMApi.CROPS.registerCrop(new CropSeedFood("potato", SPRING, 150, 80, 0, Blocks.potatoes, 0x8D782A));
        cucumber = HMApi.CROPS.registerCrop("cucumber", SPRING, 200, 60, 10, 5, 0, 0x137B27);
        strawberry = HMApi.CROPS.registerCrop("strawberry", SPRING, 150, 30, 9, 7, 3, 0xA42F14);
        cabbage = HMApi.CROPS.registerCrop("cabbage", SPRING, 500, 250, 15, 0, 8, 0x8DF146);

        //Summer Crops
        tomato = HMApi.CROPS.registerCrop("tomato", SUMMER, 200, 60, 10, 7, 0, 0xF23B0C);
        onion = HMApi.CROPS.registerCrop("onion", SUMMER, 150, 80, 8, 0, 0, 0xF3B073);
        corn = HMApi.CROPS.registerCrop("corn", SUMMER, 300, 100, 15, 12, 0, 0xF8E048);
        pumpkin = HMApi.CROPS.registerCrop("pumpkin", SUMMER, 500, 250, 15, 0, 3, 0x54971E);
        pineapple = HMApi.CROPS.registerCrop("pineapple", SUMMER, 1000, 500, 21, 5, 8, 0xEECD33);

        //Autumn Crops
        eggplant = HMApi.CROPS.registerCrop("eggplant", AUTUMN, 120, 80, 10, 7, 0, 0x9F61C8);
        carrot = new CropSeedFood("carrot", AUTUMN, 300, 120, 0, Blocks.carrots, 0xF79316);
        sweet_potato = HMApi.CROPS.registerCrop("potato_sweet", AUTUMN, 300, 120, 6, 4, 0, 0xCD1A8B);
        spinach = HMApi.CROPS.registerCrop("spinach", AUTUMN, 200, 80, 6, 0, 3, 0x30B028);
        green_pepper = HMApi.CROPS.registerCrop("pepper_green", AUTUMN, 150, 40, 8, 2, 8, 0x1F5F12);

        //All Seasons
        grass = HMApi.CROPS.registerCrop(new Crop("grass", new Season[] { SPRING, SUMMER, AUTUMN }, 500, 0, 11, 0, 0, 0x006633).setIsStatic().setHasAlternativeName());
        wheat = HMApi.CROPS.registerCrop(new CropWheat("wheat", new Season[] { SPRING, SUMMER, AUTUMN }, 150, 100, 0, Blocks.wheat, 0x8C8C00));
        if (HMConfiguration.vanilla.POTATO_OVERRIDE) potato.setItem(Items.potato);
        if (HMConfiguration.vanilla.CARROT_OVERRIDE) carrot.setItem(Items.carrot);
        if (HMConfiguration.vanilla.WHEAT_OVERRIDE) wheat.setItem(Items.wheat);
    }
}
