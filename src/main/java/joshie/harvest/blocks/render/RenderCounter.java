package joshie.harvest.blocks.render;

import joshie.harvest.blocks.BlockCookware;
import joshie.harvest.blocks.BlockIcons;
import joshie.harvest.core.util.RenderBase;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

public class RenderCounter extends RenderBase {
    @Override
    public void renderBlock() {
        if (isItem()) {
            GL11.glScalef(0.95F, 0.95F, 0.95F);
            GL11.glTranslated(0F, -0.05F, 0F);
        }
    }
}