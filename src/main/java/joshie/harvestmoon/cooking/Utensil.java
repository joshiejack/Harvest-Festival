package joshie.harvestmoon.cooking;

import java.util.HashSet;
import java.util.Set;

import joshie.harvestmoon.util.SafeStack;

public enum Utensil {
    KITCHEN(250), POT(1000), FRYING_PAN(1200), MIXER(1200), OVEN(2500), STEAMER(2000), KNIFE(1500), ROLLING_PIN(750), WHISK(500);
    
    public Set<SafeStack> keys = new HashSet();
    public int cost;

    private Utensil(int cost) {
        this.cost = cost;
    }
}
