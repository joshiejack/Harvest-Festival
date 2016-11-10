package joshie.harvest.buildings;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.buildings.IBuildingRegistry;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;

@HFApiImplementation
public class BuildingRegistry implements IBuildingRegistry {
    public static final IForgeRegistry<BuildingImpl> REGISTRY = new RegistryBuilder<BuildingImpl>().setName(new ResourceLocation("harvestfestival", "buildings")).setType(BuildingImpl.class).setIDRange(0, 32000).create();
    public static final BuildingRegistry INSTANCE = new BuildingRegistry();

    private BuildingRegistry() {}

    @Override
    public Building getBuildingFromName(ResourceLocation name) {
        return REGISTRY.getValue(name);
    }

    @Override
    public ResourceLocation getNameForBuilding(Building building) {
        return REGISTRY.getKey((BuildingImpl)building);
    }

    @Override
    public Building registerBuilding(ResourceLocation resource) {
        BuildingImpl building = new BuildingImpl().setRegistryName(resource);
        REGISTRY.register(building);
        return building;
    }

    @Override //TODO: Remove in 0.7+
    @Deprecated
    public Building registerBuilding(ResourceLocation resource, long cost, int wood, int stone) {
        BuildingImpl building = new BuildingImpl().setRegistryName(resource).setCosts(cost, wood, stone);
        REGISTRY.register(building);
        return building;
    }
}
