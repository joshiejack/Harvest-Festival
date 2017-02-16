package joshie.harvest.shops.requirement;

import joshie.harvest.mining.item.ItemMaterial.Material;

public class Gold extends Materials {
    private Gold(int cost) {
        super(Material.GOLD, cost);
    }

    public static Gold of(int amount) {
        return new Gold(amount);
    }
}
