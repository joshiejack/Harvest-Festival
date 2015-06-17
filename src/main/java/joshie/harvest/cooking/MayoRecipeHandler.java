package joshie.harvest.cooking;

import java.util.ArrayList;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.ISpecialRecipeHandler;
import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.items.HFItems;
import net.minecraft.item.ItemStack;

public class MayoRecipeHandler implements ISpecialRecipeHandler {
    @Override
    public ItemStack getResult(IUtensil utensil, ArrayList<ItemStack> ingredients) {
        if (utensil != Utensil.KITCHEN) return null;
        if (ingredients.size() != 2) return null;
        boolean is0Oil = ingredients.get(0).getItem() == HFItems.general;
        ItemStack oil = is0Oil ? ingredients.get(0) : ingredients.get(1);
        ItemStack egg = is0Oil ? ingredients.get(1) : ingredients.get(0);
        if (HFApi.COOKING.getCookingComponents(oil).contains(HFIngredients.oil)) {
            if (HFApi.COOKING.getCookingComponents(egg).contains(HFIngredients.egg)) {
                return new ItemStack(HFItems.mayonnaise, 1, egg.getItemDamage());
            }
        }

        return null;
    }
}
