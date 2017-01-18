package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

@HFQuest("friendship.jade.flowers")
public class QuestJade15KFlowers extends QuestFriendship {
    public QuestJade15KFlowers() {
        super(HFNPCs.FLOWER_GIRL, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.JADE_10K);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.quests.completeQuestConditionally(Quests.FLOWER_BUYER, player);
    }
}
