package joshie.harvest.quests.town.seeds;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.npcs.HFNPCs;

@HFQuest("seeds.pineapple")
public class QuestPineapple extends QuestShipping {
    public QuestPineapple() {
        super(HFNPCs.GS_OWNER, Season.SUMMER, 1000);
    }
}
