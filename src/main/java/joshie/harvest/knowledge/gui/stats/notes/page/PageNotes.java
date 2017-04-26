package joshie.harvest.knowledge.gui.stats.notes.page;

import joshie.harvest.api.knowledge.Category;
import joshie.harvest.api.knowledge.Note;
import joshie.harvest.api.knowledge.NoteRender;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.knowledge.HFNotes;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.knowledge.gui.stats.button.ButtonNext;
import joshie.harvest.knowledge.gui.stats.button.ButtonPrevious;
import joshie.harvest.knowledge.gui.stats.button.ButtonTabRight;
import joshie.harvest.knowledge.gui.stats.notes.button.ButtonNote;
import joshie.harvest.knowledge.gui.stats.notes.button.ButtonNoteNext;
import joshie.harvest.knowledge.gui.stats.notes.button.ButtonNotePrevious;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PageNotes extends BookPage<GuiStats> {
    public static Note note = HFNotes.BLUEPRINTS;
    private static int page;
    private static String[] split;
    private static int pages;
    private final List<Note> list;

    PageNotes(Category category, ItemStack stack) {
        super("note", category.getUnlocalizedName(), stack);
        this.list = new ArrayList<>();
        this.list.addAll(Note.REGISTRY.values().stream().filter(note -> note.getCategory() == category).collect(Collectors.toList()));
    }

    public static void setDisplayPage(int amount) {
        page += amount;
        MCClientHelper.initGui();
    }

    public static void setNote(Note note) {
        PageNotes.note = note;
        PageNotes.page = 0;
        String[] split = WordUtils.wrap(note.getDescription(), 26, "\n", false).split("\n");
        if (split.length < 20) PageNotes.split = null;
        else {
            PageNotes.split = split;
            pages = split.length / 18;
        }

        MCClientHelper.initGui();
    }

    @Override
    public void initGui(GuiStats gui, List<GuiButton> buttonList, List<GuiLabel> labelList) {
        super.initGui(gui, buttonList, labelList);
        labelList.add(createLabel(TextFormatting.UNDERLINE + getDisplayName(), labelList.size(), gui.guiLeft + 60, gui.guiTop + 8, 60, 20));
        buttonList.add(new ButtonTabRight(gui, PageFarming.INSTANCE, buttonList.size(), 308, 32));
        buttonList.add(new ButtonTabRight(gui, PageActivities.INSTANCE, buttonList.size(), 308, 66));
        buttonList.add(new ButtonTabRight(gui, PageTown.INSTANCE, buttonList.size(), 308, 100));
        //buttonList.add(new ButtonTabRight(gui, PageOther.INSTANCE, buttonList.size(), 308, 134));

        //Remove secret notes that we haven't discovered from even showing up
        List<Note> list = new ArrayList<>(this.list);
        Iterator<Note> it = list.iterator();
        while (it.hasNext()) {
            Note note = it.next();
            if (note.isSecret() && !HFTrackers.getClientPlayerTracker().getTracking().getLearntNotes().contains(note.getResource())) {
                it.remove();
            }
        }

        int j = 0;
        int k = 0;
        int added = 0;
        for (int i = start * 56; added < start * 56 + 56 && i < list.size(); i++) {
            Note note = list.get(i);
            if (k == 7) {
                k = 0;
                j++;
            }

            k++;

            added++;
            buttonList.add(new ButtonNote(gui, note, buttonList.size(), 3 + k * 18, 24 + j * 18));
        }

        int maxStart = list.size() / 112;
        if (start < maxStart) buttonList.add(new ButtonNext(gui, buttonList.size(), 130, 172));
        if (start != 0) buttonList.add(new ButtonPrevious(gui, buttonList.size(), 20, 172));

        if (split != null) {
            if (page < pages)
                buttonList.add(new ButtonNoteNext(gui, buttonList.size(), 273, 172));
            if (page != 0)
                buttonList.add(new ButtonNotePrevious(gui, buttonList.size(), 173, 172));
        }

        if (note.getRender() != null) note.getRender().initRender(gui.mc, gui, gui.guiLeft, gui.guiTop);
    }

    @Override
    public void drawScreen(int x, int y) {
        if (split != null) {
            int pos = 0;
            int factor = page * 18;
            for (int i = factor; i < split.length && i < factor + 18; i++) {
                drawUnicodeFont(split[i], 164, 23 + (pos * 8), 500);
                pos++;
            }
        } else drawUnicodeFont(note.getDescription(), 164, 23, 126);

        NoteRender render = note.getRender();
        if (render != null) {
            if (!render.isInit()) {
                render.initRender(gui.mc, gui, gui.guiLeft, gui.guiTop);
            }

            render.drawScreen(x, y);
        }
    }
}
