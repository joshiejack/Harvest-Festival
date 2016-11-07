package joshie.harvest.core.base.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import static net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.FIXED;

public class TileSpecialRendererItem<T extends TileEntity> extends TileEntitySpecialRenderer<T> {
    protected static final Minecraft MINECRAFT = Minecraft.getMinecraft();

    protected void renderItem(ItemStack stack, RenderData render, int i) {
        renderItem(stack, render.heightOffset[i], render.rotations[i], render.offset1[i], render.offset2[i]);
    }

    protected void translateItem(boolean isBlock, float position, float rotation, float offset1, float offset2) {

        GlStateManager.translate(0.5F, 0.05F, 0.5F);
        GlStateManager.rotate(-15, 0F, 1F, 0F);
        GlStateManager.scale(0.25F, 0.25F, 0.25F);
        if (!isBlock) {
            //GlStateManager.rotate(-180, 1F, 0F, 0F);
            GlStateManager.rotate(rotation, 0F, 1F, 0F);
            GlStateManager.rotate(-190, 0F, 1F, 0F);
            GlStateManager.translate(offset1 * 3F, offset2 * 3.5F, position * 0.75F);
        } else {
            GlStateManager.rotate(90, 1F, 0F, 0F);
            GlStateManager.translate(offset1 * 1.4F, 0.8F - offset2 * 2.5F, position - 1F);
        }
    }

    protected void renderItem(ItemStack stack, float position, float rotation, float offset1, float offset2) {
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (Minecraft.isAmbientOcclusionEnabled())  {
            GL11.glShadeModel(GL11.GL_SMOOTH);
        } else  {
            GL11.glShadeModel(GL11.GL_FLAT);
        }

        translateItem(stack.getItem() instanceof ItemBlock, position, rotation, offset1, offset2);
        GlStateManager.blendFunc(GL11.GL_CONSTANT_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
        GL14.glBlendColor(1F, 1F, 1F, 1F);
        MINECRAFT.getRenderItem().renderItem(stack, FIXED);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
