package joshie.harvest.blocks.render;

import org.lwjgl.opengl.GL11;

import joshie.harvest.core.util.RenderBase;
import net.minecraft.init.Blocks;

public class RenderShipping extends RenderBase {
    @Override
    public void renderBlock() {
        setTexture(Blocks.planks);
        renderBlock(0.0D, 0.399D, 0.8D, 1.0D, 0.449D, 0.85D);
        renderBlock(0.0D, 0.4D, 0.2D, 1.0D, 0.5D, 0.25D);
        renderBlock(0.99D, 0.0D, 0.1D, 1.0D, 0.4D, 0.9D);
        renderBlock(0.0D, 0.399D, 0.35D, 1.0D, 0.6D, 0.649D);
        renderBlock(0.01D, 0.0D, 0.1D, 0.99D, 0.4D, 0.9D);
        renderBlock(0.0D, 0.4D, 0.25D, 1.0D, 0.55D, 0.35D);
        renderBlock(0.0D, 0.4D, 0.65D, 1.0D, 0.55D, 0.75D);
        renderBlock(0.0D, 0.399D, 0.15D, 1.0D, 0.449D, 0.2D);
        renderBlock(0.0D, 0.4D, 0.75D, 1.0D, 0.5D, 0.8D);
    }
}
