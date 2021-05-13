package uk.joshiejack.penguinlib.client.renderer.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.item.PenguinItems;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class EntitySpawnerRenderer extends TileEntityItemStackRenderer {
    private static final ResourceLocation SHADOW = new ResourceLocation(PenguinLib.MOD_ID, "textures/entity/shadow.png");
    public static boolean renderShadow;

    @Override
    public void renderByItem(@Nonnull ItemStack stack) {
        Entity entity = PenguinItems.ENTITY.getEntityFromStack(stack);
        if (entity instanceof EntityLiving) {
            GlStateManager.enableColorMaterial();
            GlStateManager.pushMatrix();
            float[] adjustments = EntityRendererOffsets.getAdjustments(entity);
            GlStateManager.translate(adjustments[1], adjustments[2], adjustments[3]);
            GlStateManager.scale(-adjustments[0], adjustments[0], adjustments[0]);
            GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, 0.0F, 0.0F);
            RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
            rendermanager.setPlayerViewY(180.0F);
            rendermanager.setRenderShadow(false);
            try {
                Render<Entity> model = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(entity);
                if (model != null) {
                    //RenderSpider
                    model.doRender(entity, 0F, 0F, 0F, 0F, 0F);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //rendermanager.renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
            rendermanager.setRenderShadow(true);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        }
    }
}
