package joshie.harvestmoon.blocks.render;

import joshie.harvestmoon.util.RenderBase;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

public class RenderFridge extends RenderBase {
    @Override
    public void renderBlock() {
        if(isItem()) {
            GL11.glScalef(0.6F, 0.6F, 0.6F);
            GL11.glTranslatef(0F, 0F, 0.5F);
        }
        
        setTexture(Blocks.iron_block);
        if (dir == ForgeDirection.WEST || isItem()) {
            renderBlock(0.0D, 1.45D, 0.05D, 0.05D, 1.55D, 0.45D);
            renderBlock(0.1D, 0.05D, 0.95D, 1.0D, 1.95D, 1.0D);
            renderBlock(0.0D, 1.2D, 0.05D, 0.05D, 1.3D, 0.45D);
            renderBlock(0.95D, 0.05D, 0.05D, 1.0D, 1.95D, 0.95D);
            renderBlock(0.05D, 0.049D, 0.05D, 0.1D, 1.35D, 0.95D);
            renderBlock(0.1D, 0.0D, 0.0D, 1.0D, 0.05D, 1.0D);
            renderBlock(0.1D, 1.95D, 0.0D, 1.0D, 2.0D, 1.0D);
            renderBlock(0.05D, 1.4D, 0.05D, 0.1D, 1.95D, 0.95D);
            renderBlock(0.1D, 1.35D, 0.05D, 0.95D, 1.4D, 0.95D);
            renderBlock(0.1D, 0.05D, 0.0D, 1.0D, 1.95D, 0.05D);
        } else if (dir == ForgeDirection.NORTH) {
            renderBlock(0.05D, 1.45D, 0.0D, 0.45D, 1.55D, 0.05D);
            renderBlock(0.95D, 0.05D, 0.1D, 1.0D, 1.95D, 1.0D);
            renderBlock(0.05D, 1.2D, 0.0D, 0.45D, 1.3D, 0.05D);
            renderBlock(0.05D, 0.05D, 0.95D, 0.95D, 1.95D, 1.0D);
            renderBlock(0.05D, 0.049D, 0.05D, 0.95D, 1.35D, 0.1D);
            renderBlock(0.0D, 0.0D, 0.1D, 1.0D, 0.05D, 1.0D);
            renderBlock(0.0D, 1.95D, 0.1D, 1.0D, 2.0D, 1.0D);
            renderBlock(0.05D, 1.4D, 0.05D, 0.95D, 1.95D, 0.1D);
            renderBlock(0.05D, 1.35D, 0.1D, 0.95D, 1.4D, 0.95D);
            renderBlock(0.0D, 0.05D, 0.1D, 0.05D, 1.95D, 1.0D);
        } else if (dir == ForgeDirection.EAST) {
           renderBlock(0.95D, 1.45D, 0.05D, 1D, 1.55D, 0.45D);
           renderBlock(0D, 0.05D, 0.95D, 0.9D, 1.95D, 1.0D);
           renderBlock(0.95D, 1.2D, 0.05D, 1D, 1.3D, 0.45D);
           renderBlock(0D, 0.05D, 0.05D, 0.05D, 1.95D, 0.95D);
           renderBlock(0.9D, 0.049D, 0.05D, 0.95D, 1.35D, 0.95D);
           renderBlock(0D, 0.0D, 0.0D, 0.9D, 0.05D, 1.0D);
           renderBlock(0D, 1.95D, 0.0D, 0.9D, 2.0D, 1.0D);
           renderBlock(0.9D, 1.4D, 0.05D, 0.95D, 1.95D, 0.95D);
           renderBlock(0.05D, 1.35D, 0.05D, 0.9D, 1.4D, 0.95D);
           renderBlock(0D, 0.05D, 0.0D, 0.9D, 1.95D, 0.05D);
        } else if (dir == ForgeDirection.SOUTH) {
           renderBlock(0.05D, 1.45D, 0.95D, 0.45D, 1.55D, 1D);
           renderBlock(0.95D, 0.05D, 0D, 1.0D, 1.95D, 0.9D);
           renderBlock(0.05D, 1.2D, 0.95D, 0.45D, 1.3D, 1D);
           renderBlock(0.05D, 0.05D, 0D, 0.95D, 1.95D, 0.05D);
           renderBlock(0.05D, 0.049D, 0.9D, 0.95D, 1.35D, 0.95D);
           renderBlock(0.0D, 0.0D, 0D, 1.0D, 0.05D, 0.9D);
           renderBlock(0.0D, 1.95D, 0D, 1.0D, 2.0D, 0.9D);
           renderBlock(0.05D, 1.4D, 0.9D, 0.95D, 1.95D, 0.95D);
           renderBlock(0.05D, 1.35D, 0.05D, 0.95D, 1.4D, 0.9D);
           renderBlock(0.0D, 0.05D, 0D, 0.05D, 1.95D, 0.9D);
        }
    }
}
