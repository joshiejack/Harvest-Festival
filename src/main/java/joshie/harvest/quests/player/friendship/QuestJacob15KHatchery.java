package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

@HFQuest("friendship.jacob.hatchery")
public class QuestJacob15KHatchery extends QuestFriendship {
    public QuestJacob15KHatchery() {
        super(HFNPCs.FISHERMAN, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.JACOB_10K);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        super.onQuestCompleted(player);
        HFApi.quests.completeQuestConditionally(Quests.HATCHERY, player);
    }
}
