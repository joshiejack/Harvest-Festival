package joshie.harvestmoon.blocks.render;

import joshie.harvestmoon.util.RenderBase;
import net.minecraft.init.Blocks;

public class RenderPanFrying extends RenderBase {
    @Override
    public void renderBlock() {
        setTexture(Blocks.iron_block);
        //Main Frame of the Frying Pan
        renderBlock(0.15D, 0D, 0.15D, 0.85D, 0.05D, 0.85D);
        renderBlock(0.1D, 0.05D, 0.1D, 0.9D, 0.075D, 0.9D);
        renderBlock(0.05D, 0.05D, 0.1D, 0.1D, 0.15D, 0.9D);
        renderBlock(0.9D, 0.05D, 0.1D, 0.95D, 0.15D, 0.9D);
        renderBlock(0.1D, 0.05D, 0.9D, 0.9D, 0.15D, 0.95D);
        renderBlock(0.1D, 0.05D, 0.05D, 0.9D, 0.15D, 0.1D);
        renderBlock(0.1D, 0.1D, 0.0D, 0.9D, 0.25D, 0.05D);
        renderBlock(0D, 0.1D, 0.1D, 0.05D, 0.25D, 0.9D);
        renderBlock(0.95D, 0.1D, 0.1D, 1D, 0.25D, 0.9D);
        renderBlock(0.1D, 0.1D, 0.95D, 0.9D, 0.25D, 1D);
        renderBlock(0.05D, 0.1D, 0.9D, 0.1D, 0.25D, 0.95D);
        renderBlock(0.9D, 0.1D, 0.9D, 0.95D, 0.25D, 0.95D);
        renderBlock(0.05D, 0.1D, 0.05D, 0.1D, 0.25D, 0.1D);
        renderBlock(0.9D, 0.1D, 0.05D, 0.95D, 0.25D, 0.1D);
        
        //Handle (Dependent on the Frying pans rotation)
        renderBlock(0.45D, 0.125D, 1D, 0.55D, 0.2D, 1.25D);
        renderBlock(0.45D, 0.155D, 1.25D, 0.55D, 0.2D, 1.35D);
        renderBlock(0.45D, 0.2D, 1.2D, 0.55D, 0.235D, 1.45D);
        renderBlock(0.45D, 0.235D, 1.3D, 0.55D, 0.285D, 1.75D);
        renderBlock(0.43D, 0.23D, 1.75D, 0.57D, 0.29D, 1.85D);
        
        //Render Oil in the pan
        if(!isItem()) { //TODO: Render Oil
            //setTexture(Blocks.water);
            //renderBlock(0.1D, 0.06D, 0.1D, 0.9D, 0.125D, 0.9D);
        }
    }
}