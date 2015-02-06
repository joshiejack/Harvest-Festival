package joshie.harvestmoon.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

public abstract class RenderBase {
    private static final double RENDER_OFFSET = 0.0010000000474974513D;
    private static final float LIGHT_Y_POS = 1.0F;

    public RenderBlocks render;
    public ForgeDirection dir = ForgeDirection.UNKNOWN;
    public IBlockAccess world;
    public int x, y, z;
    public IIcon icon;
    public Block block;
    public boolean isItem;
    public int brightness = -1;
    public float rgb_red = 1.0F;
    public float rgb_green = 1.0F;
    public float rgb_blue = 1.0F;

    public RenderBase() {}

    public RenderBase setFacing(ForgeDirection dir) {
        this.dir = dir;
        return this;
    }

    //World Based Rendering
    public boolean render(RenderBlocks render, IBlockAccess world, int x, int y, int z) {
        isItem = false;
        this.render = render;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        block = world.getBlock(x, y, z);
        render();
        return true;
    }

    //Item Based Rendering
    public void render(RenderBlocks render, Block block) {
        isItem = true;
        this.render = render;
        this.block = block;
        render();
    }

    public boolean render() {
        if (isItem()) {
            GL11.glPushMatrix();
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.4F, -0.5F);
            GL11.glTranslatef(0F, -0.1F, 0F);
            renderBlock();
            GL11.glPopMatrix();
        } else {
            renderBlock();
            if (render.hasOverrideBlockTexture()) {
                render.clearOverrideBlockTexture();
            }
        }

        return true;
    }

    public abstract void renderBlock();

    public boolean isItem() {
        return isItem;
    }

    protected void setTexture(IIcon texture) {
        icon = texture;
        if (!isItem()) {
            render.setOverrideBlockTexture(texture);
        }
    }

    protected void setTexture(ItemStack stack) {
        setTexture(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
    }

    protected void setTexture(Block block, int meta) {
        setTexture(block.getIcon(0, meta));
    }

    protected void setTexture(Block block) {
        setTexture(block, 0);
    }

    //Can only be called by the in world renderer
    protected void renderColoredBlock(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Block block) {
        render.renderAllFaces = true;
        render.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
        render.renderStandardBlock(block, x, y, z);
        render.renderAllFaces = false;
    }

    protected void renderBlock(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if (isItem()) {
            renderItemBlock(minX, minY, minZ, maxX, maxY, maxZ);
        } else {
            renderWorldBlock(minX, minY, minZ, maxX, maxY, maxZ);
        }
    }

    protected void renderFace(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
        //Diagonal Guessing
        Tessellator tessellator = Tessellator.instance;
        IIcon iicon = icon;
        if (!isItem()) {
            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        }
        if (brightness > -1) {
            tessellator.setBrightness(brightness);
        }

        tessellator.setColorOpaque_F(rgb_red, rgb_green, rgb_blue);
        double d0 = iicon.getMinU();
        double d1 = iicon.getMinV();
        double d2 = iicon.getMaxU();
        double d3 = iicon.getMaxV();
        double d4 = 0.0625D;
        double d5 = x + 1 + x1;
        double d6 = x + 1 + x2;
        double d7 = x + 0 + x3;
        double d8 = x + 0 + x4;
        double d9 = z + 0 + z1;
        double d10 = z + 1 + z2;
        double d11 = z + 1 + z3;
        double d12 = z + 0 + z4;
        double d13 = y + d4 + y1;
        double d14 = y + d4 + y2;
        double d15 = y + d4 + y3;
        double d16 = y + d4 + y4;

        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);
    }

    protected void renderFluid(FluidStack fluid, int max, double scale, int xPlus, int yPlus, int zPlus) {
        int x2 = x + xPlus;
        int y2 = y + yPlus;
        int z2 = z + zPlus;

        Tessellator tessellator = Tessellator.instance;
        int color = block.colorMultiplier(world, x2, y2, z2);
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        double extra = (double) fluid.amount / max * scale;
        double height = 0.4D + extra;
        IIcon iconStill = fluid.getFluid().getIcon();

        height += RENDER_OFFSET;

        double u1, u2, u3, u4, v1, v2, v3, v4;
        u2 = iconStill.getInterpolatedU(0.0D);
        v2 = iconStill.getInterpolatedV(0.0D);
        u1 = u2;
        v1 = iconStill.getInterpolatedV(16.0D);
        u4 = iconStill.getInterpolatedU(16.0D);
        v4 = v1;
        u3 = u4;
        v3 = v2;

        tessellator.setBrightness(200);
        tessellator.setColorOpaque_F(LIGHT_Y_POS * red, LIGHT_Y_POS * green, LIGHT_Y_POS * blue);
        tessellator.addVertexWithUV(x2 + 0, y2 + height, z2 + 0, u2, v2);
        tessellator.addVertexWithUV(x2 + 0, y2 + height, z2 + 1, u1, v1);
        tessellator.addVertexWithUV(x2 + 1, y2 + height, z2 + 1, u4, v4);
        tessellator.addVertexWithUV(x2 + 1, y2 + height, z2 + 0, u3, v3);

        render.renderMinY = 0;
        render.renderMaxY = 1;
    }

    protected void renderFluidBlock(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        render.renderAllFaces = true;
        render.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
        render.renderStandardBlock(Blocks.lava, x, y, z);
        render.renderAllFaces = false;
    }

    private void renderWorldBlock(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        render.renderAllFaces = true;
        render.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
        render.renderStandardBlock(block, x, y, z);
        render.renderAllFaces = false;
    }

    protected void renderWorldBlockColored(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int color) {
        render.renderAllFaces = true;
        render.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
        renderWorldBlockColored(block, x, y, z, color);
        render.renderAllFaces = false;
    }

    public boolean renderWorldBlockColored(Block block, int x, int y, int z, int color) {
        int l = color;
        float f = (float) (l >> 16 & 255) / 255.0F;
        float f1 = (float) (l >> 8 & 255) / 255.0F;
        float f2 = (float) (l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable) {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        return Minecraft.isAmbientOcclusionEnabled() && block.getLightValue() == 0 ? (render.partialRenderBounds ? render.renderStandardBlockWithAmbientOcclusionPartial(block, x, y, z, f, f1, f2) : render.renderStandardBlockWithAmbientOcclusion(block, x, y, z, f, f1, f2)) : render.renderStandardBlockWithColorMultiplier(block, x, y, z, f, f1, f2);
    }

    protected void renderAngledBlock(double x2, double y2, double z2, double x3, double y3, double z3, double x1, double y1, double z1, double x4, double y4, double z4) {
        renderAngledBlock(x2, y2, z2, x3, y3, z3, x1, y1, z1, x4, y4, z4, 0D, 0D, 0D);
    }

    protected void renderAngledBlock(double x2, double y2, double z2, double x3, double y3, double z3, double x1, double y1, double z1, double x4, double y4, double z4, double xDim, double height, double zDim) {
        if (icon == null) return;
        renderFace(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
        renderFace(x1, y1 + height, z1, x2, y2 + height, z2, x3, y3 + height, z3, x4, y4 + height, z4);
        renderFace(x1, y1, z1, x2, y2, z2, x2 + 1, y2 + height, z2, x1 + 1, y1 + height, z1);
        renderFace(x4 - 1, y4, z4, x3 - 1, y3, z3, x3, y3 + height, z3, x4, y4 + height, z4);
        renderFace(x1, y1, z1, x1, y1 + height, z1 - 1, x4, y4 + height, z4 - 1, x4, y4, z4);
        renderFace(x2, y2, z2 + 1, x2, y2 + height, z2, x3, y3 + height, z3, x3, y3, z3 + 1);
    }

    private void renderItemBlock(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if (icon == null) return;
        render.renderMinX = minX;
        render.renderMinY = minY;
        render.renderMinZ = minZ;
        render.renderMaxX = maxX;
        render.renderMaxY = maxY;
        render.renderMaxZ = maxZ;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        render.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        render.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        render.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        render.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        render.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        render.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
    }
}
