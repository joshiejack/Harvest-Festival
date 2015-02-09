package joshie.harvestmoon.cooking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import joshie.harvestmoon.util.SafeStack;

public class Seasoning implements ICookingComponent {
    //A list of all registered seasonings
    public static HashMap<String, Seasoning> seasonings = new HashMap();
    public Set<SafeStack> keys = new HashSet();
    
    public float saturation;
    public int stamina;

    public Seasoning(String uniqueIdentifier, float saturation, int stamina) {
        this.saturation = saturation;
        this.stamina = stamina;
        this.seasonings.put(uniqueIdentifier, this);
    }

    @Override
    public boolean isEquivalent(String unlocalized, ICookingComponent component) {
        ArrayList<ICookingComponent> equals = FoodRegistry.getEquivalents(unlocalized);
        if (equals != null && equals.contains(component)) {
            return true;
        } else return component.equals(this);
    }

    @Override
    public Seasoning addKey(SafeStack key) {
        keys.add(key);
        return this;
    }
}
