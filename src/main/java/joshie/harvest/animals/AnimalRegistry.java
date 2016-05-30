package joshie.harvest.animals;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.*;
import joshie.harvest.player.tracking.TrackingData.AbstractItemHolder;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

public class AnimalRegistry implements IAnimalHandler {
    private final HashMap<String, IAnimalType> types = new HashMap<>();
    private final HashMap<AbstractItemHolder, AnimalFoodType> registry = new HashMap<>();
    private final Multimap<Item, AbstractItemHolder> keyMap = HashMultimap.create();

    //Internal Convenience method
    static void registerFoodsAsType(AnimalFoodType type, Item... items) {
        for (Item item : items) {
            HFApi.animals.registerFoodAsType(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), type);
        }
    }

    @Override
    public void registerFoodAsType(ItemStack stack, AnimalFoodType type) {
        AbstractItemHolder holder = AbstractItemHolder.getStack(stack);
        keyMap.get(stack.getItem()).add(holder);
        registry.put(holder, type);
    }

    @Override
    public boolean canEat(ItemStack stack, AnimalFoodType... types) {
        for (AbstractItemHolder holder: keyMap.get(stack.getItem())) {
            if (holder.matches(stack)) {
                AnimalFoodType type = registry.get(holder);
                if (type == null) continue;
                else {
                    for (AnimalFoodType t: types) {
                        if (type == t) return true;
                    }
                }
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