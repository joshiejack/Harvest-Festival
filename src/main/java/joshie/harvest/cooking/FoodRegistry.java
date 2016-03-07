package joshie.harvest.cooking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import joshie.harvest.api.cooking.ICookingComponent;
import joshie.harvest.api.cooking.IFoodRegistry;
import joshie.harvest.api.cooking.IMeal;
import joshie.harvest.api.cooking.IMealRecipe;
import joshie.harvest.api.cooking.ISpecialRecipeHandler;
import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.core.helpers.SafeStackHelper;
import joshie.harvest.core.util.SafeStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class FoodRegistry implements IFoodRegistry {
    private static final Multimap<SafeStack, ICookingComponent> registry = ArrayListMultimap.create();
    private static final ArrayList<IMealRecipe> recipes = new ArrayList(250);
    private static final HashMap<String, ICookingComponent> components = new HashMap();
    private static final HashSet<ISpecialRecipeHandler> specials = new HashSet();
    private static final HashSet meals = new HashSet();

    @Override
    public void register(ItemStack stack, ICookingComponent component) {
        if (stack == null || stack.getItem() == null || component == null) return; //Fail silently
        FoodRegistry.registry.get(SafeStackHelper.getSafeStackType(stack)).add(component);

        //Register the component
        if (!components.containsKey(component.getUnlocalizedName())) {
            components.put(component.getUnlocalizedName(), component);
        }
    }
    
    @Override
    public void registerRecipeHandler(ISpecialRecipeHandler handler) {
        specials.add(handler);
    }

    @Override
    public List<ICookingComponent> getCookingComponents(ItemStack stack) {
        return (List<ICookingComponent>) SafeStackHelper.getResult(stack, registry);
    }

    @Override
    public ICookingComponent getComponent(String unlocalized) {
        return components.get(unlocalized);
    }
    
    @Override
    public ICookingComponent newCategory(String unlocalized) {
        return new Ingredient(unlocalized);
    }
    
    @Override
    public ICookingComponent newIngredient(String unlocalized, int stamina, int fatigue, int hunger, float saturation, int eatTimer) {
        return new Ingredient(unlocalized, stamina, fatigue, hunger, saturation, eatTimer);
    }

    @Override
    public Fluid getFluid(ItemStack ingredient) {
        List<ICookingComponent> components = ((List<ICookingComponent>)SafeStackHelper.getResult(ingredient, registry));
        return components.size() < 1 ? null : components.get(0).getFluid();
    }

    @Override
    public IUtensil getUtensil(String unlocalized) {
        for (Utensil utensil : Utensil.values()) {
            if (utensil.name().equalsIgnoreCase(unlocalized)) return utensil;
        }

        return Utensil.COUNTER;
    }

    @Override
    public IMealRecipe addRecipe(IMealRecipe recipe) {
        recipes.add(recipe);
        return recipe;
    }
    
    @Override
    public ArrayList<IMealRecipe> getRecipes() {
        return recipes;
    }

    @Override
    public ItemStack getBestMeal(String string) {
        for (IMealRecipe recipe: getRecipes()) {
            if (recipe.getBestMeal().getUnlocalizedName().equals(string)) {
                return recipe.getBestMeal().cook(recipe.getBestMeal());
            }
        }
        
        return null;
    }

    @Override
    public ItemStack getMeal(String string) {
        for (IMealRecipe recipe: getRecipes()) {
            if (recipe.getBestMeal().getUnlocalizedName().equals(string)) {
                return recipe.getBestMeal().cook(recipe.getMeal());
            }
        }
        
        return null;
    }

    @Override
    public ItemStack getResult(IUtensil utensil, ArrayList<ItemStack> ingredients) {
        //Check the special recipes first
        for (ISpecialRecipeHandler recipe: specials) {
            ItemStack ret = recipe.getResult(utensil, ingredients);
            if (ret != null) {
                return ret;
            }
        }
                
        //Convert all the stacks in to their relevant ingredients
        HashSet<ICookingComponent> components = new HashSet();
        for (ItemStack stack : ingredients) {
            components.addAll(getCookingComponents(stack));
        }

        for (IMealRecipe recipe : recipes) {
            IMeal meal = recipe.getMeal(utensil, components);
            if (meal != null) {
                return meal.cook(meal);
            }
        }

        ItemStack burnt = Meal.BURNT.copy();
        burnt.setItemDamage(utensil.ordinal());
        return burnt;
    }
}
