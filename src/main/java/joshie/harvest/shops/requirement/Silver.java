package joshie.harvest.shops.requirement;

import joshie.harvest.mining.item.ItemMaterial.Material;

public class Silver extends Materials {
    private Silver(int cost) {
        super(Material.SILVER, cost);
    }

    public static Silver of(int amount) {
        return new Silver(amount);
    }
}
