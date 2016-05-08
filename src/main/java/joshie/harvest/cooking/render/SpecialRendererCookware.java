package joshie.harvest.cooking.render;

import joshie.harvest.api.HFApi;
import joshie.harvest.blocks.tiles.TileCooking;
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

import static net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.FIXED;
import static net.minecraft.client.renderer.texture.TextureMap.LOCATION_BLOCKS_TEXTURE;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_TEX;

@SideOnly(Side.CLIENT)
public abstract class SpecialRendererCookware<T extends TileCooking> extends TileEntitySpecialRenderer<T> {
    public static final Minecraft MINECRAFT = Minecraft.getMinecraft();

    @Override
    public final void renderTileEntityAt(T tile, double x, double y, double z, float tick, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        renderCookware(tile);
        GlStateManager.popMatrix();
    }

    protected void renderCookware(T tile) {
        ArrayList<ItemStack> ingredients = tile.getIngredients();
        ItemStack result = tile.getResult();
        if (result != null) {
            renderResult(result);
        }

        int max = ingredients.size();
        for (int i = 0; i < max; i++) {
            ItemStack ingredient = ingredients.get(i);
            ResourceLocation fluid = HFApi.COOKING.getFluid(ingredient);
            if (fluid == null) {
                renderIngredient(ingredient, tile.heightOffset[i], tile.rotations[i], tile.offset1[i], tile.offset2[i]);
            } else renderFluid(i, tile.getWorld(), fluid);
        }
    }

    public void renderFluid(int i, World world, ResourceLocation fluid) {}

    public abstract void translateIngredient(boolean isBlock, float position, float rotation, float offset1, float offset2);
    public abstract void translateResult(boolean isBlock);

    private void renderIngredient(ItemStack stack, float position, float rotation, float offset1, float offset2) {
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
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, FIXED);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void renderResult(ItemStack stack) {
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (Minecraft.isAmbientOcclusionEnabled())  {
            GL11.glShadeModel(GL11.GL_SMOOTH);
        } else  {
            GL11.glShadeModel(GL11.GL_FLAT);
        }

        translateResult(stack.getItem() instanceof ItemBlock);
        GlStateManager.blendFunc(GL11.GL_CONSTANT_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
        GL14.glBlendColor(1F, 1F, 1F, 1F);
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, FIXED);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    protected void renderFluid(ResourceLocation fluid, float x, float y, float z, float size) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer wr = tessellator.getBuffer();
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.toString());
        if (sprite != null) {
            MINECRAFT.renderEngine.bindTexture(LOCATION_BLOCKS_TEXTURE);
            double uMin = (double) sprite.getMinU();
            double uMax = (double) sprite.getMaxU();
            double vMin = (double) sprite.getMinV();
            double vMax = (double) sprite.getMaxV();

            wr.begin(7, POSITION_TEX);
            wr.pos(size / 2f, 0, size / 2f).tex(uMax, vMax).endVertex();
            wr.pos(size / 2f, 0, -size / 2f).tex(uMax, vMin).endVertex();
            wr.pos(-size / 2f, 0, -size / 2f).tex(uMin, vMin).endVertex();
            wr.pos(-size / 2f, 0, size / 2f).tex(uMin, vMax).endVertex();

            tessellator.draw();
        }

        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
