package joshie.harvest.init;

import joshie.harvest.api.HFApi;
import joshie.harvest.quests.QuestGoddess;

public class HFQuests {
    public static void preInit() {
        HFApi.QUESTS.register("tutorial.farming", new QuestGoddess());
        //HFApi.QUESTS.register("tutorial.cow", new QuestCowCare());
        //HFApi.QUESTS.register("tutorial.chicken", new QuestChickenCare());
    }
}
