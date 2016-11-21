package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.knowledge.HFNotes;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

@HFQuest("friendship.jim.notes")
public class QuestJim15KNotes extends QuestFriendship {
    public QuestJim15KNotes() {
        super(HFNPCs.BARN_OWNER, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.JIM_10K);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().learnNote(HFNotes.SECRET_LIVESTOCK);
    }
}
