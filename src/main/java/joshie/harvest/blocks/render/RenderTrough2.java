package joshie.harvest.blocks.render;

import org.lwjgl.opengl.GL11;

import joshie.harvest.blocks.BlockWood;
import joshie.harvest.core.util.RenderBase;
import joshie.harvest.init.HFBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class RenderTrough2 extends RenderBase {
    @Override
    public void renderBlock() {
        if (!isItem()) {
            Block blockZMinus = world.getBlock(x, y, z - 1);
            int metaZMinus = world.getBlockMetadata(x, y, z - 1);
            Block blockZPlus = world.getBlock(x, y, z + 1);
            int metaZPlus = world.getBlockMetadata(x, y, z + 1);

            setTexture(Blocks.planks, 5);
            boolean onLeft = blockZMinus == HFBlocks.woodmachines && metaZMinus == BlockWood.TROUGH_2;
            boolean onRight = blockZPlus == HFBlocks.woodmachines && metaZPlus == BlockWood.TROUGH_2;

            boolean extended = false;
            if (onLeft && onRight) {
                extended = true;
                setTexture(Blocks.hay_block);
                renderBlock(0.1D, 0.3D, 0.0D, 0.9D, 0.7D, 1D);
                setTexture(Blocks.planks, 5);
                renderBlock(0.1D, 0.25D, 0D, 0.9D, 0.3D, 1D);
                renderBlock(0.05D, 0.27D, 0D, 0.1D, 0.77D, 1D);
                renderBlock(0.9D, 0.27D, 0D, 0.95D, 0.77D, 1D);
            } else {
                if (onLeft) {
                    extended = true;
                    setTexture(Blocks.hay_block);
                    renderBlock(0.1D, 0.3D, 0.0D, 0.9D, 0.7D, 0.95D);
                    setTexture(Blocks.planks, 5);
                    renderBlock(0.1D, 0.25D, 0D, 0.9D, 0.3D, 0.95D);
                    renderBlock(0.05D, 0.27D, 0D, 0.1D, 0.77D, 0.97D);
                    renderBlock(0.9D, 0.27D, 0D, 0.95D, 0.77D, 0.97D);
                    if (!onRight) {
                        renderBlock(0.1D, 0.0D, 0.8D, 0.9D, 0.25D, 0.9D);
                    }
                } else renderBlock(0.1D, 0.27D, 0.0D, 0.9D, 0.77D, 0.05D);

                if (onRight) {
                    extended = true;
                    setTexture(Blocks.hay_block);
                    renderBlock(0.1D, 0.3D, 0.05D, 0.9D, 0.7D, 1D);
                    setTexture(Blocks.planks, 5);
                    renderBlock(0.1D, 0.25D, 0.05D, 0.9D, 0.3D, 1D);
                    renderBlock(0.05D, 0.27D, 0.03D, 0.1D, 0.77D, 1D);
                    renderBlock(0.9D, 0.27D, 0.03D, 0.95D, 0.77D, 1D);
                    if (!onLeft) {
                        renderBlock(0.1D, 0.0D, 0.1D, 0.9D, 0.25D, 0.2D);
                    }
                } else renderBlock(0.1D, 0.27D, 0.95D, 0.9D, 0.77D, 1.0D);
            }

            if (!extended) {
                setTexture(Blocks.hay_block);
                renderBlock(0.1D, 0.3D, 0.05D, 0.9D, 0.7D, 0.95D);
                setTexture(Blocks.planks, 5);
                renderBlock(0.05D, 0.27D, 0.03D, 0.1D, 0.77D, 0.97D);
                renderBlock(0.9D, 0.27D, 0.03D, 0.95D, 0.77D, 0.97D);
                renderBlock(0.1D, 0.0D, 0.1D, 0.9D, 0.25D, 0.2D);
                renderBlock(0.1D, 0.0D, 0.8D, 0.9D, 0.25D, 0.9D);
                renderBlock(0.1D, 0.25D, 0.05D, 0.9D, 0.3D, 0.95D);
            }
        }

        if (isItem()) {
            setTexture(Blocks.planks, 5);
            renderBlock(0.95D, 0.27D, 0.1D, 1.0D, 0.77D, 0.9D);
            renderBlock(0.05D, 0.25D, 0.1D, 0.95D, 0.3D, 0.9D);
            renderBlock(0.03D, 0.27D, 0.9D, 0.97D, 0.77D, 0.95D);
            renderBlock(0.03D, 0.27D, 0.05D, 0.97D, 0.77D, 0.1D);
            renderBlock(0.1D, 0.0D, 0.1D, 0.2D, 0.25D, 0.9D);
            renderBlock(0.8D, 0.0D, 0.1D, 0.9D, 0.25D, 0.9D);
            renderBlock(0.0D, 0.27D, 0.1D, 0.05D, 0.77D, 0.9D);

            setTexture(Blocks.hay_block);
            renderBlock(0.05D, 0.3D, 0.1D, 0.95D, 0.7D, 0.9D);
        }
    }
}
