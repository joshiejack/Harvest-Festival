package joshie.harvest.cooking;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import joshie.harvest.api.cooking.*;
import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.cooking.recipe.MealImpl;
import joshie.harvest.cooking.recipe.MealBuilder;
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
public class CookingAPI implements CookingManager {
    public static final FMLControlledNamespacedRegistry<MealImpl> REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation(MODID, "meals"), MealImpl.class, null, 100, 32000, true, null, null, null);
    public static final CookingAPI INSTANCE = new CookingAPI();
    private final Set<RecipeHandler> handlers = new HashSet<>();
    private final Multimap<AbstractItemHolder, Ingredient> registry = ArrayListMultimap.create();
    private final Multimap<Item, AbstractItemHolder> keyMap = HashMultimap.create();
    private final Cache<Ingredient, List<ItemStack>> ingredientToStacks = CacheBuilder.newBuilder().maximumSize(512).build();
    private final Set<ItemStackHolder> knives = new HashSet<>();

    private CookingAPI(){}

    public List<ItemStack> getStacksForIngredient(Ingredient ingredient) {
        try {
            return ingredientToStacks.get(ingredient, new Callable<List<ItemStack>>() {
                @Override
                public List<ItemStack> call() throws Exception {
                    ArrayList<ItemStack> result = new ArrayList<>();
                    for (Map.Entry<AbstractItemHolder, Ingredient> entry : registry.entries()) {
                        if (ingredient.isEqual(entry.getValue())) result.addAll(entry.getKey().getMatchingStacks());
                    }

                    return result;
                }
            });
        } catch (Exception e) { return new ArrayList<>(); }
    }

    @Override
    public void register(ItemStack stack, Ingredient component) {
        if (stack == null || stack.getItem() == null || component == null) return; //Fail silently
        AbstractItemHolder holder = AbstractItemHolder.getStack(stack);
        keyMap.get(stack.getItem()).add(holder);
        registry.get(holder).add(component);
    }

    @Override
    public void registerKnife(ItemStack stack) {
        knives.add(ItemStackHolder.of(stack));
    }

    @Override
    public void registerRecipeHandler(RecipeHandler handler) {
        handlers.add(handler);
    }

    public List<Ingredient> getCookingComponents(ItemStack stack) {
        for (AbstractItemHolder holder: keyMap.get(stack.getItem())) {
            if (holder.matches(stack)) return (List<Ingredient>) registry.get(holder);
        }

        return new ArrayList<>();
    }

    @Override
    public ResourceLocation getFluid(ItemStack ingredient) {
        List<Ingredient> components = getCookingComponents(ingredient);
        return components.size() < 1 ? null : components.get(0).getFluid();
    }

    @Override
    public MealImpl addMeal(ResourceLocation key, Utensil utensil, int hunger, float saturation, float exhaustion, int eatTimer, Ingredient... components) {
        String unlocalised = key.getResourceDomain() + ".meal." + key.getResourcePath().replace("_", ".");
        MealImpl recipe = new MealImpl(unlocalised, components, new MealBuilder(hunger, saturation, exhaustion, eatTimer));
        recipe.setRegistryName(key);
        recipe.setRequiredTool(utensil);
        REGISTRY.register(recipe);
        return recipe;
    }

    @Override
    public void addRecipe(ItemStack output, Utensil utensil, Ingredient... ingredients) {
        RecipeStack.INSTANCE.addRecipe(output, utensil, ingredients);
    }

    @Override
    public ItemStack getBestMeal(String string) {
        ResourceLocation location = string.contains(":") ? new ResourceLocation(string) : new ResourceLocation(MODID, string);
        for (MealImpl recipe : REGISTRY.getValues()) {
            if (recipe.getRegistryName().equals(location)) {
                return recipe.cook(recipe.getBestMeal());
            }
        }

        return null;
    }

    @Override
    public ItemStack getMeal(String string) {
        ResourceLocation location = string.contains(":") ? new ResourceLocation(string) : new ResourceLocation(MODID, string);
        for (MealImpl recipe : REGISTRY.getValues()) {
            if (recipe.getRegistryName().equals(location)) {
                return recipe.cook(recipe.getMeal());
            }
        }

        return null;
    }

    @Override
    public ItemStack getResult(Utensil utensil, List<ItemStack> ingredients) {
        //Convert all the stacks in to their relevant ingredients
        List<Ingredient> components = new ArrayList<>();
        for (ItemStack stack : ingredients) {
            for (Ingredient ingredient: getCookingComponents(stack)) {
                if (!components.contains(ingredient)) components.add(ingredient);
            }
        }

        //Check the special recipes first
        for (RecipeHandler recipe : handlers) {
            ItemStack ret = recipe.getResult(utensil, ingredients, components);
            if (ret != null) {
                return ret;
            }
        }

        ItemStack burnt = MealBuilder.BURNT.copy();
        burnt.setItemDamage(utensil.getIndex());
        return burnt;
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
        return getCookingComponents(stack).size() > 0;
    }

    @Override
    public boolean isKnife(ItemStack stack) {
        return knives.contains(ItemStackHolder.of(stack)) || knives.contains(ItemStackHolder.of(stack.getItem(), OreDictionary.WILDCARD_VALUE));
    }
}
