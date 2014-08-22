package harvestmoon.cooking;

import harvestmoon.cooking.registry.ICookingComponent;
import harvestmoon.util.SafeStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Seasoning implements ICookingComponent {
    public Set<SafeStack> keys = new HashSet();
    // How much this seasoning boosts a recipes quality by
    public String name;
    public float saturation;
    public int stamina;
    public int quality;

    public Seasoning(float saturation, int stamina, int quality) {
        this.saturation = saturation;
        this.stamina = stamina;
        this.quality = quality;
    }
    
    @Override
    public boolean isEquivalent(String unlocalized, ICookingComponent component) {
        ArrayList<ICookingComponent> equals = FoodRegistry.getEquivalents(unlocalized);
        if(equals != null && equals.contains(component)) {
            return true;
        } else return component.equals(this);
    }

    @Override
    public Seasoning addKey(SafeStack key) {
        keys.add(key);
        return this;
    }
}
