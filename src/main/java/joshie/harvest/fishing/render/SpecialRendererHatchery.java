package joshie.harvest.fishing.render;

import joshie.harvest.core.base.render.TileSpecialRendererItem;
import joshie.harvest.fishing.tile.TileHatchery;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class SpecialRendererHatchery extends TileSpecialRendererItem<TileHatchery> {
    @Override
    public void renderTileEntityAt(TileHatchery tile, double x, double y, double z, float tick, int destroyStage) {
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
}
