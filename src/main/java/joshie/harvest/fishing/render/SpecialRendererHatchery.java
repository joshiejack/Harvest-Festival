package joshie.harvest.fishing.render;

import joshie.harvest.core.base.render.TileSpecialRendererItem;
import joshie.harvest.fishing.tile.TileHatchery;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

@SideOnly(Side.CLIENT)
public class SpecialRendererHatchery extends TileSpecialRendererItem<TileHatchery> {
    @Override
    public void render(@Nonnull TileHatchery tile, double x, double y, double z, float tick, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        List<ItemStack> fish = tile.getFish();
        if (fish.size() > 0) {
            for (int i = 0; i < fish.size(); i++) {
                renderItem(fish.get(i), tile.render, i);
            }
        }

        GlStateManager.popMatrix();
    }

    @Override
    protected void translateItem(boolean isBlock, float position, float rotation, float offset1, float offset2) {
        GlStateManager.translate(0.5F, -1.05F, 0.5F);
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
}
