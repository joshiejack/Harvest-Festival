package joshie.harvest.knowledge.stats.button;

import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.knowledge.stats.GuiStats;

public class ButtonTabLeft extends ButtonTab {
    @SuppressWarnings("unchecked")
    public ButtonTabLeft(GuiStats gui, BookPage page, int buttonId, int x, int y) {
        super(gui, page, buttonId, x, y, page.getCategory(), 26, 10);
    }
}
