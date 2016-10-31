package joshie.harvest.cooking;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.*;
import joshie.harvest.cooking.recipe.*;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.core.util.holders.HolderRegistry;
import joshie.harvest.core.util.holders.HolderRegistryMulti;
import joshie.harvest.core.util.holders.ItemStackHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

import static joshie.harvest.cooking.recipe.RecipeHelper.toIngredientStacks;
import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFApiImplementation
public class CookingAPI implements CookingManager {
    //public static final IForgeRegistry<MealImpl> REGISTRY = new RegistryBuilder<MealImpl>().setName(new ResourceLocation("harvestfestival", "meals")).setType(MealImpl.class).setIDRange(0, 32000).create();
    public static final CookingAPI INSTANCE = new CookingAPI();
    private final Set<RecipeHandler> handlers = new HashSet<>();
    private final Set<ItemStackHolder> knives = new HashSet<>();
    private final Set<CookingHandler> cookingHandlers = new HashSet<>();
    private final HolderRegistry<ItemStack> alternatives = new HolderRegistry<>();
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

    private CookingAPI(){}

    //Return the stacks, doesn't need cache as the result is cached
    public List<ItemStack> getStacksForIngredient(Ingredient ingredient) {
        return ingredientRegistry.getStacksFor(ingredient);
    }

    public ItemStack getRealStackForItem(ItemStack stack) {
        ItemStack ret = alternatives.getValueOf(stack);
        return ret == null ? ret : stack;
    }

    @Override
    public void register(ItemStack stack, Ingredient ingredient) {
        if (stack == null || ingredient == null) return; //Fail silently
        ingredientRegistry.register(stack, ingredient);
        ingredient.onStackAdded(HFApi.shipping.getSellValue(stack));
    }

    @Override
    public void registerKnife(ItemStack stack) {
        knives.add(ItemStackHolder.of(stack));
    }

    @Override
    @Deprecated
    public void registerRecipeHandler(RecipeHandler handler) {
        handlers.add(handler);
    }

    @Override
    public void registerCookingHandler(CookingHandler handler) {
        cookingHandlers.add(handler);
    }

    public Ingredient getCookingComponents(ItemStack stack) {
        Ingredient result = ingredientRegistry.getValueOf(stack);
        return result != null ? result : null;
    }

    @Override
    public ResourceLocation getFluid(ItemStack ingredient) {
        Ingredient components = getCookingComponents(ingredient);
        return components == null ? null : components.getFluid();
    }

    @Override
    public Meal addMeal(ResourceLocation key, Utensil utensil, int hunger, float saturation, int eatTimer, Ingredient... components) {
        Recipe recipe = new RecipeHF(utensil, hunger, saturation, toIngredientStacks(components));
        recipe.setRegistryName(key);
        Recipe.REGISTRY.register(recipe);
        return recipe;
    }

    @Override
    @Deprecated
    public void addRecipe(ItemStack output, Utensil utensil, Ingredient... ingredients) {
        Recipe recipe = new RecipeVanilla(output, utensil, toIngredientStacks(ingredients));
        recipe.setRegistryName(new ResourceLocation(MODID, output.getUnlocalizedName()));
        Recipe.REGISTRY.register(recipe);
    }

    @Override
    public Recipe addBasicRecipe(ResourceLocation key, Utensil utensil, ItemStack output, IngredientStack... ingredients) {
        Recipe recipe = new RecipeVanilla(output, utensil, ingredients);
        recipe.setRegistryName(key);
        Recipe.REGISTRY.register(recipe);
        return recipe;
    }

    @Override
    public ItemStack getBestMeal(String string) {
        ResourceLocation location = string.contains(":") ? new ResourceLocation(string) : new ResourceLocation(MODID, string);
        for (Recipe recipe : Recipe.REGISTRY.getValues()) {
            if (recipe.getRegistryName().equals(location)) {
                ArrayList<IngredientStack> stacks = new ArrayList<>();
                stacks.addAll(recipe.getRequired());
                if (recipe.getOptional().size() > 0)stacks.addAll(recipe.getOptional());
                return RecipeMaker.BUILDER.build(recipe, stacks).get(0);
            }
        }

        return null;
    }

    @Override
    public ItemStack getMeal(String string) {
        ResourceLocation location = string.contains(":") ? new ResourceLocation(string) : new ResourceLocation(MODID, string);
        for (Recipe recipe : Recipe.REGISTRY.getValues()) {
            if (recipe.getRegistryName().equals(location)) {
                return CookingHelper.makeRecipe(recipe);
            }
        }

        return null;
    }

    private IngredientStack toIngredientStack(ItemStack item) {
        return new IngredientStack(ingredientRegistry.getValueOf(item));
    }

    private List<IngredientStack> toIngredients(List<ItemStack> stacks) {
        List<IngredientStack> ingredients = new ArrayList<>();
        for (ItemStack stack: stacks) {
            ingredients.add(toIngredientStack(stack));
        }

        return ingredients;
    }

    @Override
    public List<ItemStack> getCookingResult(Utensil utensil, List<ItemStack> stacks) {
        List<IngredientStack> ingredients = toIngredients(stacks);
        for (CookingHandler handler: cookingHandlers) {
            List<ItemStack> ret = handler.getResult(utensil, stacks, ingredients);
            if (ret != null) {
                return ret;
            }
        }

        return Collections.singletonList(getResult(utensil, stacks));
    }

    @Override
    public ItemStack getResult(Utensil utensil, List<ItemStack> stacks) {
        //TODO Remove in 0.7+ from here
        //Convert all the stacks in to their relevant ingredients
        List<Ingredient> components = new ArrayList<>();
        for (ItemStack stack : stacks) {
            Ingredient ingredient = getCookingComponents(stack);
            if (!components.contains(ingredient)) components.add(ingredient);
        }

        //Check the special recipes first
        for (RecipeHandler recipe : handlers) {
            ItemStack ret = recipe.getResult(utensil, stacks, components);
            if (ret != null) {
                return ret;
            }
        }

        //TODO Remove in 0.7+ to here

        return new ItemStack(HFCooking.MEAL, 1, utensil.getIndex());
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
        return getCookingComponents(stack) != null;
    }

    @Override
    public boolean isKnife(ItemStack stack) {
        return knives.contains(ItemStackHolder.of(stack)) || knives.contains(ItemStackHolder.of(stack.getItem(), OreDictionary.WILDCARD_VALUE));
    }
}
