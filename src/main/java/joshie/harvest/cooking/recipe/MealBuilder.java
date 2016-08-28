package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.cooking.HFCooking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MealBuilder {
    public static final ItemStack BURNT = new ItemStack(HFCooking.MEAL);
    private static final double HUNGER_MODIFIER = 1.0D;
    private static final double SATURATION_MODIFIER = 1.0D;
    private static final double EXHAUSTION_MODIFIER = 1.0D;

    private int hunger = 0;
    private float saturation = 0;
    private float exhaustion = 0;
    private int eatTime = 32;
    private boolean isLiquid = false;
    private int hunger_cap = 20;
    private float saturation_cap = 2F;

    public MealBuilder(int hunger, float saturation, float exhaustion, int eatTime) {
        this.hunger = (int) (hunger * HUNGER_MODIFIER);
        this.saturation = (float) (saturation * SATURATION_MODIFIER);
        this.exhaustion =  (float) (exhaustion * EXHAUSTION_MODIFIER);
        this.eatTime = eatTime;
    }

    public MealBuilder(MealBuilder meal) {
        this.hunger = meal.hunger;
        this.saturation = meal.saturation;
        this.exhaustion = meal.exhaustion;
        this.eatTime = meal.eatTime;
        this.isLiquid = meal.isLiquid;
        this.hunger_cap = meal.hunger_cap;
        this.saturation_cap = meal.saturation_cap;
    }

    public MealBuilder setIsDrink() {
        this.isLiquid = true;
        return this;
    }

    public MealBuilder addIngredient(Ingredient ingredient) {
        this.eatTime += ingredient.getEatTime();
        this.hunger += ingredient.getHunger();
        this.saturation += ingredient.getSaturation();
        this.exhaustion += ingredient.getExhaustion();
        this.hunger = Math.min(hunger_cap, this.hunger);
        this.saturation = Math.min(saturation_cap, this.saturation);
        return this;
    }

    public ItemStack cook(ItemStack stack) {
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setInteger("FoodLevel", hunger);
        stack.getTagCompound().setFloat("FoodSaturation", saturation);
        stack.getTagCompound().setFloat("FoodExhaustion", exhaustion);
        stack.getTagCompound().setBoolean("IsDrink", isLiquid);
        stack.getTagCompound().setInteger("EatTime", eatTime);
        return stack;
    }
}
