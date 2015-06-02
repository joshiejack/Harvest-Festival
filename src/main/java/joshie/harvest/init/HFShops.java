package joshie.harvest.init;

import static joshie.harvest.api.core.Weekday.FRIDAY;
import static joshie.harvest.api.core.Weekday.MONDAY;
import static joshie.harvest.api.core.Weekday.SATURDAY;
import static joshie.harvest.api.core.Weekday.SUNDAY;
import static joshie.harvest.api.core.Weekday.THURSDAY;
import static joshie.harvest.api.core.Weekday.TUESDAY;
import static joshie.harvest.api.core.Weekday.WEDNESDAY;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.buildings.Building;
import joshie.harvest.items.ItemAnimal;
import joshie.harvest.items.ItemGeneral;
import joshie.harvest.shops.PurchaseableBlueFeather;
import joshie.harvest.shops.PurchaseableBuilding;
import joshie.harvest.shops.PurchaseableCropSeeds;
import joshie.harvest.shops.PurchaseableEntity;
import joshie.harvest.shops.ShopInventoryGui;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class HFShops {
    public static IShop barn;
    public static IShop blacksmith;
    public static IShop cafe;
    public static IShop carpenter;
    public static IShop poultry;
    public static IShop supermarket;
    public static boolean isClient;

    public static void init() {
        barn();
        blacksmith();
        cafe();
        carpenter();
        poultry();
        supermarket();
        
        isClient = FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
    }
    
    private static void barn() {
        barn = HFApi.SHOPS.newShop("barn", HFNPCs.animal_owner);
        if (isClient) {
            barn.setGuiOverlay(new ShopInventoryGui(67));
        }
        
        barn.addItem(20, HFCrops.grass.getCropStack());
        barn.addItem(1000, new ItemStack(HFItems.general, 1, ItemGeneral.MEDICINE));
        barn.addItem(new PurchaseableEntity("Sheep", 4000, new ItemStack(HFItems.animal, 1, ItemAnimal.SHEEP), true));
        barn.addItem(new PurchaseableEntity("Cow", 5000, new ItemStack(HFItems.animal, 1, ItemAnimal.COW), true));
        barn.addItem(3000, new ItemStack(HFItems.general, 1, ItemGeneral.MIRACLE));
        barn.addOpening(MONDAY, 10000, 15000).addOpening(TUESDAY, 10000, 15000).addOpening(WEDNESDAY, 10000, 15000);
        barn.addOpening(THURSDAY, 10000, 15000).addOpening(FRIDAY, 10000, 15000).addOpening(SATURDAY, 10000, 15000);
    }
    
    private static void blacksmith() {
        blacksmith = HFApi.SHOPS.newShop("blacksmith", HFNPCs.tool_owner);
        if (isClient) {
            blacksmith.setGuiOverlay(new ShopInventoryGui(132));
        }
        
        blacksmith.addItem(800, new ItemStack(HFItems.general, 1, ItemGeneral.BRUSH));
        blacksmith.addItem(2000, new ItemStack(HFItems.general, 1, ItemGeneral.MILKER));
        blacksmith.addItem(1800, new ItemStack(Items.shears));
        blacksmith.addOpening(SUNDAY, 10000, 16000).addOpening(MONDAY, 10000, 16000).addOpening(TUESDAY, 10000, 16000);
        blacksmith.addOpening(WEDNESDAY, 10000, 16000).addOpening(FRIDAY, 10000, 16000).addOpening(SATURDAY, 10000, 16000);
    }
    
    private static void cafe() {
        cafe = HFApi.SHOPS.newShop("cafe", HFNPCs.cafe_owner);
        if (isClient) {
            cafe.setGuiOverlay(new ShopInventoryGui(100));
        }
        
        cafe.addItem(0, new ItemStack(Items.potionitem));
        cafe.addItem(300, HFApi.COOKING.getMeal("salad"));
        cafe.addItem(200, HFApi.COOKING.getMeal("cookies"));
        cafe.addItem(300, HFApi.COOKING.getMeal("juice.pineapple"));
        cafe.addItem(250, HFApi.COOKING.getMeal("corn.baked"));
        cafe.addOpening(MONDAY, 9500, 17000).addOpening(TUESDAY, 9500, 17000).addOpening(WEDNESDAY, 9500, 17000).addOpening(THURSDAY, 9500, 17000);
        cafe.addOpening(FRIDAY, 9500, 17000).addOpening(SATURDAY, 9500, 17000).addOpening(SUNDAY, 9500, 17000);
    }
    
    private static void carpenter() {
        carpenter = HFApi.SHOPS.newShop("cafe", HFNPCs.builder);
        if (isClient) {
            carpenter.setGuiOverlay(new ShopInventoryGui(100));
        }
        
        for (Building building: Building.buildings) {
            carpenter.addItem(new PurchaseableBuilding(building));
        }
        
        carpenter.addOpening(MONDAY, 11000, 16000).addOpening(TUESDAY, 11000, 16000).addOpening(WEDNESDAY, 11000, 16000);
        carpenter.addOpening(THURSDAY, 11000, 16000).addOpening(FRIDAY, 11000, 16000).addOpening(SUNDAY, 11000, 16000);
    }
    
    private static void poultry() {
        poultry = HFApi.SHOPS.newShop("poultry", HFNPCs.poultry);
        if (isClient) {
            poultry.setGuiOverlay(new ShopInventoryGui(34));
        }
        
        poultry.addItem(new PurchaseableEntity("Chicken", 1500, new ItemStack(HFItems.animal, 1, ItemAnimal.CHICKEN), false));
        poultry.addItem(1000, new ItemStack(HFItems.general, 1, ItemGeneral.MEDICINE));
        poultry.addItem(10, new ItemStack(HFItems.general, 1, ItemGeneral.CHICKEN_FEED));
        poultry.addOpening(MONDAY, 11000, 16000).addOpening(TUESDAY, 11000, 16000).addOpening(WEDNESDAY, 11000, 16000);
        poultry.addOpening(THURSDAY, 11000, 16000).addOpening(FRIDAY, 11000, 16000).addOpening(SATURDAY, 11000, 16000);
    }

    private static void supermarket() {
        supermarket = HFApi.SHOPS.newShop("general", HFNPCs.gs_owner);
        if (isClient) {
            supermarket.setGuiOverlay(new ShopInventoryGui(166));
        }
        
        for (ICrop crop: HFApi.CROPS.getCrops()) {
            supermarket.addItem(new PurchaseableCropSeeds(crop));
        }
        
        supermarket.addItem(100, new ItemStack(Items.bread));
        supermarket.addItem(50, new ItemStack(HFItems.general, 1, ItemGeneral.FLOUR));
        supermarket.addItem(100, new ItemStack(HFItems.general, 1, ItemGeneral.CHOCOLATE));
        supermarket.addItem(50, new ItemStack(HFItems.general, 1, ItemGeneral.OIL));
        supermarket.addItem(100, new ItemStack(HFItems.general, 1, ItemGeneral.RICEBALL));
        supermarket.addItem(25, new ItemStack(HFItems.general, 1, ItemGeneral.SALT));
        supermarket.addItem(new PurchaseableBlueFeather(1000, new ItemStack(HFItems.general, 1, ItemGeneral.BLUE_FEATHER)));
        supermarket.addOpening(MONDAY, 9000, 17000).addOpening(TUESDAY, 9000, 17000).addOpening(THURSDAY, 9000, 17000);
        supermarket.addOpening(FRIDAY, 9000, 17000).addOpening(SATURDAY, 11000, 15000);
    }
}
