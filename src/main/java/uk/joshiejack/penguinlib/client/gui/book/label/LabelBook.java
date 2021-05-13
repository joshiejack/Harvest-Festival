package uk.joshiejack.penguinlib.client.gui.book.label;

import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackRenderHelper;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LabelBook extends GuiLabel {
    public final GuiBook gui;

    public LabelBook(GuiBook gui, int xIn, int yIn) {
        super(gui.mc.fontRenderer, 0, gui.getGuiLeft() + xIn, gui.getGuiTop() + yIn, 0, 0, 4210752);
        this.gui = gui;
    }

    public void drawStack(ItemStack stack, int x, int y, float scale) {
        StackRenderHelper.drawStack(stack, this.x + x, this.y + y, scale);
    }

    protected void drawGreyStack(ItemStack stack, int x, int y, float scale) {
        StackRenderHelper.drawGreyStack(stack, this.x + x, this.y + y, scale);
    }

    protected void drawScaledRect(boolean comparison, int x, int y, int u, int v, int size, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.color(1F, 1F, 1F);
        GlStateManager.scale(scale, scale, 1.0F);
        drawTexturedModalRect((int) ((this.x + x) / scale), (int) ((this.y + y) / scale), u, v + (comparison ? size: 0), size, size);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    protected void drawRect(boolean comparison, int x, int y, int u, int v, int size) {
        if (comparison) drawTexturedModalRect(this.x + x, this.y + y, u, v, size, size);
        else drawTexturedModalRect(this.x + x, this.y + y, u, v + size, size, size);
    }

    protected void drawStack(boolean comparison, ItemStack stack, int x, int y, float scale) {
        if (comparison) drawStack(stack, x, y, scale);
        else drawGreyStack(stack, x, y, scale);
    }
}
