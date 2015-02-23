package joshie.harvestmoon.init;

import static joshie.harvestmoon.calendar.Season.AUTUMN;
import static joshie.harvestmoon.calendar.Season.SPRING;
import static joshie.harvestmoon.calendar.Season.SUMMER;
import joshie.harvestmoon.HMConfiguration;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.crops.CropSeedFood;
import joshie.harvestmoon.crops.CropWheat;
import joshie.harvestmoon.lib.CropMeta;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class HMCrops {
    public static Crop turnip;
    public static Crop potato;
    public static Crop cucumber;
    public static Crop strawberry;
    public static Crop cabbage;
    public static Crop tomato;
    public static Crop onion;
    public static Crop corn;
    public static Crop pumpkin;
    public static Crop pineapple;
    public static Crop eggplant;
    public static Crop carrot;
    public static Crop sweet_potato;
    public static Crop spinach;
    public static Crop green_pepper;
    public static Crop grass;
    public static Crop wheat;

    public static void init() {
        //Spring Crops
        turnip = new Crop("turnip", SPRING, 120, 60, 5, 0, 0, CropMeta.TURNIP, 0xEDE1B5);
        potato = new CropSeedFood("potato", SPRING, 150, 80, 0, CropMeta.POTATO, Blocks.potatoes, 0x8D782A);
        cucumber = new Crop("cucumber", SPRING, 200, 60, 10, 5, 0, CropMeta.CUCUMBER, 0x137B27);
        strawberry = new Crop("strawberry", SPRING, 150, 30, 9, 7, 3, CropMeta.STRAWBERRY, 0xA42F14);
        cabbage = new Crop("cabbage", SPRING, 500, 250, 15, 0, 8, CropMeta.CABBAGE, 0x8DF146);

        //Summer Crops
        tomato = new Crop("tomato", SUMMER, 200, 60, 10, 7, 0, CropMeta.TOMATO, 0xF23B0C);
        onion = new Crop("onion", SUMMER, 150, 80, 8, 0, 0, CropMeta.ONION, 0xF3B073);
        corn = new Crop("corn", SUMMER, 300, 100, 15, 12, 0, CropMeta.CORN, 0xF8E048);
        pumpkin = new Crop("pumpkin", SUMMER, 500, 250, 15, 0, 3, CropMeta.PUMPKIN, 0x54971E);
        pineapple = new Crop("pineapple", SUMMER, 1000, 500, 21, 5, 8, CropMeta.PINEAPPLE, 0xEECD33);

        //Autumn Crops
        eggplant = new Crop("eggplant", AUTUMN, 120, 80, 10, 7, 0, CropMeta.EGGPLANT, 0x9F61C8);
        carrot = new CropSeedFood("carrot", AUTUMN, 300, 120, 0, CropMeta.CARROT, Blocks.carrots, 0xF79316);
        sweet_potato = new Crop("potato_sweet", AUTUMN, 300, 120, 6, 4, 0, CropMeta.SWEET_POTATO, 0xCD1A8B);
        spinach = new Crop("spinach", AUTUMN, 200, 80, 6, 0, 3, CropMeta.SPINACH, 0x30B028);
        green_pepper = new Crop("pepper_green", AUTUMN, 150, 40, 8, 2, 8, CropMeta.GREEN_PEPPER, 0x1F5F12);

        //All Seasons
        grass = new Crop("grass", new Season[] { SPRING, SUMMER, AUTUMN }, 500, 0, 11, 0, 0, CropMeta.GRASS, 0x006633).setIsStatic().setHasAlternativeName();
        wheat = new CropWheat("wheat", new Season[] { SPRING, SUMMER, AUTUMN }, 150, 100, 0, CropMeta.WHEAT, Blocks.wheat, 0x8C8C00);
        if (HMConfiguration.vanilla.POTATO_OVERRIDE) potato.setVanillaItem(Items.potato);
        if (HMConfiguration.vanilla.CARROT_OVERRIDE) carrot.setVanillaItem(Items.carrot);
        if (HMConfiguration.vanilla.WHEAT_OVERRIDE) wheat.setVanillaItem(Items.wheat);
    }
}
