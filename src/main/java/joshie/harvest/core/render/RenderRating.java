package joshie.harvest.core.render;

import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@Deprecated
public class RenderRating {
    private static final int FULL = 0;
    private static final int HALF = 8;
    private static final int EMPTY = 16;
    
    @SideOnly(Side.CLIENT)
    private static int getXFromRating(int position, int rating) {
        if(position == 0) {
            if(rating == 0) return HALF;
            if(rating >= 1) return FULL;
        } else if (position == 1) {
            if(rating <= 1) return EMPTY;
            if(rating == 2) return HALF;
            if(rating >= 3) return FULL;
        } else if (position == 2) {
            if(rating <= 3) return EMPTY;
            if(rating == 4) return HALF;
            if(rating >= 5) return FULL;
        } else if (position == 3) {
            if(rating <= 5) return EMPTY;
            if(rating == 6) return HALF;
            if(rating >= 7) return FULL;
        } else if (position == 4) {
            if(rating <= 7) return EMPTY;
            if(rating == 8) return HALF;
            if(rating >= 9) return FULL;
        }
        
        return 0;
    }
    
    @SideOnly(Side.CLIENT)
    public static void drawStarRating(GuiScreen gui, int x, int y, int rating, FontRenderer font) {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        int k = 44;
        int i1 = x + 12;
        int j1 = y - 12;
        int k1 = 8;
    
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
        for(int x2 = 0; x2 < 5; x2++) {
            gui.drawTexturedModalRect(i1 + (x2 * 9), j1, RenderRating.getXFromRating(x2, rating), 0, 8, 8);
        }
    
        gui.zLevel = 0.0F;
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }
}
