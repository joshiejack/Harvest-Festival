package joshie.harvest.cooking.gui;

import joshie.harvest.cooking.tile.TileFridge;
import joshie.harvest.core.base.gui.ContainerExpanded.SlotHF;
import joshie.harvest.core.base.gui.GuiBaseContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GuiFridge extends GuiBaseContainer {
    private FridgeFont font;

    public GuiFridge(EntityPlayer player, InventoryPlayer playerInv, TileFridge fridge) {
        super(new ContainerFridge(player, playerInv, fridge), "fridge", 56);
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        super.setWorldAndResolution(mc, width, height);
        this.font = new FridgeFont(fontRendererObj);
    }

    @Override
    public void drawSlot(@Nonnull Slot slot) {
        if (slot instanceof SlotHF) {
            FontRenderer temp = fontRendererObj;
            fontRendererObj = font;
            super.drawSlot(slot);
            fontRendererObj = temp;
        } else super.drawSlot(slot);
    }

    /** Borrowed from Chisel and Bits **/
    public static class FridgeFont extends FontRenderer {
        private final FontRenderer original;
        private final int offsetX, offsetY;
        private final double scale;

        public FridgeFont(FontRenderer font) {
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
}
