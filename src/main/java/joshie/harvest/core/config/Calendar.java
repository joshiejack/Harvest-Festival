package joshie.harvest.core.config;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getInteger;
import net.minecraftforge.common.config.Configuration;

public class Calendar {
    public static int DAYS_PER_SEASON;
    public static long TICKS_PER_DAY;

    public static void init(Configuration config) {
        DAYS_PER_SEASON = getInteger("Days per Season", 30);
        TICKS_PER_DAY = getInteger("Ticks per Day", 24000);
    }
}
