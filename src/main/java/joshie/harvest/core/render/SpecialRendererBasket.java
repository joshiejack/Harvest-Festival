package joshie.harvest.core.render;

import joshie.harvest.core.base.render.TileSpecialRendererItem;
import joshie.harvest.core.tile.TileBasket;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class SpecialRendererBasket extends TileSpecialRendererItem<TileBasket> {
    @Override
    public void render(@Nonnull TileBasket tile, double x, double y, double z, float tick, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        if (!tile.getStack().isEmpty()) {
            renderItem(tile.getStack(), 0F, 0F, 0F, 0F);
            renderItem(tile.getStack(), 1F, 0F, 0F, 0F);
            renderItem(tile.getStack(), 2F, 0F, 0F, 0F);
        }

        GlStateManager.popMatrix();
    }

    @Override
    protected void translateItem(boolean isBlock, float position, float rotation, float offset1, float offset2) {
        if (position == 0F) GlStateManager.translate(0.55F, 0.3F, 0.5F);
        else if (position == 1F) GlStateManager.translate(0.45F, 0.3F, 0.3F);
        else if (position == 2F) GlStateManager.translate(0.45F, 0.3F, -0.4F);
        GlStateManager.rotate(90, 0F, 0F, 1F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(rotation, 1F, 0F, 0F);
        GlStateManager.rotate(-90, 0F, 0F, 1F);
        GlStateManager.translate(offset1 * 3F, offset2 * 3.5F, position * 0.75F);
    }
}
