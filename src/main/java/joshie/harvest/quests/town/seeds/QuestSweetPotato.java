package joshie.harvest.quests.town.seeds;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.core.achievements.HFAchievements;
import joshie.harvest.npcs.HFNPCs;

@HFQuest("seeds.sweetpotato")
public class QuestSweetPotato extends QuestShipping {
    public QuestSweetPotato() {
        super(HFAchievements.sweetPotatoes, HFNPCs.GS_OWNER, Season.AUTUMN, 1000);
    }
}
