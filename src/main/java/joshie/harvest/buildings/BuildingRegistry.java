package joshie.harvest.buildings;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.buildings.IBuildingRegistry;
import joshie.harvest.buildings.building.BuildingFestivalDebug;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.util.HFTemplate;
import joshie.harvest.core.util.ResourceLoader;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

import static joshie.harvest.buildings.HFBuildings.getGson;

@HFApiImplementation
public class BuildingRegistry implements IBuildingRegistry {
    public static final BuildingRegistry INSTANCE = new BuildingRegistry();
    private final HashMap<Building, HFTemplate> instructions = new HashMap<>();

    private BuildingRegistry() {
    }

    @Override
    @Nonnull
    public ItemStack getBlueprint(Building building) {
        return HFBuildings.BLUEPRINTS.getStackFromObject(building);
    }

    @Override
    @Nonnull
    public ItemStack getSpawner(Building building) {
        return HFBuildings.STRUCTURES.getStackFromObject(building);
    }

    @Nullable
    public HFTemplate getTemplateForBuilding(Building building) {
        if (HFCore.DEBUG_MODE && building instanceof BuildingFestivalDebug) {
            HFTemplate template = (getGson().fromJson(ResourceLoader.getJSONResource(HFBuildings.FESTIVAL_GROUNDS.getResource(), "buildings"), HFTemplate.class));
            HFTemplate festival = (getGson().fromJson(ResourceLoader.getJSONResource(building.getResource(), "festivals"), HFTemplate.class));
            template.merge(festival);
            return template; //Merged yo!
        }

        return instructions.computeIfAbsent(building, b -> (getGson().fromJson(ResourceLoader.getJSONResource(b.getResource(), "buildings"), HFTemplate.class)));
    }
}