package joshie.harvest.api.buildings;

import net.minecraft.util.ResourceLocation;

public interface IBuildingRegistry {
    IBuilding getBuildingFromName(ResourceLocation building);
    ResourceLocation registerBuilding(ResourceLocation resource);
}
