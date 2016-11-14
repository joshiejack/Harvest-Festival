package joshie.harvest.knowledge.stats.relations.page;

import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.knowledge.stats.GuiStats;
import joshie.harvest.knowledge.stats.button.ButtonTabRight;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.item.ItemStack;

import java.util.List;

public class PageStats extends BookPage<GuiStats> {
    PageStats(String string, ItemStack stack) {
        super("relationships", string, stack);
    }

    @Override
    public void initGui(GuiStats gui, List<GuiButton> buttonList, List<GuiLabel> labelList) {
        super.initGui(gui, buttonList, labelList);
        buttonList.add(new ButtonTabRight(gui, PageNPC.INSTANCE, buttonList.size(), 308, 32));
        buttonList.add(new ButtonTabRight(gui, PageAnimals.INSTANCE, buttonList.size(), 308, 66));
    }
}
