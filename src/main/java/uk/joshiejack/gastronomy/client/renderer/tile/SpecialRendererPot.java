package uk.joshiejack.gastronomy.client.renderer.tile;

import uk.joshiejack.gastronomy.tile.TilePot;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
@PenguinLoader(value = "pot", side = Side.CLIENT)
public class SpecialRendererPot extends SpecialRendererCookware<TilePot> {
    @Override
    public void renderFluid(int i, ResourceLocation fluid) {
        renderFluidPlane(fluid, 0.5F, 0.125F + (i * 0.001F), 0.5F, 0.499F, 0.499F);
    }

    @Override
    public void translateItem(boolean isBlock, float position, float rotation, float offset1, float offset2) {
        GlStateManager.translate(0.5F, 0.05F, 0.5F);
        GlStateManager.scale(0.25F, 0.25F, 0.25F);
        if (!isBlock) {
            GlStateManager.rotate(-90, 1F, 0F, 0F);
            GlStateManager.rotate(rotation, 0.15F, 0.15F, 1F);
            GlStateManager.translate(offset1 * 1.4F, offset2 * 1.5F, position * 0.75F);
        } else {
            GlStateManager.rotate(90, 1F, 0F, 0F);
            GlStateManager.translate(offset1 * 1.4F, 0.8F - offset2 * 2.5F, position - 1F);
        }
    }

    @Override
    public void translateResult(TilePot pot, boolean isBlock) {
        GlStateManager.translate(0.5F, 0.075F, 0.5F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        if (!isBlock) {
            GlStateManager.rotate(-90, 1F, 0F, 0F);
        } else {
            GlStateManager.rotate(90, 0F, 1F, 0F);
        }
    }
}
