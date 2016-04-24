package joshie.harvest.cooking;

import joshie.harvest.api.cooking.ICookingComponent;
import net.minecraftforge.fluids.Fluid;

import java.util.HashSet;

public class Ingredient implements ICookingComponent {
    private HashSet<Ingredient> equivalents = new HashSet();
    private String unlocalized;
    public int stamina;
    public int fatigue;
    public int hunger;
    public float saturation;
    public int eatTime;
    public Fluid fluid;

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
    
    public Ingredient(String unlocalized) {
        this.unlocalized = unlocalized;
        equivalents.add(this);
    }

    @Override
    public ICookingComponent add(ICookingComponent... components) {
        for (ICookingComponent component: components) {
            equivalents.add((Ingredient) component);
        }
        
        return this;
    }

    @Override
    public ICookingComponent assign(ICookingComponent... ingredient) {
        for (ICookingComponent c: ingredient) {
            c.add(this);
        }

        return this;
    }
    
    @Override
    public ICookingComponent setFluid(Fluid fluid) {
        this.fluid = fluid;
        return this;
    }
    
    @Override
    public Fluid getFluid() {
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
    
    /** This should return true if the passed in ingredient is the same as this one **/
    @Override
    public boolean isEqual(ICookingComponent ingredient) {
        for (Ingredient i : equivalents) { //Return true if the item passed in matches this one
            if (i.getUnlocalizedName().equals(ingredient.getUnlocalizedName())) return true; //Loops the equivalents list, this item is contained in that list by default
        }

        return false;
    }
}
