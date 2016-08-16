package joshie.harvest.api.cooking;

import joshie.harvest.cooking.Utensil;
import net.minecraft.item.ItemStack;

import java.util.List;

/** Implement this on items that should handle recipes in a non
 *  default, special way. */
public interface ISpecialRecipeHandler {
    /** Returns the result of this recipe for this item **/
    ItemStack getResult(Utensil utensil, List<ItemStack> ingredients);
}