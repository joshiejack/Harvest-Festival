package joshie.harvestmoon.init;

import java.util.HashMap;

import joshie.harvestmoon.quests.Quest;
import joshie.harvestmoon.quests.QuestChickenCare;
import joshie.harvestmoon.quests.QuestCowCare;
import joshie.harvestmoon.quests.QuestTomatoes;

public class HMQuests {
    private static HashMap<String, Quest> quests = new HashMap();

    public static void register(String name, Quest quest) {
        quests.put(name, quest.setName(name));
    }

    public static HashMap<String, Quest> getQuests() {
        return quests;
    }

    public static Quest get(String name) {
        return quests.get(name);
    }

    public static void init() {
        register("tutorial.tomatoes", new QuestTomatoes());
        register("tutorial.cow", new QuestCowCare());
        register("tutorial.chicken", new QuestChickenCare());
    }
}
