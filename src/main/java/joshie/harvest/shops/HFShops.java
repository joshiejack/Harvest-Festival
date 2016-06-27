package joshie.harvest.shops;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.entity.EntityHarvestSheep;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.crops.Crop;
import joshie.harvest.crops.CropRegistry;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.items.HFItems;
import joshie.harvest.items.ItemGeneral;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.blocks.BlockStone;
import joshie.harvest.mining.blocks.BlockStone.Type;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.shops.purchaseable.*;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.animals.item.ItemAnimalSpawner.Spawner.*;
import static joshie.harvest.animals.item.ItemAnimalTool.Tool.*;
import static joshie.harvest.api.calendar.Weekday.*;
import static joshie.harvest.cooking.items.ItemIngredients.Ingredient.*;

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
        BARN = HFApi.shops.newShop("barn", HFNPCs.ANIMAL_OWNER);
        BARN.addItem(20, HFCrops.GRASS.getCropStack());
        BARN.addItem(1000, HFAnimals.TOOLS.getStackFromEnum(MEDICINE));
        BARN.addItem(new PurchaseableEntity(EntityHarvestSheep.class, 4000, HFAnimals.ANIMAL.getStackFromEnum(SHEEP), true));
        BARN.addItem(new PurchaseableEntity(EntityHarvestCow.class, 5000, HFAnimals.ANIMAL.getStackFromEnum(COW), true));
        BARN.addItem(3000, HFAnimals.TOOLS.getStackFromEnum(MIRACLE_POTION));

        BARN.addOpening(MONDAY, 10000, 15000).addOpening(TUESDAY, 10000, 15000).addOpening(WEDNESDAY, 10000, 15000);
        BARN.addOpening(THURSDAY, 10000, 15000).addOpening(FRIDAY, 10000, 15000).addOpening(SATURDAY, 10000, 15000);
    }

    private static void registerBlacksmith() {
        BLACKSMITH = HFApi.shops.newShop("blacksmith", HFNPCs.TOOL_OWNER);
        BLACKSMITH.addItem(800, HFAnimals.TOOLS.getStackFromEnum(BRUSH));
        BLACKSMITH.addItem(2000, HFAnimals.TOOLS.getStackFromEnum(MILKER));
        BLACKSMITH.addItem(1800, new ItemStack(Items.SHEARS));

        BLACKSMITH.addOpening(SUNDAY, 10000, 16000).addOpening(MONDAY, 10000, 16000).addOpening(TUESDAY, 10000, 16000);
        BLACKSMITH.addOpening(WEDNESDAY, 10000, 16000).addOpening(FRIDAY, 10000, 16000).addOpening(SATURDAY, 10000, 16000);
    }

    private static void registerCafe() {
        CAFE = HFApi.shops.newShop("cafe", HFNPCs.CAFE_OWNER);
        CAFE.addItem(0, new ItemStack(Items.POTIONITEM));
        CAFE.addItem(300, HFApi.cooking.getMeal("salad"));
        CAFE.addItem(200, HFApi.cooking.getMeal("cookies"));
        CAFE.addItem(300, HFApi.cooking.getMeal("juice_pineapple"));
        CAFE.addItem(250, HFApi.cooking.getMeal("corn_baked"));

        CAFE.addOpening(MONDAY, 9500, 17000).addOpening(TUESDAY, 9500, 17000).addOpening(WEDNESDAY, 9500, 17000).addOpening(THURSDAY, 9500, 17000);
        CAFE.addOpening(FRIDAY, 9500, 17000).addOpening(SATURDAY, 9500, 17000).addOpening(SUNDAY, 9500, 17000);
    }

    private static void registerCarpenter() {
        CARPENTER = HFApi.shops.newShop("carpenter", HFNPCs.BUILDER);
        for (Building building : BuildingRegistry.REGISTRY.getValues()) {
            CARPENTER.addItem(new PurchaseableBuilding(building));
        }

        CARPENTER.addOpening(MONDAY, 11000, 16000).addOpening(TUESDAY, 11000, 16000).addOpening(WEDNESDAY, 11000, 16000);
        CARPENTER.addOpening(THURSDAY, 11000, 16000).addOpening(FRIDAY, 11000, 16000).addOpening(SUNDAY, 11000, 16000);
    }

    private static void registerPoultry() {
        POULTRY = HFApi.shops.newShop("poultry", HFNPCs.POULTRY);
        POULTRY.addItem(new PurchaseableEntity(EntityChicken.class, 1500, HFAnimals.ANIMAL.getStackFromEnum(CHICKEN), false));
        POULTRY.addItem(1000, HFAnimals.TOOLS.getStackFromEnum(MEDICINE));
        POULTRY.addItem(10, HFAnimals.TOOLS.getStackFromEnum(CHICKEN_FEED));

        POULTRY.addOpening(MONDAY, 11000, 16000).addOpening(TUESDAY, 11000, 16000).addOpening(WEDNESDAY, 11000, 16000);
        POULTRY.addOpening(THURSDAY, 11000, 16000).addOpening(FRIDAY, 11000, 16000).addOpening(SATURDAY, 11000, 16000);
    }

    private static void registerSupermarket() {
        SUPERMARKET = HFApi.shops.newShop("general", HFNPCs.GS_OWNER);
        for (Crop crop : CropRegistry.REGISTRY.getValues()) {
            SUPERMARKET.addItem(new PurchaseableCropSeeds(crop));
        }

        SUPERMARKET.addItem(100, new ItemStack(Items.BREAD));
        SUPERMARKET.addItem(50, HFCooking.INGREDIENTS.getStackFromEnum(FLOUR));
        SUPERMARKET.addItem(100, HFCooking.INGREDIENTS.getStackFromEnum(CHOCOLATE));
        SUPERMARKET.addItem(50, HFCooking.INGREDIENTS.getStackFromEnum(OIL));
        SUPERMARKET.addItem(100, HFCooking.INGREDIENTS.getStackFromEnum(RICEBALL));
        SUPERMARKET.addItem(25, HFCooking.INGREDIENTS.getStackFromEnum(SALT));
        SUPERMARKET.addItem(new PurchaseableBlueFeather(1000, new ItemStack(HFItems.GENERAL, 1, ItemGeneral.BLUE_FEATHER)));

        SUPERMARKET.addOpening(MONDAY, 9000, 17000).addOpening(TUESDAY, 9000, 17000).addOpening(THURSDAY, 9000, 17000);
        SUPERMARKET.addOpening(FRIDAY, 9000, 17000).addOpening(SATURDAY, 11000, 15000);
    }

    private static void registerMiner() {
        MINER = HFApi.shops.newShop("miner", HFNPCs.MINER);
        MINER.addItem(new PurchaseableDirt(1000, new ItemStack(HFMining.DIRT, 16, 1)));

        for (Type type: BlockStone.Type.values()) {
            if (!type.isReal()) {
                MINER.addItem(new PurchaseableStone(1000, new ItemStack(HFMining.STONE, 16, type.ordinal())));
            }
        }

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