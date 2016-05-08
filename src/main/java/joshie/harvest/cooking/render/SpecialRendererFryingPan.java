package joshie.harvest.cooking.render;

import joshie.harvest.blocks.tiles.TileFryingPan;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import static net.minecraft.client.renderer.texture.TextureMap.LOCATION_BLOCKS_TEXTURE;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_TEX;

@SideOnly(Side.CLIENT)
public class SpecialRendererFryingPan extends SpecialRendererCookware<TileFryingPan> {
    @Override
    public void renderFluid(int i, World world, ResourceLocation fluid) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5F, 0.065F, 0.5F);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer wr = tessellator.getBuffer();

        float size = 0.499f;
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

    @Override
    public void translateIngredient(boolean isBlock, float position, float rotation, float offset1, float offset2) {
        GlStateManager.translate(0.5F, -0.05F, 0.5F);
        GlStateManager.scale(0.25F, 0.25F, 0.25F);
        if (!isBlock) {
            GlStateManager.rotate(-90, 1F, 0F, 0F);
            GlStateManager.rotate(rotation, 0F, 0F, 1F);
            GlStateManager.translate(offset1, offset2, position);
        } else {
            GlStateManager.rotate(90, 1F, 0F, 0F);
            GlStateManager.translate(offset1 * 1.4F, 0.8F - offset2 * 2.5F, position - 1F);
        }
    }

    @Override
    public void translateResult(boolean isBlock) {
        GlStateManager.translate(0.5F, 0.075F, 0.5F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        if (!isBlock) {
            GlStateManager.rotate(-90, 1F, 0F, 0F);
        } else {
            GlStateManager.rotate(90, 0F, 1F, 0F);
        }
    }
}