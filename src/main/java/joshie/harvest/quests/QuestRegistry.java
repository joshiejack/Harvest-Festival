package joshie.harvest.quests;

import java.util.HashMap;

import joshie.harvest.api.quest.IQuest;
import joshie.harvest.api.quest.IQuestRegistry;

public class QuestRegistry implements IQuestRegistry {
    private static HashMap<String, IQuest> quests = new HashMap();

    @Override
    public IQuest register(String name, IQuest quest) {
        quest.setUniqueName(name);
        quests.put(name, quest);
        return quest;
    }

    public static HashMap<String, IQuest> getQuests() {
        return quests;
    }

    @Override
    public IQuest get(String name) {
        return quests.get(name);
    }
}
