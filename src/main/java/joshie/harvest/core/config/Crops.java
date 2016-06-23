package joshie.harvest.core.config;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getBoolean;
import static joshie.harvest.core.helpers.generic.ConfigHelper.getInteger;

public class Crops {
    public static boolean seasonalBonemeal;
    public static boolean enableBonemeal;
    public static boolean alwaysGrow;
    public static boolean disableVanillaHoe;
    public static boolean disableVanillaSeeds;
    public static boolean disableVanillaMoisture;
    public static int sprinklerDrain;

    public static void init() {
        alwaysGrow = getBoolean("Crops > Always Grow", false, "This setting when set to true, will make crops grow based on random tick instead of day by day, Take note that this also affects the number of seeds a crop bag will plant. It will only plant 3 seeds instead of a 3x3");
        enableBonemeal = getBoolean("Crops > Enable Bonemeal", false, "Enabling this will allow you to use bonemeal on plants to grow them.");
        seasonalBonemeal = getBoolean("Crops > Seasonal Bonemeal", true, "If you have bonemeal enabled, with this setting active, bonemeal will only work when the crop is in season");
        disableVanillaSeeds = getBoolean("Disable Vanilla Seeds", false, "If this is true, vanilla seeds will not plant their crops");
        disableVanillaHoe = getBoolean("Disable Vanilla Hoe", false, "If this is true, vanilla hoes will not till dirt");
        disableVanillaMoisture = getBoolean("Disable Vanilla Moisture", true, "If this is set to true then farmland will not automatically become wet, and must be watered, it will also not automatically revert to dirt. (Basically disables random ticks for farmland)");
        sprinklerDrain = getInteger("Sprinkler > Daily Consumption", 250, "This number NEEDs to be a factor of 1000, Otherwise you'll have trouble refilling the sprinkler manually. Acceptable values are: 1, 2, 4, 5, 8, 10, 20, 25, 40, 50, 100, 125, 200, 250, 500, 1000");
    }
}