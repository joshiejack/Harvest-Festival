package joshie.harvestmoon.init;

import java.util.HashMap;

import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.quest.IQuest;
import joshie.harvestmoon.api.quest.IQuestRegistry;
import joshie.harvestmoon.quests.QuestChickenCare;
import joshie.harvestmoon.quests.QuestCowCare;
import joshie.harvestmoon.quests.QuestTomatoes;

public class HMQuests implements IQuestRegistry {
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

    public static void init() {
        HMApi.QUESTS.register("tutorial.tomatoes", new QuestTomatoes());
        HMApi.QUESTS.register("tutorial.cow", new QuestCowCare());
        HMApi.QUESTS.register("tutorial.chicken", new QuestChickenCare());
    }
}
