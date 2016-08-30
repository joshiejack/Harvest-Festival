package joshie.harvest.cooking.recipe;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.cooking.HFCooking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class MealBuilder {
    public static final ItemStack BURNT = new ItemStack(HFCooking.MEAL);
    private static final double HUNGER_MODIFIER = 1.0D;
    private static final double SATURATION_MODIFIER = 1.0D;

    private List<ItemStack> ingredients = new ArrayList<>();
    private int hunger = 0;
    private float saturation = 0;
    private float exhaustion = 0;
    private int eatTime = 32;
    private boolean isLiquid = false;
    private int hunger_cap = 20;
    private float saturation_cap = 2F;

    public MealBuilder(int hunger, float saturation, int eatTime) {
        this.hunger = (int) (hunger * HUNGER_MODIFIER);
        this.saturation = (float) (saturation * SATURATION_MODIFIER);
        this.eatTime = eatTime;
    }

    public MealBuilder(MealBuilder meal, List<ItemStack> stacks) {
        this.ingredients = stacks;
        this.hunger = meal.hunger;
        this.saturation = meal.saturation;
        this.exhaustion = meal.exhaustion;
        this.eatTime = meal.eatTime;
        this.isLiquid = meal.isLiquid;
        this.hunger_cap = meal.hunger_cap;
        this.saturation_cap = meal.saturation_cap;
    }

    public void setIsDrink() {
        this.isLiquid = true;}

    public void setExhaustion(float exhaustion) {
        this.exhaustion = exhaustion;
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

    public long getSellValue(List<ItemStack> stacks) {
        long value = 0;
        for (ItemStack stack: stacks) {
            value += HFApi.shipping.getSellValue(stack);
        }

        //Return an increased value so it's better than selling on it's own
        return (long) (value * 1.25);
    }

    public ItemStack cook(ItemStack stack) {
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setInteger("FoodLevel", hunger);
        stack.getTagCompound().setFloat("FoodSaturation", saturation);
        stack.getTagCompound().setFloat("FoodExhaustion", exhaustion);
        stack.getTagCompound().setBoolean("IsDrink", isLiquid);
        stack.getTagCompound().setInteger("EatTime", Math.max(eatTime, 8));
        stack.getTagCompound().setLong("SellValue", getSellValue(ingredients));
        return stack;
    }
}
