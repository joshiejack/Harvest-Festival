package uk.joshiejack.penguinlib.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class MicroFont extends FontRenderer {
    public static final MicroFont INSTANCE = new MicroFont(Minecraft.getMinecraft().fontRenderer);
    private final FontRenderer original;
    private final int offsetX, offsetY;
    private final double scale;

    MicroFont(FontRenderer font) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
        original = font;
        scale = 0.75;
        offsetX = 3;
        offsetY = 2;
    }

    @Override
    public int getStringWidth(String text) {
        return original.getStringWidth(text);
    }

    @Override
    public int drawString(String text, float x, float y, int color, boolean dropShadow) {
        try {
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);

            x /= scale;
            y /= scale;
            x += offsetX;
            y += offsetY;

            return original.drawString(text, x, y, color, dropShadow);
        } finally {
            GlStateManager.popMatrix();
        }
    }
}
