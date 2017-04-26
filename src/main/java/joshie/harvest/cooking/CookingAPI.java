package joshie.harvest.cooking;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.*;
import joshie.harvest.cooking.recipe.RecipeMaker;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.core.util.holders.HolderRegistryMulti;
import joshie.harvest.core.util.holders.ItemStackHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFApiImplementation
public class CookingAPI implements CookingManager {
    public static final CookingAPI INSTANCE = new CookingAPI();
    private final Set<ItemStackHolder> knives = new HashSet<>();
    private final Set<CookingHandler> cookingHandlers = new HashSet<>();
    private final HolderRegistryMulti<Ingredient> ingredientRegistry = new HolderRegistryMulti<Ingredient>() {
        @Override
        public boolean isEqual(Ingredient r1, Ingredient r2) {
            for (Ingredient i : r1.getEquivalents()) { //Return true if the item passed in matches this one
                if (i.getUnlocalized().equals(r2.getUnlocalized()))
                    return true; //Loops the equivalents list, this item is contained in that list by default
            }

            return false;

        }
    };

    private CookingAPI() {
    }

    //Return the stacks, doesn't need cache as the result is cached
    public NonNullList<ItemStack> getStacksForIngredient(Ingredient ingredient) {
        return ingredientRegistry.getStacksFor(ingredient);
    }

    @Override
    public void register(@Nonnull ItemStack stack, Ingredient ingredient) {
        if (stack.isEmpty() || ingredient == null) return; //Fail silently
        ingredientRegistry.register(stack, ingredient);
        ingredient.onStackAdded(HFApi.shipping.getSellValue(stack));
    }

    @Override
    public void registerKnife(@Nonnull ItemStack stack) {
        knives.add(ItemStackHolder.of(stack));
    }

    @Override
    public void registerCookingHandler(CookingHandler handler) {
        cookingHandlers.add(handler);
    }

    public Ingredient getCookingComponents(@Nonnull ItemStack stack) {
        return ingredientRegistry.getValueOf(stack);
    }

    @Override
    public ResourceLocation getFluid(@Nonnull ItemStack ingredient) {
        Ingredient components = getCookingComponents(ingredient);
        return components == null ? null : components.getFluid();
    }

    @Override
    @Nonnull
    public ItemStack getBestMeal(String string) {
        ResourceLocation location = string.contains(":") ? new ResourceLocation(string) : new ResourceLocation(MODID, string);
        for (Recipe recipe : Recipe.REGISTRY.values()) {
            if (recipe.getResource().equals(location)) {
                ArrayList<IngredientStack> stacks = new ArrayList<>();
                stacks.addAll(recipe.getRequired());
                if (recipe.getOptional().size() > 0) stacks.addAll(recipe.getOptional());
                return RecipeMaker.BUILDER.build(recipe, stacks).get(0);
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack getMeal(String string) {
        ResourceLocation location = string.contains(":") ? new ResourceLocation(string) : new ResourceLocation(MODID, string);
        for (Recipe recipe : Recipe.REGISTRY.values()) {
            if (recipe.getResource().equals(location)) {
                return CookingHelper.makeRecipe(recipe);
            }
        }

        return ItemStack.EMPTY;
    }

    private IngredientStack toIngredientStack(@Nonnull ItemStack item) {
        return new IngredientStack(ingredientRegistry.getValueOf(item));
    }

    private List<IngredientStack> toIngredients(NonNullList<ItemStack> stacks) {
        return stacks.stream().map(this::toIngredientStack).collect(Collectors.toList());
    }

    @Override
    public NonNullList<ItemStack> getCookingResult(Utensil utensil, NonNullList<ItemStack> stacks) {
        List<IngredientStack> ingredients = toIngredients(stacks);
        for (CookingHandler handler : cookingHandlers) {
            NonNullList<ItemStack> ret = handler.getResult(utensil, stacks, ingredients);
            if (ret != null) {
                return ret;
            }
        }

        return NonNullList.withSize(1, utensil.getBurntItem().copy());
    }

    @Override
    public boolean isIngredient(@Nonnull ItemStack stack) {
        return getCookingComponents(stack) != null;
    }

    @Override
    public boolean isKnife(@Nonnull ItemStack stack) {
        return knives.contains(ItemStackHolder.of(stack)) || knives.contains(ItemStackHolder.of(stack.getItem(), OreDictionary.WILDCARD_VALUE));
    }
}