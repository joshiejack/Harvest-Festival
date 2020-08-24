package joshie.harvest.knowledge.gui.stats.button;

import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.core.base.gui.ButtonBook;
import joshie.harvest.core.helpers.StackRenderHelper;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ButtonTab extends ButtonBook<GuiStats> {
    protected final BookPage page;
    private final ItemStack icon;
    private final int xTexture;
    protected final int xStack;

    @SuppressWarnings("unchecked")
    public ButtonTab(GuiStats gui, BookPage page, int buttonId, int xPos, int yPos, String string, int xTexture, int xStack) {
        super(gui, buttonId, xPos, yPos, string);
        this.icon = page.getIcon();
        this.page = page;
        this.width = 26;
        this.height = 32;
        this.xTexture = xTexture;
        this.xStack = x + xStack;
    }

    public void drawIcon() {
        StackRenderHelper.drawStack(getIcon(), xStack, y + 8, 1F);
    }

    @Nonnull
    public ItemStack getIcon() {
        return icon;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            mc.getTextureManager().bindTexture(TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            int state = getHoverState(hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(x, y, xTexture, state * 32, width, height);
            drawIcon();
            if (hovered) gui.addTooltip(displayString);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        gui.setPage(page);
    }
}
