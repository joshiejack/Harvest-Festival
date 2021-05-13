package uk.joshiejack.gastronomy.cooking;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Ingredient {
    //List of everything that qualifies as this ingredient
    public static final Map<String, Ingredient> ingredients = Maps.newHashMap();
    public static final Ingredient NONE = getIngredient("none"); //Null_Value
    public static final HolderRegistry<Ingredient> registry = new HolderRegistry<>(NONE);
    private final NonNullList<ItemStack> stacks = NonNullList.create();
    private final Cache<ItemStack, Boolean> isIngredient = CacheBuilder.newBuilder().build();
    public final String name;

    private Ingredient(String name) {
        this.name = name;
    }

    public static Ingredient getIngredient(String name) {
        if (!ingredients.containsKey(name)) {
            ingredients.put(name, new Ingredient(name));
        }

        return ingredients.get(name);
    }

    public boolean isNone() {
        return this == NONE;
    }

    public void setAsIngredient(Holder holder) {
        registry.register(holder, this);
    }

    public NonNullList<ItemStack> getStacks() {
        if (stacks.isEmpty()) {
            List<Holder> holders = registry.getKeys(this);
            for (Holder holder: holders) {
                stacks.addAll(holder.getStacks());
            }
        }

        return stacks;
    }

    public boolean isIngredient(ItemStack stack) {
        try {
            return isIngredient.get(stack, () -> registry.getValue(stack) == (this));
        } catch (ExecutionException e) {
            return false;
        }
    }
}
