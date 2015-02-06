package joshie.harvestmoon.blocks.render;

import joshie.harvestmoon.util.RenderBase;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

public class RenderPot extends RenderBase {
    @Override
    public void renderBlock() {
        setTexture(Blocks.iron_block);
        //Base of Pot
        renderBlock(0.15D, 0D, 0.15D, 0.85D, 0.025D, 0.85D);
        renderBlock(0.1D, 0.025D, 0.1D, 0.9D, 0.05D, 0.9D);

        //Sides of Pot
        renderBlock(0.9D, 0.045D, 0.05D, 0.95D, 0.5D, 0.95D);
        renderBlock(0.05D, 0.045D, 0.05D, 0.1D, 0.5D, 0.95D);
        renderBlock(0.1D, 0.045D, 0.9D, 0.9D, 0.5D, 0.95D);
        renderBlock(0.1D, 0.045D, 0.05D, 0.9D, 0.5D, 0.1D);
        renderBlock(0.95D, 0.15D, 0.05D, 0.975D, 0.65D, 0.95D);
        renderBlock(0.975D, 0.45D, 0.0225D, 1D, 0.75D, 0.975D);
        renderBlock(0.0225D, 0.15D, 0.05D, 0.05D, 0.65D, 0.95D);
        renderBlock(0D, 0.45D, 0.0225D, 0.0225D, 0.75D, 0.975D);
        renderBlock(0.0225D, 0.15D, 0.95D, 0.975D, 0.65D, 0.975D);
        renderBlock(0.0225D, 0.45D, 0.975D, 0.975D, 0.75D, 1D);
        renderBlock(0.0225D, 0.15D, 0.0225D, 0.975D, 0.65D, 0.05D);
        renderBlock(0.0225D, 0.45D, 0D, 0.975D, 0.75D, 0.0225D);

        //Handles of Pot
        if (dir == ForgeDirection.SOUTH || dir == ForgeDirection.NORTH || isItem()) {
            renderBlock(-0.2D, 0.55D, 0.25D, 0D, 0.6D, 0.35D);
            renderBlock(-0.2D, 0.55D, 0.65D, 0D, 0.6D, 0.75D);
            renderBlock(-0.3D, 0.55D, 0.35D, -0.15D, 0.6D, 0.45D);
            renderBlock(-0.3D, 0.55D, 0.55D, -0.15D, 0.6D, 0.65D);
            renderBlock(-0.35D, 0.55D, 0.45D, -0.25D, 0.6D, 0.55D);
            renderBlock(1D, 0.55D, 0.25D, 1.2D, 0.6D, 0.35D);
            renderBlock(1D, 0.55D, 0.65D, 1.2D, 0.6D, 0.75D);
            renderBlock(1.15D, 0.55D, 0.35D, 1.3D, 0.6D, 0.45D);
            renderBlock(1.15D, 0.55D, 0.55D, 1.3D, 0.6D, 0.65D);
            renderBlock(1.25D, 0.55D, 0.45D, 1.35D, 0.6D, 0.55D);
        } else {
            renderBlock(0.25D, 0.55D, -0.2D, 0.35D, 0.6D, 0D);
            renderBlock(0.65D, 0.55D, -0.2D, 0.75D, 0.6D, 0D);
            renderBlock(0.35D, 0.55D, -0.3D, 0.45D, 0.6D, -0.15D);
            renderBlock(0.55D, 0.55D, -0.3D, 0.65D, 0.6D, -0.15D);
            renderBlock(0.45D, 0.55D, -0.35D, 0.55D, 0.6D, -0.25D);
            renderBlock(0.25D, 0.55D, 1D, 0.35D, 0.6D, 1.2D);
            renderBlock(0.65D, 0.55D, 1D, 0.75D, 0.6D, 1.2D);
            renderBlock(0.35D, 0.55D, 1.15D, 0.45D, 0.6D, 1.3D);
            renderBlock(0.55D, 0.55D, 1.15D, 0.65D, 0.6D, 1.3D);
            renderBlock(0.45D, 0.55D, 1.25D, 0.55D, 0.6D, 1.35D);
        }

        //Render Water in Pan
        //TODO:
        if (!isItem()) {

        }
    }
}