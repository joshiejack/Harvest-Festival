package joshie.harvest.quests;

import joshie.harvest.api.HFApi;
import joshie.harvest.quests.tutorial.QuestGoddess;

public class HFQuests {
    public static void preInit() {
        HFApi.quests.register("tutorial.farming", new QuestGoddess());
        //HFApi.quests.register("tutorial.cow", new QuestCowCare());
        //HFApi.quests.register("tutorial.chicken", new QuestChickenCare());
    }
}
