package joshie.harvest.core.config;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getDouble;
import static joshie.harvest.core.helpers.generic.ConfigHelper.getInteger;

public class NPC {
    public static boolean freezeNPC = false;
    public static int maximumFriendship;
    public static int marriageRequirement;
    public static double townDistance;

    public static void init() {
        maximumFriendship = getInteger("Maximum Friendship", 65535);
        marriageRequirement = getInteger("Marriage Requirement", 60000);
        townDistance = getDouble("Distance Between Towns", 256D);
    }
}
