package joshie.harvest.buildings.render;

import joshie.harvest.blocks.tiles.TileMarker;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class PreviewRender extends TileEntitySpecialRenderer<TileMarker> {
    //private BlockRendererDispatcher blockRenderer;

    @Override
    public void renderTileEntityAt(TileMarker te, double x, double y, double z, float partialTicks, int destroyStage) {
        /*if(blockRenderer == null) blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        BlockPos blockpos = te.getPos();
        IBlockState iblockstate = te.getPistonState();
        Block block = iblockstate.getBlock();

        if (iblockstate.getMaterial() != Material.AIR && te.getProgress(partialTicks) < 1.0F) {
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer vertexbuffer = tessellator.getBuffer();
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();

            if (Minecraft.isAmbientOcclusionEnabled())
            {
                GlStateManager.shadeModel(7425);
            }
            else
            {
                GlStateManager.shadeModel(7424);
            }

            vertexbuffer.begin(7, DefaultVertexFormats.BLOCK);
            vertexbuffer.setTranslation((double)((float)x - (float)blockpos.getX() + te.getOffsetX(partialTicks)), (double)((float)y - (float)blockpos.getY() + te.getOffsetY(partialTicks)), (double)((float)z - (float)blockpos.getZ() + te.getOffsetZ(partialTicks)));
            World world = this.getWorld();

            if (block == Blocks.PISTON_HEAD && te.getProgress(partialTicks) < 0.5F)
            {
                iblockstate = iblockstate.withProperty(BlockPistonExtension.SHORT, Boolean.valueOf(true));
                this.renderStateModel(blockpos, iblockstate, vertexbuffer, world, true);
            }
            else if (te.shouldPistonHeadBeRendered() && !te.isExtending())
            {
                BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = block == Blocks.STICKY_PISTON ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
                IBlockState iblockstate1 = Blocks.PISTON_HEAD.getDefaultState().withProperty(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype).withProperty(BlockPistonExtension.FACING, iblockstate.getValue(BlockPistonBase.FACING));
                iblockstate1 = iblockstate1.withProperty(BlockPistonExtension.SHORT, Boolean.valueOf(te.getProgress(partialTicks) >= 0.5F));
                this.renderStateModel(blockpos, iblockstate1, vertexbuffer, world, true);
                vertexbuffer.setTranslation((double)((float)x - (float)blockpos.getX()), (double)((float)y - (float)blockpos.getY()), (double)((float)z - (float)blockpos.getZ()));
                iblockstate = iblockstate.withProperty(BlockPistonBase.EXTENDED, Boolean.valueOf(true));
                this.renderStateModel(blockpos, iblockstate, vertexbuffer, world, true);
            }
            else
            {
                this.renderStateModel(blockpos, iblockstate, vertexbuffer, world, false);
            }

            vertexbuffer.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator.draw();
            RenderHelper.enableStandardItemLighting();
        }*/
    }

    /*
    private boolean renderStateModel(BlockPos p_188186_1_, IBlockState p_188186_2_, VertexBuffer p_188186_3_, World p_188186_4_, boolean p_188186_5_)
    {
        return this.blockRenderer.getBlockModelRenderer().renderModel(p_188186_4_, this.blockRenderer.getModelForState(p_188186_2_), p_188186_2_, p_188186_1_, p_188186_3_, p_188186_5_);
    }*/
}
