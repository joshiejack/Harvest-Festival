package joshie.harvest.quests;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.Quest.Selection;

public abstract class AbstractSelection<Q extends Quest> extends Selection<Q> {
    public AbstractSelection(String title, String line1, String line2) {
        super("harvestfestival.quest." + title, "harvestfestival.quest." + line1, "harvestfestival.quest." + line2);
    }

    public AbstractSelection(String title, String line1, String line2, String line3) {
        super("harvestfestival.quest." + title, "harvestfestival.quest." + line1, "harvestfestival.quest." + line2, "harvestfestival.quest." + line3);
    }
}
