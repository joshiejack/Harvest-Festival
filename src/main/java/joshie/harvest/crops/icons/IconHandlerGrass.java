package joshie.harvest.crops.icons;

/*public class IconHandlerGrass extends AbstractIconHandler {
    private IIcon[] bottomIcons;
    private IIcon[] topIcons;

    @SideOnly(Side.CLIENT)
    //AT STAGE 6 We are double
    public IIcon getIconForStage(PlantSection section, int stage) {
        if (section == PlantSection.BOTTOM) {
            return bottomIcons[Math.max(0, stage - 1)]; //All grass
        } else {
            int index = stage - 6;
            return topIcons[(Math.max(index, 0))];
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean doCustomRender(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, Block block) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        int l = block.colorMultiplier(world, x, y, z);
        float f = (float) (l >> 16 & 255) / 255.0F;
        float f1 = (float) (l >> 8 & 255) / 255.0F;
        float f2 = (float) (l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable) {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        tessellator.setColorOpaque_F(f, f1, f2);
        double d1 = (double) x;
        double d2 = (double) y;
        double d0 = (double) z;
        long i1;

        if (block == Blocks.TALLGRASS) {
            i1 = (long) (x * 3129871) ^ (long) y * 116129781L ^ (long) z;
            i1 = i1 * i1 * 42317861L + i1 * 11L;
            d1 += ((double) ((float) (i1 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
            d2 += ((double) ((float) (i1 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
            d0 += ((double) ((float) (i1 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
        }

        IIcon iicon = renderer.getBlockIcon(block, world, x, y, z, 0);
        renderer.drawCrossedSquares(iicon, d1, d2, d0, 1.0F);
        return true;
    }
}*/