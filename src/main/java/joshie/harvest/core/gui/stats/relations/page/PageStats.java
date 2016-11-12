package joshie.harvest.core.gui.stats.relations.page;

import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.core.gui.stats.GuiStats;
import joshie.harvest.core.gui.stats.button.ButtonTabRight;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

import java.util.List;

public class PageStats extends BookPage<GuiStats> {
    PageStats(String string, ItemStack stack) {
        super("relationships", string, stack);
    }

    @Override
    public void initGui(GuiStats gui, List<GuiButton> buttonList) {
        buttonList.add(new ButtonTabRight(gui, PageNPC.INSTANCE, buttonList.size(), 308, 32));
    }
}
