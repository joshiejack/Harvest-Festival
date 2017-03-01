package joshie.harvest.cooking;

import com.google.common.collect.Lists;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.*;
import joshie.harvest.cooking.recipe.RecipeMaker;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.core.util.holders.HolderRegistryMulti;
import joshie.harvest.core.util.holders.ItemStackHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.stream.Collectors;

import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFApiImplementation
public class CookingAPI implements CookingManager {
    //TODO: Remove in 0.7+
    public static final IForgeRegistry<Recipe> REGISTRY = new RegistryBuilder<Recipe>().setName(new ResourceLocation("harvestfestival", "meals")).setType(Recipe.class).setIDRange(0, 32000).create();
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

    private CookingAPI(){}

    //Return the stacks, doesn't need cache as the result is cached
    public List<ItemStack> getStacksForIngredient(Ingredient ingredient) {
        return ingredientRegistry.getStacksFor(ingredient);
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
    public ItemStack getBestMeal(String string) {
        ResourceLocation location = string.contains(":") ? new ResourceLocation(string) : new ResourceLocation(MODID, string);
        for (Recipe recipe : Recipe.REGISTRY.values()) {
            if (recipe.getResource().equals(location)) {
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
        for (Recipe recipe : Recipe.REGISTRY.values()) {
            if (recipe.getResource().equals(location)) {
                return CookingHelper.makeRecipe(recipe);
            }
        }

        return null;
    }

    private IngredientStack toIngredientStack(ItemStack item) {
        return new IngredientStack(ingredientRegistry.getValueOf(item));
    }

    private List<IngredientStack> toIngredients(List<ItemStack> stacks) {
        return stacks.stream().map(this::toIngredientStack).collect(Collectors.toList());
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

        return Lists.newArrayList(utensil.getBurntItem().copy());
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
