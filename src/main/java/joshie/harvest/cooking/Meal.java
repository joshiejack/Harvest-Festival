package joshie.harvest.cooking;

import joshie.harvest.api.cooking.ICookingIngredient;
import joshie.harvest.api.cooking.IMeal;
import net.minecraft.item.ItemStack;

public class Meal implements IMeal {
    static final ItemStack BURNT = new ItemStack(HFCooking.MEAL);
    private static final double HUNGER_MODIFIER = 1.0D;
    private static final double SATURATION_MODIFIER = 1.0D;
    private static final double EXHAUSTION_MODIFIER = 1.0D;

    private int hunger = 0;
    private float saturation = 0;
    private float exhaustion = 0;
    private int eatTime = 32;
    private boolean isLiquid = false;
    private ItemStack alt = null;
    private int hunger_cap = 20;
    private float saturation_cap = 2F;

    public Meal(int hunger, float saturation, float exhaustion, int eatTime) {
        this.hunger = (int) (hunger * HUNGER_MODIFIER);
        this.saturation = (float) (saturation * SATURATION_MODIFIER);
        this.exhaustion =  (float) (exhaustion * EXHAUSTION_MODIFIER);
        this.eatTime = eatTime;
    }

    public Meal(IMeal meal) {
        this.hunger = meal.getHunger();
        this.saturation = meal.getSaturation();
        this.exhaustion = meal.getExhaustion();
        this.eatTime = meal.getEatTime();
        this.isLiquid = meal.isDrink();
        this.alt = meal.getAlternativeItem();
        this.hunger_cap = meal.getHungerCap();
        this.saturation_cap = meal.getSaturationCap();
    }

    @Override
    public IMeal setIsDrink() {
        this.isLiquid = true;
        return this;
    }

    @Override
    public IMeal setAlternativeItem(ItemStack alt) {
        this.alt = alt;
        return this;
    }

    @Override
    public ItemStack getAlternativeItem() {
        return alt;
    }

    @Override
    public int getHunger() {
        return hunger;
    }

    @Override
    public float getSaturation() {
        return saturation;
    }

    @Override
    public float getExhaustion() {
        return exhaustion;
    }

    @Override
    public boolean isDrink() {
        return isLiquid;
    }

    @Override
    public int getEatTime() {
        return eatTime;
    }

    @Override
    public int getHungerCap() {
        return hunger_cap;
    }

    @Override
    public float getSaturationCap() {
        return saturation_cap;
    }

    @Override
    public IMeal addIngredient(ICookingIngredient ingredient) {
        this.eatTime += ingredient.getEatTime();
        this.hunger += ingredient.getHunger();
        this.saturation += ingredient.getSaturation();
        this.exhaustion += ingredient.getExhaustion();
        this.hunger = Math.min(hunger_cap, this.hunger);
        this.saturation = Math.min(saturation_cap, this.saturation);
        return this;
    }
}
