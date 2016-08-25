package joshie.harvest.cooking;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import joshie.harvest.api.cooking.*;
import joshie.harvest.cooking.recipe.Ingredient;
import joshie.harvest.cooking.recipe.Recipe;
import joshie.harvest.cooking.recipe.RecipeStack;
import joshie.harvest.core.util.HFApiImplementation;
import joshie.harvest.core.util.holders.AbstractItemHolder;
import joshie.harvest.core.util.holders.ItemStackHolder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.concurrent.Callable;

import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFApiImplementation
public class FoodRegistry implements IFoodRegistry {
    public static final FMLControlledNamespacedRegistry<Recipe> REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation(MODID, "meals"), Recipe.class, null, 10, 32000, true, null, null, null);
    public static final FoodRegistry INSTANCE = new FoodRegistry();
    private final Map<String, ICookingIngredient> components = new HashMap<>();
    private final Set<ISpecialRecipeHandler> handlers = new HashSet<>();
    private final Multimap<AbstractItemHolder, ICookingIngredient> registry = ArrayListMultimap.create();
    private final Multimap<Item, AbstractItemHolder> keyMap = HashMultimap.create();
    private final Cache<ICookingIngredient, List<ItemStack>> ingredientToStacks = CacheBuilder.newBuilder().maximumSize(512).build();
    private final Set<ItemStackHolder> knives = new HashSet<>();

    private FoodRegistry(){}

    public List<ItemStack> getStacksForIngredient(ICookingIngredient ingredient) {
        try {
            return ingredientToStacks.get(ingredient, new Callable<List<ItemStack>>() {
                @Override
                public List<ItemStack> call() throws Exception {
                    ArrayList<ItemStack> result = new ArrayList<>();
                    for (Map.Entry<AbstractItemHolder, ICookingIngredient> entry : registry.entries()) {
                        if (ingredient.isEqual(entry.getValue())) result.addAll(entry.getKey().getMatchingStacks());
                    }

                    return result;
                }
            });
        } catch (Exception e) { return new ArrayList<>(); }
    }

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
    public void registerKnife(ItemStack stack) {
        knives.add(ItemStackHolder.of(stack));
    }

    @Override
    public Collection<ICookingIngredient> getIngredients() {
        return components.values();
    }

    @Override
    public void registerRecipeHandler(ISpecialRecipeHandler handler) {
        handlers.add(handler);
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

    @Override
    public ResourceLocation getFluid(ItemStack ingredient) {
        List<ICookingIngredient> components = getCookingComponents(ingredient);
        return components.size() < 1 ? null : components.get(0).getFluid();
    }

    @Override
    public Utensil getUtensil(String unlocalized) {
        for (Utensil utensil : Utensil.values()) {
            if (utensil.name().equalsIgnoreCase(unlocalized)) return utensil;
        }

        return Utensil.COUNTER;
    }

    @Override
    public IMealRecipe addMeal(ResourceLocation key, Utensil utensil, int hunger, float saturation, float exhaustion, int eatTimer, ICookingIngredient... components) {
        String unlocalised = key.getResourceDomain() + ".meal." + key.getResourcePath().replace("_", ".");
        Recipe recipe = new Recipe(unlocalised, components, new Meal(hunger, saturation, exhaustion, eatTimer));
        recipe.setRegistryName(key);
        recipe.setRequiredTool(utensil);
        REGISTRY.register(recipe);
        return recipe;
    }

    @Override
    public void addRecipe(ItemStack output, Utensil utensil, ICookingIngredient... ingredients) {
        RecipeStack.INSTANCE.addRecipe(output, utensil, ingredients);
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
    public ItemStack getResult(Utensil utensil, List<ItemStack> ingredients) {
        //Convert all the stacks in to their relevant ingredients
        List<ICookingIngredient> components = new ArrayList<>();
        for (ItemStack stack : ingredients) {
            for (ICookingIngredient ingredient: getCookingComponents(stack)) {
                if (!components.contains(ingredient)) components.add(ingredient);
            }
        }

        //Check the special recipes first
        for (ISpecialRecipeHandler recipe : handlers) {
            ItemStack ret = recipe.getResult(utensil, ingredients, components);
            if (ret != null) {
                return ret;
            }
        }

        ItemStack burnt = Meal.BURNT.copy();
        burnt.setItemDamage(utensil.ordinal());
        return burnt;
    }

    public boolean isKnife(ItemStack stack) {
        if (stack.getItem() instanceof IKnife) {
            return ((IKnife)stack.getItem()).isKnife(stack);
        }

        return knives.contains(ItemStackHolder.of(stack)) || knives.contains(ItemStackHolder.of(stack.getItem(), OreDictionary.WILDCARD_VALUE));
    }
}
