package joshie.harvest.buildings.render;

import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.blocks.BlockPreview.Direction;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.blocks.tiles.TileMarker;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PreviewRender extends TileEntitySpecialRenderer<TileMarker> {
    private BlockRendererDispatcher blockRenderer;

    @Override
    public void renderTileEntityAt(TileMarker te, double x, double y, double z, float partialTicks, int destroyStage) {
        if(blockRenderer == null) blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        BlockPos blockpos = te.getPos();
        IBlockState iblockstate = Blocks.STONE.getDefaultState();
        if (iblockstate.getMaterial() != Material.AIR) {
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
            vertexbuffer.setTranslation((double)((float)x - (float)blockpos.getX()), (double)((float)y - (float)blockpos.getY()), (double)((float)z - (float)blockpos.getZ()));
            World world = this.getWorld();

            IBuilding building = te.getBuilding();
            if (building != null) {
                Direction direction = HFBlocks.PREVIEW.getEnumFromState(te.getWorld().getBlockState(te.getPos()));
                Mirror mirror = direction.getMirror();
                Rotation rotation = direction.getRotation();
                for (Placeable placeable: building.getList()) {
                    if (!(placeable instanceof PlaceableBlock)) continue;
                    PlaceableBlock block = ((PlaceableBlock)placeable);

                    renderStateModel(block.getTransformedPosition(blockpos, mirror, rotation), block.getTransformedState(mirror, rotation), vertexbuffer, world, false);
                }
            } else renderStateModel(blockpos, iblockstate, vertexbuffer, world, false);
            vertexbuffer.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator.draw();
            RenderHelper.enableStandardItemLighting();
        }
    }


    private boolean renderStateModel(BlockPos p_188186_1_, IBlockState p_188186_2_, VertexBuffer p_188186_3_, World p_188186_4_, boolean p_188186_5_)
    {
        return this.blockRenderer.getBlockModelRenderer().renderModel(p_188186_4_, this.blockRenderer.getModelForState(p_188186_2_), p_188186_2_, p_188186_1_, p_188186_3_, p_188186_5_);
    }
}
