package uk.joshiejack.penguinlib.client.gui.book.button;

import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackRenderHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

public class ButtonBook extends GuiButton {
    protected final GuiBook gui;

    public ButtonBook(GuiBook gui, int buttonId, int x, int y, String string) {
        super(buttonId, gui.getGuiLeft() + x, gui.getGuiTop() + y, string);
        this.gui = gui;
    }

    protected void drawStack(boolean comparison, ItemStack stack, int x, int y, float scale) {
        if (comparison) StackRenderHelper.drawStack(stack, this.x + x, this.y + y, scale);
        else StackRenderHelper.drawGreyStack(stack, this.x + x, this.y + y, scale);
    }

    protected void drawStack(ItemStack stack, int x, int y, float scale) {
        StackRenderHelper.drawStack(stack, this.x + x, this.y + y, scale);
    }

    protected void drawRect(boolean comparison, int x, int y, int u, int v, int size) {
        if (comparison) drawTexturedModalRect(this.x + x, this.y + y, u, v, size, size);
        else drawTexturedModalRect(this.x + x, this.y + y, u, v + size, size, size);
    }
}
