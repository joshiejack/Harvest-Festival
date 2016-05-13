package joshie.harvest.api.buildings;

import net.minecraft.util.ResourceLocation;

import java.util.Collection;

public interface IBuildingRegistry {
     Collection<IBuilding> getBuildings();
    IBuilding registerBuilding(IBuilding building);
    IBuilding getBuildingFromName(ResourceLocation building);
    IBuilding registerBuilding(ResourceLocation resource);
}
