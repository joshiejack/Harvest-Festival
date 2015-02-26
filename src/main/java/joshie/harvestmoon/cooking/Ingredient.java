package joshie.harvestmoon.cooking;

import java.util.HashSet;

import joshie.harvestmoon.api.core.ICookingComponent;

public class Ingredient implements ICookingComponent {
    private HashSet<Ingredient> equivalents = new HashSet();
    private String unlocalized;
    public int stamina;
    public int fatigue;
    public int hunger;
    public float saturation;
    public int eatTime;

    /**@param stamina - how much this restored stamina
     * @param fatigue - how much this adds to fatigue
     * @param hunger - how much minecraft hunger restores
     * @param saturation - how much minecraft saturation restores
     * @param eatTime - how long this adds to eat time*/
    public Ingredient(String unlocalized, int stamina, int fatigue, int hunger, float saturation, int eatTime) {
        this.unlocalized = unlocalized;
        this.stamina = stamina;
        this.fatigue = fatigue;
        this.hunger = hunger;
        this.saturation = saturation;
        this.eatTime = eatTime;
        equivalents.add(this);
    }

    @Override
    public Ingredient add(ICookingComponent component) {
        equivalents.add((Ingredient) component);
        return this;
    }

    @Override
    public Ingredient assign(ICookingComponent ingredient) {
        ingredient.add(this);
        return this;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalized;
    }

    /** This should return true if the passed in ingredient is the same as this one **/
    @Override
    public boolean isEqual(ICookingComponent ingredient) {
        for (Ingredient i : equivalents) { //Return true if the item passed in matches this one
            if (i.getUnlocalizedName().equals(ingredient.getUnlocalizedName())) return true; //Loops the equivalents list, this item is contained in that list by default
        }

        return false;
    }
}
