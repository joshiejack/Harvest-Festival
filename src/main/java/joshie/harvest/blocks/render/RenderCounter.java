package joshie.harvest.blocks.render;

import org.lwjgl.opengl.GL11;

import joshie.harvest.core.util.RenderBase;

public class RenderCounter extends RenderBase {
    @Override
    public void renderBlock() {
        if (isItem()) {
            GL11.glScalef(0.95F, 0.95F, 0.95F);
            GL11.glTranslated(0F, -0.05F, 0F);
        }
    }
}