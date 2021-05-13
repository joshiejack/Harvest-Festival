package uk.joshiejack.penguinlib.client.gui.book.page;

import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import uk.joshiejack.penguinlib.util.helpers.generic.MathsHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.List;

import static uk.joshiejack.penguinlib.client.gui.book.page.PageMultiple.PageSide.LEFT;
import static uk.joshiejack.penguinlib.client.gui.book.page.PageMultiple.PageSide.RIGHT;

public abstract class PageMultiple<R> extends Page {
    private boolean singleSided;
    private boolean hasForwardsButton;
    private int itemsPerPage;
    protected int start;

    public PageMultiple(String name, int itemsPerPage) {
        super(name);
        this.itemsPerPage = itemsPerPage;
    }

    public PageMultiple(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public PageMultiple setSingleSided() {
        singleSided = true;
        return this;
    }

    @Override
    public void initGui(List<GuiButton> buttonList, List<GuiLabel> labelList) {
        List<R> list = getList();
        int size = list.size();
        hasForwardsButton = start + itemsPerPage < size;

        for (int i = 0; i < (itemsPerPage / (singleSided ? 1 : 2)) ; i++) {
            int index = start + i; //Page One
            if (index < size) {
                addToList(buttonList, labelList, gui, list.get(index), LEFT, index - start);
            }

            if (!singleSided) {
                //Page Two
                int index2 = start + i + (itemsPerPage / 2); //Page 2
                if (index2 < size) {
                    addToList(buttonList, labelList, gui, list.get(index2), RIGHT, index - start);
                }
            }
        }
    }

    public enum PageSide {
        LEFT, RIGHT
    }

    protected abstract void addToList(List<GuiButton> buttonList, List<GuiLabel> labelList, GuiBook gui, R entry, PageSide side, int position);

    public abstract List<R> getList();

    @Override
    public boolean hasForwardsButton() {
        return hasForwardsButton;
    }

    @Override
    public boolean hasBackwardsButton() {
        return start != 0;
    }

    @Override
    public void onForward() {
        start = MathsHelper.constrainToRangeInt(start + itemsPerPage, 0, Integer.MAX_VALUE);
        gui.setPage(this);
    }

    @Override
    public void onBack() {
        if (start != 0) {
            start = MathsHelper.constrainToRangeInt(start - itemsPerPage, 0, Integer.MAX_VALUE);
            gui.setPage(this);
        } else {
            Page previous = getPreviousPage();
            if (previous != null) {
                gui.setPage(previous);
            }
        }
    }

    @Nullable
    protected Page getPreviousPage() {
        return this;
    }

    public abstract static class PageMultipleLabel<R> extends PageMultiple<R> {
        protected final String unlocalized;
        protected boolean empty;

        public PageMultipleLabel(String name, int itemsPerPage) {
            super(name, itemsPerPage);
            this.unlocalized = name + ".empty";
        }

        @Override
        public void initGui(List<GuiButton> buttonList, List<GuiLabel> labelList) {
            empty = getList().isEmpty(); //Add the emptyness
            labelList.add(createLabel(TextFormatting.UNDERLINE + getDisplayName(), labelList.size(), gui.getGuiLeft() + 60, gui.getGuiTop() + 8, 60, 20));
            super.initGui(buttonList, labelList);
        }

        @Override
        public void drawScreen(int x, int y) {
            if (empty) drawStringWithWrap(StringHelper.localize(unlocalized), 20, 24, 124);
        }

        @Override
        protected void addToList(List<GuiButton> buttonList, List<GuiLabel> labelList, GuiBook gui, R entry, PageSide side, int position) {
            labelList.add(createLabel(gui, entry, side, position));
        }

        protected abstract LabelBook createLabel(GuiBook gui, R entry, PageSide side, int position);
    }

    public abstract static class PageMultipleButton<R> extends PageMultiple<R> {
        private final String unlocalized;
        protected boolean empty;

        public PageMultipleButton(String name, int itemsPerPage) {
            super(name, itemsPerPage);
            this.unlocalized = name + ".empty";
        }

        @Override
        public void initGui(List<GuiButton> buttonList, List<GuiLabel> labelList) {
            empty = getList().isEmpty(); //Add the emptyness
            labelList.add(createLabel(TextFormatting.UNDERLINE + getDisplayName(), labelList.size(), gui.getGuiLeft() + 60, gui.getGuiTop() + 8, 60, 20));
            super.initGui(buttonList, labelList);
        }

        @Override
        public void drawScreen(int x, int y) {
            if (empty) drawStringWithWrap(StringHelper.localize(unlocalized), 20, 24, 124);
        }

        @Override
        protected void addToList(List<GuiButton> buttonList, List<GuiLabel> labelList, GuiBook gui, R entry, PageSide side, int position) {
            buttonList.add(createButton(gui, entry, side, buttonList.size(), position));
        }

        protected abstract ButtonBook createButton(GuiBook gui, R entry, PageSide side, int id, int position);
    }
}
