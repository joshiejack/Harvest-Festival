package joshie.harvest.cooking;

import joshie.harvest.api.cooking.IUtensil;

public enum Utensil implements IUtensil {
    COUNTER(250), POT(1000), FRYING_PAN(1200), MIXER(1200), OVEN(2500);

    private final int cost;

    Utensil(int cost) {
        this.cost = cost;
    }

    @Override
    public int getCost() {
        return cost;
    }
}
