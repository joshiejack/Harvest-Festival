package joshie.harvest.core.base.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public abstract class ButtonTextured extends GuiButton {
    public ButtonTextured(int id, int x, int y, int width, int height) {
        super(id, x, y, "");
        this.width = width;
        this.height = height;
    }

    public abstract ResourceLocation getResource();
    public abstract int getPositionX();
    public abstract int getPositionY();

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            mc.getTextureManager().bindTexture(getResource());
            drawTexturedModalRect(xPosition, yPosition, getPositionX(), getPositionY(), width, height);
            mouseDragged(mc, mouseX, mouseY);
        }
    }
}
