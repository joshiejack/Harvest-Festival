package joshie.harvest.quests;

import joshie.harvest.core.helpers.RegistryHelper;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.quests.block.BlockQuestBoard;
import joshie.harvest.quests.block.TileQuestBoard;

import static joshie.harvest.core.helpers.ConfigHelper.getInteger;

@HFLoader
public class HFQuests {
    public static final BlockQuestBoard QUEST_BLOCK = new BlockQuestBoard().register("quest_block");

    public static void preInit(){
        RegistryHelper.registerTiles(TileQuestBoard.class);
    }

    public static int LOGS_CARPENTER;

    public static void configure() {
        LOGS_CARPENTER = getInteger("Logs for Carpenter", 24);
    }
}
