package joshie.harvestmoon.helpers;

import joshie.harvestmoon.mining.MineTrackerServer;

public class MineHelper {
    public static void newDay() {
        ServerHelper.getMineTracker().newDay();
    }

    public static MineTrackerServer getServerTracker() {
        return ServerHelper.getMineTracker();
    }
}
