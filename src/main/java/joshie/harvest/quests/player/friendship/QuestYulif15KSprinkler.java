package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

@HFQuest("friendship.yulif.sprinkler")
public class QuestYulif15KSprinkler extends QuestFriendship {
    public QuestYulif15KSprinkler() {
        super(HFNPCs.BUILDER, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.YULIF_10K);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        super.onQuestCompleted(player);
        HFApi.quests.completeQuestConditionally(Quests.SPRINKLER, player);
    }
}
