package joshie.harvestmoon.cooking;

import java.util.HashSet;

public class Seasoning implements ICookingComponent {
    private HashSet<Seasoning> equivalents = new HashSet();
    private String unlocalized;
    public float saturation;
    public int stamina;

    public Seasoning(String unlocalized, float saturation, int stamina) {
        this.unlocalized = unlocalized;
        this.saturation = saturation;
        this.stamina = stamina;
    }

    @Override
    public Seasoning add(ICookingComponent component) {
        equivalents.add((Seasoning) component);
        return this;
    }

    @Override
    public Seasoning assign(ICookingComponent ingredient) {
        ingredient.add(this);
        return this;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalized;
    }

    @Override
    public boolean isEqual(ICookingComponent seasoning) {
        for (Seasoning s : equivalents) { //Return true if the item passed in matches this one
            if (s.getUnlocalizedName().equals(seasoning.getUnlocalizedName())) return true; //Loops the equivalents list, this item is contained in that list by default
        }

        return false;
    }
}
