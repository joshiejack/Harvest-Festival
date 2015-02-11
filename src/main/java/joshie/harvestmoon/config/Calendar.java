package joshie.harvestmoon.config;

import static joshie.harvestmoon.helpers.generic.ConfigHelper.getInteger;
import net.minecraftforge.common.config.Configuration;

public class Calendar {
    public static int DAYS_PER_SEASON = 30;

    public static void init(Configuration config) {
        DAYS_PER_SEASON = getInteger("Days per Season", 30);
    }
}
