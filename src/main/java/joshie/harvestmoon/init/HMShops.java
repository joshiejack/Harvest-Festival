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
    public static ShopInventory barn;
    public static ShopInventory cafe;
    public static ShopInventory poultry;
    public static ShopInventory supermarket;

    public static void init() {
        barn();
        cafe();
        poultry();
        supermarket();
    }
    
    private static void barn() {
        barn = new ShopInventory("barn", 67);
        HMNPCs.animal_owner.setShop(barn);
    }
    
    private static void cafe() {
        barn = new ShopInventory("cafe", 100);
        HMNPCs.cafe_owner.setShop(cafe);
    }
    
    private static void poultry() {
        barn = new ShopInventory("poultry", 34);
        HMNPCs.poultry.setShop(poultry);
    }

    private static void supermarket() {
        /* Register all crop seeds to the seed shop */
        supermarket = new ShopInventory("general", 166);
        for (Crop crop : Crop.crops) {
            supermarket.addItem(new PurchaseableCropSeeds(crop));
        }

        /* Peaceful Opening Hours */
        supermarket.addOpening(PEACEFUL, MONDAY, 0, 24000).addOpening(PEACEFUL, TUESDAY, 0, 24000).addOpening(PEACEFUL, WEDNESDAY, 0, 24000);
        supermarket.addOpening(PEACEFUL, THURSDAY, 0, 24000).addOpening(PEACEFUL, FRIDAY, 0, 24000).addOpening(PEACEFUL, SATURDAY, 0, 24000);
        /* Easy Opening Hours */
        supermarket.addOpening(EASY, MONDAY, 5000, 22000).addOpening(EASY, TUESDAY, 5000, 22000).addOpening(EASY, WEDNESDAY, 5000, 22000);
        supermarket.addOpening(EASY, THURSDAY, 5000, 22000).addOpening(EASY, FRIDAY, 5000, 22000).addOpening(EASY, SATURDAY, 9000, 21000);
        /* Normal Opening Hours */
        supermarket.addOpening(NORMAL, MONDAY, 6000, 19000).addOpening(NORMAL, TUESDAY, 600, 19000).addOpening(NORMAL, THURSDAY, 600, 19000);
        supermarket.addOpening(NORMAL, FRIDAY, 6000, 19000).addOpening(NORMAL, SATURDAY, 10000, 18000);
        /* Hard Opening Hours */
        supermarket.addOpening(HARD, MONDAY, 9000, 17000).addOpening(HARD, TUESDAY, 9000, 17000).addOpening(HARD, THURSDAY, 9000, 17000);
        supermarket.addOpening(HARD, FRIDAY, 9000, 17000).addOpening(HARD, SATURDAY, 11000, 15000);
        
        HMNPCs.gs_owner.setShop(supermarket);
    }
}
