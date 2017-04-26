package joshie.harvest.api.cooking;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;
import java.util.List;

public interface CookingHandler {
    /** fetch the resulting stack for this recipe
     * @param utensil       the utensil being used
     * @param stacks        the original stack list
     * @param ingredients   the list of ingredients
     * @return the resulting item */
    @Nullable
    NonNullList<ItemStack> getResult(Utensil utensil, NonNullList<ItemStack> stacks, List<IngredientStack> ingredients);
}