package joshie.harvest.npcs.greeting;

import joshie.harvest.quests.Quests;

public class GreetingBeforeAshlee extends GreetingBeforeQuest {
    public GreetingBeforeAshlee(String text) {
        super("harvestfestival.quest." + text, Quests.JADE_MEET, Quests.ASHLEE_MEET);
    }
}
