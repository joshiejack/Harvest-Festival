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
    public Set<Note> getNotes() {
        return Sets.newHashSet(HFNotes.SECRET_LIVESTOCK);
    }
}
