package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;

import java.util.Set;

@HFQuest("friendship.tomas.cheaper")
public class QuestTomas15KCheaperBlessing extends QuestFriendship {
    public QuestTomas15KCheaperBlessing() {
        super(HFNPCs.PRIEST, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.TOMAS_10K);
    }
}
