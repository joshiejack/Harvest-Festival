package joshie.harvest.knowledge.gui.stats.button;

import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.knowledge.gui.stats.collection.page.PageCollection;
import joshie.harvest.knowledge.gui.stats.notes.page.PageNotes;
import joshie.harvest.knowledge.gui.stats.quests.page.PageQuests;
import joshie.harvest.knowledge.gui.stats.relations.page.PageRelationship;

import static joshie.harvest.knowledge.gui.stats.GuiStats.*;

public abstract class ButtonTabLeft extends ButtonTab {
    @SuppressWarnings("unchecked")
    public ButtonTabLeft(GuiStats gui, BookPage page, int buttonId, int x, int y) {
        super(gui, page, buttonId, x, y, page.getCategory(), 26, 10);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        updateStaticValues();
        gui.setPage(getNewPage() != null ? getNewPage() : this.page);
        updateStaticValues();
    }

    private void updateStaticValues() {
        BookPage existing = gui.getPage();
        if (existing instanceof PageCollection) {
            collection = existing;
        } else if (existing instanceof PageRelationship) {
            relationships = existing;
        } else if (existing instanceof PageNotes) {
            notes = existing;
        } else if (existing instanceof PageQuests) {
            quests = existing;
        }
    }

    public abstract BookPage getNewPage();
}
