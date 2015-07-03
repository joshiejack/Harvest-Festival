package joshie.harvest.quests;

import joshie.harvest.api.HFApi;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.quests.shops.QuestStore;
import joshie.harvest.quests.tutorial.QuestGoddess;

public class HFQuests {
    public static void preInit() {
        HFApi.QUESTS.register("tutorial.farming", new QuestGoddess());
        HFApi.QUESTS.register("shops.general", new QuestStore(HFNPCs.gs_owner));
        //HFApi.QUESTS.register("tutorial.cow", new QuestCowCare());
        //HFApi.QUESTS.register("tutorial.chicken", new QuestChickenCare());
    }
}
