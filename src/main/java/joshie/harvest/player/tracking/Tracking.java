package joshie.harvest.player.tracking;

import joshie.harvest.cooking.recipe.MealImpl;
import joshie.harvest.core.util.holders.ItemStackHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class Tracking {
    protected Set<ItemStackHolder> obtained = new HashSet<>(); //Items that have been obtained
    protected Set<ResourceLocation> recipes = new HashSet<>(); //Recipe Learnt

    public boolean learnRecipe(MealImpl recipe) {
        return recipes.add(recipe.getRegistryName());
    }

    public void addAsObtained(ItemStack stack) {
        obtained.add(ItemStackHolder.of(stack));
    }

    public boolean hasObtainedItem(ItemStack stack) {
        for (ItemStackHolder holder: obtained) {
            if (holder.matches(stack)) return true;
        }

        return false;
    }
}