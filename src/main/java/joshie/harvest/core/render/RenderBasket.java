package joshie.harvest.core.render;

import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockStorage.Storage;
import joshie.harvest.core.entity.EntityBasket;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.tile.TileBasket;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class RenderBasket extends Render<EntityBasket> {
    protected final ResourceLocation texture;
    private final IBlockState state = HFCore.STORAGE.getStateFromEnum(Storage.BASKET);
    private final RenderItem itemRenderer;
    private final Random random = new Random();

    public RenderBasket(RenderManager manager) {
        super(manager);
        shadowSize = 1F;
        texture = new ResourceLocation(HFModInfo.MODID, "textures/models/basket.png");
        itemRenderer = Minecraft.getMinecraft().getRenderItem();
    }

    @Override
    @Nonnull
    protected ResourceLocation getEntityTexture(EntityBasket entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void doRender(EntityBasket entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (state.getRenderType() == EnumBlockRenderType.MODEL) {
            World world = entity.getEntityWorld();
            if (state != world.getBlockState(new BlockPos(entity)) && state.getRenderType() != EnumBlockRenderType.INVISIBLE) {
                bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                GlStateManager.pushMatrix();
                GlStateManager.disableLighting();
                Tessellator tessellator = Tessellator.getInstance();
                VertexBuffer vertexbuffer = tessellator.getBuffer();

                if (renderOutlines) {
                    GlStateManager.enableColorMaterial();
                    GlStateManager.enableOutlineMode(this.getTeamColor(entity));
                }

                vertexbuffer.begin(7, DefaultVertexFormats.BLOCK);
                BlockPos blockpos = new BlockPos(entity.posX, entity.getEntityBoundingBox().maxY, entity.posZ);
                GlStateManager.translate((float)(x - (double)blockpos.getX() - 0.5D), (float)(y - (double)blockpos.getY() + 0.5F), (float)(z - (double)blockpos.getZ() - 0.5D));
                BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(state), state, blockpos, vertexbuffer, false, MathHelper.getPositionRandom(entity.getPosition()));
                tessellator.draw();

                if (renderOutlines)  {
                    GlStateManager.disableOutlineMode();
                    GlStateManager.disableColorMaterial();
                }

                GlStateManager.enableLighting();
                GlStateManager.popMatrix();
            }
        }

        if (entity.getEntityItem() != null && entity.getEntityItem().getItem() != TileBasket.STONE) {
            renderItem(entity, x, y, z);
        }
    }

    private void renderItem(EntityBasket entity, double x, double y, double z) {
        ItemStack itemstack = entity.getEntityItem();
        int i;

        if (itemstack != null && itemstack.getItem() != null)
        {
            i = Item.getIdFromItem(itemstack.getItem()) + itemstack.getMetadata();
        }
        else
        {
            i = 187;
        }

        this.random.setSeed((long)i);
        boolean flag = false;

        if (this.bindEntityTexture(entity))
        {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).setBlurMipmap(false, false);
            flag = true;
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0F, 2F, 0F);
        IBakedModel ibakedmodel = this.itemRenderer.getItemModelWithOverrides(itemstack, entity.worldObj, null);
        int j = 3;
        boolean flag1 = ibakedmodel.isGui3d();

        if (!flag1)
        {
            float f3 = -0.0F * (float)(j - 1) * 0.5F;
            float f4 = -0.0F * (float)(j - 1) * 0.5F;
            float f5 = -0.09375F * (float)(j - 1) * 0.5F;
            GlStateManager.translate(f3, f4, f5);
        }

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        for (int k = 0; k < j; ++k)
        {
            if (flag1)
            {
                GlStateManager.pushMatrix();

                if (k > 0)
                {
                    float f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    GlStateManager.translate(0, 0, f6);
                }

                ibakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
                this.itemRenderer.renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
            }
            else
            {
                GlStateManager.pushMatrix();

                if (k > 0)
                {
                    float f8 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    float f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    GlStateManager.translate(f8, f10, 0.0F);
                }

                ibakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
                this.itemRenderer.renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
                GlStateManager.translate(0.0F, 0.0F, 0.09375F);
            }
        }

        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.bindEntityTexture(entity);

        if (flag)
        {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).restoreLastBlurMipmap();
        }
    }
}
