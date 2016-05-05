package joshie.harvest.api.buildings;

import net.minecraft.util.ResourceLocation;

public interface IBuildingRegistry {
    IBuilding registerBuilding(IBuilding building);
    IBuilding getBuildingFromName(ResourceLocation building);
    IBuilding registerBuilding(ResourceLocation resource);
}
