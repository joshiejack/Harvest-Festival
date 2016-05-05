package joshie.harvest.buildings.render;

import joshie.harvest.blocks.tiles.TileMarker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PreviewRender extends TileEntitySpecialRenderer<TileMarker> {
    private BlockRendererDispatcher blockRenderer;

    @Override //TODO: Not Gonna happen, need to find another way to do this that is more friendly
    public void renderTileEntityAt(TileMarker te, double x, double y, double z, float partialTicks, int destroyStage) {
        /*
        HFDebug.start("tile");
        if(blockRenderer == null) blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        BlockPos blockpos = te.getPos();
        IBlockState iblockstate = Blocks.STONE.getDefaultState();
        if (iblockstate.getMaterial() != Material.AIR) {
            HFDebug.start("setup");
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer vertexbuffer = tessellator.getBuffer();
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();

            if (Minecraft.isAmbientOcclusionEnabled()) {
                GlStateManager.shadeModel(7425);
            } else  {
                GlStateManager.shadeModel(7424);
            }

            vertexbuffer.begin(7, DefaultVertexFormats.BLOCK);
            vertexbuffer.color(1F, 1F, 1F, 0.1F);
            vertexbuffer.setTranslation((double)((float)x - (float)blockpos.getX()), (double)((float)y - (float)blockpos.getY()), (double)((float)z - (float)blockpos.getZ()));
            World world = this.getWorld();
            HFDebug.end("setup");
            HFDebug.start("building");
            IBuilding building = te.getBuilding();
            if (building != null && building.getProvider() != null) {
                HFDebug.start("direction");
                Direction direction = te.getDirection();
                HFDebug.end("direction");
                HFDebug.start("loop");
                for (PlaceableBlock block: te.getList()) {
                    HFDebug.start("pos");
                    BlockPos pos = block.getTransformedPosition(blockpos, direction);
                    HFDebug.end("pos");
                    HFDebug.start("state");
                    IBlockState state = block.getTransformedState(direction);
                    HFDebug.end("state");
                    HFDebug.start("render");
                    renderStateModel(pos, state, vertexbuffer, world, false);
                    HFDebug.end("render");
                }

                HFDebug.end("loop");
            } else renderStateModel(blockpos, iblockstate, vertexbuffer, world, false);
            HFDebug.end("building");
            HFDebug.start("end");
            vertexbuffer.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator.draw();
            RenderHelper.enableStandardItemLighting();
            HFDebug.end("end");
        }

        HFDebug.end("tile"); */
    }


    private boolean renderStateModel(BlockPos p_188186_1_, IBlockState p_188186_2_, VertexBuffer p_188186_3_, World p_188186_4_, boolean p_188186_5_)
    {
        return this.blockRenderer.getBlockModelRenderer().renderModel(p_188186_4_, this.blockRenderer.getModelForState(p_188186_2_), p_188186_2_, p_188186_1_, p_188186_3_, p_188186_5_);
    }
}
