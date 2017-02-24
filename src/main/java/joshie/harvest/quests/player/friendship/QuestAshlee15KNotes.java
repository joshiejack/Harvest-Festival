package joshie.harvest.quests.player.friendship;

import com.google.common.collect.Sets;
import joshie.harvest.api.knowledge.Note;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.knowledge.HFNotes;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;

import java.util.Set;

@HFQuest("friendship.ashlee.notes")
public class QuestAshlee15KNotes extends QuestFriendship {
    public QuestAshlee15KNotes() {
        super(HFNPCs.POULTRY, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.ASHLEE_10K);
    }

    @Override
    public Set<Note> getNotes() {
        return Sets.newHashSet(HFNotes.SECRET_CHICKENS);
    }
}
