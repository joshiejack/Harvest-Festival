package joshie.harvest.shops.gui.button;

import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.shops.gui.GuiNPCShop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;

import static joshie.harvest.shops.gui.GuiNPCShop.SHOP_EXTRA;

public class ButtonListingOutOfStock extends GuiButton {
    protected final GuiNPCShop shop;

    @SuppressWarnings("unchecked")
    public ButtonListingOutOfStock(GuiNPCShop shop, int buttonId, int x, int y) {
        super(buttonId, x, y, TextHelper.translate("shop.outof." + shop.selling));
        this.height = 18;
        this.shop = shop;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(SHOP_EXTRA);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            drawBackground();
            drawForeground(fontrenderer, 14737632);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    private void drawBackground() {
        drawTexturedModalRect(xPosition, yPosition, 0, 18, width / 2, height);
        drawTexturedModalRect(xPosition + width / 2, yPosition, 200 - width / 2, 18, width / 2, height);
    }

    private void drawForeground(FontRenderer fontrenderer, int j) {
        drawString(fontrenderer, displayString, xPosition + 10, yPosition + (height - 8) / 2, j);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
    }
}
