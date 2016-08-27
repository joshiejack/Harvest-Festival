package joshie.harvest.cooking.render;

import joshie.harvest.cooking.blocks.TileCounter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class SpecialRendererCounter extends SpecialRendererCookware<TileCounter> {
    @Override
    protected void renderCookware(TileCounter tile) {
        ArrayList<ItemStack> ingredients = tile.getIngredients();
        ItemStack result = tile.getResult();
        if (result != null) {
            renderResult(tile, result);
        }

        int max = ingredients.size();
        for (int i = 0; i < max; i++) {
            renderIngredient(ingredients.get(i), tile.heightOffset[i], tile.rotations[i], tile.offset1[i], tile.offset2[i]);
        }
    }

    @Override
    public void translateIngredient(boolean isBlock, float position, float rotation, float offset1, float offset2) {
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
    public void translateResult(boolean isBlock) {
        GlStateManager.translate(0.5F, 1.025F, 0.5F);
        if (!isBlock) {
            GlStateManager.rotate(-90, 1F, 0F, 0F);
        } else {
            GlStateManager.rotate(90, 0F, 1F, 0F);
        }
    }
}