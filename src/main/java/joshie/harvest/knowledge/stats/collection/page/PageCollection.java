package joshie.harvest.knowledge.stats.collection.page;

import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.knowledge.stats.GuiStats;
import joshie.harvest.knowledge.stats.button.ButtonTabRight;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class PageCollection extends BookPage<GuiStats> {
    PageCollection(String string, ItemStack stack) {
        super("collection", string, stack);
    }

    @Override
    public void initGui(GuiStats gui, List<GuiButton> buttonList, List<GuiLabel> labelList) {
        super.initGui(gui, buttonList, labelList);
        labelList.add(createLabel(TextFormatting.UNDERLINE + getDisplayName(), labelList.size(), gui.guiLeft + 60, gui.guiTop + 8, 60, 20));
        buttonList.add(new ButtonTabRight(gui, PageShipping.INSTANCE, buttonList.size(), 308, 32));
        buttonList.add(new ButtonTabRight(gui, PageFishing.INSTANCE, buttonList.size(), 308, 66));
        buttonList.add(new ButtonTabRight(gui, PageMining.INSTANCE, buttonList.size(), 308, 100));
        buttonList.add(new ButtonTabRight(gui, PageCooking.INSTANCE, buttonList.size(), 308, 134));
    }
}
