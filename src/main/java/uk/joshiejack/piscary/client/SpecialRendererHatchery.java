package uk.joshiejack.piscary.client;

import uk.joshiejack.penguinlib.client.renderer.tile.SpecialRendererItem;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.piscary.tile.TileHatchery;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
@PenguinLoader(value = "hatchery", side = Side.CLIENT)
public class SpecialRendererHatchery extends SpecialRendererItem<TileHatchery> {
    @Override
    public void render(@Nonnull TileHatchery tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        ItemStack fish = tile.getStack();
        if (!fish.isEmpty()) {
            for (int i = 0; i < fish.getCount(); i++) {
                renderItem(fish, tile.getRenderData(), i);
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
