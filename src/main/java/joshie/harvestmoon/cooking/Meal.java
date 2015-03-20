package joshie.harvestmoon.cooking;

import joshie.harvestmoon.init.HMItems;
import net.minecraft.item.ItemStack;

public class Meal {
    private static int lastMeta;
    static final ItemStack BURNT = new ItemStack(HMItems.meal);
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
    
    public int meta;
    
    public Meal(String unlocalized, int stamina, int fatigue, int hunger, float saturation, int eatTime) {
        this.unlocalized = unlocalized;
        this.stamina = stamina;
        this.fatigue = fatigue;
        this.hunger = (int) (hunger * hunger_modifier);
        this.saturation = (float) (saturation * saturation_modifier);
        this.eatTime = eatTime;
        this.meta = lastMeta++;
    }
    
    public Meal(Meal meal) {        
        this.unlocalized = meal.unlocalized;
        this.stamina = meal.stamina;
        this.fatigue = meal.fatigue;
        this.hunger = meal.hunger;
        this.saturation = meal.saturation;
        this.hunger_cap = meal.hunger_cap;
        this.saturation_cap = meal.saturation_cap;
        this.meta = meal.meta;
    }
    
    public Meal setDrink() {
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
    
    public Meal addIngredient(Ingredient ingredient) {
        this.eatTime += ingredient.eatTime;
        this.stamina += ingredient.stamina;
        this.fatigue += ingredient.fatigue;
        this.hunger += ingredient.hunger;
        this.saturation += ingredient.saturation;
        this.hunger = Math.min(hunger_cap, this.hunger);
        this.saturation = Math.min(saturation_cap, this.saturation);
        
        return this;
    }
}
