package joshie.harvestmoon.init;

import static joshie.harvestmoon.calendar.Weekday.FRIDAY;
import static joshie.harvestmoon.calendar.Weekday.MONDAY;
import static joshie.harvestmoon.calendar.Weekday.SATURDAY;
import static joshie.harvestmoon.calendar.Weekday.THURSDAY;
import static joshie.harvestmoon.calendar.Weekday.TUESDAY;
import static joshie.harvestmoon.calendar.Weekday.WEDNESDAY;
import static net.minecraft.world.EnumDifficulty.EASY;
import static net.minecraft.world.EnumDifficulty.HARD;
import static net.minecraft.world.EnumDifficulty.NORMAL;
import static net.minecraft.world.EnumDifficulty.PEACEFUL;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.shops.PurchaseableCropSeeds;
import joshie.harvestmoon.shops.ShopInventory;

public class HMShops {
    public static ShopInventory seed_shop;

    public static void init() {
        seedShop();
    }

    private static void seedShop() {
        /* Register all crop seeds to the seed shop */
        seed_shop = new ShopInventory("seeds", 1);
        for (Crop crop : Crop.crops) {
            seed_shop.addItem(new PurchaseableCropSeeds(crop));
        }

        /* Peaceful Opening Hours */
        seed_shop.addOpening(PEACEFUL, MONDAY, 0, 24000).addOpening(PEACEFUL, TUESDAY, 0, 24000).addOpening(PEACEFUL, WEDNESDAY, 0, 24000);
        seed_shop.addOpening(PEACEFUL, THURSDAY, 0, 24000).addOpening(PEACEFUL, FRIDAY, 0, 24000).addOpening(PEACEFUL, SATURDAY, 0, 24000);
        /* Easy Opening Hours */
        seed_shop.addOpening(EASY, MONDAY, 5000, 22000).addOpening(EASY, TUESDAY, 5000, 22000).addOpening(EASY, WEDNESDAY, 5000, 22000);
        seed_shop.addOpening(EASY, THURSDAY, 5000, 22000).addOpening(EASY, FRIDAY, 5000, 22000).addOpening(EASY, SATURDAY, 9000, 21000);
        /* Normal Opening Hours */
        seed_shop.addOpening(NORMAL, MONDAY, 6000, 19000).addOpening(NORMAL, TUESDAY, 600, 19000).addOpening(NORMAL, THURSDAY, 600, 19000);
        seed_shop.addOpening(NORMAL, FRIDAY, 6000, 19000).addOpening(NORMAL, SATURDAY, 10000, 18000);
        /* Hard Opening Hours */
        seed_shop.addOpening(HARD, MONDAY, 9000, 17000).addOpening(HARD, TUESDAY, 9000, 17000).addOpening(HARD, THURSDAY, 9000, 17000);
        seed_shop.addOpening(HARD, FRIDAY, 9000, 17000).addOpening(HARD, SATURDAY, 11000, 15000);
        HMNPCs.seed_owner.setShop(seed_shop);
    }
}
