package joshie.harvestmoon.blocks.render;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import joshie.harvestmoon.util.RenderBase;

public class RenderOven extends RenderBase {
    @Override
    public void renderBlock() {
        if(isItem()) {
            GL11.glScalef(0.95F, 0.95F, 0.95F);
            GL11.glTranslated(0F, -0.05F, 0F);
        }
        
        setTexture(Blocks.snow);
        if (dir == ForgeDirection.WEST || isItem()) {
            setTexture(Blocks.snow);
            renderBlock(0.95D, 0.1D, 0D, 1D, 0.95D, 1D);
            renderBlock(0.1D, 0.1D, 0D, 0.95D, 0.95D, 0.05D);
            renderBlock(0.1D, 0.1D, 0.95D, 0.95D, 0.95D, 1D);
            renderBlock(0.1D, 0.95D, 0D, 0.9D, 1D, 1D);
            renderBlock(0.1D, 0.1D, 0.05D, 0.95D, 0.15D, 0.95D);
            renderBlock(0.1D, 0.7D, 0.05D, 0.95D, 0.75D, 0.95D);
            renderBlock(0.9D, 0.95D, 0D, 1D, 1.1D, 1D);
            
            //Doors
            renderBlock(0.075D, 0.9D, 0.1D, 0.1D, 0.95D, 0.9D);
            renderBlock(0.075D, 0.65D, 0.1D, 0.1D, 0.75D, 0.9D);
            renderBlock(0.075D, 0.05D, 0.1D, 0.1D, 0.2D, 0.9D);
            renderBlock(0.075D, 0.05D, 0D, 0.1D, 0.95D, 0.1D);
            renderBlock(0.075D, 0.05D, 0.9D, 0.1D, 0.95D, 1D);
            //Grates
            setTexture(Blocks.iron_bars);
            renderBlock(0.125D, 0.3D, 0.05D, 0.95D, 0.31D, 0.95D);
            renderBlock(0.125D, 0.5D, 0.05D, 0.95D, 0.51D, 0.95D);
            //Glass
            setTexture(Blocks.glass);
            renderBlock(0.1D, 0.75D, 0.1D, 0.11D, 0.9D, 0.9D);
            renderBlock(0.1D, 0.15D, 0.1D, 0.11D, 0.65D, 0.9D);
            //Hob
            setTexture(Blocks.obsidian);
            renderBlock(0.2D, 1D, 0.05D, 0.9D, 1.01D, 0.95D);
            //Legs
            renderBlock(0.1D, 0D, 0.1D, 0.2D, 0.1D, 0.2D);
            renderBlock(0.8D, 0D, 0.1D, 0.9D, 0.1D, 0.2D);
            renderBlock(0.1D, 0D, 0.8D, 0.2D, 0.1D, 0.9D);
            renderBlock(0.8D, 0D, 0.8D, 0.9D, 0.1D, 0.9D);
            
            
            //Handles - Top
            renderBlock(0.025D, 0.9D, 0.1D, 0.05D, 0.95D, 0.9D);
            renderBlock(0.025D, 0.9D, 0.1D, 0.075D, 0.95D, 0.2D);
            renderBlock(0.025D, 0.9D, 0.8D, 0.075D, 0.95D, 0.9D);
            //Handles - Side
            renderBlock(0.025D, 0.2D, 0.9D, 0.05D, 0.7D, 0.95D);
            renderBlock(0.025D, 0.65D, 0.9D, 0.075D, 0.7D, 0.95D);
            renderBlock(0.025D, 0.2D, 0.9D, 0.075D, 0.25D, 0.95D);
            
        } else if (dir == ForgeDirection.NORTH) {

        } else if (dir == ForgeDirection.EAST) {

        } else if (dir == ForgeDirection.SOUTH) {

        }
    }
}