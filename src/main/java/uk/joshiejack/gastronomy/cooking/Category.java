package uk.joshiejack.gastronomy.cooking;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Collection;
import java.util.Map;

public class Category {
    //List of everything that qualifies as this ingredient
    public static final Map<String, Category> categories = Maps.newHashMap();
    private static final Multimap<Ingredient, Category> ingredientCategories = HashMultimap.create();
    private final NonNullList<ItemStack> list = NonNullList.create();
    public final String name;

    private Category(String name) {
        this.name = name;
    }

    public static void registerIngredient(Category category, Ingredient ingredient) {
        ingredientCategories.get(ingredient).add(category);
    }

    public static Category getCategory(String name) {
        if (!categories.containsKey(name)) {
            categories.put(name, new Category(name));
        }

        return categories.get(name);
    }

    public static Collection<Category> getCategories(Ingredient ingredient) {
        return ingredientCategories.get(ingredient);
    }

    public NonNullList<ItemStack> getStacks() {
        if (list.isEmpty()) {
            for (Map.Entry<Ingredient, Category> entry : ingredientCategories.entries()) {
                if (entry.getValue().name.equals(this.name)) {
                    list.addAll(entry.getKey().getStacks());
                }
            }
        }

        return list;
    }
}
