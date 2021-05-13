package uk.joshiejack.penguinlib.client.renderer.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import javax.annotation.Nonnull;

import static net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.FIXED;
import static net.minecraft.client.renderer.texture.TextureMap.LOCATION_BLOCKS_TEXTURE;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_TEX;

public class SpecialRendererItem<T extends TileEntity> extends TileEntitySpecialRenderer<T> {
    protected void translateItem(boolean isBlock, float position, float rotation, float offset1, float offset2) {
        GlStateManager.translate(0.5F, 0.05F, 0.5F);
        GlStateManager.rotate(-15, 0F, 1F, 0F);
        GlStateManager.scale(0.25F, 0.25F, 0.25F);
        if (!isBlock) {
            GlStateManager.rotate(rotation, 0F, 1F, 0F);
            GlStateManager.rotate(-190, 0F, 1F, 0F);
            GlStateManager.translate(offset1 * 3F, offset2 * 3.5F, position * 0.75F);
        } else {
            GlStateManager.rotate(90, 1F, 0F, 0F);
            GlStateManager.translate(offset1 * 1.4F, 0.8F - offset2 * 2.5F, position - 1F);
        }
    }

    protected void renderItem(ItemStack stack, SpecialRenderData render, int i) {
        renderItem(stack, () -> translateItem(stack.getItem() instanceof ItemBlock, render.heightOffset[i], render.rotations[i], render.offset1[i], render.offset2[i]));
    }

    protected void renderItem(@Nonnull ItemStack stack, Runnable... r) {
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (Minecraft.isAmbientOcclusionEnabled())  {
            GL11.glShadeModel(GL11.GL_SMOOTH);
        } else  {
            GL11.glShadeModel(GL11.GL_FLAT);
        }

        for (Runnable r2: r) {
            r2.run(); //Execute the translations
        }

        GlStateManager.blendFunc(GL11.GL_CONSTANT_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
        GL14.glBlendColor(1F, 1F, 1F, 1F);
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, FIXED);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    protected void renderGuiTexture(EnumFacing facing, TextureAtlasSprite sprite, float x, float y, float z, float size, int texUMin, int texVMin, int uWidth, int vHeight) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vb = tessellator.getBuffer();
        if (sprite != null) {
            bindTexture(LOCATION_BLOCKS_TEXTURE);
            double uMin = (double) sprite.getMinU();
            double uMax = (double) sprite.getMaxU();
            double vMin = (double) sprite.getMinV();
            double vMax = (double) sprite.getMaxV();

            if (facing == EnumFacing.NORTH) {
                GlStateManager.rotate(90F, 1F, 0F, 0F);
                GlStateManager.rotate(180F, 0F, 1F, 0F);
                vb.begin(7, POSITION_TEX);
                vb.pos(size / 2f, -size / 2f, -size / 2f).tex(uMax, vMax).endVertex();//Top Right
                vb.pos(size / 2f, -size / 2f, size / 2f).tex(uMax, vMin).endVertex(); //Top Left
                vb.pos(-size / 2f, -size / 2f, size / 2f).tex(uMin, vMin).endVertex(); //Bottom Left
                vb.pos(-size / 2f, -size / 2f, -size / 2f).tex(uMin, vMax).endVertex(); //Bottom Right
                tessellator.draw();
            } else if (facing == EnumFacing.SOUTH) {
                GlStateManager.rotate(90F, 1F, 0F, 0F);
                vb.begin(7, POSITION_TEX);
                vb.pos(size / 2f, 0, size / 2f).tex(uMax, vMax).endVertex();
                vb.pos(size / 2f, 0, -size / 2f).tex(uMax, vMin).endVertex();
                vb.pos(-size / 2f, 0, -size / 2f).tex(uMin, vMin).endVertex();
                vb.pos(-size / 2f, 0, size / 2f).tex(uMin, vMax).endVertex();
                tessellator.draw();
            } else if (facing == EnumFacing.WEST) {
                GlStateManager.rotate(90F, 1F, 0F, 0F);
                GlStateManager.rotate(90F, 0F, 0F, 1F);
                vb.begin(7, POSITION_TEX);
                vb.pos(size / 2f, 0, size / 2f).tex(uMax, vMax).endVertex();
                vb.pos(size / 2f, 0, -size / 2f).tex(uMax, vMin).endVertex();
                vb.pos(-size / 2f, 0, -size / 2f).tex(uMin, vMin).endVertex();
                vb.pos(-size / 2f, 0, size / 2f).tex(uMin, vMax).endVertex();
                tessellator.draw();
            } else {
                GlStateManager.rotate(180F, 0F, 0F, 1F);
                GlStateManager.rotate(90F, 1F, 0F, 0F);
                GlStateManager.rotate(270F, 0F, 0F, 1F);
                vb.begin(7, POSITION_TEX);
                vb.pos(size / 2f, -size / 2f, -size / 2f).tex(uMax, vMax).endVertex();//Top Right
                vb.pos(size / 2f, -size / 2f, size / 2f).tex(uMax, vMin).endVertex(); //Top Left
                vb.pos(-size / 2f, -size / 2f, size / 2f).tex(uMin, vMin).endVertex(); //Bottom Left
                vb.pos(-size / 2f, -size / 2f, -size / 2f).tex(uMin, vMax).endVertex(); //Bottom
                tessellator.draw();
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    protected void renderFluidCube(ResourceLocation fluid, float x, float y, float z, float size) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vb = tessellator.getBuffer();
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.toString());
        if (sprite != null) {
            bindTexture(LOCATION_BLOCKS_TEXTURE);
            double uMin = (double) sprite.getMinU();
            double uMax = (double) sprite.getMaxU();
            double vMin = (double) sprite.getMinV();
            double vMax = (double) sprite.getMaxV();

            //Draw Top
            //
            vb.begin(7, POSITION_TEX);
            vb.pos(size / 2f, 0, size / 2f).tex(uMax, vMax).endVertex();
            vb.pos(size / 2f, 0, -size / 2f).tex(uMax, vMin).endVertex();
            vb.pos(-size / 2f, 0, -size / 2f).tex(uMin, vMin).endVertex();
            vb.pos(-size / 2f, 0, size / 2f).tex(uMin, vMax).endVertex();
            tessellator.draw();

            //Draw Bottom
            vb.begin(7, POSITION_TEX);
            vb.pos(size / 2f, -size / 2f, -size / 2f).tex(uMax, vMax).endVertex();//Top Right
            vb.pos(size / 2f, -size / 2f, size / 2f).tex(uMax, vMin).endVertex(); //Top Left
            vb.pos(-size / 2f, -size / 2f, size / 2f).tex(uMin, vMin).endVertex(); //Bottom Left
            vb.pos(-size / 2f, -size / 2f, -size / 2f).tex(uMin, vMax).endVertex(); //Bottom Right
            tessellator.draw();

            //Draw Side 1
            vb.begin(7, POSITION_TEX);
            vb.pos(-size / 2f, 0, size / 2f).tex(uMax, vMax).endVertex();
            vb.pos(-size / 2f, 0, -size / 2f).tex(uMax, vMin).endVertex();
            vb.pos(-size / 2f, -size / 2f, -size / 2f).tex(uMin, vMin).endVertex();
            vb.pos(-size / 2f, -size / 2f, size / 2f).tex(uMin, vMax).endVertex();
            tessellator.draw();

            //Draw Side 2
            vb.begin(7, POSITION_TEX);
            vb.pos(size / 2f, 0, -size / 2f).tex(uMax, vMax).endVertex();
            vb.pos(size / 2f, 0, size / 2f).tex(uMax, vMin).endVertex();
            vb.pos(size / 2f, -size / 2f, size / 2f).tex(uMin, vMin).endVertex();
            vb.pos(size / 2f, -size / 2f, -size / 2f).tex(uMin, vMax).endVertex();
            tessellator.draw();

            //Draw Side 3
            vb.begin(7, POSITION_TEX);
            vb.pos(size / 2f, 0, size / 2f).tex(uMax, vMax).endVertex(); // Top Right
            vb.pos(-size / 2f, 0, size / 2f).tex(uMax, vMin).endVertex(); //Top Left
            vb.pos(-size / 2f, -size / 2f, size / 2f).tex(uMin, vMin).endVertex(); //Bottom Left
            vb.pos(size / 2f, -size / 2f, size / 2f).tex(uMin, vMax).endVertex(); //Bottom Right
            tessellator.draw();

            //Draw Side 2
            vb.begin(7, POSITION_TEX);
            vb.pos(-size / 2f, 0, -size / 2f).tex(uMax, vMax).endVertex(); //Top Right
            vb.pos(size / 2f, 0, -size / 2f).tex(uMax, vMin).endVertex(); //Top Left
            vb.pos(size / 2f, -size / 2f, -size / 2f).tex(uMin, vMin).endVertex(); //Bottom Left
            vb.pos(-size / 2f, -size / 2f, -size / 2f).tex(uMin, vMax).endVertex(); //Bottom Right
            tessellator.draw();
        }

        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    @SuppressWarnings("ConstantConditions")
    protected void renderFluidPlane(ResourceLocation fluid, float x, float y, float z, float width, float length) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vb = tessellator.getBuffer();
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.toString());
        if (sprite != null) {
            bindTexture(LOCATION_BLOCKS_TEXTURE);
            double uMin = (double) sprite.getMinU();
            double uMax = (double) sprite.getMaxU();
            double vMin = (double) sprite.getMinV();
            double vMax = (double) sprite.getMaxV();

            vb.begin(7, POSITION_TEX);
            vb.pos(width / 2f, 0, length / 2f).tex(uMax, vMax).endVertex();
            vb.pos(width / 2f, 0, -length / 2f).tex(uMax, vMin).endVertex();
            vb.pos(-width / 2f, 0, -length / 2f).tex(uMin, vMin).endVertex();
            vb.pos(-width / 2f, 0, length / 2f).tex(uMin, vMax).endVertex();
            tessellator.draw();
        }

        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
