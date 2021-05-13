package uk.joshiejack.penguinlib.template.render;

import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class TemplateRendererer<W extends TemplateWorldAccess> {
    private static final TemplateVertexUploader vertexUploader = new TemplateVertexUploader();
    private static final float offset = 0.00390625F;
    protected final RegionRenderCacheBuilder renderer;
    private final boolean demolish;
    private BlockPos pos;

    public TemplateRendererer(boolean demolish, W world, BlockPos pos) {
        this.pos = pos;
        this.renderer = new RegionRenderCacheBuilder();
        this.demolish = demolish;
        setupRender(world);
    }

    public void render(EntityPlayer player, float partialTick) {
        GlStateManager.pushMatrix();
        double posX = player.prevPosX + (player.posX - player.prevPosX) * partialTick;
        double posY = player.prevPosY + (player.posY - player.prevPosY) * partialTick;
        double posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTick;
        GlStateManager.translate(-posX + offset, -posY + offset, -posZ + offset);
        GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
        draw();
        GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());
        GlStateManager.translate(posX, posY, posZ);
        GlStateManager.popMatrix();
    }

    public void draw() {
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

    protected void setupRender(W world) {
        for (BlockRenderLayer layer: BlockRenderLayer.values()) {
            BufferBuilder buffer = renderer.getWorldRendererByLayer(layer);
            buffer.begin(7, DefaultVertexFormats.BLOCK);
            for (Map.Entry<BlockPos, IBlockState> placeable: world.getBlockMap().entrySet()) {
                addRender(world, placeable.getValue(), placeable.getKey(), layer, buffer);
            }
        }
    }

    protected void addRender(IBlockAccess world, IBlockState state, BlockPos pos, BlockRenderLayer layer, BufferBuilder buffer) {
        if (demolish) state = Blocks.STAINED_GLASS.getDefaultState().withProperty(BlockStainedGlass.COLOR, EnumDyeColor.RED); //Render everything as TNT
        if (state.getBlock().canRenderInLayer(state, layer)) {
           // if (demolish) buffer.noColor(); //HMM
            Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(state, pos, world, buffer);
        }
    }

    public TemplateRendererer setPosition(BlockPos position) {
        this.pos = position;
        return this;
    }

    public BlockPos getPos() {
        return this.pos;
    }
}
