package joshie.harvest.animals;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.*;
import joshie.harvest.core.util.HFApiImplementation;
import joshie.harvest.core.util.holders.HolderRegistry;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

@HFApiImplementation
public class AnimalRegistry implements IAnimalHandler {
    public static final AnimalRegistry INSTANCE = new AnimalRegistry();
    private final HashMap<String, IAnimalType> types = new HashMap<>();
    //private final HashMap<AbstractItemHolder, AnimalFoodType> registry = new HashMap<>();
    //private final Multimap<Item, AbstractItemHolder> keyMap = HashMultimap.create();
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
        for (AnimalFoodType t: tracked.getType().getFoodTypes()) {
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
    public IAnimalData newData(IAnimalTracked animal) {
        return new AnimalData(animal);
    }

    @Override
    public IAnimalType getTypeFromString(String string) {
        return types.get(string.toLowerCase());
    }

    @Override
    public void registerType(String key, IAnimalType type) {
        types.put(key.toLowerCase(), type);
    }

    @Override
    public IAnimalType getType(EntityAnimal animal) {
        if (animal instanceof EntityCow) {
            return types.get("cow");
        } else if (animal instanceof EntitySheep) {
            return types.get("sheep");
        } else if (animal instanceof EntityChicken) {
            return types.get("chicken");
        } else return null;
    }
}