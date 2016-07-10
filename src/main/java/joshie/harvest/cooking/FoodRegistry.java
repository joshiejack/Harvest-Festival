package joshie.harvest.cooking;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import joshie.harvest.api.cooking.*;
import joshie.harvest.player.tracking.TrackingData.AbstractItemHolder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;

import java.text.DecimalFormat;
import java.util.*;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class FoodRegistry implements IFoodRegistry {
    public static final FMLControlledNamespacedRegistry<Recipe> REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation(MODID, "meals"), Recipe.class, null, 10, 32000, true, null, null, null);
    private final HashMap<String, ICookingIngredient> components = new HashMap<>();
    private final HashSet<ISpecialRecipeHandler> specials = new HashSet<>();
    private final Multimap<AbstractItemHolder, ICookingIngredient> registry = ArrayListMultimap.create();
    private final Multimap<Item, AbstractItemHolder> keyMap = HashMultimap.create();

    @Override
    public void register(ItemStack stack, ICookingIngredient component) {
        if (stack == null || stack.getItem() == null || component == null) return; //Fail silently
        AbstractItemHolder holder = AbstractItemHolder.getStack(stack);
        keyMap.get(stack.getItem()).add(holder);
        registry.get(holder).add(component);

        //Register the component
        if (!components.containsKey(component.getUnlocalizedName())) {
            components.put(component.getUnlocalizedName(), component);
        }
    }

    @Override
    public Collection<ICookingIngredient> getIngredients() {
        return components.values();
    }

    @Override
    public void registerRecipeHandler(ISpecialRecipeHandler handler) {
        specials.add(handler);
    }

    @Override
    public List<ICookingIngredient> getCookingComponents(ItemStack stack) {
        for (AbstractItemHolder holder: keyMap.get(stack.getItem())) {
            if (holder.matches(stack)) return (List<ICookingIngredient>) registry.get(holder);
        }

        return new ArrayList<>();
    }

    @Override
    public ICookingIngredient getIngredient(String unlocalized) {
        return components.get(unlocalized);
    }

    @Override
    public ICookingIngredient newCategory(String unlocalized) {
        return new Ingredient(unlocalized);
    }

    @Override
    public ICookingIngredient newIngredient(String unlocalized, int hunger, float saturation, float exhaustion, int eatTimer) {
        return new Ingredient(unlocalized, hunger, saturation, exhaustion, eatTimer);
    }

    float roundTwoDecimals(float d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Float.valueOf(twoDForm.format(d));
    }

    @Override
    public ResourceLocation getFluid(ItemStack ingredient) {
        List<ICookingIngredient> components = getCookingComponents(ingredient);
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
    public IMealRecipe addMeal(ResourceLocation key, IUtensil utensil, int hunger, float saturation, float exhaustion, int eatTimer, ICookingIngredient... components) {
        String unlocalised = key.getResourceDomain() + ".meal." + key.getResourcePath().replace("_", ".");
        Recipe recipe = new Recipe(unlocalised, components, new Meal(hunger, saturation, exhaustion, eatTimer));
        recipe.setRegistryName(key);
        recipe.setRequiredTool(utensil);
        REGISTRY.register(recipe);
        return recipe;
    }

    @Override
    public ItemStack getBestMeal(String string) {
        ResourceLocation location = string.contains(":") ? new ResourceLocation(string) : new ResourceLocation(MODID, string);
        for (Recipe recipe : REGISTRY.getValues()) {
            if (recipe.getRegistryName().equals(location)) {
                return recipe.cook(recipe.getBestMeal());
            }
        }

        return null;
    }

    @Override
    public ItemStack getMeal(String string) {
        ResourceLocation location = string.contains(":") ? new ResourceLocation(string) : new ResourceLocation(MODID, string);
        for (Recipe recipe : REGISTRY.getValues()) {
            if (recipe.getRegistryName().equals(location)) {
                return recipe.cook(recipe.getMeal());
            }
        }

        return null;
    }

    @Override
    public ItemStack getResult(IUtensil utensil, List<ItemStack> ingredients) {
        //Check the special recipes first
        for (ISpecialRecipeHandler recipe : specials) {
            ItemStack ret = recipe.getResult(utensil, ingredients);
            if (ret != null) {
                return ret;
            }
        }

        //Convert all the stacks in to their relevant ingredients
        HashSet<ICookingIngredient> components = new HashSet<>();
        for (ItemStack stack : ingredients) {
            components.addAll(getCookingComponents(stack));
        }

        for (Recipe recipe : REGISTRY.getValues()) {
            IMeal meal = recipe.getMeal(utensil, components);
            if (meal != null) {
                return recipe.cook(meal);
            }
        }

        ItemStack burnt = Meal.BURNT.copy();
        burnt.setItemDamage(utensil.ordinal());
        return burnt;
    }
}
