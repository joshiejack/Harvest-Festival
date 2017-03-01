package joshie.harvest.shops.purchasable;

import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.cooking.CookingHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.core.registry.ShippingRegistry.SELL_VALUE;

public class PurchasableMeal extends Purchasable {
    protected final Recipe recipe;

    public PurchasableMeal(long cost, ResourceLocation resource) {
        this.recipe = Recipe.REGISTRY.get(resource);
        this.cost = cost;
        this.resource = ((cost >= 0) ? "buy:" : "sell:") + resource.toString().replace(":", "_");
    }

    @Override
    public ItemStack getDisplayStack() {
        if (stack == null) {
            stack = CookingHelper.makeRecipe(recipe);
            if (stack.getTagCompound() != null) {
                stack.getTagCompound().setLong(SELL_VALUE, 0L);
            }
        }

        return stack;
    }
}
