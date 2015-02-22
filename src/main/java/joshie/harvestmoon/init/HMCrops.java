package joshie.harvestmoon.init;

import static joshie.harvestmoon.calendar.Season.AUTUMN;
import static joshie.harvestmoon.calendar.Season.SPRING;
import static joshie.harvestmoon.calendar.Season.SUMMER;
import joshie.harvestmoon.HMConfiguration;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.lib.CropMeta;
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

    public static void init() {
        //Spring Crops
        turnip = new Crop("turnip", SPRING, 120, 60, 5, 0, 0, CropMeta.TURNIP);
        potato = new Crop("potato", SPRING, 150, 80, 8, 0, 0, CropMeta.POTATO);
        cucumber = new Crop("cucumber", SPRING, 200, 60, 10, 5, 0, CropMeta.CUCUMBER);
        strawberry = new Crop("strawberry", SPRING, 150, 30, 9, 7, 3, CropMeta.STRAWBERRY);
        cabbage = new Crop("cabbage", SPRING, 500, 250, 15, 0, 8, CropMeta.CABBAGE);

        //Summer Crops
        tomato = new Crop("tomato", SUMMER, 200, 60, 10, 7, 0, CropMeta.TOMATO);
        onion = new Crop("onion", SUMMER, 150, 80, 8, 0, 0, CropMeta.ONION);
        corn = new Crop("corn", SUMMER, 300, 100, 15, 12, 0, CropMeta.CORN);
        pumpkin = new Crop("pumpkin", SUMMER, 500, 250, 15, 0, 3, CropMeta.PUMPKIN);
        pineapple = new Crop("pineapple", SUMMER, 1000, 500, 21, 5, 8, CropMeta.PINEAPPLE);

        //Autumn Crops
        eggplant = new Crop("eggplant", AUTUMN, 120, 80, 10, 7, 0, CropMeta.EGGPLANT);
        carrot = new Crop("carrot", AUTUMN, 300, 120, 8, 0, 0, CropMeta.CARROT);
        sweet_potato = new Crop("potato_sweet", AUTUMN, 300, 120, 6, 4, 0, CropMeta.SWEET_POTATO);
        spinach = new Crop("spinach", AUTUMN, 200, 80, 6, 0, 3, CropMeta.SPINACH);
        green_pepper = new Crop("pepper_green", AUTUMN, 150, 40, 8, 2, 8, CropMeta.GREEN_PEPPER);

        //All Season Grass
        grass = new Crop("grass", new Season[] { SPRING, SUMMER, AUTUMN }, 500, 0, 11, 0, 0, CropMeta.GRASS).setIsStatic().setHasAlternativeName();
        if (HMConfiguration.overrides.potato) potato.setVanillaItem(Items.potato);
        if (HMConfiguration.overrides.carrot) carrot.setVanillaItem(Items.carrot);
    }
}
