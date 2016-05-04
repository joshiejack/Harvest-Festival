package joshie.harvest.shops;

import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.entity.EntityHarvestSheep;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.buildings.Building;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.items.HFItems;
import joshie.harvest.items.ItemAnimal;
import joshie.harvest.items.ItemGeneral;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.shops.purchaseable.*;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.api.calendar.Weekday.*;

public class HFShops {
    public static IShop BARN;
    public static IShop BLACKSMITH;
    public static IShop CAFE;
    public static IShop CARPENTER;
    public static IShop POULTRY;
    public static IShop SUPERMARKET;
    public static IShop MINER;

    public static void preInit() {
        registerBarn();
        registerBlacksmith();
        registerCafe();
        registerCarpenter();
        registerPoultry();
        registerSupermarket();
        registerMiner();
    }

    private static void registerBarn() {
        BARN = HFApi.SHOPS.newShop("barn", HFNPCs.ANIMAL_OWNER);
        BARN.addItem(20, HFCrops.grass.getCropStack());
        BARN.addItem(1000, new ItemStack(HFItems.GENERAL, 1, ItemGeneral.MEDICINE));
        BARN.addItem(new PurchaseableEntity(EntityHarvestSheep.class, 4000, new ItemStack(HFItems.ANIMAL, 1, ItemAnimal.SHEEP), true));
        BARN.addItem(new PurchaseableEntity(EntityHarvestCow.class, 5000, new ItemStack(HFItems.ANIMAL, 1, ItemAnimal.COW), true));
        BARN.addItem(3000, new ItemStack(HFItems.GENERAL, 1, ItemGeneral.MIRACLE));

        BARN.addOpening(MONDAY, 10000, 15000).addOpening(TUESDAY, 10000, 15000).addOpening(WEDNESDAY, 10000, 15000);
        BARN.addOpening(THURSDAY, 10000, 15000).addOpening(FRIDAY, 10000, 15000).addOpening(SATURDAY, 10000, 15000);
    }

    private static void registerBlacksmith() {
        BLACKSMITH = HFApi.SHOPS.newShop("blacksmith", HFNPCs.TOOL_OWNER);
        BLACKSMITH.addItem(800, new ItemStack(HFItems.GENERAL, 1, ItemGeneral.BRUSH));
        BLACKSMITH.addItem(2000, new ItemStack(HFItems.GENERAL, 1, ItemGeneral.MILKER));
        BLACKSMITH.addItem(1800, new ItemStack(Items.SHEARS));

        BLACKSMITH.addOpening(SUNDAY, 10000, 16000).addOpening(MONDAY, 10000, 16000).addOpening(TUESDAY, 10000, 16000);
        BLACKSMITH.addOpening(WEDNESDAY, 10000, 16000).addOpening(FRIDAY, 10000, 16000).addOpening(SATURDAY, 10000, 16000);
    }

    private static void registerCafe() {
        CAFE = HFApi.SHOPS.newShop("cafe", HFNPCs.CAFE_OWNER);
        CAFE.addItem(0, new ItemStack(Items.POTIONITEM));
        CAFE.addItem(300, HFApi.COOKING.getMeal("salad"));
        CAFE.addItem(200, HFApi.COOKING.getMeal("cookies"));
        CAFE.addItem(300, HFApi.COOKING.getMeal("juice.pineapple"));
        CAFE.addItem(250, HFApi.COOKING.getMeal("corn.baked"));

        CAFE.addOpening(MONDAY, 9500, 17000).addOpening(TUESDAY, 9500, 17000).addOpening(WEDNESDAY, 9500, 17000).addOpening(THURSDAY, 9500, 17000);
        CAFE.addOpening(FRIDAY, 9500, 17000).addOpening(SATURDAY, 9500, 17000).addOpening(SUNDAY, 9500, 17000);
    }

    private static void registerCarpenter() {
        CARPENTER = HFApi.SHOPS.newShop("carpenter", HFNPCs.BUILDER);
        for (IBuilding building : Building.buildings) {
            CARPENTER.addItem(new PurchaseableBuilding(building));
        }

        CARPENTER.addOpening(MONDAY, 11000, 16000).addOpening(TUESDAY, 11000, 16000).addOpening(WEDNESDAY, 11000, 16000);
        CARPENTER.addOpening(THURSDAY, 11000, 16000).addOpening(FRIDAY, 11000, 16000).addOpening(SUNDAY, 11000, 16000);
    }

    private static void registerPoultry() {
        POULTRY = HFApi.SHOPS.newShop("poultry", HFNPCs.POULTRY);
        POULTRY.addItem(new PurchaseableEntity(EntityChicken.class, 1500, new ItemStack(HFItems.ANIMAL, 1, ItemAnimal.CHICKEN), false));
        POULTRY.addItem(1000, new ItemStack(HFItems.GENERAL, 1, ItemGeneral.MEDICINE));
        POULTRY.addItem(10, new ItemStack(HFItems.GENERAL, 1, ItemGeneral.CHICKEN_FEED));

        POULTRY.addOpening(MONDAY, 11000, 16000).addOpening(TUESDAY, 11000, 16000).addOpening(WEDNESDAY, 11000, 16000);
        POULTRY.addOpening(THURSDAY, 11000, 16000).addOpening(FRIDAY, 11000, 16000).addOpening(SATURDAY, 11000, 16000);
    }

    private static void registerSupermarket() {
        SUPERMARKET = HFApi.SHOPS.newShop("general", HFNPCs.GS_OWNER);
        for (ICrop crop : HFApi.CROPS.getCrops()) {
            SUPERMARKET.addItem(new PurchaseableCropSeeds(crop));
        }
        SUPERMARKET.addItem(100, new ItemStack(Items.BREAD));
        SUPERMARKET.addItem(50, new ItemStack(HFItems.GENERAL, 1, ItemGeneral.FLOUR));
        SUPERMARKET.addItem(100, new ItemStack(HFItems.GENERAL, 1, ItemGeneral.CHOCOLATE));
        SUPERMARKET.addItem(50, new ItemStack(HFItems.GENERAL, 1, ItemGeneral.OIL));
        SUPERMARKET.addItem(100, new ItemStack(HFItems.GENERAL, 1, ItemGeneral.RICEBALL));
        SUPERMARKET.addItem(25, new ItemStack(HFItems.GENERAL, 1, ItemGeneral.SALT));
        SUPERMARKET.addItem(new PurchaseableBlueFeather(1000, new ItemStack(HFItems.GENERAL, 1, ItemGeneral.BLUE_FEATHER)));

        SUPERMARKET.addOpening(MONDAY, 9000, 17000).addOpening(TUESDAY, 9000, 17000).addOpening(THURSDAY, 9000, 17000);
        SUPERMARKET.addOpening(FRIDAY, 9000, 17000).addOpening(SATURDAY, 11000, 15000);
    }

    private static void registerMiner() {
        MINER = HFApi.SHOPS.newShop("miner", HFNPCs.MINER);
        MINER.addItem(new PurchaseableDirt(1000, new ItemStack(HFBlocks.DIRT, 16, 1)));
        MINER.addItem(new PurchaseableStone(1000, new ItemStack(HFBlocks.STONE, 16, 1)));

        MINER.addOpening(MONDAY, 11000, 16000).addOpening(TUESDAY, 11000, 16000).addOpening(WEDNESDAY, 11000, 16000); //You decide what time it will be open yoshie
        MINER.addOpening(THURSDAY, 11000, 16000).addOpening(FRIDAY, 11000, 16000).addOpening(SATURDAY, 11000, 16000);
    }


    @SideOnly(Side.CLIENT)
    public static void initClient() {
        BARN.setGuiOverlay(new ShopGui(67));
        BLACKSMITH.setGuiOverlay(new ShopGui(132));
        CAFE.setGuiOverlay(new ShopGui(100));
        CARPENTER.setGuiOverlay(new ShopGui(199));
        SUPERMARKET.setGuiOverlay(new ShopGui(166));
        POULTRY.setGuiOverlay(new ShopGui(34));
        MINER.setGuiOverlay(new ShopGui(15)); //Look into this yoshie
    }
}