package uk.joshiejack.gastronomy.client.renderer.tile;

import uk.joshiejack.gastronomy.tile.TileCounter;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
@PenguinLoader(value = "counter", side = Side.CLIENT)
public class SpecialRendererCounter extends SpecialRendererCookware<TileCounter> {
    @Override
    public void renderFluid(int i, ResourceLocation fluid) {
        renderFluidPlane(fluid, 0.5F, 1.0125F + (i * 0.0001F), 0.5F, 0.825F, 0.725F);
    }

    @Override
    public void translateItem(boolean isBlock, float position, float rotation, float offset1, float offset2) {
        GlStateManager.translate(0.5F, 0.75F, 0.5F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
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
    public void translateResult(TileCounter counter, boolean isBlock) {
        GlStateManager.translate(0.5F, 1.025F, 0.5F);
        if (!isBlock) {
            GlStateManager.rotate(-90, 1F, 0F, 0F);
        } else {
            GlStateManager.rotate(90, 0F, 1F, 0F);
        }
    }
}
