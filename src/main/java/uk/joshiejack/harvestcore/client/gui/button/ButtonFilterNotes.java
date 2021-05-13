package uk.joshiejack.harvestcore.client.gui.button;

import uk.joshiejack.harvestcore.client.gui.page.PageNotes;
import uk.joshiejack.harvestcore.registry.Note;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonTextField;

public class ButtonFilterNotes extends ButtonTextField {
    private final PageNotes note;

    public ButtonFilterNotes(GuiBook gui, PageNotes note, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, 14);
        this.note = note;
    }

    @Override
    public String getText() {
        return note.getSearch();
    }

    @Override
    public void setText(String text) {
        note.setFilter(text, (note) -> {
            if (text.startsWith("@")) {
                String modid = note.getRegistryName().getNamespace().toLowerCase();
                return modid.contains(text.substring(1));
            }

            return note.getLocalizedName().toLowerCase().contains(text);
        });
    }

    public interface Filter {
        boolean contains(Note note);
    }
}
