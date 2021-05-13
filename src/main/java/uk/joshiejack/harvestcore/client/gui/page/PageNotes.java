package uk.joshiejack.harvestcore.client.gui.page;

import uk.joshiejack.harvestcore.client.gui.button.ButtonFilterNotes;
import uk.joshiejack.harvestcore.client.gui.button.ButtonNote;
import uk.joshiejack.harvestcore.client.gui.button.PageSearchable;
import uk.joshiejack.harvestcore.client.gui.label.LabelNote;
import uk.joshiejack.harvestcore.registry.Note;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

import static uk.joshiejack.penguinlib.client.gui.book.page.PageMultiple.PageSide.LEFT;

public class PageNotes extends PageSearchable.MultipleButton<Note> {
    private ButtonFilterNotes.Filter filter = (s) -> true;
    private final String category;
    private LabelNote note;

    public PageNotes(String category, ItemStack icon) {
        super("harvestcore.guide.notes." + category, 49);
        this.category = category;
        this.icon = new Icon(icon, 0, 0);
    }

    public void setFilter(String text, ButtonFilterNotes.Filter filter) {
        this.search = text;
        this.filter = filter;
    }

    @Override
    public void setGui(GuiBook gui) {
        super.setGui(gui); //Set the gui underneath
        note = new LabelNote(gui, 164, 20, note);
    }

    public Note getNote() {
        return note.getNote();
    }

    @Override
    protected void addToList(List<GuiButton> buttonList, List<GuiLabel> labelList, GuiBook gui, Note entry, PageSide side, int position) {
        super.addToList(buttonList, labelList, gui, entry, side, position);
        labelList.add(note); //Add the note label itself
        buttonList.add(new ButtonFilterNotes(gui, this, buttonList.size(), 177, 202));
    }

    public void setNote(Note note) {
        this.note.set(note);
    }

    @Override
    protected ButtonBook createButton(GuiBook gui, Note entry, PageSide side, int id, int position) {
        int x = position % 7;
        int y = position / 7;
        return new ButtonNote(gui, this, entry, id, (side == LEFT ? 21 : 163) + (x * 18), 24 + (y * 18));
    }

    @Override
    public List<Note> getList() {
        return Note.REGISTRY.values().stream().filter(b -> filter.contains(b) && (b.category().equals(category) && (!b.isHidden() || b.isUnlocked(gui.mc.player)))).collect(Collectors.toList());
    }
}
