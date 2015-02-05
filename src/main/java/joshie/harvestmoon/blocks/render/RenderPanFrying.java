package joshie.harvestmoon.blocks.render;

import org.lwjgl.opengl.GL11;

import joshie.harvestmoon.util.RenderBase;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

public class RenderPanFrying extends RenderBase {
    @Override
    public void renderBlock() {
        if(isItem) {
            GL11.glScalef(0.9F, 0.9F, 0.9F);
            GL11.glTranslatef(-0.2F, 0.65F, 0.5F);
        }
        
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
        if (dir == ForgeDirection.SOUTH) {
            renderBlock(0.45D, 0.125D, 1D, 0.55D, 0.2D, 1.25D);
            renderBlock(0.45D, 0.155D, 1.25D, 0.55D, 0.2D, 1.35D);
            renderBlock(0.45D, 0.2D, 1.2D, 0.55D, 0.235D, 1.45D);
            renderBlock(0.45D, 0.235D, 1.3D, 0.55D, 0.285D, 1.75D);
            renderBlock(0.43D, 0.23D, 1.75D, 0.57D, 0.29D, 1.85D);
        } else if (dir == ForgeDirection.EAST) {
            renderBlock(1D, 0.125D, 0.45D, 1.25D, 0.2D, 0.55D);
            renderBlock(1.25D, 0.155D, 0.45D, 1.35D, 0.2D, 0.55D);
            renderBlock(1.2D, 0.2D, 0.45D, 1.45D, 0.235D, 0.55D);
            renderBlock(1.3D, 0.235D, 0.45D, 1.75D, 0.285D, 0.55D);
            renderBlock(1.75D, 0.23D, 0.43D, 1.85D, 0.29D, 0.57D);
        } else if (dir == ForgeDirection.WEST) {
            renderBlock(-0.25D, 0.125D, 0.45D, 0D, 0.2D, 0.55D);
            renderBlock(-0.35D, 0.155D, 0.45D, -0.25D, 0.2D, 0.55D);
            renderBlock(-0.45D, 0.2D, 0.45D, -0.2D, 0.235D, 0.55D);
            renderBlock(-0.75D, 0.235D, 0.45D, -0.3D, 0.285D, 0.55D);
            renderBlock(-0.85D, 0.23D, 0.43D, -0.75D, 0.29D, 0.57D);
        } else if (dir == ForgeDirection.NORTH) {
            renderBlock(0.45D, 0.125D, -0.25D, 0.55D, 0.2D, 0D);
            renderBlock(0.45D, 0.155D, -0.35D, 0.55D, 0.2D, -0.25D);
            renderBlock(0.45D, 0.2D, -0.45D, 0.55D, 0.235D, -0.2D);
            renderBlock(0.45D, 0.235D, -0.75D, 0.55D, 0.285D, -0.3D);
            renderBlock(0.43D, 0.23D, -0.85D, 0.57D, 0.29D, -0.75D);
        }

        //Render Oil in the pan
        if (!isItem()) { //TODO: Render Oil

        }
    }
}