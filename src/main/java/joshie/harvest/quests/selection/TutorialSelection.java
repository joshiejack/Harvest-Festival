package joshie.harvest.quests.selection;

import joshie.harvest.api.quests.QuestQuestion;
import joshie.harvest.api.quests.QuestQuestion.QuestSelection;

public class TutorialSelection extends QuestSelection<QuestQuestion> {
    public TutorialSelection(String name) {
        super("harvestfestival.quest.tutorial." + name + ".question", "harvestfestival.quest.tutorial." + name + ".tutorial", "harvestfestival.quest.tutorial." + name + ".skip");
    }
}
