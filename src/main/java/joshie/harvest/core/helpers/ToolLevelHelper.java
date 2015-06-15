package joshie.harvest.core.helpers;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ToolLevelHelper {
    @SideOnly(Side.CLIENT)
    public static void drawToolProgress(GuiScreen gui, int x, int y, ToolTier tier, int level, FontRenderer font) {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        int k = level < 10? 65: level < 100? 70: 80;
        int i1 = x + 12;
        int j1 = y - 12;
        int k1 = 7;

        if (i1 + k > gui.width) {
            i1 -= 28 + k;
        }

        if (j1 + k1 + 6 > gui.height) {
            j1 = gui.height - k1 - 6;
        }

        gui.zLevel = 500.0F;
        int l1 = -267386864;
        gui.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
        gui.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
        gui.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
        gui.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
        gui.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
        int i2 = 1347420415;
        int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
        gui.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
        gui.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
        gui.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
        gui.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);
        
        gui.mc.renderEngine.bindTexture(HFModInfo.stars);
        gui.drawTexturedModalRect(i1 - 1, j1 - 1, 0, 8, 52, 9);
        int posY = tier != null && tier.ordinal() >= ToolTier.MYSTRIL.ordinal()? 26: 17;
        int width = (level >> 1) + 2;
        gui.drawTexturedModalRect(i1 - 1, j1 - 1, 0, posY, width, 9);
        
        GL11.glPushMatrix();
        GL11.glTranslatef(53F, 0F, 0F);
        gui.mc.fontRenderer.drawString(level + "%", i1, j1, 0xFFFFFF);
        GL11.glPopMatrix();
       
        gui.zLevel = 0.0F;
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }
}
