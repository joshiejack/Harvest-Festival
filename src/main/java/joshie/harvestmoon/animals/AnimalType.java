package joshie.harvestmoon.animals;

import java.util.HashMap;

import joshie.harvestmoon.api.AnimalFoodType;
import joshie.harvestmoon.core.config.Calendar;
import joshie.harvestmoon.core.helpers.SafeStackHelper;
import joshie.harvestmoon.core.util.SafeStack;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public enum AnimalType {
    COW(12 * (Calendar.DAYS_PER_SEASON * 4), 20 * (Calendar.DAYS_PER_SEASON * 4), 1, AnimalFoodType.GRASS), 
    SHEEP(8 * (Calendar.DAYS_PER_SEASON * 4), 12 * (Calendar.DAYS_PER_SEASON * 4), 7, AnimalFoodType.GRASS), 
    CHICKEN(3 * (Calendar.DAYS_PER_SEASON * 4), 10 * (Calendar.DAYS_PER_SEASON * 4), 1, AnimalFoodType.SEED), 
    HORSE(20 * (Calendar.DAYS_PER_SEASON * 4), 30 * (Calendar.DAYS_PER_SEASON * 4), 0, AnimalFoodType.GRASS, AnimalFoodType.VEGETABLE, AnimalFoodType.FRUIT), 
    PIG(6 * (Calendar.DAYS_PER_SEASON * 4), 10 * (Calendar.DAYS_PER_SEASON * 4), 0, AnimalFoodType.VEGETABLE, AnimalFoodType.FRUIT), 
    CAT(10 * (Calendar.DAYS_PER_SEASON * 4), 20 * (Calendar.DAYS_PER_SEASON * 4), 0, AnimalFoodType.FISH, AnimalFoodType.CHICKEN), 
    DOG(9 * (Calendar.DAYS_PER_SEASON * 4), 16 * (Calendar.DAYS_PER_SEASON * 4), 0, AnimalFoodType.REDMEAT, AnimalFoodType.CHICKEN), 
    OTHER(5 * (Calendar.DAYS_PER_SEASON * 4), 10 * (Calendar.DAYS_PER_SEASON * 4), 0);

    private final int min;
    private final int max;
    private final int days;
    private final AnimalFoodType[] types;

    private AnimalType(int min, int max, int days, AnimalFoodType... types) {
        this.min = min;
        this.max = max;
        this.days = days;
        this.types = types;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getDays() {
        return days;
    }

    public boolean eatsGrass() {
        for (AnimalFoodType type : types) {
            if (type == AnimalFoodType.GRASS) return true;
        }

        return false;
    }

    public static AnimalType getType(EntityAnimal animal) {
        if (animal instanceof EntityChicken) {
            return CHICKEN;
        } else if (animal instanceof EntitySheep) {
            return SHEEP;
        } else if (animal instanceof EntityCow) {
            return COW;
        } else if (animal instanceof EntityPig) {
            return PIG;
        } else if (animal instanceof EntityHorse) {
            return HORSE;
        } else if (animal instanceof EntityOcelot) {
            return CAT;
        } else if (animal instanceof EntityWolf) {
            return DOG;
        } else {
            return OTHER;
        }
    }

    private static HashMap<SafeStack, AnimalFoodType> registry = new HashMap();
    
    public static void registerFoodsAsType(AnimalFoodType type, Item... items) {
        for (Item item: items) {
            registerFoodAsType(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), type);
        }
    }
    
    public static void registerFoodAsType(ItemStack stack, AnimalFoodType type) {
        registry.put(SafeStackHelper.getSafeStackType(stack), type);
    }

    public boolean canEat(ItemStack stack) {
        AnimalFoodType type = (AnimalFoodType) SafeStackHelper.getResult(stack, registry);        
        if (type == null) return false;
        else {
            for (AnimalFoodType t : types) {
                if (type == t) return true;
            }
        }

        return false;
    }
}
