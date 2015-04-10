package joshie.harvestmoon.init;

import static joshie.harvestmoon.api.core.Weekday.FRIDAY;
import static joshie.harvestmoon.api.core.Weekday.MONDAY;
import static joshie.harvestmoon.api.core.Weekday.SATURDAY;
import static joshie.harvestmoon.api.core.Weekday.THURSDAY;
import static joshie.harvestmoon.api.core.Weekday.TUESDAY;
import static joshie.harvestmoon.api.core.Weekday.WEDNESDAY;
import static net.minecraft.world.EnumDifficulty.EASY;
import static net.minecraft.world.EnumDifficulty.HARD;
import static net.minecraft.world.EnumDifficulty.NORMAL;
import static net.minecraft.world.EnumDifficulty.PEACEFUL;
import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.shops.IShop;
import joshie.harvestmoon.items.ItemGeneral;
import joshie.harvestmoon.shops.Purchaseable;
import joshie.harvestmoon.shops.PurchaseableBlueFeather;
import joshie.harvestmoon.shops.ShopInventoryGui;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class HMShops {
    public static IShop barn;
    public static IShop cafe;
    public static IShop poultry;
    public static IShop supermarket;
    public static boolean isClient;

    public static void init() {
        barn();
        cafe();
        poultry();
        supermarket();
        
        isClient = FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
    }
    
    private static void barn() {
        barn = HMApi.SHOPS.newShop("barn", HMNPCs.animal_owner);
        if (isClient) {
            barn.setGuiOverlay(new ShopInventoryGui(67));
        }
    }
    
    private static void cafe() {
        cafe = HMApi.SHOPS.newShop("cafe", HMNPCs.cafe_owner);
        if (isClient) {
            cafe.setGuiOverlay(new ShopInventoryGui(100));
        }
    }
    
    private static void poultry() {
        poultry = HMApi.SHOPS.newShop("poultry", HMNPCs.poultry);
        if (isClient) {
            poultry.setGuiOverlay(new ShopInventoryGui(34));
        }
    }

    private static void supermarket() {
        supermarket = HMApi.SHOPS.newShop("general", HMNPCs.gs_owner);
        if (isClient) {
            supermarket.setGuiOverlay(new ShopInventoryGui(166));
        }
        
        supermarket.addItem(new Purchaseable(100, new ItemStack(HMItems.general, 1, ItemGeneral.CHOCOLATE)));
        supermarket.addItem(new Purchaseable(50, new ItemStack(HMItems.general, 1, ItemGeneral.OIL)));
        supermarket.addItem(new Purchaseable(100, new ItemStack(HMItems.general, 1, ItemGeneral.RICEBALL)));
        supermarket.addItem(new PurchaseableBlueFeather(1000, new ItemStack(HMItems.general, 1, ItemGeneral.BLUE_FEATHER)));

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
    }
}
