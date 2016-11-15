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

@HFQuest("friendship.danieru.notes")
public class QuestDanieru15KNotes extends QuestFriendship {
    public QuestDanieru15KNotes() {
        super(HFNPCs.BLACKSMITH, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.DANIERU_10K);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().learnNote(HFNotes.SECRET_CURSED_TOOLS);
    }
}
