package joshie.harvest.init;

import java.util.HashMap;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.api.quest.IQuestRegistry;
import joshie.harvest.quests.QuestGoddess;

public class HFQuests implements IQuestRegistry {
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
        HFApi.QUESTS.register("tutorial.farming", new QuestGoddess());
        //HFApi.QUESTS.register("tutorial.cow", new QuestCowCare());
        //HFApi.QUESTS.register("tutorial.chicken", new QuestChickenCare());
    }
}
