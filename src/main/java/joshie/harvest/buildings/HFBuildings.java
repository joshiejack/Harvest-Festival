package joshie.harvest.buildings;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.buildings.block.BlockInternalAir;
import joshie.harvest.buildings.building.BuildingFestival;
import joshie.harvest.buildings.item.ItemBlueprint;
import joshie.harvest.buildings.item.ItemBuilding;
import joshie.harvest.buildings.item.ItemCheat;
import joshie.harvest.buildings.item.ItemCheat.Cheat;
import joshie.harvest.buildings.loader.*;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.render.BuildingItemRenderer;
import joshie.harvest.buildings.render.BuildingItemRenderer.BuildingTile;
import joshie.harvest.buildings.special.SpecialRuleBuildings;
import joshie.harvest.buildings.special.SpecialRuleChurch;
import joshie.harvest.buildings.special.SpecialRuleFestivals;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.base.render.BuildingDefinition;
import joshie.harvest.core.base.render.MeshIdentical;
import joshie.harvest.core.proxy.HFClientProxy;
import joshie.harvest.core.util.HFTemplate;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.npcs.HFNPCs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import java.lang.reflect.InvocationTargetException;

import static joshie.harvest.core.helpers.ConfigHelper.getBoolean;
import static joshie.harvest.core.lib.LoadOrder.HFBUILDING;
import static joshie.harvest.npcs.HFNPCs.CLOCKMAKER_CHILD;

@HFLoader(priority = HFBUILDING)
@SuppressWarnings("unchecked")
public class HFBuildings {
    public static final ItemBuilding STRUCTURES = new ItemBuilding().register("structures");
    public static final ItemBlueprint BLUEPRINTS = new ItemBlueprint().register("blueprint");

    private static final ISpecialRules NEVER = (w, p, a) -> false;
    public static final Building BARN = registerBuilding("barn").setInhabitants(HFNPCs.BARN_OWNER).setOffset(6, -1, 8);
    public static final Building BLACKSMITH = registerBuilding("blacksmith").setRequirements("miningHill").setInhabitants(HFNPCs.BLACKSMITH).setOffset(3, -2, 6);
    public static final Building CAFE = registerBuilding("cafe").setRequirements("supermarket").setInhabitants(HFNPCs.CAFE_OWNER, HFNPCs.CAFE_GRANNY).setOffset(7, -1, 10);
    public static final Building CARPENTER = registerBuilding("carpenter").setSpecialRules(NEVER).setInhabitants(HFNPCs.CARPENTER, HFNPCs.FLOWER_GIRL).setOffset(3, -1, 8);
    public static final Building CHURCH = registerBuilding("church").setSpecialRules(new SpecialRuleChurch()).setOffset(6, -1, 13);
    public static final Building CLOCKMAKER = registerBuilding("clockmaker").setRequirements("festivals").setInhabitants(HFNPCs.CLOCKMAKER, CLOCKMAKER_CHILD).setOffset(3, -1, 10);
    public static final Building FISHING_HOLE = registerBuilding("fishingHole").setRequirements("fishingHut").setOffset(5, -5, 12);
    public static final Building FISHING_HUT = registerBuilding("fishingHut").setRequirements("barn", "poultryFarm").setInhabitants(HFNPCs.FISHERMAN).setOffset(6, -1, 8);
    public static final Building GODDESS_POND = registerBuilding("goddessPond").setSpecialRules(new SpecialRuleBuildings(5)).setInhabitants(HFNPCs.GODDESS).setOffset(11, -1, 20);
    public static final Building MINING_HILL = registerBuilding("miningHill").setRequirements("supermarket").setInhabitants(HFNPCs.MINER).setOffset(10, -3, 11);
    public static final Building POULTRY_FARM = registerBuilding("poultryFarm").setInhabitants(HFNPCs.POULTRY).setOffset(4, -1, 12);
    public static final Building SUPERMARKET = registerBuilding("supermarket").setInhabitants(HFNPCs.MILKMAID, HFNPCs.GS_OWNER).setOffset(7, -1, 12).setTickTime(5);
    public static final Building TOWNHALL = registerBuilding("townhall").setSpecialRules(new SpecialRuleBuildings(9)).setInhabitants(HFNPCs.MAYOR, HFNPCs.PRIEST, HFNPCs.DAUGHTER_ADULT, HFNPCs.DAUGHTER_CHILD).setOffset(10, -1, 17);
    public static final Building FESTIVAL_GROUNDS = registerBuilding("festivals", BuildingFestival.class).setSpecialRules(new SpecialRuleFestivals()).setInhabitants(HFNPCs.TRADER).setOffset(14, -1, 32);

    //Debug mode only blocks/items
    public static ItemCheat CHEAT;
    public static BlockInternalAir AIR;
    public static void preInit() {
        if (HFCore.DEBUG_MODE) {
            AIR =  new BlockInternalAir().register("air");
            CHEAT = new ItemCheat().register("cheat");
            HFApi.npc.getGifts().addToBlacklist(CHEAT);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomMeshDefinition(STRUCTURES, new BuildingDefinition(STRUCTURES));
        ModelLoader.setCustomMeshDefinition(BLUEPRINTS, new MeshIdentical(BLUEPRINTS));
    }

    public static void init() {
        HFApi.npc.getGifts().addToBlacklist(STRUCTURES, BLUEPRINTS);
        for (Building building: Building.REGISTRY.values()) {
            HFTemplate template = BuildingRegistry.INSTANCE.getTemplateForBuilding(building);
            if (template != null) template.initTemplate();
            else HarvestFestival.LOGGER.log(Level.WARN, "Failed to load the template for the building: " + building.getResource().toString());
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        BuildingDefinition.registerEverything();
        if (HFCore.DEBUG_MODE) {
            HFClientProxy.RENDER_MAP.put(CHEAT, BuildingTile.INSTANCE);
            ClientRegistry.bindTileEntitySpecialRenderer(BuildingTile.class, new BuildingItemRenderer());
            ForgeHooksClient.registerTESRItemStack(CHEAT, Cheat.BUILDING_PREVIEW.ordinal(), BuildingTile.class);
        }
    }

    @SuppressWarnings("unchecked")
    public static <B extends Building> B registerBuilding(String name, Class<B>... clazzes) {
        Class<B> clazz = clazzes.length == 1 ? clazzes[0] : null;
        Building building = null;
        try {
            if (clazz != null) building = clazz.getConstructor(ResourceLocation.class).newInstance(new ResourceLocation("harvestfestival", name));
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) { /**/}

        if (building == null) {
            building = new Building(new ResourceLocation("harvestfestival", name));
        }

        return (B) building;
    }

    private static Gson gson; //Temporary
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
            builder.registerTypeAdapter(EnumFacing.class, new FacingAdapter());
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
    public static boolean ENABLE_DEMOLITION;

    public static void configure() {
        FULL_BUILDING_RENDER = getBoolean("Use Full Building Render", true);
        CHEAT_BUILDINGS = getBoolean("Use Cheat Buildings", false);
        ENABLE_DEMOLITION = getBoolean("Enable demolition", true);
    }
}