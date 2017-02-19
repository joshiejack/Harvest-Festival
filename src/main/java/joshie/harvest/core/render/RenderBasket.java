package joshie.harvest.core.render;

import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockStorage.Storage;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBasket extends RenderEntity {
    protected final ResourceLocation texture;
    private final IBlockState state = HFCore.STORAGE.getStateFromEnum(Storage.BASKET);

    public RenderBasket(RenderManager manager) {
        super(manager);
        shadowSize = 1F;
        texture = new ResourceLocation(HFModInfo.MODID, "textures/models/basket.png");
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    }
}
