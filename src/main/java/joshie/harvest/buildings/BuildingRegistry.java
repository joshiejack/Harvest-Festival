package joshie.harvest.buildings;

import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.buildings.IBuildingRegistry;

import java.util.HashMap;

public class BuildingRegistry implements IBuildingRegistry {
    public static final HashMap<String, IBuilding> buildings = new HashMap<String, IBuilding>();

    @Override
    public IBuilding registerBuilding(IBuilding building) {
        buildings.put(building.getName(), building);
        return building;
    }

    @Override
    public IBuilding getBuildingFromName(String name) {
        return buildings.get(name);
    }
}
