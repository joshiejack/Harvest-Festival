package joshie.harvest.buildings;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.buildings.block.BlockInternalAir;
import joshie.harvest.buildings.item.ItemBlueprint;
import joshie.harvest.buildings.item.ItemBuilding;
import joshie.harvest.buildings.item.ItemCheat;
import joshie.harvest.buildings.loader.*;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.special.SpecialRuleBuildings;
import joshie.harvest.buildings.special.SpecialRuleChurch;
import joshie.harvest.buildings.special.SpecialRuleFestivals;
import joshie.harvest.core.base.render.FMLDefinition;
import joshie.harvest.core.base.render.MeshIdentical;
import joshie.harvest.core.util.HFTemplate;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.core.helpers.ConfigHelper.getBoolean;
import static joshie.harvest.core.lib.LoadOrder.HFBUILDING;
import static joshie.harvest.npc.HFNPCs.CLOCKMAKER_CHILD;

@HFLoader(priority = HFBUILDING)
public class HFBuildings {
    public static final ItemBuilding STRUCTURES = new ItemBuilding().register("structures");
    public static final ItemBlueprint BLUEPRINTS = new ItemBlueprint().register("blueprint");
    public static final ItemCheat CHEAT = new ItemCheat().register("cheat");
    public static final BlockInternalAir AIR = new BlockInternalAir().register("air");

    public static final BuildingImpl null_building = new BuildingImpl();
    public static final ISpecialRules NEVER = (w, p, a) -> false;
    public static final Building BARN = registerBuilding("barn").setRequirements("carpenter").setInhabitants(HFNPCs.BARN_OWNER).setOffset(6, -1, 8);
    public static final Building BLACKSMITH = registerBuilding("blacksmith").setRequirements("miningHill").setInhabitants(HFNPCs.BLACKSMITH).setOffset(3, -2, 6);
    public static final Building CAFE = registerBuilding("cafe").setRequirements("supermarket").setInhabitants(HFNPCs.CAFE_OWNER, HFNPCs.CAFE_GRANNY).setOffset(7, -1, 10);
    public static final Building CARPENTER = registerBuilding("carpenter", 0L, 0, 0).setSpecialRules(NEVER).setInhabitants(HFNPCs.BUILDER, HFNPCs.FLOWER_GIRL).setOffset(3, -1, 8);
    public static final Building CHURCH = registerBuilding("church").setSpecialRules(new SpecialRuleChurch()).setOffset(6, -1, 13);
    public static final Building CLOCKMAKER = registerBuilding("clockmaker").setRequirements("festivals").setInhabitants(HFNPCs.CLOCKMAKER, CLOCKMAKER_CHILD).setOffset(3, -1, 10);
    public static final Building FISHING_HOLE = registerBuilding("fishingHole").setRequirements("fishingHut").setInhabitants(HFNPCs.FISHERMAN).setOffset(6, -4, 7);
    public static final Building FISHING_HUT = registerBuilding("fishingHut").setRequirements("barn", "poultryFarm").setOffset(4, -1, 10);
    public static final Building GODDESS_POND = registerBuilding("goddessPond").setSpecialRules(new SpecialRuleBuildings(5)).setInhabitants(HFNPCs.GODDESS).setOffset(11, -1, 20);
    public static final Building MINING_HILL = registerBuilding("miningHill").setRequirements("supermarket").setInhabitants(HFNPCs.MINER).setOffset(10, -3, 11);
    public static final Building POULTRY_FARM = registerBuilding("poultryFarm").setRequirements("carpenter").setInhabitants(HFNPCs.POULTRY).setOffset(4, -1, 12);
    public static final Building SUPERMARKET = registerBuilding("supermarket").setRequirements("carpenter").setInhabitants(HFNPCs.MILKMAID, HFNPCs.GS_OWNER).setOffset(7, -1, 12).setTickTime(5);
    public static final Building TOWNHALL = registerBuilding("townhall").setSpecialRules(new SpecialRuleBuildings(9)).setInhabitants(HFNPCs.MAYOR, HFNPCs.PRIEST, HFNPCs.DAUGHTER_ADULT, HFNPCs.DAUGHTER_CHILD).setOffset(10, -1, 17);
    //0.6+ Buildings
    public static final Building FESTIVALS = registerBuilding("festivals").setSpecialRules(new SpecialRuleFestivals()).setInhabitants(HFNPCs.TRADER);

    public static void preInit() {}

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomMeshDefinition(STRUCTURES, new FMLDefinition<>(STRUCTURES, "buildings", BuildingRegistry.REGISTRY));
        ModelLoader.setCustomMeshDefinition(BLUEPRINTS, new MeshIdentical(BLUEPRINTS));
    }

    public static void init() {
        HFApi.npc.getGifts().addToBlacklist(STRUCTURES, BLUEPRINTS);
        for (BuildingImpl building: BuildingRegistry.REGISTRY.getValues()) {
            building.initBuilding(getGson().fromJson(ResourceLoader.getJSONResource(building.getRegistryName(), "buildings"), HFTemplate.class));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        FMLDefinition.getDefinition("buildings").registerEverything();
    }

    private static Building registerBuilding(String building, long cost, int wood, int stone) {
        return HFApi.buildings.registerBuilding(new ResourceLocation("harvestfestival", building), cost, wood, stone);
    }

    private static Building registerBuilding(String building) {
        return HFApi.buildings.registerBuilding(new ResourceLocation("harvestfestival", building));
    }

    private static Gson gson; //Temporary
    public static void loadBuilding(BuildingImpl building) {
        HFTemplate template = (getGson().fromJson(ResourceLoader.getJSONResource(building.getRegistryName(), "buildings"), HFTemplate.class));
        if (template != null) {
            building.components = template.getComponents();
        }
    }

    public static Gson getGson() {
        //Create the gson if it's null
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().setExclusionStrategies(new SuperClassExclusionStrategy());
            builder.registerTypeAdapter(Placeable.class, new PlaceableAdapter());
            builder.registerTypeAdapter(IBlockState.class, new StateAdapter());
            builder.registerTypeAdapter(ItemStack.class, new StackAdapter());
            builder.registerTypeAdapter(ResourceLocation.class, new ResourceAdapter());
            builder.registerTypeAdapter(ITextComponent.class, new TextComponentAdapter());
            builder.registerTypeAdapter(BlockPos.class, new BlockPosAdapter());
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
    public static boolean CHEAT_BUILDINGS;

    public static void configure() {
        FULL_BUILDING_RENDER = getBoolean("Use Full Building Render", true);
        CHEAT_BUILDINGS = getBoolean("Use Cheat Buildings", false);
    }
}