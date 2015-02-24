package joshie.harvestmoon.core.config;

import static joshie.harvestmoon.core.helpers.generic.ConfigHelper.getInteger;
import net.minecraftforge.common.config.Configuration;

public class NPC {
    public static boolean FREEZE_NPC = false;
    public static int MARRIAGE_REQUIREMENT;

    public static void init(Configuration config) {
        MARRIAGE_REQUIREMENT = getInteger("Marriage Requirement", 60000);
    }
}
