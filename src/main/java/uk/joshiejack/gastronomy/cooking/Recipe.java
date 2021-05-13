package uk.joshiejack.gastronomy.cooking;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import uk.joshiejack.gastronomy.api.Appliance;
import uk.joshiejack.penguinlib.data.holder.HolderMeta;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Recipe {
    public static final Multimap<Appliance, Recipe> RECIPES = HashMultimap.create();
    public static final Multimap<Appliance, Recipe> PRIORITY_RECIPES = HashMultimap.create();
    public static final Map<HolderMeta, Recipe> RECIPE_BY_STACK = Maps.newHashMap();
    public static final Map<String, ItemStack> DISHES = Maps.newHashMap();
    private final NonNullList<IngredientStack> required = NonNullList.create();
    private final ItemStack result;
    private final ItemStack dish;

    public Recipe (ItemStack result, ItemStack dish, IngredientStack... ingredients) {
        this.result = result;
        this.dish = dish;
        RECIPE_BY_STACK.put(new HolderMeta(result), this);
        Collections.addAll(required, ingredients);
    }

    public NonNullList<IngredientStack> getRequired() {
        return required;
    }

    public ItemStack getResult() {
        return result;
    }

    public ItemStack getDish() {
        return dish;
    }

    private int getMealCount(List<IngredientStack> ingredients) {
        int count = 0;
        for (int t = 0; t < 1000; t++) { //Fuck while loop
            requiredLoop:
            for (IngredientStack requiredStack : required) {
                //Loop through the ingredients to find a match and shrink when appropriate
                int found = 0; //We need to match the required amount, if we have we can exit, otherwise we continue
                for (IngredientStack ingredientStack : ingredients) {
                    if (ingredientStack.matches(requiredStack)) {
                        int take = Math.min(requiredStack.getAmount(), ingredientStack.getAmount());
                        found += take; //We found this many
                        ingredientStack.shrink(take); //Decrease the volume
                        if (found >= requiredStack.getAmount()) {
                            continue requiredLoop;
                        }
                    }
                }

                return count; //We didn't find the right amount anymore so exit
            }

            count++;
        }

        return count;
    }

    /** @return ItemStack.EMPTY if we can't cook anything **/
    public ItemStack cook(List<IngredientStack> ingredients) {
        //Ensure that EVERY required item is in the ingredients
        requiredLoop:
        for (IngredientStack requiredStack: required) {
            int found = 0;
            for (IngredientStack ingredientStack: ingredients) {
                if (ingredientStack.matches(requiredStack)) {
                    found += ingredientStack.getAmount();
                }

                if (found >= requiredStack.getAmount()) continue requiredLoop;
            }

            return ItemStack.EMPTY;
        }

        ingredientLoop:
        for (IngredientStack ingredientStack: ingredients) {
            for (IngredientStack requiredStack: required) {
                if (ingredientStack.matches(requiredStack)) continue ingredientLoop;
            }

            return ItemStack.EMPTY;
        }

        ItemStack ret = result.copy();
        ret.setCount(getMealCount(ingredients));
        return ret;
    }
}
