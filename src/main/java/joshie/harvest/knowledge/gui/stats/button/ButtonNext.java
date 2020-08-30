package joshie.harvest.knowledge.gui.stats.button;

import joshie.harvest.core.base.gui.ButtonBook;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;

public class ButtonNext extends ButtonBook<GuiStats> {
    public ButtonNext(GuiStats gui, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, "");
        width = 15;
        height = 10;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            mc.getTextureManager().bindTexture(TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            int state = getHoverState(hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(x, y, 0, 224 + state * 11, width, height);
            if (hovered) gui.addTooltip(TextHelper.translate("stats.next"));
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        gui.getPage().start++;
        gui.initGui(); //reload
    }
}
