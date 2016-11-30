package joshie.harvest.shops.requirement;

import joshie.harvest.mining.item.ItemMaterial.Material;

public class Copper extends Materials {
    private Copper(int cost) {
        super(Material.COPPER, cost);
    }

    public static Copper of(int amount) {
        return new Copper(amount);
    }
}
