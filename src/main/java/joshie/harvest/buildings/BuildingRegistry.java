package joshie.harvest.buildings;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.buildings.IBuildingRegistry;
import joshie.harvest.core.util.HFTemplate;
import joshie.harvest.core.util.ResourceLoader;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

import static joshie.harvest.buildings.HFBuildings.getGson;

@HFApiImplementation
public class BuildingRegistry implements IBuildingRegistry {
    public static final BuildingRegistry INSTANCE = new BuildingRegistry();
    private HashMap<Building, HFTemplate> instructions = new HashMap<>();

    private BuildingRegistry() {}

    @Override
    public ItemStack getBlueprint(Building building) {
        return HFBuildings.BLUEPRINTS.getStackFromObject(building);
    }

    @Override
    public ItemStack getSpawner(Building building) {
        return HFBuildings.STRUCTURES.getStackFromObject(building);
    }

    public HFTemplate getTemplateForBuilding(Building building) {
        HFTemplate template = instructions.get(building);
        if (template == null) {
            template = (getGson().fromJson(ResourceLoader.getJSONResource(building.getRegistryName(), "buildings"), HFTemplate.class));
            instructions.put(building, template);
        }

        return template;
    }
}
