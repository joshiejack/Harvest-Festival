package joshie.harvest.api.cooking;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/** Implement this on items that should handle recipes in a non
 *  default, special way. */
public interface ISpecialRecipeHandler {
    /** Returns the result of this recipe for this item **/
    public ItemStack getResult(IUtensil utensil, ArrayList<ItemStack> ingredients);
}