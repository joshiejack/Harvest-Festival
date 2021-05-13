package uk.joshiejack.penguinlib.client.gui.book.button;

import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;
import uk.joshiejack.penguinlib.client.GuiElements;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class AbstractButtonTab extends ButtonBook {
    protected final Page page;
    private final boolean selected;
    private final int xTexture;
    protected final int xOffset;

    public AbstractButtonTab(GuiBook book, Page page, int buttonId, int x, int y, int xTexture, int xOffset) {
        super(book, buttonId, x, y, page.getDisplayName());
        this.page = page;
        this.selected = book.getPage() == page;
        this.width = 26;
        this.height = 32;
        this.xTexture = xTexture;
        this.xOffset = xOffset;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            int state = getHoverState(hovered);
            mc.getTextureManager().bindTexture(GuiElements.BOOK_LEFT);
            drawTexturedModalRect(x, y, xTexture, state * 32, width, height);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            drawIcon();
            if (hovered) {
                gui.addTooltip(displayString);
            }
        }
    }

    public Page.Icon getIcon() {
        return page.getIcon();
    }

    private void drawIcon() {
        Page.Icon icon = getIcon();
        if (icon.getTexture() != null) {
            gui.mc.getTextureManager().bindTexture(icon.getTexture());
            gui.drawTexturedModalRect(x + xOffset, y + 8, icon.getX(), icon.getY(), 16, 16);
        } else if (icon.getStack() != null) StackRenderHelper.drawStack(icon.getStack(), x + xOffset, y + 8, 1F);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        gui.setPage(page);
    }
}
