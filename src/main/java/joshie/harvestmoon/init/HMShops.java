package joshie.harvestmoon.init;

import static joshie.harvestmoon.calendar.Weekday.FRIDAY;
import static joshie.harvestmoon.calendar.Weekday.MONDAY;
import static joshie.harvestmoon.calendar.Weekday.SUNDAY;
import static joshie.harvestmoon.calendar.Weekday.THURSDAY;
import static joshie.harvestmoon.calendar.Weekday.TUESDAY;
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
        seed_shop = new ShopInventory("seeds", 1);
        seed_shop.addOpening(MONDAY, 900, 1700).addOpening(TUESDAY, 900, 1700).addOpening(THURSDAY, 900, 1700);
        seed_shop.addOpening(FRIDAY, 900, 1700).addOpening(SUNDAY, 1100, 1500);
        for (Crop crop : Crop.crops) {
            seed_shop.addItem(new PurchaseableCropSeeds(crop));
        }

        HMNPCs.seed_owner.setShop(seed_shop);
    }
}
