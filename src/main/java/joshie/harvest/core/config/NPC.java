package joshie.harvest.core.config;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getDouble;
import static joshie.harvest.core.helpers.generic.ConfigHelper.getInteger;

public class NPC {
    public static boolean FREZZE_NPCS = false;
    public static int MAX_FRIENDSHIP;
    public static int MARRIAGE_REQUIREMENT;
    public static double TOWN_DISTANCE;

    public static void init() {
        MAX_FRIENDSHIP = getInteger("Maximum Friendship", 65535);
        MARRIAGE_REQUIREMENT = getInteger("Marriage Requirement", 60000);
        TOWN_DISTANCE = getDouble("Distance Between Towns", 256D);
    }
}
