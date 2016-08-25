package joshie.harvest.cooking.recipe;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.ICookingIngredient;
import joshie.harvest.api.cooking.ISpecialRecipeHandler;
import joshie.harvest.cooking.Utensil;
import joshie.harvest.core.helpers.ToolHelper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeMayo implements ISpecialRecipeHandler {
    @Override
    public ItemStack getResult(Utensil utensil, List<ItemStack> stacks, List<ICookingIngredient> ingredients) {
        if (stacks.size() != 2) return null;
        boolean is0Oil = ToolHelper.isOil(stacks.get(0));
        ItemStack oil = is0Oil ? stacks.get(0) : stacks.get(1);
        ItemStack egg = is0Oil ? stacks.get(1) : stacks.get(0);
        if (HFApi.cooking.getCookingComponents(oil).contains(HFIngredients.OIL)) {
            if (HFApi.cooking.getCookingComponents(egg).contains(HFIngredients.EGG)) {
                return new ItemStack(HFAnimals.MAYONNAISE, 1, egg.getItemDamage());
            }
        }

        return null;
    }
}