package harvestmoon.cooking;

import harvestmoon.cooking.registry.ICookingComponent;
import harvestmoon.util.SafeStack;

import java.util.HashSet;
import java.util.Set;

public enum Utensil implements ICookingComponent {
    KITCHEN(4000), POT(1000), FRYING_PAN(1200), MIXER(1200), OVEN(2500), STEAM(2000);
    
    public Set<SafeStack> keys = new HashSet();
    public int cost;

    private Utensil(int cost) {
        this.cost = cost;
    }
    
    @Override
    public boolean isEquivalent(String unlocalized, ICookingComponent component) {
        return component.equals(this);
    }

    @Override
    public Utensil addKey(SafeStack key) {
        keys.add(key);
        return this;
    }
}
