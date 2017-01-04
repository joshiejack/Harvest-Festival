package joshie.harvest.knowledge.gui.stats.button;

import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.core.base.gui.ButtonBook;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.core.helpers.StackRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

public class ButtonTab extends ButtonBook<GuiStats> {
    protected final BookPage page;
    private final ItemStack icon;
    private final int xTexture;
    private final int xStack;

    @SuppressWarnings("unchecked")
    public ButtonTab(GuiStats gui, BookPage page, int buttonId, int x, int y, String string, int xTexture, int xStack) {
        super(gui, buttonId, x, y, string);
        this.icon = page.getIcon();
        this.page = page;
        this.width = 26;
        this.height = 32;
        this.xTexture = xTexture;
        this.xStack = xPosition + xStack;
    }

    public ItemStack getIcon() {
        return icon;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            mc.getTextureManager().bindTexture(TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            int state = getHoverState(hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(xPosition, yPosition, xTexture, state * 32, width, height);
            StackRenderHelper.drawStack(getIcon(), xStack, yPosition + 8, 1F);
            if (hovered) gui.addTooltip(displayString);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        gui.setPage(page);
    }
}
