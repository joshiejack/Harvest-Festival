package joshie.harvestmoon.api.cooking;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

/** Implement this on items that should handle recipes in a non
 *  default, special way. */
public interface ISpecialRecipeHandler {
    /** Returns the result of this recipe for this item **/
    public ItemStack getResult(IUtensil utensil, ArrayList<ItemStack> ingredients);
}
