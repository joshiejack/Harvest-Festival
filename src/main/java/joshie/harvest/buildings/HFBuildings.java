package joshie.harvest.buildings;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.Building;
import joshie.harvest.buildings.block.BlockInternalAir;
import joshie.harvest.buildings.item.ItemBlueprint;
import joshie.harvest.buildings.item.ItemBuilding;
import joshie.harvest.buildings.item.ItemCheat;
import joshie.harvest.buildings.loader.*;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.core.base.FMLDefinition;
import joshie.harvest.core.base.MeshIdentical;
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

import static joshie.harvest.core.helpers.ConfigHelper.getBoolean;
import static joshie.harvest.core.lib.LoadOrder.HFBUILDING;

@HFLoader(priority = HFBUILDING)
public class HFBuildings {
    public static final ItemBuilding STRUCTURES = new ItemBuilding().register("structures");
    public static final ItemBlueprint BLUEPRINTS = new ItemBlueprint().register("blueprint");
    public static final ItemCheat CHEAT = new ItemCheat().register("cheat");
    public static final BlockInternalAir AIR = new BlockInternalAir().register("air");

    public static final BuildingImpl null_building = new BuildingImpl();
    public static final Building BARN = registerBuilding("barn", 6000L, 100, 0).setRequirements("carpenter").setOffset(6, -1, 8);
    public static final Building BLACKSMITH = registerBuilding("blacksmith", 7000L, 16, 160).setRequirements("supermarket", "barn", "poultryFarm").setOffset(3, -2, 6);
    public static final Building CAFE = registerBuilding("cafe", 16000L, 200, 110).setRequirements("miningHill", "goddessPond").setOffset(7, -1, 10);
    public static final Building CARPENTER = registerBuilding("carpenter", 0L, 0, 0).setSpecialRules((w, p) -> false).setOffset(3, -1, 8);
    public static final Building CHURCH = registerBuilding("church", 20000L, 160, 128).setRequirements("miningHill", "goddessPond").setOffset(6, -1, 13);
    public static final Building CLOCKMAKER = registerBuilding("clockmaker", 9600L, 120, 78).setRequirements("miningHill", "goddessPond").setOffset(3, -1, 10);
    public static final Building FISHING_HOLE = registerBuilding("fishingHole", 3000L, 16, 0).setRequirements("fishingHut").setOffset(6, -4, 7);
    public static final Building FISHING_HUT = registerBuilding("fishingHut", 12000L, 64, 0).setRequirements("miningHill", "goddessPond").setOffset(4, -1, 10);
    public static final Building GODDESS_POND = registerBuilding("goddessPond", 500L, 16, 0).setRequirements("blacksmith").setOffset(11, -1, 20);
    public static final Building MINING_HILL = registerBuilding("miningHill", 2000L, 0, 32).setRequirements("miningHut").setOffset(10, -3, 11);
    public static final Building MINING_HUT = registerBuilding("miningHut", 6000L, 64, 64).setRequirements("supermarket", "barn", "poultryFarm").setOffset(10, -1, 10);
    public static final Building POULTRY_FARM = registerBuilding("poultryFarm", 4000L, 100, 0).setRequirements("carpenter").setOffset(4, -1, 12);
    public static final Building SUPERMARKET = registerBuilding("supermarket", 3000L, 192, 64).setRequirements("carpenter").setOffset(7, -10, 12).setTickTime(5);
    public static final Building TOWNHALL = registerBuilding("townhall", 32000L, 640, 256).setRequirements("blacksmith", "miningHill", "goddessPond").setOffset(10, -1, 17);

    public static void preInit() {
        HarvestFestival.LOGGER.log(Level.INFO, "Creating Harvest Festival Buildings!");
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomMeshDefinition(STRUCTURES, new FMLDefinition<>(STRUCTURES, "buildings", BuildingRegistry.REGISTRY));
        ModelLoader.setCustomMeshDefinition(BLUEPRINTS, new MeshIdentical(BLUEPRINTS));
    }

    //Reload the Building data at this stage
    public static void init() {
        for (BuildingImpl building: BuildingRegistry.REGISTRY.getValues()) {
            building.initBuilding(getBuilding(building.getRegistryName()));
        }

        //Clear out the unused Gson
        gson = null;
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        FMLDefinition.getDefinition("buildings").registerEverything();
    }

    private static Building registerBuilding(String building, long cost, int wood, int stone) {
        return HFApi.buildings.registerBuilding(new ResourceLocation("harvestfestival", building), cost, wood, stone);
    }

    private static Gson gson; //Temporary
    private static BuildingImpl getBuilding(ResourceLocation resource) {
        return getGson().fromJson(ResourceLoader.getJSONResource(resource, "buildings"), BuildingImpl.class);
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

    //Configuration
    public static boolean FULL_BUILDING_RENDER;

    public static void configure() {
        FULL_BUILDING_RENDER = getBoolean("Full Building Render", true);
    }
}