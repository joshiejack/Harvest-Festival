package joshie.harvest.shops.requirement;

import joshie.harvest.mining.item.ItemMaterial.Material;

public class Adamantite extends Materials {
    private Adamantite(int cost) {
        super(Material.ADAMANTITE, cost);
    }

    public static Adamantite of(int amount) {
        return new Adamantite(amount);
    }
}
