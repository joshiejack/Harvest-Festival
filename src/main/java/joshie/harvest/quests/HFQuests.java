package joshie.harvest.quests;

import joshie.harvest.core.util.HFLoader;

import static joshie.harvest.core.helpers.ConfigHelper.getInteger;

@HFLoader
public class HFQuests {
    public static int LOGS_CARPENTER;

    public static void configure() {
        LOGS_CARPENTER = getInteger("Logs for Carpenter", 24);
    }
}
