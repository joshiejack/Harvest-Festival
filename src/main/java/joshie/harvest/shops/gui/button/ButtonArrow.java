package joshie.harvest.shops.gui.button;

import joshie.harvest.shops.gui.GuiNPCShop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;

import static joshie.harvest.shops.gui.GuiNPCShop.SHOP_EXTRA;

public abstract class ButtonArrow extends GuiButton {
    protected final GuiNPCShop shop;
    private final int xCoord;
    private final int amount;

    public ButtonArrow(GuiNPCShop shop, int amount, int xCoord, int buttonId, int x, int y) {
        super(buttonId, x, y, "");
        this.shop = shop;
        this.xCoord = xCoord;
        this.width = 14;
        this.height = 12;
        this.amount = amount;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        updateVisiblity();
        if (visible) {
            mc.getTextureManager().bindTexture(SHOP_EXTRA);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            int state = getHoverState(hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(xPosition, yPosition, xCoord, state * 12, width, height);
            mouseDragged(mc, mouseX, mouseY);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    protected abstract void updateVisiblity();

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (GuiScreen.isShiftKeyDown()) shop.scroll(amount * 10);
        else shop.scroll(amount);
    }
}
