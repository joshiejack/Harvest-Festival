package joshie.harvest.blocks.render;

import joshie.harvest.blocks.BlockWood;
import joshie.harvest.core.util.RenderBase;
import joshie.harvest.init.HFBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class RenderTrough extends RenderBase {
    @Override
    public void renderBlock() {
        if (!isItem()) {
            Block blockXMinus = world.getBlock(x - 1, y, z);
            int metaXMinus = world.getBlockMetadata(x - 1, y, z);
            Block blockXPlus = world.getBlock(x + 1, y, z);
            int metaXPlus = world.getBlockMetadata(x + 1, y, z);

            setTexture(Blocks.planks, 5);

            boolean onLeft = blockXMinus == HFBlocks.woodmachines && metaXMinus == BlockWood.TROUGH;
            boolean onRight = blockXPlus == HFBlocks.woodmachines && metaXPlus == BlockWood.TROUGH;
            boolean extended = false;
            
            if (onLeft && onRight) {
                extended = true;
                setTexture(Blocks.hay_block);
                renderBlock(0.0D, 0.3D, 0.1D, 1D, 0.7D, 0.9D);
                setTexture(Blocks.planks, 5);
                renderBlock(0D, 0.25D, 0.1D, 1D, 0.3D, 0.9D);
                renderBlock(0D, 0.27D, 0.05D, 1D, 0.77D, 0.1D);
                renderBlock(0D, 0.27D, 0.9D, 1D, 0.77D, 0.95D);
            } else {
                if (onLeft) {
                    extended = true;
                    setTexture(Blocks.hay_block);
                    renderBlock(0.0D, 0.3D, 0.1D, 0.95D, 0.7D, 0.9D);
                    setTexture(Blocks.planks, 5);
                    renderBlock(0D, 0.25D, 0.1D, 0.95D, 0.3D, 0.9D);
                    renderBlock(0D, 0.27D, 0.05D, 0.97D, 0.77D, 0.1D);
                    renderBlock(0D, 0.27D, 0.9D, 0.97D, 0.77D, 0.95D);
                    if (!onRight) {
                        renderBlock(0.8D, 0.0D, 0.1D, 0.9D, 0.25D, 0.9D);
                    }
                } else renderBlock(0.0D, 0.27D, 0.1D, 0.05D, 0.77D, 0.9D);

                if (onRight) {
                    extended = true;
                    setTexture(Blocks.hay_block);
                    renderBlock(0.05D, 0.3D, 0.1D, 1D, 0.7D, 0.9D);
                    setTexture(Blocks.planks, 5);
                    renderBlock(0.05D, 0.25D, 0.1D, 1D, 0.3D, 0.9D);
                    renderBlock(0.03D, 0.27D, 0.05D, 1D, 0.77D, 0.1D);
                    renderBlock(0.03D, 0.27D, 0.9D, 1D, 0.77D, 0.95D);
                    if (!onLeft) {
                        renderBlock(0.1D, 0.0D, 0.1D, 0.2D, 0.25D, 0.9D);
                    }
                } else renderBlock(0.95D, 0.27D, 0.1D, 1.0D, 0.77D, 0.9D);
            }
            if (!extended) {
                setTexture(Blocks.hay_block);
                renderBlock(0.05D, 0.3D, 0.1D, 0.95D, 0.7D, 0.9D);
                setTexture(Blocks.planks, 5);
                renderBlock(0.03D, 0.27D, 0.05D, 0.97D, 0.77D, 0.1D);
                renderBlock(0.03D, 0.27D, 0.9D, 0.97D, 0.77D, 0.95D);
                renderBlock(0.1D, 0.0D, 0.1D, 0.2D, 0.25D, 0.9D);
                renderBlock(0.8D, 0.0D, 0.1D, 0.9D, 0.25D, 0.9D);
                renderBlock(0.05D, 0.25D, 0.1D, 0.95D, 0.3D, 0.9D);
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
