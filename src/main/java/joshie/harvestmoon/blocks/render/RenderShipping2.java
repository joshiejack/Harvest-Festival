package joshie.harvestmoon.blocks.render;

import joshie.harvestmoon.core.util.RenderBase;
import net.minecraft.init.Blocks;

public class RenderShipping2 extends RenderBase {
    @Override
    public void renderBlock() {
        setTexture(Blocks.planks);
        renderBlock(0.8D, 0.399D, 0.0D, 0.85D, 0.449D, 1.0D);
        renderBlock(0.2D, 0.4D, 0.0D, 0.25D, 0.5D, 1.0D);
        renderBlock(0.1D, 0.0D, 0.99D, 0.9D, 0.4D, 1.0D);
        renderBlock(0.35D, 0.399D, 0.0D, 0.649D, 0.6D, 1.0D);
        renderBlock(0.1D, 0.0D, 0.01D, 0.9D, 0.4D, 0.99D);
        renderBlock(0.25D, 0.4D, 0.0D, 0.35D, 0.55D, 1.0D);
        renderBlock(0.65D, 0.4D, 0.0D, 0.75D, 0.55D, 1.0D);
        renderBlock(0.15D, 0.399D, 0.0D, 0.2D, 0.449D, 1.0D);
        renderBlock(0.75D, 0.4D, 0.0D, 0.8D, 0.5D, 1.0D);
    }
}
