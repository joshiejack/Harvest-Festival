package joshie.harvestmoon.blocks.render;

import joshie.harvestmoon.util.RenderBase;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

public class RenderSteamer extends RenderBase {
    @Override
    public void renderBlock() {
        setTexture(Blocks.snow);
        if (dir == ForgeDirection.WEST || isItem()) {
            renderBlock(0.135D, 0.0D, 0.875D, 0.185D, 0.05D, 0.925D);
            renderBlock(0.25D, 0.9D, 0.61D, 0.459D, 0.95D, 0.82D);
            renderBlock(0.13D, 0.1D, 0.08D, 0.87D, 0.3D, 0.919D);
            renderBlock(0.825D, 0.0D, 0.075D, 0.875D, 0.05D, 0.125D);
            renderBlock(0.825D, 0.0D, 0.875D, 0.875D, 0.05D, 0.925D);
            renderBlock(0.55D, 0.4D, 0.1D, 0.85D, 0.9D, 0.9D);
            renderBlock(0.12D, 0.3D, 0.07D, 0.88D, 0.4D, 0.929D);
            renderBlock(0.135D, 0.0D, 0.075D, 0.185D, 0.05D, 0.125D);
            renderBlock(0.1D, 0.049D, 0.05D, 0.9D, 0.099D, 0.95D);
            renderBlock(0.15D, 0.4D, 0.51D, 0.54D, 0.9D, 0.9D);
            renderBlock(0.25D, 0.9D, 0.19D, 0.459D, 0.95D, 0.4D);
            renderBlock(0.15D, 0.4D, 0.1D, 0.54D, 0.9D, 0.49D);
            renderBlock(0.12D, 0.1D, 0.18D, 0.13D, 0.3D, 0.82D);
            renderBlock(0.59D, 0.9D, 0.39D, 0.799D, 0.95D, 0.61D);
        } else if (dir == ForgeDirection.NORTH) {
            renderBlock(0.875D, 0.0D, 0.135D, 0.925D, 0.05D, 0.185D);
            renderBlock(0.61D, 0.9D, 0.25D, 0.82D, 0.95D, 0.459D);
            renderBlock(0.08D, 0.1D, 0.13D, 0.919D, 0.3D, 0.87D);
            renderBlock(0.075D, 0.0D, 0.825D, 0.125D, 0.05D, 0.875D);
            renderBlock(0.875D, 0.0D, 0.825D, 0.925D, 0.05D, 0.875D);
            renderBlock(0.1D, 0.4D, 0.55D, 0.9D, 0.9D, 0.85D);
            renderBlock(0.07D, 0.3D, 0.12D, 0.929D, 0.4D, 0.88D);
            renderBlock(0.075D, 0.0D, 0.135D, 0.125D, 0.05D, 0.185D);
            renderBlock(0.05D, 0.049D, 0.1D, 0.95D, 0.099D, 0.9D);
            renderBlock(0.51D, 0.4D, 0.15D, 0.9D, 0.9D, 0.54D);
            renderBlock(0.19D, 0.9D, 0.25D, 0.4D, 0.95D, 0.459D);
            renderBlock(0.1D, 0.4D, 0.15D, 0.49D, 0.9D, 0.54D);
            renderBlock(0.18D, 0.1D, 0.12D, 0.82D, 0.3D, 0.13D);
            renderBlock(0.39D, 0.9D, 0.59D, 0.61D, 0.95D, 0.799D);
        } else if (dir == ForgeDirection.EAST) {
            renderBlock(0.815D, 0.0D, 0.875D, 0.865D, 0.05D, 0.925D);
            renderBlock(0.54D, 0.9D, 0.61D, 0.75D, 0.95D, 0.82D);
            renderBlock(0.13D, 0.1D, 0.08D, 0.87D, 0.3D, 0.919D);
            renderBlock(0.125D, 0.0D, 0.075D, 0.175D, 0.05D, 0.125D);
            renderBlock(0.125D, 0.0D, 0.875D, 0.175D, 0.05D, 0.925D);
            renderBlock(0.15D, 0.4D, 0.1D, 0.449D, 0.9D, 0.9D);
            renderBlock(0.12D, 0.3D, 0.07D, 0.88D, 0.4D, 0.929D);
            renderBlock(0.815D, 0.0D, 0.075D, 0.865D, 0.05D, 0.125D);
            renderBlock(0.099D, 0.049D, 0.05D, 0.9D, 0.099D, 0.95D);
            renderBlock(0.459D, 0.4D, 0.51D, 0.85D, 0.9D, 0.9D);
            renderBlock(0.54D, 0.9D, 0.19D, 0.75D, 0.95D, 0.4D);
            renderBlock(0.459D, 0.4D, 0.1D, 0.85D, 0.9D, 0.49D);
            renderBlock(0.87D, 0.1D, 0.18D, 0.88D, 0.3D, 0.82D);
            renderBlock(0.2D, 0.9D, 0.39D, 0.41D, 0.95D, 0.61D);
        } else if (dir == ForgeDirection.SOUTH) {
            renderBlock(0.875D, 0.0D, 0.815D, 0.925D, 0.05D, 0.865D);
            renderBlock(0.61D, 0.9D, 0.54D, 0.82D, 0.95D, 0.75D);
            renderBlock(0.08D, 0.1D, 0.13D, 0.919D, 0.3D, 0.87D);
            renderBlock(0.075D, 0.0D, 0.125D, 0.125D, 0.05D, 0.175D);
            renderBlock(0.875D, 0.0D, 0.125D, 0.925D, 0.05D, 0.175D);
            renderBlock(0.1D, 0.4D, 0.15D, 0.9D, 0.9D, 0.449D);
            renderBlock(0.07D, 0.3D, 0.12D, 0.929D, 0.4D, 0.88D);
            renderBlock(0.075D, 0.0D, 0.815D, 0.125D, 0.05D, 0.865D);
            renderBlock(0.05D, 0.049D, 0.099D, 0.95D, 0.099D, 0.9D);
            renderBlock(0.51D, 0.4D, 0.459D, 0.9D, 0.9D, 0.85D);
            renderBlock(0.19D, 0.9D, 0.54D, 0.4D, 0.95D, 0.75D);
            renderBlock(0.1D, 0.4D, 0.459D, 0.49D, 0.9D, 0.85D);
            renderBlock(0.18D, 0.1D, 0.87D, 0.82D, 0.3D, 0.88D);
            renderBlock(0.39D, 0.9D, 0.2D, 0.61D, 0.95D, 0.41D);
        }
    }
}
