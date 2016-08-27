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
    public static final IBuilding BARN = registerBuilding("barn", 3000L, 160, 0).setRequirements("carpenter").setOffset(6, -1, 8);
    public static final IBuilding BLACKSMITH = registerBuilding("blacksmith", 3500L, 32, 244).setRequirements("supermarket", "barn", "poultryFarm").setOffset(3, -2, 6);
    public static final IBuilding CAFE = registerBuilding("cafe", 8800L, 320, 160).setRequirements("miningHill", "goddessPond").setOffset(7, -1, 10);
    public static final IBuilding CARPENTER = registerBuilding("carpenter", 0L, 0, 0).setSpecialRules((w, p) -> false).setOffset(3, -1, 8);
    public static final IBuilding CHURCH = registerBuilding("church", 10000L, 160, 128).setRequirements("miningHill", "goddessPond").setOffset(4, -1, 13);
    public static final IBuilding CLOCKMAKER = registerBuilding("clockmaker", 6800L, 192, 112).setRequirements("miningHill", "goddessPond").setOffset(3, -1, 10);
    public static final IBuilding FISHING_HOLE = registerBuilding("fishingHole", 1000L, 16, 0).setRequirements("fishingHut").setOffset(3, -4, 7);
    public static final IBuilding FISHING_HUT = registerBuilding("fishingHut", 6000L, 96, 0).setRequirements("miningHill", "goddessPond").setOffset(3, -1, 10);
    public static final IBuilding GODDESS_POND = registerBuilding("goddessPond", 250L, 32, 0).setRequirements("blacksmith").setOffset(9, -1, 17);
    public static final IBuilding MINING_HILL = registerBuilding("miningHill", 1000L, 0, 64).setRequirements("miningHut").setOffset(8, -3, 11);
    public static final IBuilding MINING_HUT = registerBuilding("miningHut", 3000L, 96, 96).setRequirements("supermarket", "barn", "poultryFarm").setOffset(10, -1, 10);
    public static final IBuilding POULTRY_FARM = registerBuilding("poultryFarm", 2000L, 160, 0).setRequirements("carpenter").setOffset(4, 0, 12);
    public static final IBuilding SUPERMARKET = registerBuilding("supermarket", 1280L, 512, 320).setRequirements("carpenter").setOffset(11, -10, 12).setTickTime(5);
    public static final IBuilding TOWNHALL = registerBuilding("townhall", 16400L, 768, 256).setRequirements("blacksmith", "miningHill", "goddessPond").setOffset(10, -1, 17);

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