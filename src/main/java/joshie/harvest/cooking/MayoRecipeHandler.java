package joshie.harvest.cooking;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.ISpecialRecipeHandler;
import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.items.HFItems;
import net.minecraft.item.ItemStack;

import java.util.List;

public class MayoRecipeHandler implements ISpecialRecipeHandler {
    @Override
    public ItemStack getResult(IUtensil utensil, List<ItemStack> ingredients) {
        if (utensil != Utensil.COUNTER) return null;
        if (ingredients.size() != 2) return null;
        boolean is0Oil = ingredients.get(0).getItem() == HFItems.GENERAL;
        ItemStack oil = is0Oil ? ingredients.get(0) : ingredients.get(1);
        ItemStack egg = is0Oil ? ingredients.get(1) : ingredients.get(0);
        if (HFApi.cooking.getCookingComponents(oil).contains(HFIngredients.oil)) {
            if (HFApi.cooking.getCookingComponents(egg).contains(HFIngredients.egg)) {
                return new ItemStack(HFAnimals.MAYONNAISE, 1, egg.getItemDamage());
            }
        }

        return null;
    }
}