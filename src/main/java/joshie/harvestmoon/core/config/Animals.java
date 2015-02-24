package joshie.harvestmoon.core.config;

import static joshie.harvestmoon.core.helpers.generic.ConfigHelper.getBoolean;
import static joshie.harvestmoon.core.helpers.generic.ConfigHelper.getInteger;
import net.minecraftforge.common.config.Configuration;

public class Animals {
    public static boolean CAN_SPAWN;
    
    public static int MAX_LITTER_SIZE;
    public static int LITTER_EXTRA_CHANCE;
    public static int PREGNANCY_TIMER;
    public static int AGING_TIMER;
    public static int CHICKEN_TIMER;

    public static void init(Configuration config) {
        CAN_SPAWN = getBoolean("Enable Animal Spawning", false);
        PREGNANCY_TIMER = getInteger("Pregnancy: Number of days", 7);
        CHICKEN_TIMER = PREGNANCY_TIMER / 2;
        MAX_LITTER_SIZE = getInteger("Pregnancy: Max litter size", 5);
        LITTER_EXTRA_CHANCE = getInteger("Pregnancy: Chance of extra birth", 4);
        AGING_TIMER = getInteger("Maturity: Number of days", 14);
    }
}
