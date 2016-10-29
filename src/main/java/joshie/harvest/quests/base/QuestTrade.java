package joshie.harvest.quests.base;

import joshie.harvest.api.quests.Quest;
import java.util.Set;

public class QuestTrade extends Quest {
    public QuestTrade() {}

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return true;
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public boolean isRealQuest() {
        return false;
    }
}
