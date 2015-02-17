package joshie.harvestmoon.animals;

import java.util.HashMap;

import joshie.harvestmoon.config.Calendar;
import joshie.harvestmoon.helpers.SafeStackHelper;
import joshie.harvestmoon.util.SafeStack;
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
    COW(12 * (Calendar.DAYS_PER_SEASON * 4), 20 * (Calendar.DAYS_PER_SEASON * 4), 1, FoodType.GRASS), 
    SHEEP(8 * (Calendar.DAYS_PER_SEASON * 4), 12 * (Calendar.DAYS_PER_SEASON * 4), 7, FoodType.GRASS), 
    CHICKEN(3 * (Calendar.DAYS_PER_SEASON * 4), 10 * (Calendar.DAYS_PER_SEASON * 4), 1, FoodType.SEED), 
    HORSE(20 * (Calendar.DAYS_PER_SEASON * 4), 30 * (Calendar.DAYS_PER_SEASON * 4), 0, FoodType.GRASS, FoodType.VEGETABLE, FoodType.FRUIT), 
    PIG(6 * (Calendar.DAYS_PER_SEASON * 4), 10 * (Calendar.DAYS_PER_SEASON * 4), 0, FoodType.VEGETABLE, FoodType.FRUIT), 
    CAT(10 * (Calendar.DAYS_PER_SEASON * 4), 20 * (Calendar.DAYS_PER_SEASON * 4), 0, FoodType.FISH, FoodType.CHICKEN), 
    DOG(9 * (Calendar.DAYS_PER_SEASON * 4), 16 * (Calendar.DAYS_PER_SEASON * 4), 0, FoodType.REDMEAT, FoodType.CHICKEN), 
    OTHER(5 * (Calendar.DAYS_PER_SEASON * 4), 10 * (Calendar.DAYS_PER_SEASON * 4), 0);

    private final int min;
    private final int max;
    private final int days;
    private final FoodType[] types;

    private AnimalType(int min, int max, int days, FoodType... types) {
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
        for (FoodType type : types) {
            if (type == FoodType.GRASS) return true;
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

    private static HashMap<SafeStack, FoodType> registry = new HashMap();
    
    public static void registerFoodsAsType(FoodType type, Item... items) {
        for (Item item: items) {
            registerFoodAsType(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), type);
        }
    }
    
    public static void registerFoodAsType(ItemStack stack, FoodType type) {
        registry.put(SafeStackHelper.getSafeStackType(stack), type);
    }

    public boolean canEat(ItemStack stack) {
        FoodType type = (FoodType) SafeStackHelper.getResult(stack, registry);
        if (type == null) return false;
        else {
            for (FoodType t : types) {
                if (type == t) return true;
            }
        }

        return false;
    }

    public static enum FoodType {
        REDMEAT, CHICKEN, FISH, SEED, VEGETABLE, FRUIT, GRASS;
    }
}
