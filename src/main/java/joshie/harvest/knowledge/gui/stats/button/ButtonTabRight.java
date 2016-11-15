package joshie.harvest.knowledge.gui.stats.button;

import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.knowledge.gui.stats.GuiStats;

public class ButtonTabRight extends ButtonTab {
    @SuppressWarnings("unchecked")
    public ButtonTabRight(GuiStats gui, BookPage page, int buttonId, int x, int y) {
        super(gui, page, buttonId, x, y, page.getDisplayName(), 0, 0);
    }
}
