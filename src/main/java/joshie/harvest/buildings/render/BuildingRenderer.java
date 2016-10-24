package joshie.harvest.buildings.render;

import joshie.harvest.buildings.BuildingImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map.Entry;

@SideOnly(Side.CLIENT)
public class BuildingRenderer {
    private final RegionRenderCacheBuilder renderer;
    private final RenderKey key;
    private BlockPos pos;

    public BuildingRenderer(BuildingAccess world, RenderKey key) {
        this.key = key;
        this.pos = key.getPos();
        this.renderer = new RegionRenderCacheBuilder();
        for (BlockRenderLayer layer: BlockRenderLayer.values()) {
            VertexBuffer buffer = renderer.getWorldRendererByLayer(layer);
            buffer.begin(7, DefaultVertexFormats.BLOCK);
            for (Entry<BlockPos, IBlockState> placeable: world.getBlockMap().entrySet()) {
                addRender(world, placeable.getValue(), placeable.getKey(), layer, buffer);
            }
        }
    }

    private void addRender(IBlockAccess world, IBlockState state, BlockPos pos, BlockRenderLayer layer, VertexBuffer buffer) {
        if (state.getBlock().canRenderInLayer(state, layer)) {
            Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(state, pos, world, buffer);
        }
    }

    public BuildingRenderer setPosition(BlockPos position) {
        this.pos = position;
        return this;
    }

    public BuildingImpl getBuilding() {
        return key.getBuilding();
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public void draw(BuildingVertexUploader vertexUploader) {
        GlStateManager.disableAlpha();
        vertexUploader.draw(renderer.getWorldRendererByLayer(BlockRenderLayer.SOLID));
        GlStateManager.enableAlpha();
        vertexUploader.draw(renderer.getWorldRendererByLayer(BlockRenderLayer.CUTOUT_MIPPED));
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        vertexUploader.draw(renderer.getWorldRendererByLayer(BlockRenderLayer.CUTOUT));
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.shadeModel(7425);
        vertexUploader.draw(renderer.getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT));
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuildingRenderer renderer = (BuildingRenderer) o;
        return key != null ? key.equals(renderer.key) : renderer.key == null;

    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
