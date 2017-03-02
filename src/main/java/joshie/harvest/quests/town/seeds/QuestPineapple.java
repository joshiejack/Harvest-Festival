package joshie.harvest.quests.town.seeds;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.core.achievements.HFAchievements;
import joshie.harvest.npcs.HFNPCs;

@HFQuest("seeds.pineapple")
public class QuestPineapple extends QuestShipping {
    public QuestPineapple() {
        super(HFAchievements.pineapple, HFNPCs.GS_OWNER, Season.SUMMER, 1000);
    }
}
