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
        /* Peaceful Opening Hours */
        seed_shop.addOpening(PEACEFUL, MONDAY, 0, 2400).addOpening(PEACEFUL, TUESDAY, 0, 2400).addOpening(PEACEFUL, WEDNESDAY, 0, 2400);
        seed_shop.addOpening(PEACEFUL, THURSDAY, 0, 2400).addOpening(PEACEFUL, FRIDAY, 0, 2400).addOpening(PEACEFUL, SATURDAY, 0, 2400);
        /* Easy Opening Hours */
        seed_shop.addOpening(EASY, MONDAY, 500, 2200).addOpening(EASY, TUESDAY, 500, 2200).addOpening(EASY, WEDNESDAY, 500, 2200);
        seed_shop.addOpening(EASY, THURSDAY, 500, 2200).addOpening(EASY, FRIDAY, 500, 2200).addOpening(EASY, SATURDAY, 900, 2100);
        /* Normal Opening Hours */
        seed_shop.addOpening(NORMAL, MONDAY, 600, 1900).addOpening(NORMAL, TUESDAY, 600, 1900).addOpening(NORMAL, THURSDAY, 600, 1900);
        seed_shop.addOpening(NORMAL, FRIDAY, 600, 1900).addOpening(NORMAL, SATURDAY, 1000, 1800);
        /* Hard Opening Hours */
        seed_shop.addOpening(HARD, MONDAY, 900, 1700).addOpening(HARD, TUESDAY, 900, 1700).addOpening(HARD, THURSDAY, 900, 1700);
        seed_shop.addOpening(HARD, FRIDAY, 900, 1700).addOpening(HARD, SATURDAY, 1100, 1500);
        for (Crop crop : Crop.crops) {
            seed_shop.addItem(new PurchaseableCropSeeds(crop));
        }

        HMNPCs.seed_owner.setShop(seed_shop);
    }
}
