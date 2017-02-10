package joshie.harvest.npcs.greeting;

import joshie.harvest.quests.Quests;

public class GreetingBeforeJenni extends GreetingBeforeQuest {
    public GreetingBeforeJenni(String text) {
        super("harvestfestival.quest." + text, Quests.JADE_MEET, Quests.JENNI_MEET);
    }
}
