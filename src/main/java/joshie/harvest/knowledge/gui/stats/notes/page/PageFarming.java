package joshie.harvest.knowledge.gui.stats.notes.page;

import joshie.harvest.api.knowledge.Category;
import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.crops.HFCrops;

public class PageFarming extends PageNotes {
    public static final BookPage INSTANCE = new PageFarming();

    PageFarming() {
        super(Category.FARMING, HFCrops.STRAWBERRY.getCropStack(1));
    }
}
