package joshie.harvest.animals;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.*;
import joshie.harvest.core.util.HFApiImplementation;
import joshie.harvest.core.util.holder.HolderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Locale;

@HFApiImplementation
public class AnimalRegistry implements IAnimalHandler {
    public static final AnimalRegistry INSTANCE = new AnimalRegistry();
    private final HashMap<String, IAnimalType> types = new HashMap<>();
    private final HolderRegistry<AnimalFoodType> registry = new HolderRegistry<>();

    private AnimalRegistry() {}

    //Internal Convenience method
    static void registerFoodsAsType(AnimalFoodType type, Item... items) {
        for (Item item : items) {
            HFApi.animals.registerFoodAsType(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), type);
        }
    }

    @Override
    public void registerFoodAsType(ItemStack stack, AnimalFoodType type) {
        registry.registerItem(stack, type);
    }

    @Override
    public boolean canAnimalEatFoodType(IAnimalTracked tracked, AnimalFoodType type) {
        for (AnimalFoodType t: tracked.getData().getType().getFoodTypes()) {
            if (t == type) return true;
        }

        return false;
    }

    @Override
    public boolean canEat(ItemStack stack, AnimalFoodType... types) {
        for (AnimalFoodType type: types) {
            if (registry.matches(stack, type)) return true;
        }

        return false;
    }

    @Override
    public IAnimalData newData(IAnimalTracked animal, String type) {
        return new AnimalData(animal, types.get(type));
    }

    @Override
    public void registerType(String key, IAnimalType type) {
        types.put(key.toLowerCase(Locale.ENGLISH), type);
    }

    @Override
    public IAnimalType getTypeFromString(String string) {
        return types.get(string.toLowerCase(Locale.ENGLISH));
    }
}