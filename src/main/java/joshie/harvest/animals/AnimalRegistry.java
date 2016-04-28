package joshie.harvest.animals;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.*;
import joshie.harvest.core.helpers.SafeStackHelper;
import joshie.harvest.core.util.SafeStack;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

public class AnimalRegistry implements IAnimalHandler {
    private static final HashMap<String, IAnimalType> types = new HashMap<String, IAnimalType>();
    private static final HashMap<SafeStack, AnimalFoodType> registry = new HashMap<SafeStack, AnimalFoodType>();

    //Internal Convenience method
    public static void registerFoodsAsType(AnimalFoodType type, Item... items) {
        for (Item item : items) {
            HFApi.ANIMALS.registerFoodAsType(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), type);
        }
    }

    @Override
    public void registerFoodAsType(ItemStack stack, AnimalFoodType type) {
        registry.put(SafeStackHelper.getSafeStackType(stack), type);
    }

    @Override
    public boolean canEat(AnimalFoodType[] types, ItemStack stack) {
        AnimalFoodType type = (AnimalFoodType) SafeStackHelper.getResult(stack, registry);
        if (type == null) return false;
        else {
            for (AnimalFoodType t : types) {
                if (type == t) return true;
            }
        }

        return false;
    }

    @Override
    public IAnimalData newData(IAnimalTracked animal) {
        return new AnimalData(animal);
    }

    @Override
    public IAnimalType getTypeFromString(String string) {
        return types.get(string);
    }

    @Override
    public void registerType(String key, IAnimalType type) {
        types.put(key, type);
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