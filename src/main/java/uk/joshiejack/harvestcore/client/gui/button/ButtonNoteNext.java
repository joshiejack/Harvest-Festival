package uk.joshiejack.harvestcore.client.gui.button;

import uk.joshiejack.harvestcore.client.gui.label.LabelNote;
import uk.joshiejack.harvestcore.client.gui.page.PageNotes;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonForward;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;

public class ButtonNoteNext extends ButtonForward {
    private final LabelNote label;

    public ButtonNoteNext(PageNotes origin, LabelNote label, GuiBook gui, int buttonId, int x, int y) {
        super(origin, gui, buttonId, x, y);
        this.label = label;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        label.onForward();
    }
}
