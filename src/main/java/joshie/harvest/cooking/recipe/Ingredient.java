package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.ICookingIngredient;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;

public class Ingredient implements ICookingIngredient {
    private HashSet<Ingredient> equivalents = new HashSet<>();
    private String unlocalized;
    private int hunger;
    private float saturation;
    private float exhaustion;
    private int eatTime;
    public ResourceLocation fluid;

    public Ingredient(String unlocalized, int hunger, float saturation, float exhaustion, int eatTime) {
        this.unlocalized = unlocalized;
        this.hunger = hunger;
        this.saturation = saturation;
        this.exhaustion = exhaustion;
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
