package joshie.harvestmoon.blocks.render;

import joshie.harvestmoon.util.RenderBase;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

public class RenderKitchen extends RenderBase {
    @Override
    public void renderBlock() {
        if(isItem()) {
            GL11.glScalef(0.95F, 0.95F, 0.95F);
            GL11.glTranslated(0F, -0.05F, 0F);
        }
        
        setTexture(ExtraIcons.COUNTER_SIDE);
        if (dir == ForgeDirection.WEST || isItem()) {
            renderBlock(0.1D, 0D, 0D, 1D, 0.95D, 1D);
            setTexture(ExtraIcons.COUNTER_SURFACE);
            renderBlock(0.9D, 0.95D, 0D, 1D, 1.1D, 1D);
            renderBlock(0.05D, 0.95D, 0D, 0.9D, 1D, 1D);
            setTexture(ExtraIcons.METALLIC);
            renderBlock(0.05D, 0.4D, 0.25D, 0.09D, 0.6D, 0.35D);
            renderBlock(0.05D, 0.4D, 0.65D, 0.09D, 0.6D, 0.75D);
            setTexture(ExtraIcons.COUNTER_FRONT);
            renderBlock(0.09D, 0D, 0D, 0.1D, 0.95D, 1D);
        } else if (dir == ForgeDirection.NORTH) {
            renderBlock(0D, 0D, 0.1D, 1D, 0.95D, 1D);
            setTexture(ExtraIcons.COUNTER_SURFACE);
            renderBlock(0D, 0.95D, 0.9D, 1D, 1.1D, 1D);
            renderBlock(0D, 0.95D, 0.05D, 1D, 1D, 0.9D);
            setTexture(ExtraIcons.METALLIC);
            renderBlock(0.25D, 0.4D, 0.05D, 0.35D, 0.6D, 0.09D);
            renderBlock(0.65D, 0.4D, 0.05D, 0.75D, 0.6D, 0.09D);
            setTexture(ExtraIcons.COUNTER_FRONT);
            renderBlock(0D, 0D, 0.09D, 1D, 0.95D, 0.1D);
        } else if (dir == ForgeDirection.EAST) {
            renderBlock(0D, 0D, 0D, 0.9D, 0.95D, 1D);
            setTexture(ExtraIcons.COUNTER_SURFACE);
            renderBlock(0D, 0.95D, 0D, 0.1D, 1.1D, 1D);
            renderBlock(0.1D, 0.95D, 0D, 0.95D, 1D, 1D);
            setTexture(ExtraIcons.METALLIC);
            renderBlock(0.91D, 0.4D, 0.25D, 0.95D, 0.6D, 0.35D);
            renderBlock(0.91D, 0.4D, 0.65D, 0.95D, 0.6D, 0.75D);
            setTexture(ExtraIcons.COUNTER_FRONT);
            renderBlock(0.9D, 0D, 0D, 0.91D, 0.95D, 1D);
        } else if (dir == ForgeDirection.SOUTH) {
            renderBlock(0D, 0D, 0D, 1D, 0.95D, 0.9D);
            setTexture(ExtraIcons.COUNTER_SURFACE);
            renderBlock(0D, 0.95D, 0D, 1D, 1.1D, 0.11D);
            renderBlock(0D, 0.95D, 0.1D, 1D, 1D, 0.95D);
            setTexture(ExtraIcons.METALLIC);
            renderBlock(0.25D, 0.4D, 0.91D, 0.35D, 0.6D, 0.95D);
            renderBlock(0.65D, 0.4D, 0.91D, 0.75D, 0.6D, 0.95D);
            setTexture(ExtraIcons.COUNTER_FRONT);
            renderBlock(0D, 0D, 0.9D, 1D, 0.95D, 0.91D);
        }
    }
}