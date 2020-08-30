package joshie.harvest.npcs.greeting;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.quests.Quests;

public class GreetingBeforeDanieru extends GreetingBeforeQuest {
    public GreetingBeforeDanieru(NPC npc) {
        super("harvestfestival.quest.tutorial.upgrading.reminder.blacksmith." + npc.getResource().getPath(), Quests.JADE_MEET, Quests.DANIERU_MEET);
    }
}
