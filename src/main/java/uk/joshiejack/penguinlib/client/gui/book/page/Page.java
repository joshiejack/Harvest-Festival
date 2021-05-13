package uk.joshiejack.penguinlib.client.gui.book.page;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBack;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonForward;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonTextField;
import uk.joshiejack.penguinlib.client.GuiElements;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page {
    public static final Map<String, Page> REGISTRY = new HashMap<>();
    private ButtonTextField inFocus;
    protected final String name;
    protected FontRenderer fontRenderer;
    protected GuiBook gui;
    protected Icon icon = Icon.EMPTY;
    public int container = -1;
    public int start;

    public Page() {
        this("");
    }

    public Page(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return StringHelper.localize(name);
    }

    public Icon getIcon() {
        return icon;
    }

    public ButtonBack createBackButton(List<GuiButton> buttonList) { return new ButtonBack(this, gui, buttonList.size(), 24, 168); }
    public ButtonForward createForwardButton(List<GuiButton> buttonList) { return new ButtonForward(this, gui, buttonList.size(), 270, 168); }

    protected GuiLabel createLabel(String title, int id, int x, int y, int width, int height) {
        GuiLabel label = new GuiLabel(gui.mc.fontRenderer, id, x, y, width, height, 0xFFFFFFFF);
        label.addLine(title);
        label.setCentered();
        return label;
    }

    protected void drawUnicodeFont(String displayString, int x, int y, int wrap) {
        boolean flag = fontRenderer.getUnicodeFlag();
        fontRenderer.setUnicodeFlag(true);
        fontRenderer.drawSplitString(TextFormatting.BOLD + displayString, gui.getGuiLeft() + x, gui.getGuiTop() + y, wrap, 0x857754);
        fontRenderer.setUnicodeFlag(flag);
    }

    protected void drawTexturedModalRect(int x, int y, int texX, int texY, int width, int height) {
        gui.drawTexturedModalRect(gui.getGuiLeft() + x, gui.getGuiTop() + y, texX, texY, width, height);
    }

    protected void drawString(String text,int x, int y) {
        gui.mc.fontRenderer.drawString(text, gui.getGuiLeft() + x, gui.getGuiTop() + y, 4210752);
    }

    protected void drawStringWithWrap(String text,int x, int y, int wrap) {
        gui.mc.fontRenderer.drawSplitString(text, gui.getGuiLeft() + x, gui.getGuiTop() + y, wrap, 4210752);
    }

    public void setGui(GuiBook gui) {
        this.gui = gui;
        this.fontRenderer = gui.mc.fontRenderer;
    }

    public void initGui(List<GuiButton> buttonList, List<GuiLabel> labelList) {}

    public void drawScreen(int x, int y) {}

    public ButtonTextField getFocus() {
        return inFocus;
    }

    public void setInFocus(ButtonTextField text) {
        this.inFocus = text;
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (inFocus != null) {
            inFocus.keyTyped(typedChar, keyCode);
        }
    }

    public boolean hasBackwardsButton() { return false; }
    public boolean hasForwardsButton() { return false; }
    public void onBack() { }
    public void onForward() { }

    public static class Icon {
        static final Icon EMPTY = new Icon(GuiElements.ICONS, 0, 0);
        private final ResourceLocation resource;
        private final ItemStack stack;
        private final int x, y;
        
        public Icon(ResourceLocation resource, int x, int y) {
            this.resource = resource;
            this.stack = ItemStack.EMPTY;
            this.x = x;
            this.y = y;
        }

        public Icon(ItemStack stack, int x, int y) {
            this.resource = null;
            this.stack = stack;
            this.x = x;
            this.y = y;
        }

        public ItemStack getStack() {
            return stack;
        }

        @Nullable
        public ResourceLocation getTexture() {
            return resource;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
