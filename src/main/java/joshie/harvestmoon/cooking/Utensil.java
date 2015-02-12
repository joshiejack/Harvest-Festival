package joshie.harvestmoon.cooking;

import java.util.HashSet;
import java.util.Set;

import joshie.harvestmoon.util.SafeStack;

public enum Utensil implements ICookingComponent {
    KITCHEN(4000), POT(1000), FRYING_PAN(1200), MIXER(1200), OVEN(2500), STEAMER(2000), KNIFE(250), ROLLING_PIN(500);
    
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
