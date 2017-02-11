package joshie.harvest.npcs.greeting;

import joshie.harvest.quests.Quests;

public class GreetingBeforeDanieru extends GreetingBeforeQuest {
    public GreetingBeforeDanieru(String text) {
        super("harvestfestival.quest.tutorial.upgrading.reminder.blacksmith." + text, Quests.JADE_MEET, Quests.JENNI_MEET);
    }
}
