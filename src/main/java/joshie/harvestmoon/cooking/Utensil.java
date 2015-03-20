package joshie.harvestmoon.cooking;

import java.util.HashSet;
import java.util.Set;

import joshie.harvestmoon.api.cooking.IUtensil;
import joshie.harvestmoon.core.util.SafeStack;

public enum Utensil implements IUtensil {
    KITCHEN(250), POT(1000), FRYING_PAN(1200), MIXER(1200), OVEN(2500), STEAMER(2000);
    
    private final int cost;

    private Utensil(int cost) {
        this.cost = cost;
    }

    @Override
    public int getCost() {
        return cost;
    }
}
