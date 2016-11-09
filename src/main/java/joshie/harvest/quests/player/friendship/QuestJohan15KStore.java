package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;

import java.util.Set;

@HFQuest("friendship.johan.vanilla")
public class QuestJohan15KStore extends QuestFriendship {
    public QuestJohan15KStore() {
        super(HFNPCs.TRADER, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.JOHAN_10K);
    }
}
