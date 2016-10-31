package joshie.harvest.cooking.render;

import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.cooking.tile.TileCooking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.FIXED;
import static net.minecraft.client.renderer.texture.TextureMap.LOCATION_BLOCKS_TEXTURE;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_TEX;

@SideOnly(Side.CLIENT)
public abstract class SpecialRendererCookware<T extends TileCooking> extends TileEntitySpecialRenderer<T> {
    public static final Minecraft MINECRAFT = Minecraft.getMinecraft();

    @Override
    public void renderTileEntityAt(T tile, double x, double y, double z, float tick, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        renderCookware(tile);
        GlStateManager.popMatrix();
    }

    protected void renderCookware(T tile) {
        ArrayList<ItemStack> ingredients = tile.getIngredients();
        List<ItemStack> results = tile.getResult();
        if (results != null) {
            for (ItemStack result: results) {
                renderResult(tile, result);
            }
        }

        int fluidId = 0;
        int max = ingredients.size();
        for (int i = 0; i < max; i++) {
            ItemStack ingredient = ingredients.get(i);
            ResourceLocation fluid = CookingAPI.INSTANCE.getFluid(ingredient);
            if (fluid == null) {
                renderIngredient(ingredient, tile.heightOffset[i], tile.rotations[i], tile.offset1[i], tile.offset2[i]);
            } else {
                renderFluid(fluidId, tile.getWorld(), fluid);
                fluidId++;
            }
        }
    }

    public void renderFluid(int i, World world, ResourceLocation fluid) {}

    public abstract void translateIngredient(boolean isBlock, float position, float rotation, float offset1, float offset2);
    public void translateResult(boolean isBlock) {}

    public void translateResult(T t, boolean isBlock) {
        translateResult(isBlock);
    }

    protected void renderIngredient(ItemStack stack, float position, float rotation, float offset1, float offset2) {
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (Minecraft.isAmbientOcclusionEnabled())  {
            GL11.glShadeModel(GL11.GL_SMOOTH);
        } else  {
            GL11.glShadeModel(GL11.GL_FLAT);
        }

        translateIngredient(stack.getItem() instanceof ItemBlock, position, rotation, offset1, offset2);
        GlStateManager.blendFunc(GL11.GL_CONSTANT_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
        GL14.glBlendColor(1F, 1F, 1F, 1F);
        MINECRAFT.getRenderItem().renderItem(stack, FIXED);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    protected void renderResult(T t, ItemStack stack) {
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (Minecraft.isAmbientOcclusionEnabled())  {
            GL11.glShadeModel(GL11.GL_SMOOTH);
        } else  {
            GL11.glShadeModel(GL11.GL_FLAT);
        }

        translateResult(t, stack.getItem() instanceof ItemBlock);
        GlStateManager.blendFunc(GL11.GL_CONSTANT_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
        GL14.glBlendColor(1F, 1F, 1F, 1F);
        MINECRAFT.getRenderItem().renderItem(stack, FIXED);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    protected void renderFluidPlane(ResourceLocation fluid, float x, float y, float z, float size) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vb = tessellator.getBuffer();
        TextureAtlasSprite sprite = MINECRAFT.getTextureMapBlocks().getTextureExtry(fluid.toString());
        if (sprite != null) {
            MINECRAFT.renderEngine.bindTexture(LOCATION_BLOCKS_TEXTURE);
            double uMin = (double) sprite.getMinU();
            double uMax = (double) sprite.getMaxU();
            double vMin = (double) sprite.getMinV();
            double vMax = (double) sprite.getMaxV();

            vb.begin(7, POSITION_TEX);
            vb.pos(size / 2f, 0, size / 2f).tex(uMax, vMax).endVertex();
            vb.pos(size / 2f, 0, -size / 2f).tex(uMax, vMin).endVertex();
            vb.pos(-size / 2f, 0, -size / 2f).tex(uMin, vMin).endVertex();
            vb.pos(-size / 2f, 0, size / 2f).tex(uMin, vMax).endVertex();
            tessellator.draw();
        }

        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    protected void renderFluidCube(ResourceLocation fluid, float x, float y, float z, float size) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vb = tessellator.getBuffer();
        TextureAtlasSprite sprite = MINECRAFT.getTextureMapBlocks().getTextureExtry(fluid.toString());
        if (sprite != null) {
            MINECRAFT.renderEngine.bindTexture(LOCATION_BLOCKS_TEXTURE);
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
}
