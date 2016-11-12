package joshie.harvest.core.gui.stats.button;

import joshie.harvest.core.base.gui.GuiBaseBook;
import net.minecraft.client.gui.GuiButton;

public class ButtonBook<G extends GuiBaseBook> extends GuiButton {
    protected G gui;

    public ButtonBook(G gui, int buttonId, int x, int y, String string) {
        super(buttonId, gui.guiLeft + x, gui.guiTop + y, string);
        this.gui = gui;
    }
}
