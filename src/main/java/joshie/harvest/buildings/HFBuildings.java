package joshie.harvest.buildings;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.buildings.items.ItemBlueprint;
import joshie.harvest.buildings.items.ItemBuilding;
import joshie.harvest.buildings.items.ItemCheat;
import joshie.harvest.buildings.loader.*;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.core.base.FMLDefinition;
import joshie.harvest.core.base.FMLIdentical;
import joshie.harvest.core.helpers.ResourceLoader;
import joshie.harvest.core.util.HFLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import static joshie.harvest.core.lib.LoadOrder.HFBUILDING;

@HFLoader(priority = HFBUILDING)
public class HFBuildings {
    public static final ItemBuilding STRUCTURES = new ItemBuilding().register("structures");
    public static final ItemBlueprint BLUEPRINTS = new ItemBlueprint().register("blueprint");
    public static final ItemCheat CHEAT = new ItemCheat().register("cheat");

    public static final Building null_building = new Building();
    public static final IBuilding BARN = registerBuilding("barn", 3000L, 160, 0).setRequirements("blacksmith").setOffset(6, -1, 8);
    public static final IBuilding BLACKSMITH = registerBuilding("blacksmith", 3500L, 32, 244).setRequirements("supermarket").setOffset(3, -2, 6);
    public static final IBuilding CAFE = registerBuilding("cafe", 8800L, 320, 160).setRequirements("miningHill", "miningHut", "goddessPond").setOffset(7, -1, 10);
    public static final IBuilding CARPENTER = registerBuilding("carpenter", 0L, 0, 0).setSpecialRules((w, p) -> false).setOffset(3, -1, 8);
    public static final IBuilding CHURCH = registerBuilding("church", 10000L, 160, 128).setRequirements("miningHill", "miningHut", "goddessPond").setOffset(4, -1, 13);
    public static final IBuilding CLOCKMAKER = registerBuilding("clockmaker", 6800L, 192, 112).setRequirements("miningHill", "miningHut", "goddessPond").setOffset(3, -1, 10);
    public static final IBuilding FISHING_HOLE = registerBuilding("fishingHole", 1000L, 16, 0).setRequirements("fishingHut").setOffset(3, -4, 7);
    public static final IBuilding FISHING_HUT = registerBuilding("fishingHut", 6000L, 96, 0).setRequirements("miningHill", "miningHut", "goddessPond").setOffset(3, -1, 10);
    public static final IBuilding GODDESS_POND = registerBuilding("goddessPond", 250L, 32, 0).setOffset(9, -1, 17);
    public static final IBuilding MINING_HILL = registerBuilding("miningHill", 1000L, 0, 64).setRequirements("miningHut").setOffset(8, -3, 11);
    public static final IBuilding MINING_HUT = registerBuilding("miningHut", 3000L, 96, 96).setRequirements("poultryFarm", "barn").setOffset(10, -1, 10);
    public static final IBuilding POULTRY_FARM = registerBuilding("poultryFarm", 2000L, 160, 0).setRequirements("blacksmith").setOffset(4, 0, 12);
    public static final IBuilding SUPERMARKET = registerBuilding("supermarket", 1280L, 512, 320).setRequirements("carpenter").setOffset(11, -10, 12).setTickTime(5);
    public static final IBuilding TOWNHALL = registerBuilding("townhall", 16400L, 768, 256).setRequirements("miningHill", "miningHut", "goddessPond").setOffset(10, -1, 17);

    public static void preInit() {
        HarvestFestival.LOGGER.log(Level.INFO, "Creating Harvest Festival Buildings!");
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomMeshDefinition(STRUCTURES, new FMLDefinition<>(STRUCTURES, "buildings", BuildingRegistry.REGISTRY));
        ModelLoader.setCustomMeshDefinition(BLUEPRINTS, new FMLIdentical(BLUEPRINTS));
    }

    //Reload the Building data at this stage
    public static void init() {
        for (Building building: BuildingRegistry.REGISTRY.getValues()) {
            building.initBuilding(getBuilding(building.getRegistryName()));
        }

        //Clear out the unused Gson
        gson = null;
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        FMLDefinition.getDefinition("buildings").registerEverything();
    }

    public static void postInit() {
        //Blacksmith Frame
        /*ChestGenHooks.addItem(LootStrings.BLACKSMITH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.IRON_AXE), 1, 1, 10));
        ChestGenHooks.addItem(LootStrings.BLACKSMITH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.IRON_SWORD), 1, 1, 3));
        ChestGenHooks.addItem(LootStrings.BLACKSMITH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.IRON_SHOVEL), 1, 1, 6));
        ChestGenHooks.addItem(LootStrings.BLACKSMITH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.IRON_PICKAXE), 1, 1, 1));*/

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

        //Poultry Frame
        /*ChestGenHooks.addItem(LootStrings.POULTRY_FRAME, new WeightedRandomChestContent(HFCrops.wheat.getCropStack(), 3, 9, 5));
        ChestGenHooks.addItem(LootStrings.POULTRY_FRAME, new WeightedRandomChestContent(HFCrops.wheat.getSeedStack(), 1, 3, 1));*/

        //Mining Frame
        /*ChestGenHooks.addItem(LootStrings.MINING_FRAME, new WeightedRandomChestContent(new ItemStack(Items.STONE_PICKAXE), 1, 1, 20));
        ChestGenHooks.addItem(LootStrings.MINING_FRAME, new WeightedRandomChestContent(new ItemStack(Items.IRON_PICKAXE), 1, 1, 5));
        ChestGenHooks.addItem(LootStrings.MINING_FRAME, new WeightedRandomChestContent(new ItemStack(Items.GOLDEN_PICKAXE), 1, 1, 3));
        ChestGenHooks.addItem(LootStrings.MINING_FRAME, new WeightedRandomChestContent(new ItemStack(Blocks.TORCH), 7, 21, 10));
        ChestGenHooks.addItem(LootStrings.MINING_FRAME, new WeightedRandomChestContent(new ItemStack(Blocks.TNT), 2, 3, 4));*/

        //Cafe Frame
        /*ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.MUSHROOM_STEW), 1, 1, 10));
        ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.COOKED_BEEF), 1, 3, 15));
        ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.CAKE), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.PUMPKIN_PIE), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.BAKED_POTATO), 1, 2, 6));
        ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.BREAD), 2, 3, 7));*/

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

        //Market Entry
        /*ChestGenHooks.addItem(LootStrings.MARKET_ENTRY_FRAME, new WeightedRandomChestContent(HFCrops.turnip.getSeedStack(), 1, 1, 10));
        ChestGenHooks.addItem(LootStrings.MARKET_ENTRY_FRAME, new WeightedRandomChestContent(HFCrops.potato.getSeedStack(), 1, 1, 5));
        ChestGenHooks.addItem(LootStrings.MARKET_ENTRY_FRAME, new WeightedRandomChestContent(HFCrops.cucumber.getSeedStack(), 1, 1, 1));*/

        //Market Bedroom Frame
        /*ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_FRAME, new WeightedRandomChestContent(new ItemStack(Items.BOOK), 1, 1, 3));
        ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_FRAME, new WeightedRandomChestContent(new ItemStack(Items.DYE, 1, 0), 2, 3, 10));
        ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_FRAME, new WeightedRandomChestContent(new ItemStack(Items.PAPER), 2, 3, 5));*/

        //Item Frame generations for Clockmaker
        /*ChestGenHooks.addItem(LootStrings.CLOCKMAKER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.REDSTONE), 3, 7, 10));
        ChestGenHooks.addItem(LootStrings.CLOCKMAKER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.GOLD_NUGGET), 2, 3, 8));
        ChestGenHooks.addItem(LootStrings.CLOCKMAKER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.GOLD_INGOT), 1, 3, 2));
        ChestGenHooks.addItem(LootStrings.CLOCKMAKER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.CLOCK), 1, 1, 5));*/

        //Item Frame generations for Carpenter
        /*ChestGenHooks.addItem(LootStrings.CARPENTER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.STICK), 3, 7, 10));
        ChestGenHooks.addItem(LootStrings.CARPENTER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.STONE_AXE), 2, 3, 8));
        ChestGenHooks.addItem(LootStrings.CARPENTER_FRAME, new WeightedRandomChestContent(new ItemStack(Blocks.SAPLING, 1, 5), 1, 3, 2));
        ChestGenHooks.addItem(LootStrings.CARPENTER_FRAME, new WeightedRandomChestContent(new ItemStack(Blocks.WOODEN_SLAB), 1, 1, 5));*/
    }

    private static IBuilding registerBuilding(String building, long cost, int wood, int stone) {
        return HFApi.buildings.registerBuilding(new ResourceLocation("harvestfestival", building), cost, wood, stone);
    }

    private static Gson gson; //Temporary
    private static Building getBuilding(ResourceLocation resource) {
        return getGson().fromJson(ResourceLoader.getJSONResource(resource, "buildings"), Building.class);
    }

    public static Gson getGson() {
        //Create the gson if it's null
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder().setPrettyPrinting().setExclusionStrategies(new SuperClassExclusionStrategy());
            builder.registerTypeAdapter(Placeable.class, new PlaceableAdapter());
            builder.registerTypeAdapter(IBlockState.class, new StateAdapter());
            builder.registerTypeAdapter(ItemStack.class, new StackAdapter());
            builder.registerTypeAdapter(ResourceLocation.class, new ResourceAdapter());
            builder.registerTypeAdapter(TextComponentString.class, new TextComponentAdapter());
            gson = builder.create();
        }

        return gson;
    }

    private static class SuperClassExclusionStrategy implements ExclusionStrategy {
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return field.getDeclaringClass().equals(Impl.class);
        }
    }
}