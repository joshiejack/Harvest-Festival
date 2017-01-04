package joshie.harvest.knowledge.gui.stats.notes.page;

import joshie.harvest.api.knowledge.Category;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.base.gui.BookPage;

public class PageTown extends PageNotes {
    public static final BookPage INSTANCE = new PageTown();

    PageTown() {
        super(Category.TOWNSHIP, HFBuildings.CARPENTER.getSpawner());
    }
}
