package joshie.harvest.cooking;

import joshie.harvest.api.cooking.ICookingIngredient;
import joshie.harvest.api.cooking.IMeal;
import joshie.harvest.items.HFItems;
import net.minecraft.item.ItemStack;

public class Meal implements IMeal {
    static final ItemStack BURNT = new ItemStack(HFItems.MEAL);
    private static double hunger_modifier = 1.0D;
    private static double saturation_modifier = 1.0D;

    private int stamina = 0;
    private int fatigue = 0;
    private int hunger = 0;
    private float saturation = 0;
    private int eatTime = 32;
    private boolean isLiquid = false;
    private ItemStack alt = null;
    private int hunger_cap = 20;
    private float saturation_cap = 2F;

    public Meal(int stamina, int fatigue, int hunger, float saturation, int eatTime) {
        this.stamina = stamina;
        this.fatigue = fatigue;
        this.hunger = (int) (hunger * hunger_modifier);
        this.saturation = (float) (saturation * saturation_modifier);
        this.eatTime = eatTime;
    }

    public Meal(IMeal meal) {
        this.stamina = meal.getStamina();
        this.fatigue = meal.getFatigue();
        this.hunger = meal.getHunger();
        this.saturation = meal.getSaturation();
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
    public int getStamina() {
        return stamina;
    }

    @Override
    public int getFatigue() {
        return fatigue;
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
        this.stamina += ingredient.getStamina();
        this.fatigue += ingredient.getFatigue();
        this.hunger += ingredient.getHunger();
        this.saturation += ingredient.getSaturation();
        this.hunger = Math.min(hunger_cap, this.hunger);
        this.saturation = Math.min(saturation_cap, this.saturation);
        return this;
    }
}
