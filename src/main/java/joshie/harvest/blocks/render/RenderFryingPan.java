package joshie.harvest.blocks.render;

import java.util.ArrayList;

import joshie.harvest.api.HFApi;
import joshie.harvest.blocks.tiles.TileCooking;
import joshie.harvest.core.util.RenderBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class RenderFryingPan extends RenderBase {
    @Override
    public void renderBlock() {
        if (!isItem()) {
            TileCooking tile = (TileCooking) world.getTileEntity(x, y, z);
            ArrayList<ItemStack> ingredients = tile.getIngredients();
            int max = ingredients.size();
            for (int i = 0; i < max; i++) {
                ItemStack ingredient = ingredients.get(i);
                Fluid fluid = HFApi.COOKING.getFluid(ingredient);
                if (fluid != null) {
                    setTexture(fluid.getStillIcon());
                    renderFluidBlock(0.08D, 0.1D, 0.08D, 0.94D, 0.132D + (i * 0.0001D), 0.95D);
                    render.clearOverrideBlockTexture();
                }
            }
        }

        renderBlock(0D, 0D, 0D, 0D, 0D, 0D);
    }
}