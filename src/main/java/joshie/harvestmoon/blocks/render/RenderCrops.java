package joshie.harvestmoon.blocks.render;

import joshie.harvestmoon.core.lib.RenderIds;
import joshie.harvestmoon.init.HMBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderCrops implements ISimpleBlockRenderingHandler {
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        return;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (block == HMBlocks.crops) {
            Tessellator tessellator = Tessellator.instance;
            tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
            int l = block.colorMultiplier(world, x, y, z);
            float f = (float) (l >> 16 & 255) / 255.0F;
            float f1 = (float) (l >> 8 & 255) / 255.0F;
            float f2 = (float) (l & 255) / 255.0F;
            tessellator.setColorOpaque_F(f, f1, f2);
            IIcon iicon = renderer.getBlockIcon(block, world, x, y, z, 0);
            return renderCrops(iicon, (double) x, (double) ((float) y - 0.0625F), (double) z);
        } else return false;
    }

    public boolean renderCrops(IIcon iicon, double x, double y, double z) {
        Tessellator tessellator = Tessellator.instance;
        double d3 = (double) iicon.getMinU();
        double d4 = (double) iicon.getMinV();
        double d5 = (double) iicon.getMaxU();
        double d6 = (double) iicon.getMaxV();
        double d7 = x + 0.5D - 0.25D;
        double d8 = x + 0.5D + 0.25D;
        double d9 = z + 0.5D - 0.5D;
        double d10 = z + 0.5D + 0.5D;
        tessellator.addVertexWithUV(d7, y + 1.0D, d9, d3, d4);
        tessellator.addVertexWithUV(d7, y + 0.0D, d9, d3, d6);
        tessellator.addVertexWithUV(d7, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d7, y + 1.0D, d10, d5, d4);
        tessellator.addVertexWithUV(d7, y + 1.0D, d10, d3, d4);
        tessellator.addVertexWithUV(d7, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d7, y + 0.0D, d9, d5, d6);
        tessellator.addVertexWithUV(d7, y + 1.0D, d9, d5, d4);
        tessellator.addVertexWithUV(d8, y + 1.0D, d10, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d9, d5, d6);
        tessellator.addVertexWithUV(d8, y + 1.0D, d9, d5, d4);
        tessellator.addVertexWithUV(d8, y + 1.0D, d9, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d9, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d8, y + 1.0D, d10, d5, d4);
        d7 = x + 0.5D - 0.5D;
        d8 = x + 0.5D + 0.5D;
        d9 = z + 0.5D - 0.25D;
        d10 = z + 0.5D + 0.25D;
        tessellator.addVertexWithUV(d7, y + 1.0D, d9, d3, d4);
        tessellator.addVertexWithUV(d7, y + 0.0D, d9, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d9, d5, d6);
        tessellator.addVertexWithUV(d8, y + 1.0D, d9, d5, d4);
        tessellator.addVertexWithUV(d8, y + 1.0D, d9, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d9, d3, d6);
        tessellator.addVertexWithUV(d7, y + 0.0D, d9, d5, d6);
        tessellator.addVertexWithUV(d7, y + 1.0D, d9, d5, d4);
        tessellator.addVertexWithUV(d8, y + 1.0D, d10, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d7, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d7, y + 1.0D, d10, d5, d4);
        tessellator.addVertexWithUV(d7, y + 1.0D, d10, d3, d4);
        tessellator.addVertexWithUV(d7, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d8, y + 1.0D, d10, d5, d4);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return RenderIds.CROPS;
    }
}
