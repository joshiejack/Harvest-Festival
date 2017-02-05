package joshie.harvest.festivals.contest.render;

import joshie.harvest.core.base.render.TileSpecialRendererItem;
import joshie.harvest.festivals.contest.tile.TileStand;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class SpecialRendererStand extends TileSpecialRendererItem<TileStand> {
    @Override
    public void renderTileEntityAt(@Nonnull TileStand tile, double x, double y, double z, float tick, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        if (tile.getContents() != null) {
            renderItem(tile.getContents(), 0F, 0F, 0F, 0F);
        }

        GlStateManager.popMatrix();
    }

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
