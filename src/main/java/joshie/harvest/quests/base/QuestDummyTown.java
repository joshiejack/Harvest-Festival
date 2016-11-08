package joshie.harvest.quests.base;

import joshie.harvest.api.quests.Quest;

import java.util.Set;

public abstract class QuestDummyTown extends QuestTown {
    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return false;
    }
}
