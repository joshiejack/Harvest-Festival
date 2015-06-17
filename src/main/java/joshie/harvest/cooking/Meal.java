package joshie.harvest.cooking;

import joshie.harvest.api.cooking.ICookingComponent;
import joshie.harvest.api.cooking.IMeal;
import joshie.harvest.items.HFItems;
import joshie.harvest.items.ItemMeal;
import net.minecraft.item.ItemStack;

public class Meal implements IMeal {
    static final ItemStack BURNT = new ItemStack(HFItems.meal);
    private static double hunger_modifier = 1.0D;
    private static double saturation_modifier = 1.0D;

    public String unlocalized;
    public int stamina = 0;
    public int fatigue = 0;
    public int hunger = 0;
    public float saturation = 0;
    public int eatTime = 32;

    public boolean isLiquid = false;
    public int hunger_cap = 20;
    public float saturation_cap = 2F;

    public Meal(String unlocalized, int stamina, int fatigue, int hunger, float saturation, int eatTime) {
        this.unlocalized = unlocalized;
        this.stamina = stamina;
        this.fatigue = fatigue;
        this.hunger = (int) (hunger * hunger_modifier);
        this.saturation = (float) (saturation * saturation_modifier);
        this.eatTime = eatTime;
    }

    public Meal(IMeal meal) {
        this.unlocalized = meal.getUnlocalizedName();
        this.stamina = meal.getStamina();
        this.fatigue = meal.getFatigue();
        this.hunger = meal.getHunger();
        this.saturation = meal.getSaturation();
        this.isLiquid = meal.isDrink();
        this.hunger_cap = meal.getHungerCap();
        this.saturation_cap = meal.getSaturationCap();
    }

    @Override
    public IMeal setIsDrink() {
        this.isLiquid = true;
        return this;
    }

    public Meal addEatTime(int time) {
        this.eatTime += time;
        return this;
    }

    public Meal setEatTime(int time) {
        this.eatTime = time;
        return this;
    }

    public Meal setStamina(int stamina) {
        this.stamina = stamina;
        return this;
    }

    public Meal setFatigue(int fatigue) {
        this.fatigue = fatigue;
        return this;
    }

    public Meal setHunger(int hunger) {
        this.hunger = hunger;
        return this;
    }

    public Meal setSaturation(float saturation) {
        this.saturation = saturation;
        return this;
    }

    public Meal setHungerCap(int cap) {
        this.hunger_cap = cap;
        return this;
    }

    public Meal setSaturationCap(float cap) {
        this.saturation_cap = cap;
        return this;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalized;
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
    public IMeal addIngredient(ICookingComponent ingredient) {
        this.eatTime += ingredient.getEatTime();
        this.stamina += ingredient.getStamina();
        this.fatigue += ingredient.getFatigue();
        this.hunger += ingredient.getHunger();
        this.saturation += ingredient.getSaturation();
        this.hunger = Math.min(hunger_cap, this.hunger);
        this.saturation = Math.min(saturation_cap, this.saturation);

        return this;
    }

    @Override
    public ItemStack cook(IMeal meal) {
        return ItemMeal.cook(new ItemStack(HFItems.meal), meal);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof IMeal)) {
            return false;
        }

        return getUnlocalizedName().equals(((IMeal) o).getUnlocalizedName());
    }

    @Override
    public int hashCode() {
        return unlocalized.hashCode();
    }
}
