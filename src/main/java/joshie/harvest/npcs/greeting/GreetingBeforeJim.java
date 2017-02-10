package joshie.harvest.npcs.greeting;

import joshie.harvest.quests.Quests;

public class GreetingBeforeJim extends GreetingBeforeQuest {
    public GreetingBeforeJim(String text) {
        super("harvestfestival.quest." + text, Quests.JADE_MEET, Quests.JIM_MEET);
    }
}
