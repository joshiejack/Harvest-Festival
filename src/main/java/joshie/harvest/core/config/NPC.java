package joshie.harvest.core.config;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getInteger;
import net.minecraftforge.common.config.Configuration;

public class NPC {
    public static boolean FREEZE_NPC = false;
    public static int REAL_MARRIAGE_REQUIREMENT;
    public static int ADJUSTED_MARRIAGE_REQUIREMENT;

    public static void init(Configuration config) {
        ADJUSTED_MARRIAGE_REQUIREMENT = getInteger("Marriage Requirement", 60000);
        REAL_MARRIAGE_REQUIREMENT = ADJUSTED_MARRIAGE_REQUIREMENT - Short.MAX_VALUE;
    }
}
