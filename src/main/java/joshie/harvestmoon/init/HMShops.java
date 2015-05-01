package joshie.harvestmoon.init;

import static joshie.harvestmoon.api.core.Weekday.FRIDAY;
import static joshie.harvestmoon.api.core.Weekday.MONDAY;
import static joshie.harvestmoon.api.core.Weekday.SATURDAY;
import static joshie.harvestmoon.api.core.Weekday.SUNDAY;
import static joshie.harvestmoon.api.core.Weekday.THURSDAY;
import static joshie.harvestmoon.api.core.Weekday.TUESDAY;
import static joshie.harvestmoon.api.core.Weekday.WEDNESDAY;
import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.api.shops.IShop;
import joshie.harvestmoon.buildings.Building;
import joshie.harvestmoon.items.ItemAnimal;
import joshie.harvestmoon.items.ItemGeneral;
import joshie.harvestmoon.shops.PurchaseableBlueFeather;
import joshie.harvestmoon.shops.PurchaseableBuilding;
import joshie.harvestmoon.shops.PurchaseableCropSeeds;
import joshie.harvestmoon.shops.PurchaseableEntity;
import joshie.harvestmoon.shops.ShopInventoryGui;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class HMShops {
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
        barn = HMApi.SHOPS.newShop("barn", HMNPCs.animal_owner);
        if (isClient) {
            barn.setGuiOverlay(new ShopInventoryGui(67));
        }
        
        barn.addItem(20, HMCrops.grass.getCropStack());
        barn.addItem(1000, new ItemStack(HMItems.general, 1, ItemGeneral.MEDICINE));
        barn.addItem(new PurchaseableEntity("Sheep", 4000, new ItemStack(HMItems.animal, 1, ItemAnimal.SHEEP), true));
        barn.addItem(new PurchaseableEntity("Cow", 5000, new ItemStack(HMItems.animal, 1, ItemAnimal.COW), true));
        barn.addItem(3000, new ItemStack(HMItems.general, 1, ItemGeneral.MIRACLE));
        barn.addOpening(MONDAY, 10000, 15000).addOpening(TUESDAY, 10000, 15000).addOpening(WEDNESDAY, 10000, 15000);
        barn.addOpening(THURSDAY, 10000, 15000).addOpening(FRIDAY, 10000, 15000).addOpening(SATURDAY, 10000, 15000);
    }
    
    private static void blacksmith() {
        blacksmith = HMApi.SHOPS.newShop("blacksmith", HMNPCs.tool_owner);
        if (isClient) {
            blacksmith.setGuiOverlay(new ShopInventoryGui(132));
        }
        
        blacksmith.addItem(800, new ItemStack(HMItems.general, 1, ItemGeneral.BRUSH));
        blacksmith.addItem(2000, new ItemStack(HMItems.general, 1, ItemGeneral.MILKER));
        blacksmith.addItem(1800, new ItemStack(Items.shears));
        blacksmith.addOpening(SUNDAY, 10000, 16000).addOpening(MONDAY, 10000, 16000).addOpening(TUESDAY, 10000, 16000);
        blacksmith.addOpening(WEDNESDAY, 10000, 16000).addOpening(FRIDAY, 10000, 16000).addOpening(SATURDAY, 10000, 16000);
    }
    
    private static void cafe() {
        cafe = HMApi.SHOPS.newShop("cafe", HMNPCs.cafe_owner);
        if (isClient) {
            cafe.setGuiOverlay(new ShopInventoryGui(100));
        }
        
        cafe.addItem(0, new ItemStack(Items.potionitem));
        cafe.addItem(300, HMApi.COOKING.getMeal("salad"));
        cafe.addItem(200, HMApi.COOKING.getMeal("cookies"));
        cafe.addItem(300, HMApi.COOKING.getMeal("juice.pineapple"));
        cafe.addItem(250, HMApi.COOKING.getMeal("corn.baked"));
        cafe.addOpening(MONDAY, 9500, 17000).addOpening(TUESDAY, 9500, 17000).addOpening(WEDNESDAY, 9500, 17000).addOpening(THURSDAY, 9500, 17000);
        cafe.addOpening(FRIDAY, 9500, 17000).addOpening(SATURDAY, 9500, 17000).addOpening(SUNDAY, 9500, 17000);
    }
    
    private static void carpenter() {
        carpenter = HMApi.SHOPS.newShop("cafe", HMNPCs.builder);
        if (isClient) {
            carpenter.setGuiOverlay(new ShopInventoryGui(100));
        }
        
        for (Building building: Building.buildings) {
            carpenter.addItem(new PurchaseableBuilding(building));
        }
        
        HMNPCs.builder.setShop(carpenter);
    }
    
    private static void poultry() {
        poultry = HMApi.SHOPS.newShop("poultry", HMNPCs.poultry);
        if (isClient) {
            poultry.setGuiOverlay(new ShopInventoryGui(34));
        }
        
        poultry.addItem(new PurchaseableEntity("Chicken", 1500, new ItemStack(HMItems.animal, 1, ItemAnimal.CHICKEN), false));
        poultry.addItem(1000, new ItemStack(HMItems.general, 1, ItemGeneral.MEDICINE));
        poultry.addItem(10, new ItemStack(HMItems.general, 1, ItemGeneral.CHICKEN_FEED));
        poultry.addOpening(MONDAY, 11000, 16000).addOpening(TUESDAY, 11000, 16000).addOpening(WEDNESDAY, 11000, 16000);
        poultry.addOpening(THURSDAY, 11000, 16000).addOpening(FRIDAY, 11000, 16000).addOpening(SATURDAY, 11000, 16000);
    }

    private static void supermarket() {
        supermarket = HMApi.SHOPS.newShop("general", HMNPCs.gs_owner);
        if (isClient) {
            supermarket.setGuiOverlay(new ShopInventoryGui(166));
        }
        
        for (ICrop crop: HMApi.CROPS.getCrops()) {
            supermarket.addItem(new PurchaseableCropSeeds(crop));
        }
        
        supermarket.addItem(100, new ItemStack(Items.bread));
        supermarket.addItem(50, new ItemStack(HMItems.general, 1, ItemGeneral.FLOUR));
        supermarket.addItem(100, new ItemStack(HMItems.general, 1, ItemGeneral.CHOCOLATE));
        supermarket.addItem(50, new ItemStack(HMItems.general, 1, ItemGeneral.OIL));
        supermarket.addItem(100, new ItemStack(HMItems.general, 1, ItemGeneral.RICEBALL));
        supermarket.addItem(25, new ItemStack(HMItems.general, 1, ItemGeneral.SALT));
        supermarket.addItem(new PurchaseableBlueFeather(1000, new ItemStack(HMItems.general, 1, ItemGeneral.BLUE_FEATHER)));
        supermarket.addOpening(MONDAY, 9000, 17000).addOpening(TUESDAY, 9000, 17000).addOpening(THURSDAY, 9000, 17000);
        supermarket.addOpening(FRIDAY, 9000, 17000).addOpening(SATURDAY, 11000, 15000);
    }
}
