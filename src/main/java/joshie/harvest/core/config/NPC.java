package joshie.harvest.core.config;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getInteger;

public class NPC {
    public static boolean FREEZE_NPC = false;
    public static int MAXIMUM_FRIENDSHIP;
    public static int REAL_MARRIAGE_REQUIREMENT;
    public static int ADJUSTED_MARRIAGE_REQUIREMENT;

    public static void init() {
        MAXIMUM_FRIENDSHIP = getInteger("Maximum Friendship", 65535);
        ADJUSTED_MARRIAGE_REQUIREMENT = getInteger("Marriage Requirement", 60000);
        REAL_MARRIAGE_REQUIREMENT = ADJUSTED_MARRIAGE_REQUIREMENT - Short.MAX_VALUE;
    }
}
