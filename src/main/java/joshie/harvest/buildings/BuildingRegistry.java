package joshie.harvest.buildings;

import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.buildings.IBuildingRegistry;
import joshie.harvest.core.util.HFApiImplementation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;

import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFApiImplementation
public class BuildingRegistry implements IBuildingRegistry {
    public static final FMLControlledNamespacedRegistry<Building> REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation(MODID, "buildings"), Building.class, null, 0, 32000, true, null, null, null);
    public static final BuildingRegistry INSTANCE = new BuildingRegistry();

    private BuildingRegistry() {}

    @Override
    public IBuilding getBuildingFromName(ResourceLocation name) {
        return REGISTRY.getObject(name);
    }

    @Override
    public ResourceLocation getNameForBuilding(IBuilding building) {
        return REGISTRY.getNameForObject((Building)building);
    }

    @Override
    public IBuilding registerBuilding(ResourceLocation resource, long cost, int wood, int stone) {
        Building building = new Building().setRegistryName(resource).setCosts(cost, wood, stone);
        REGISTRY.register(building);
        return building;
    }
}
