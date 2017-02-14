package joshie.harvest.core.render;

import joshie.harvest.core.tile.TileCookingStand;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SpecialRendererCookingStand extends SpecialRendererStand<TileCookingStand> {
    @Override
    protected void translateItem(boolean isBlock, float position, float rotation, float offset1, float offset2) {
        GlStateManager.translate(0.5F, 0.97F, 0.5F);
        GlStateManager.rotate(90, 0F, 0F, 1F);
        GlStateManager.scale(0.9F, 0.9F, 0.9F);
        GlStateManager.rotate(rotation, 0F, 1F, 0F);
        GlStateManager.rotate(-90, 0F, 1F, 0F);
        GlStateManager.translate(offset1 * 3F, offset2 * 3.5F, position * 0.75F);
    }
}
