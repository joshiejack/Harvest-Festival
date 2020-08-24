package joshie.harvest.core.base.gui;

import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.List;

public class BookPage<G extends GuiBaseBook> {
    private final ItemStack icon;
    private final String name;
    private final String category;
    protected FontRenderer fontRenderer;
    protected G gui;
    public int start;

    public BookPage (String category, String name, @Nonnull ItemStack stack) {
        this.category = "harvestfestival.stats." + category;
        this.name = "harvestfestival.stats." + name;
        this.icon = stack;
    }

    public String getCategory() {
        return TextHelper.localize(category);
    }

    public String getDisplayName() {
        return TextHelper.localize(name);
    }

    public ItemStack getIcon() {
        return icon;
    }

    protected GuiLabel createLabel(String title, int id, int x, int y, int width, int height) {
        GuiLabel label = new GuiLabel(gui.mc.fontRenderer, id, x, y, width, height, 0xFFFFFFFF);
        label.addLine(title);
        label.setCentered();
        return label;
    }

    protected void drawUnicodeFont(String displayString, int x, int y, int wrap) {
        boolean flag = fontRenderer.getUnicodeFlag();
        fontRenderer.setUnicodeFlag(true);
        fontRenderer.drawSplitString(TextFormatting.BOLD + displayString, gui.guiLeft + x, gui.guiTop + y, wrap, 0x857754);
        fontRenderer.setUnicodeFlag(flag);
    }

    public void initGui(G gui, List<GuiButton> buttonList, List<GuiLabel> labelList) {
        this.gui = gui;
        this.fontRenderer = gui.mc.fontRenderer;
    }

    public void drawScreen(int x, int y) {

    }

    public void keyTyped(char typedChar, int keyCode) {}
}
