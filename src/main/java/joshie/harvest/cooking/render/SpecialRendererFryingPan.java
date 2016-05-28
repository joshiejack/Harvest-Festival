package joshie.harvest.cooking.render;

import joshie.harvest.cooking.blocks.TileFryingPan;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SpecialRendererFryingPan extends SpecialRendererCookware<TileFryingPan> {
    @Override
    public void renderFluid(int i, World world, ResourceLocation fluid) {
        renderFluid(fluid, 0.5F, 0.065F + (i * 0.001F), 0.5F, 0.499F);
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