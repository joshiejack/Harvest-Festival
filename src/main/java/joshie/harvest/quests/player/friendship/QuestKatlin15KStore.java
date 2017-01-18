package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;

import java.util.Set;

@HFQuest("friendship.katlin.kitchen")
public class QuestKatlin15KStore extends QuestFriendship {
    public QuestKatlin15KStore() {
        super(HFNPCs.CAFE_GRANNY, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.KATLIN_10K);
    }
}
