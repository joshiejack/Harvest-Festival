package joshie.harvest.knowledge.gui.stats.collection.page;

import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.knowledge.gui.stats.button.ButtonTabRight;
import joshie.harvest.knowledge.gui.stats.collection.button.ButtonSearch;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.List;

public class PageCollection extends BookPage<GuiStats> {
    public String search = "";

    PageCollection(String string, @Nonnull ItemStack stack) {
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

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (ButtonSearch.inFocus) {
            switch (keyCode) {
                case 14:
                    if (!search.isEmpty()) {
                        search = search.substring(0, search.length() - 1);
                        MCClientHelper.getMinecraft().currentScreen.initGui();
                    }

                    return;
                case 28:
                case 156:
                    if (!search.isEmpty()) {
                        MCClientHelper.getMinecraft().currentScreen.initGui();
                    }

                    return;
                default:
                    if (search.length() < 10 && ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                        search = search + Character.toString(typedChar);
                        MCClientHelper.getMinecraft().currentScreen.initGui();
                    }
            }
        }
    }
}
