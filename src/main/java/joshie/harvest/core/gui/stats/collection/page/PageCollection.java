package joshie.harvest.core.gui.stats.collection.page;

import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.core.gui.stats.GuiStats;
import joshie.harvest.core.gui.stats.button.ButtonTabRight;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

import java.util.List;

public class PageCollection extends BookPage<GuiStats> {
    PageCollection(String string, ItemStack stack) {
        super("collection", string, stack);
    }

    @Override
    public void initGui(GuiStats gui, List<GuiButton> buttonList) {
        buttonList.add(new ButtonTabRight(gui, PageShipping.INSTANCE, buttonList.size(), 308, 32));
        buttonList.add(new ButtonTabRight(gui, PageFishing.INSTANCE, buttonList.size(), 308, 66));
        buttonList.add(new ButtonTabRight(gui, PageMining.INSTANCE, buttonList.size(), 308, 100));
        buttonList.add(new ButtonTabRight(gui, PageCooking.INSTANCE, buttonList.size(), 308, 134));
    }
}
