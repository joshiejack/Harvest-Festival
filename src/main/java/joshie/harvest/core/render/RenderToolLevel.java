package joshie.harvest.core.render;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderToolLevel {
    @SideOnly(Side.CLIENT)
    public static void drawToolProgress(GuiScreen gui, int x, int y, ToolTier tier, int level, FontRenderer font) {
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        int k = level < 10 ? 65 : level < 100 ? 70 : 80;
        int i1 = x + 12;
        int j1 = y - 12;
        int k1 = 7;

        if (i1 + k > gui.width) {
            i1 -= 28 + k;
        }

        if (j1 + k1 + 6 > gui.height) {
            j1 = gui.height - k1 - 6;
        }

        int zLevel = (int) 500.0F;
        int l1 = -267386864;
        GuiUtils.drawGradientRect(zLevel, i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
        GuiUtils.drawGradientRect(zLevel, i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
        GuiUtils.drawGradientRect(zLevel, i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
        GuiUtils.drawGradientRect(zLevel, i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
        GuiUtils.drawGradientRect(zLevel, i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
        int i2 = 1347420415;
        int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
        GuiUtils.drawGradientRect(zLevel, i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
        GuiUtils.drawGradientRect(zLevel, i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
        GuiUtils.drawGradientRect(zLevel, i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
        GuiUtils.drawGradientRect(zLevel, i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        gui.mc.renderEngine.bindTexture(HFModInfo.stars);
        gui.drawTexturedModalRect(i1 - 1, j1 - 1, 0, 8, 52, 9);
        int posY = tier != null && tier.ordinal() >= ToolTier.MYSTRIL.ordinal() ? 26 : 17;
        int width = (level >> 1) + 2;
        gui.drawTexturedModalRect(i1 - 1, j1 - 1, 0, posY, width, 9);

        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.translate(53F, 0F, 0F);
        gui.mc.fontRendererObj.drawString(level + "%", i1, j1, 0xFFFFFF);
        GlStateManager.enableBlend();
        GlStateManager.popMatrix();

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
    }
}