package joshie.harvest.quests;

import joshie.harvest.api.quest.IQuest;
import joshie.harvest.api.quest.IQuestRegistry;

import java.util.Collection;
import java.util.HashMap;

public class QuestRegistry implements IQuestRegistry {
    private final HashMap<String, IQuest> quests = new HashMap<>();

    @Override
    public Collection<IQuest> getQuests() {
        return quests.values();
    }

    @Override
    public IQuest register(String name, IQuest quest) {
        quest.setUniqueName(name);
        quests.put(name, quest);
        return quest;
    }

    @Override
    public IQuest get(String name) {
        return quests.get(name);
    }
}
