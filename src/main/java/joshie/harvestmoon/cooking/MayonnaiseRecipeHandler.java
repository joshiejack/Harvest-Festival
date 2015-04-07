package joshie.harvestmoon.cooking;

import java.util.ArrayList;

import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.cooking.ISpecialRecipeHandler;
import joshie.harvestmoon.api.cooking.IUtensil;
import joshie.harvestmoon.init.HMIngredients;
import joshie.harvestmoon.init.HMItems;
import net.minecraft.item.ItemStack;

public class MayonnaiseRecipeHandler implements ISpecialRecipeHandler {
    @Override
    public ItemStack getResult(IUtensil utensil, ArrayList<ItemStack> ingredients) {
        if (utensil != Utensil.KITCHEN) return null;
        if (ingredients.size() != 2) return null;
        boolean is0Oil = ingredients.get(0).getItem() == HMItems.general;
        ItemStack oil = is0Oil ? ingredients.get(0) : ingredients.get(1);
        ItemStack egg = is0Oil ? ingredients.get(1) : ingredients.get(0);
        if (HMApi.COOKING.getCookingComponents(oil).contains(HMIngredients.oil)) {
            if (HMApi.COOKING.getCookingComponents(egg).contains(HMIngredients.egg)) {
                return new ItemStack(HMItems.mayonnaise, 1, egg.getItemDamage());
            }
        }

        return null;
    }
}
