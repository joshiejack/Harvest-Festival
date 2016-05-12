package joshie.harvest.cooking;

import joshie.harvest.api.cooking.ICookingIngredient;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;

public class Ingredient implements ICookingIngredient {
    private HashSet<Ingredient> equivalents = new HashSet<Ingredient>();
    private String unlocalized;
    public int stamina;
    public int fatigue;
    public int hunger;
    public float saturation;
    public int eatTime;
    public ResourceLocation fluid;

    /**
     * @param stamina    - how much this restored stamina
     * @param fatigue    - how much this adds to fatigue
     * @param hunger     - how much minecraft hunger restores
     * @param saturation - how much minecraft saturation restores
     * @param eatTime    - how long this adds to eat time
     */
    public Ingredient(String unlocalized, int stamina, int fatigue, int hunger, float saturation, int eatTime) {
        this.unlocalized = unlocalized;
        this.stamina = stamina;
        this.fatigue = fatigue;
        this.hunger = hunger;
        this.saturation = saturation;
        this.eatTime = eatTime;
        equivalents.add(this);
    }

    public Ingredient(String unlocalized) {
        this.unlocalized = unlocalized;
        equivalents.add(this);
    }

    @Override
    public ICookingIngredient add(ICookingIngredient... components) {
        for (ICookingIngredient component : components) {
            equivalents.add((Ingredient) component);
        }

        return this;
    }

    @Override
    public ICookingIngredient assign(ICookingIngredient... ingredient) {
        for (ICookingIngredient c : ingredient) {
            c.add(this);
        }

        return this;
    }

    @Override
    public ICookingIngredient setFluid(ResourceLocation fluid) {
        this.fluid = fluid;
        return this;
    }

    @Override
    public ResourceLocation getFluid() {
        return fluid;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalized;
    }

    @Override
    public int getEatTime() {
        return eatTime;
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
    public int getHunger() {
        return hunger;
    }

    @Override
    public float getSaturation() {
        return saturation;
    }

    /**
     * This should return true if the passed in ingredient is the same as this one
     **/
    @Override
    public boolean isEqual(ICookingIngredient ingredient) {
        for (Ingredient i : equivalents) { //Return true if the item passed in matches this one
            if (i.getUnlocalizedName().equals(ingredient.getUnlocalizedName()))
                return true; //Loops the equivalents list, this item is contained in that list by default
        }

        return false;
    }
}
