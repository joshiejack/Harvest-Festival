package joshie.harvest.quests;

import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.quests.block.BlockQuestBoard;

import static joshie.harvest.core.helpers.ConfigHelper.getInteger;

@HFLoader
public class HFQuests {
    public static final BlockQuestBoard QUEST_BLOCK = new BlockQuestBoard().register("quest_block");

    public static void preInit(){}

    public static int LOGS_CARPENTER;

    public static void configure() {
        LOGS_CARPENTER = getInteger("Logs for Carpenter", 24);
    }
}
