package joshie.harvest.buildings;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.core.util.base.FMLDefinition;
import joshie.harvest.core.util.base.ItemHFFML;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

public class HFBuildings {
    public static final ItemHFFML STRUCTURES = new ItemBuilding().setUnlocalizedName("structures");

    public static final Building null_building = new Building();
    public static final IBuilding BARN = registerBuilding("barn", 3000L, 160, 0).setRequirements("blacksmith");
    public static final IBuilding BLACKSMITH = registerBuilding("blacksmith", 3500L, 32, 244).setRequirements("supermarket").setOffsetY(-2);
    public static final IBuilding CAFE = registerBuilding("cafe", 8800L, 320, 160).setRequirements("miningHill", "miningHut", "goddessPond");
    public static final IBuilding CARPENTER = registerBuilding("carpenter", 0L, 0, 0);
    public static final IBuilding CHURCH = registerBuilding("church", 10000L, 160, 128).setRequirements("miningHill", "miningHut", "goddessPond").setOffsetY(0);
    public static final IBuilding CLOCKMAKER = registerBuilding("clockmaker", 6800L, 192, 112).setRequirements("miningHill", "miningHut", "goddessPond");
    public static final IBuilding FISHING_HOLE = registerBuilding("fishingHole", 1000L, 16, 0).setRequirements("fishingHut").setOffsetY(0);
    public static final IBuilding FISHING_HUT = registerBuilding("fishingHut", 6000L, 96, 0).setRequirements("miningHill", "miningHut", "goddessPond");
    public static final IBuilding GODDESS_POND = registerBuilding("goddessPond", 250L, 32, 0).setOffsetY(0);
    public static final IBuilding MINING_HILL = registerBuilding("miningHill", 1000L, 0, 64).setRequirements("miningHut").setOffsetY(-4);
    public static final IBuilding MINING_HUT = registerBuilding("miningHut", 3000L, 96, 96).setRequirements("poultryFarm", "barn");
    public static final IBuilding POULTRY_FARM = registerBuilding("poultryFarm", 2000L, 160, 0).setRequirements("blacksmith").setOffsetY(0);
    public static final IBuilding SUPERMARKET = registerBuilding("supermarket", 1280L, 512, 320).setRequirements("carpenter").setOffsetY(-10).setTickTime(5);
    public static final IBuilding TOWNHALL = registerBuilding("townhall", 16400L, 768, 256).setRequirements("miningHill", "miningHut", "goddessPond");

    @SideOnly(Side.CLIENT)
    private static FMLDefinition definition;

    public static void preInit() {
        HarvestFestival.LOGGER.log(Level.INFO, "Creating Harvest Festival Buildings!");
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        definition = new FMLDefinition<>(BuildingRegistry.REGISTRY);
        ModelLoader.setCustomMeshDefinition(STRUCTURES, definition);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        for (Building building : BuildingRegistry.REGISTRY.getValues()) {
            ModelResourceLocation model = new ModelResourceLocation(new ResourceLocation(building.getRegistryName().getResourceDomain(), "buildings/" + building.getRegistryName().getResourcePath()), "inventory");
            ModelBakery.registerItemVariants(STRUCTURES, model);
            definition.register(building, model);
        }
    }

    public static void init() {
        //Barn Frame
        /*ChestGenHooks.addItem(LootStrings.BARN_FRAME, new WeightedRandomChestContent(new ItemStack(Items.WHEAT), 3, 7, 10));
        ChestGenHooks.addItem(LootStrings.BARN_FRAME, new WeightedRandomChestContent(new ItemStack(Items.CARROT), 3, 7, 10));
        ChestGenHooks.addItem(LootStrings.BARN_FRAME, new WeightedRandomChestContent(new ItemStack(Items.LEAD), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.BARN_FRAME, new WeightedRandomChestContent(new ItemStack(Items.CARROT_ON_A_STICK), 1, 1, 1));
        ChestGenHooks.addItem(LootStrings.BARN_FRAME, new WeightedRandomChestContent(new ItemStack(HFItems.general, 1, ItemGeneral.BRUSH), 1, 1, 3));
        ChestGenHooks.addItem(LootStrings.BARN_FRAME, new WeightedRandomChestContent(new ItemStack(HFItems.general, 1, ItemGeneral.MEDICINE), 1, 3, 1));
        ChestGenHooks.addItem(LootStrings.BARN_FRAME, new WeightedRandomChestContent(HFCrops.grass.getCropStack(), 4, 9, 6));*/

        //Blacksmith Frame
        /*ChestGenHooks.addItem(LootStrings.BLACKSMITH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.IRON_AXE), 1, 1, 10));
        ChestGenHooks.addItem(LootStrings.BLACKSMITH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.IRON_SWORD), 1, 1, 3));
        ChestGenHooks.addItem(LootStrings.BLACKSMITH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.IRON_SHOVEL), 1, 1, 6));
        ChestGenHooks.addItem(LootStrings.BLACKSMITH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.IRON_PICKAXE), 1, 1, 1));*/

        //Blacksmith Chest
        /*ChestGenHooks.addItem(LootStrings.BLACKSMITH_CHEST, new WeightedRandomChestContent(new ItemStack(Items.IRON_INGOT), 2, 5, 4));
        ChestGenHooks.addItem(LootStrings.BLACKSMITH_CHEST, new WeightedRandomChestContent(new ItemStack(Items.LAVA_BUCKET), 1, 2, 2));
        ChestGenHooks.addItem(LootStrings.BLACKSMITH_CHEST, new WeightedRandomChestContent(new ItemStack(Items.COAL), 3, 33, 6));
        ChestGenHooks.addItem(LootStrings.BLACKSMITH_CHEST, new WeightedRandomChestContent(new ItemStack(Items.LEATHER), 13, 64, 10));*/

        //Church Frame
        /*ChestGenHooks.addItem(LootStrings.CHURCH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.BOOK), 3, 9, 10));
        ChestGenHooks.addItem(LootStrings.CHURCH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.GOLD_INGOT), 1, 7, 2));
        ChestGenHooks.addItem(LootStrings.CHURCH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.GOLD_NUGGET), 1, 15, 8));
        ChestGenHooks.addItem(LootStrings.CHURCH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.EXPERIENCE_BOTTLE), 1, 7, 10));
        ChestGenHooks.addItem(LootStrings.CHURCH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.SPECKLED_MELON), 1, 3, 3));
        ChestGenHooks.addItem(LootStrings.CHURCH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.POTIONITEM, 1, 8193), 1, 1, 1));*/

        //Fishing Frame
        /*ChestGenHooks.addItem(LootStrings.FISHING_FRAME, new WeightedRandomChestContent(new ItemStack(Items.FISHING_ROD), 1, 1, 10));
        ChestGenHooks.addItem(LootStrings.FISHING_FRAME, new WeightedRandomChestContent(new ItemStack(Items.LEATHER_BOOTS), 1, 1, 5));
        ChestGenHooks.addItem(LootStrings.FISHING_FRAME, new WeightedRandomChestContent(new ItemStack(Items.POTIONITEM, 1, 8269), 1, 1, 1));*/

        //Fishing Chest
        /*ChestGenHooks.addItem(LootStrings.FISHING_CHEST, new WeightedRandomChestContent(new ItemStack(Items.FISH, 1, 0), 3, 33, 10));
        ChestGenHooks.addItem(LootStrings.FISHING_CHEST, new WeightedRandomChestContent(new ItemStack(Items.FISH, 1, 1), 2, 22, 10));
        ChestGenHooks.addItem(LootStrings.FISHING_CHEST, new WeightedRandomChestContent(new ItemStack(Items.FISH, 1, 3), 1, 11, 5));
        ChestGenHooks.addItem(LootStrings.FISHING_CHEST, new WeightedRandomChestContent(new ItemStack(Items.FISH, 1, 2), 1, 3, 1));*/

        //Poultry Frame
        /*ChestGenHooks.addItem(LootStrings.POULTRY_FRAME, new WeightedRandomChestContent(HFCrops.wheat.getCropStack(), 3, 9, 5));
        ChestGenHooks.addItem(LootStrings.POULTRY_FRAME, new WeightedRandomChestContent(HFCrops.wheat.getSeedStack(), 1, 3, 1));*/

        //Poultry Chest
        /*ChestGenHooks.addItem(LootStrings.POULTRY_CHEST, new WeightedRandomChestContent(new ItemStack(HFItems.general, 1, ItemGeneral.CHICKEN_FEED), 8, 24, 10));
        ChestGenHooks.addItem(LootStrings.POULTRY_CHEST, new WeightedRandomChestContent(new ItemStack(HFItems.treats, 1, ItemTreat.CHICKEN), 1, 2, 5));
        ChestGenHooks.addItem(LootStrings.POULTRY_CHEST, new WeightedRandomChestContent(new ItemStack(HFItems.egg, 1, 0), 1, 2, 5));*/

        //Mining Frame
        /*ChestGenHooks.addItem(LootStrings.MINING_FRAME, new WeightedRandomChestContent(new ItemStack(Items.STONE_PICKAXE), 1, 1, 20));
        ChestGenHooks.addItem(LootStrings.MINING_FRAME, new WeightedRandomChestContent(new ItemStack(Items.IRON_PICKAXE), 1, 1, 5));
        ChestGenHooks.addItem(LootStrings.MINING_FRAME, new WeightedRandomChestContent(new ItemStack(Items.GOLDEN_PICKAXE), 1, 1, 3));
        ChestGenHooks.addItem(LootStrings.MINING_FRAME, new WeightedRandomChestContent(new ItemStack(Blocks.TORCH), 7, 21, 10));
        ChestGenHooks.addItem(LootStrings.MINING_FRAME, new WeightedRandomChestContent(new ItemStack(Blocks.TNT), 2, 3, 4));*/

        //Mining Chest
        /*ChestGenHooks.addItem(LootStrings.MINING_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.IRON_ORE), 1, 3, 10));
        ChestGenHooks.addItem(LootStrings.MINING_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.DIAMOND_ORE), 1, 1, 1));
        ChestGenHooks.addItem(LootStrings.MINING_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.GOLD_ORE), 1, 2, 3));
        ChestGenHooks.addItem(LootStrings.MINING_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.COAL_ORE), 2, 7, 10));
        ChestGenHooks.addItem(LootStrings.MINING_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.LAPIS_ORE), 1, 3, 5));
        ChestGenHooks.addItem(LootStrings.MINING_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.STONE), 7, 21, 10));*/

        //Cafe Frame
        /*ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.MUSHROOM_STEW), 1, 1, 10));
        ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.COOKED_BEEF), 1, 3, 15));
        ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.CAKE), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.PUMPKIN_PIE), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.BAKED_POTATO), 1, 2, 6));
        ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.BREAD), 2, 3, 7));*/

        //Cafe Chest
        /*ChestGenHooks.addItem(LootStrings.CAFE_CHEST, new WeightedRandomChestContent(new ItemStack(Items.SUGAR), 7, 21, 10));
        ChestGenHooks.addItem(LootStrings.CAFE_CHEST, new WeightedRandomChestContent(new ItemStack(Items.APPLE), 5, 11, 8));
        ChestGenHooks.addItem(LootStrings.CAFE_CHEST, new WeightedRandomChestContent(new ItemStack(Items.BOWL), 10, 20, 4));
        ChestGenHooks.addItem(LootStrings.CAFE_CHEST, new WeightedRandomChestContent(new ItemStack(Items.DYE, 1, 3), 2, 3, 5));*/

        //Townhall Hall Frame
        /*ChestGenHooks.addItem(LootStrings.TOWNHALL_HALL_FRAME, new WeightedRandomChestContent(new ItemStack(Items.PAPER), 2, 5, 10));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_HALL_FRAME, new WeightedRandomChestContent(new ItemStack(Items.SIGN), 2, 5, 10));*/

        //Townhall Priest Frame
        /*ChestGenHooks.addItem(LootStrings.TOWNHALL_MASTER_MAYOR_FRAME, new WeightedRandomChestContent(new ItemStack(Items.WRITABLE_BOOK), 1, 3, 10));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_MASTER_MAYOR_FRAME, new WeightedRandomChestContent(new ItemStack(Items.BOOK), 1, 1, 5));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_MASTER_MAYOR_FRAME, new WeightedRandomChestContent(new ItemStack(Items.DYE, 1, 0), 3, 9, 7));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_MASTER_MAYOR_FRAME, new WeightedRandomChestContent(new ItemStack(Items.EMERALD), 1, 3, 3));*/

        //Townhall Mayor Frame
        /*ChestGenHooks.addItem(LootStrings.TOWNHALL_MASTER_MAYOR_FRAME, new WeightedRandomChestContent(new ItemStack(Items.POTIONITEM, 1, 8201), 1, 1, 10));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_MASTER_MAYOR_FRAME, new WeightedRandomChestContent(new ItemStack(Items.POTIONITEM, 1, 8233), 1, 1, 5));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_MASTER_MAYOR_FRAME, new WeightedRandomChestContent(new ItemStack(Items.POTIONITEM, 1, 8265), 1, 1, 7));*/

        //Townhall Child Frame
        /*ChestGenHooks.addItem(LootStrings.TOWNHALL_CHILD_FRAME, new WeightedRandomChestContent(new ItemStack(Items.SADDLE), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_CHILD_FRAME, new WeightedRandomChestContent(new ItemStack(Items.SUGAR), 2, 5, 15));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_CHILD_FRAME, new WeightedRandomChestContent(new ItemStack(Items.CARROT), 2, 3, 10));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_CHILD_FRAME, new WeightedRandomChestContent(new ItemStack(Items.IRON_HORSE_ARMOR), 1, 1, 3));*/

        //Townhall Teenager Frame
        /*ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.BOOK), 2, 5, 10));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.DYE, 1, 0), 7, 15, 5));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.PAPER), 6, 11, 5));*/

        //Townhall Passage Chest
        /*ChestGenHooks.addItem(LootStrings.TOWNHALL_PASSAGE_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.WEB), 1, 1, 5));
        for (int i = 0; i < 16; i++) {
            ChestGenHooks.addItem(LootStrings.TOWNHALL_PASSAGE_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.CARPET, 1, i), 2, 7, 8));
        }*/

        /*for (int i = 0; i < 4; i++) {
            ChestGenHooks.addItem(LootStrings.TOWNHALL_PASSAGE_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.LEAVES, 1, i), 3, 11, 4));
            ChestGenHooks.addItem(LootStrings.TOWNHALL_PASSAGE_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.PLANKS, 1, i), 5, 24, 10));
        }*/

        //Townhall Teenager Chest
        /*ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_CHEST, new WeightedRandomChestContent(HFCrops.pineapple.getCropStack(), 1, 1, 1));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_CHEST, new WeightedRandomChestContent(HFCrops.strawberry.getCropStack(), 2, 3, 5));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_CHEST, new WeightedRandomChestContent(new ItemStack(Items.APPLE), 1, 3, 10));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_CHEST, new WeightedRandomChestContent(new ItemStack(Items.MELON), 7, 11, 8));*/

        //Market Entry
        /*ChestGenHooks.addItem(LootStrings.MARKET_ENTRY_FRAME, new WeightedRandomChestContent(HFCrops.turnip.getSeedStack(), 1, 1, 10));
        ChestGenHooks.addItem(LootStrings.MARKET_ENTRY_FRAME, new WeightedRandomChestContent(HFCrops.potato.getSeedStack(), 1, 1, 5));
        ChestGenHooks.addItem(LootStrings.MARKET_ENTRY_FRAME, new WeightedRandomChestContent(HFCrops.cucumber.getSeedStack(), 1, 1, 1));*/

        //Market Bedroom Frame
        /*ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_FRAME, new WeightedRandomChestContent(new ItemStack(Items.BOOK), 1, 1, 3));
        ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_FRAME, new WeightedRandomChestContent(new ItemStack(Items.DYE, 1, 0), 2, 3, 10));
        ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_FRAME, new WeightedRandomChestContent(new ItemStack(Items.PAPER), 2, 3, 5));*/

        //Market Bedroom Chests
        /*ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_CHESTS, new WeightedRandomChestContent(new ItemStack(Items.MELON), 3, 7, 7));
        ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_CHESTS, new WeightedRandomChestContent(new ItemStack(Items.BREAD), 1, 3, 10));*/

        //Market Basement Chests
        /*ChestGenHooks.addItem(LootStrings.MARKET_BASEMENT_CHESTS, new WeightedRandomChestContent(new ItemStack(Items.SUGAR), 3, 7, 9));
        ChestGenHooks.addItem(LootStrings.MARKET_BASEMENT_CHESTS, new WeightedRandomChestContent(new ItemStack(HFItems.general, 1, ItemGeneral.SALT), 2, 3, 8));
        ChestGenHooks.addItem(LootStrings.MARKET_BASEMENT_CHESTS, new WeightedRandomChestContent(new ItemStack(HFItems.general, 1, ItemGeneral.CHOCOLATE), 1, 3, 7));
        ChestGenHooks.addItem(LootStrings.MARKET_BASEMENT_CHESTS, new WeightedRandomChestContent(new ItemStack(Items.BOWL), 1, 1, 5));
        ChestGenHooks.addItem(LootStrings.MARKET_BASEMENT_CHESTS, new WeightedRandomChestContent(new ItemStack(Items.DYE, 1, 15), 3, 7, 7));*/

        //Item Frame generations for Clockmaker
        /*ChestGenHooks.addItem(LootStrings.CLOCKMAKER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.REDSTONE), 3, 7, 10));
        ChestGenHooks.addItem(LootStrings.CLOCKMAKER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.GOLD_NUGGET), 2, 3, 8));
        ChestGenHooks.addItem(LootStrings.CLOCKMAKER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.GOLD_INGOT), 1, 3, 2));
        ChestGenHooks.addItem(LootStrings.CLOCKMAKER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.CLOCK), 1, 1, 5));*/

        //Chest for Clockmaker
        /*ChestGenHooks.addItem(LootStrings.CLOCKMAKER_CHEST, new WeightedRandomChestContent(new ItemStack(Items.COOKED_BEEF), 3, 7, 10));
        ChestGenHooks.addItem(LootStrings.CLOCKMAKER_CHEST, new WeightedRandomChestContent(new ItemStack(Items.COOKED_CHICKEN), 2, 3, 10));
        ChestGenHooks.addItem(LootStrings.CLOCKMAKER_CHEST, new WeightedRandomChestContent(new ItemStack(Items.COOKED_PORKCHOP), 1, 3, 10));
        ChestGenHooks.addItem(LootStrings.CLOCKMAKER_CHEST, new WeightedRandomChestContent(new ItemStack(Items.BAKED_POTATO), 1, 1, 10));*/

        //Item Frame generations for Carpenter
        /*ChestGenHooks.addItem(LootStrings.CARPENTER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.STICK), 3, 7, 10));
        ChestGenHooks.addItem(LootStrings.CARPENTER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.STONE_AXE), 2, 3, 8));
        ChestGenHooks.addItem(LootStrings.CARPENTER_FRAME, new WeightedRandomChestContent(new ItemStack(Blocks.SAPLING, 1, 5), 1, 3, 2));
        ChestGenHooks.addItem(LootStrings.CARPENTER_FRAME, new WeightedRandomChestContent(new ItemStack(Blocks.WOODEN_SLAB), 1, 1, 5));*/

        //Chest generations for Jade Chest
        /*ChestGenHooks.addItem(LootStrings.JADE_CHEST, new WeightedRandomChestContent(new ItemStack(Items.APPLE), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.JADE_CHEST, new WeightedRandomChestContent(new ItemStack(Items.CARROT), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.JADE_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.RED_FLOWER), 1, 3, 15));
        ChestGenHooks.addItem(LootStrings.JADE_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.YELLOW_FLOWER), 1, 2, 10));
        for (ICrop crop : Crop.crops) {
            ChestGenHooks.addItem(LootStrings.JADE_CHEST, new WeightedRandomChestContent(crop.getSeedStack(), 1, 1, 3));
            ChestGenHooks.addItem(LootStrings.JADE_CHEST, new WeightedRandomChestContent(crop.getCropStack(), 2, 3, 5));
        }*/

        //Chest generations for Yulif Chest
        /*ChestGenHooks.addItem(LootStrings.YULIF_CHEST, new WeightedRandomChestContent(new ItemStack(Items.PORKCHOP), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.YULIF_CHEST, new WeightedRandomChestContent(new ItemStack(Items.BEEF), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.YULIF_CHEST, new WeightedRandomChestContent(new ItemStack(Items.IRON_AXE), 1, 1, 3));
        for (int i = 0; i < 4; i++) {
            ChestGenHooks.addItem(LootStrings.YULIF_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.LOG, 1, i), 1, 5, 10));
        }*/
    }

    private static IBuilding registerBuilding(String building, long cost, int wood, int stone) {
        return HFApi.buildings.registerBuilding(new ResourceLocation("harvestfestival", building), cost, wood, stone);
    }
}