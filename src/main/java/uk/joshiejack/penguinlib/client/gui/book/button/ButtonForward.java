package uk.joshiejack.penguinlib.client.gui.book.button;

import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;
import uk.joshiejack.penguinlib.client.GuiElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;

public class ButtonForward extends ButtonBook {
    private final Page origin;

    public ButtonForward(Page origin, GuiBook gui, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, "");
        this.origin = origin;
        this.width = 15;
        this.height = 10;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            mc.getTextureManager().bindTexture(GuiElements.BOOK_LEFT);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            GlStateManager.color(1F, 1F, 1F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(x, y, 0, (hovered ? 246 : 235), 15, 10);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        origin.onForward();
    }
}
