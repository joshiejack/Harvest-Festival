package uk.joshiejack.gastronomy.client.renderer.tile;

import uk.joshiejack.gastronomy.tile.base.TileCooking;
import uk.joshiejack.penguinlib.client.renderer.tile.SpecialRendererItem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public abstract class SpecialRendererCookware<T extends TileCooking> extends SpecialRendererItem<T> {
    @Override
    public void translateItem(boolean isBlock, float position, float rotation, float offset1, float offset2) {
        GlStateManager.translate(0.5F, 0.11F, 0.5F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
        if (!isBlock) {
            GlStateManager.rotate(-90, 1F, 0F, 0F);
            GlStateManager.rotate(rotation, 0F, 0F, 1F);
            GlStateManager.translate(offset1, offset2, position);
        } else {
            GlStateManager.rotate(90, 1F, 0F, 0F);
            GlStateManager.translate(offset1 * 1.4F, 0.8F - offset2 * 2.5F, position - 1F);
        }
    }

    public void translateResult(T cookware, boolean isBlock) {
        GlStateManager.translate(0.5F, 0.32F, 0.5F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
        if (!isBlock) {
            GlStateManager.rotate(-90, 1F, 0F, 0F);
        } else {
            GlStateManager.rotate(90, 0F, 1F, 0F);
        }
    }

    protected void renderFluid(int id, ResourceLocation fluid) {}

    @Override
    public void render(T cookware, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        renderCookware(cookware); //Additional rendering
        ItemStack result = cookware.getHandler().getStackInSlot(TileCooking.FINISH_SLOT);
        if (!result.isEmpty()) {
            renderItem(result, () -> translateResult(cookware, result.getItem() instanceof ItemBlock));
        } else {
            for (int i = 0; i < TileCooking.FINISH_SLOT; i++) {
                ItemStack stack = cookware.getHandler().getStackInSlot(i);
                if (!stack.isEmpty()) {
                    renderItem(stack, cookware.getRenderData(), i);
                }
            }
        }

        List<FluidStack> fluids = cookware.getFluids();
        for (int i = 0; i < fluids.size(); i++) {
            renderFluid(i * 10, fluids.get(i).getFluid().getStill());
        }

        GlStateManager.popMatrix();
    }

    protected void renderCookware(T cookware) { }
}
