package uk.joshiejack.settlements.client.gui.page;

import com.google.common.collect.Lists;
import uk.joshiejack.settlements.client.gui.GuiJournal;
import uk.joshiejack.settlements.client.gui.label.LabelQuest;
import uk.joshiejack.settlements.quest.settings.Information;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import uk.joshiejack.penguinlib.client.gui.book.page.PageMultiple;
import uk.joshiejack.penguinlib.util.PenguinGroup;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static uk.joshiejack.penguinlib.client.gui.book.page.PageMultiple.PageSide.LEFT;

public class PageQuests extends PageMultiple.PageMultipleLabel<Information> {
    public static final PageQuests INSTANCE = new PageQuests(null, 0);
    public static final PageQuests PLAYER = new PageQuests(PenguinGroup.PLAYER, 16);
    public static final PageQuests TEAM = new PageQuests(PenguinGroup.TEAM, 32);
    public static final PageQuests WORLD = new PageQuests(PenguinGroup.GLOBAL, 48);
    private static final List<Information> informations = Lists.newArrayList();
    private final PenguinGroup filter;

    private PageQuests(@Nullable PenguinGroup filter, int x) {
        super("settlements.journal.quests." + (filter == null ? "all" : filter.name().toLowerCase(Locale.ENGLISH)), 5);
        this.icon = new Icon(GuiJournal.ICONS, x, 0);
        this.filter = filter;
    }

    @Override
    protected LabelBook createLabel(GuiBook gui, Information entry, PageSide side, int position) {
        return new LabelQuest(gui, entry, (side == LEFT ? 21: 163), 32 + (position) * 25);
    }

    @Override
    public List<Information> getList() {
        return filter == null ? informations : informations.stream().filter(i -> i.getGroup() == filter).collect(Collectors.toList());
    }

    public void setInformations(List<Information> informations) {
        PageQuests.informations.clear();
        PageQuests.informations.addAll(informations);
    }
}
