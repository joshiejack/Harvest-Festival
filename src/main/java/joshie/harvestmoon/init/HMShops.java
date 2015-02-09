package joshie.harvestmoon.init;

import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.shops.PurchaseableCropSeeds;
import joshie.harvestmoon.shops.ShopInventory;

public class HMShops {
    public static ShopInventory seed_shop;

    public static void init() {
        seedShop();
    }

    private static void seedShop() {
        /** Register all crop seeds to the seed shop **/
        seed_shop = new ShopInventory();
        for (Crop crop : Crop.crops) {
            seed_shop.add(new PurchaseableCropSeeds(crop));
        }

        HMNPCs.seed_owner.setShop(seed_shop);
    }
}
