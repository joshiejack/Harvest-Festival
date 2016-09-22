package joshie.harvest.buildings.render;

import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.buildings.placeable.blocks.PlaceableDouble;
import joshie.harvest.buildings.placeable.blocks.PlaceableDoubleOpposite;
import joshie.harvest.core.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BuildingRenderer {
    private final RegionRenderCacheBuilder renderer;
    private final BuildingKey key;
    private int exist;

    public BuildingRenderer(IBlockAccess world, BuildingKey key) {
        BuildingImpl building = key.getBuilding();
        this.key = key;
        this.renderer = new RegionRenderCacheBuilder();
        Direction direction = Direction.withMirrorAndRotation(key.getMirror(), key.getRotation());
        for (BlockRenderLayer layer: BlockRenderLayer.values()) {
            VertexBuffer buffer = renderer.getWorldRendererByLayer(layer);
            buffer.begin(7, DefaultVertexFormats.BLOCK);
            if (HFBuildings.FULL_BUILDING_RENDER) {
                for (Placeable placeable : building.getFullList()) {
                    if (placeable.getY() >= -building.getOffsetY()) {
                        if (placeable instanceof PlaceableBlock) {
                            PlaceableBlock block = (PlaceableBlock) placeable;
                            if (block.getBlock() == Blocks.AIR) continue;
                            addRender(world, block, direction, layer, buffer);
                        }
                    }
                }
            } else {
                for (PlaceableBlock block : building.getPreviewList()) {
                    addRender(world, block, direction, layer, buffer);
                }
            }
        }
    }

    private void addRender(IBlockAccess world, PlaceableBlock block, Direction direction, BlockRenderLayer layer, VertexBuffer buffer) {
        IBlockState state = block.getTransformedState(direction);
        if (state.getBlock().canRenderInLayer(state, layer)) {
            Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(state, block.transformBlockPos(direction), world, buffer);
            if (block instanceof PlaceableDouble) {
                state = state.getBlock().getStateFromMeta(8);
                Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(state, block.transformBlockPos(direction).up(), world, buffer);
            } else if (block instanceof PlaceableDoubleOpposite) {
                state = state.getBlock().getStateFromMeta(9);
                Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(state, block.transformBlockPos(direction).up(), world, buffer);
            }
        }
    }

    public BuildingRenderer start() {
        this.exist = 2000;
        return this;
    }

    public boolean remove() {
        this.exist--;
        return this.exist <= 0;
    }

    public BuildingImpl getBuilding() {
        return key.getBuilding();
    }

    public BlockPos getPos() {
        return key.getPos();
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
