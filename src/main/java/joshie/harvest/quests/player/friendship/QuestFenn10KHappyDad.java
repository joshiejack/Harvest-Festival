package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

@HFQuest("friendship.fenn.parents")
public class QuestFenn10KHappyDad extends QuestFriendship {
    public QuestFenn10KHappyDad() {
        super(HFNPCs.CLOCKMAKER_CHILD, 10000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.FENN_5K);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.player.getRelationsForPlayer(player).affectRelationship(HFNPCs.CLOCKMAKER.getUUID(), 3000);
    }
}
