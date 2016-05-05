package joshie.harvest.api.buildings;

public interface IBuildingRegistry {
    IBuilding registerBuilding(IBuilding building);
    IBuilding getBuildingFromName(String building);
}
