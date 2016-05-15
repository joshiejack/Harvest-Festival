package joshie.harvest.buildings;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.buildings.IBuildingRegistry;
import joshie.harvest.buildings.loader.*;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.core.helpers.ResourceLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class BuildingRegistry implements IBuildingRegistry {
    public static final FMLControlledNamespacedRegistry<Building> REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation(MODID, "buildings"), Building.class, null, 0, 32000, true, null, null, null);

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
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting().setExclusionStrategies(new SuperClassExclusionStrategy());
        builder.registerTypeAdapter(Placeable.class, new PlaceableAdapter());
        builder.registerTypeAdapter(IBlockState.class, new StateAdapter());
        builder.registerTypeAdapter(ItemStack.class, new StackAdapter());
        builder.registerTypeAdapter(ResourceLocation.class, new ResourceAdapter());
        builder.registerTypeAdapter(TextComponentString.class, new TextComponentAdapter());
        Building building = builder.create().fromJson(ResourceLoader.getBuildingResource(resource, "buildings"), Building.class);
        if (building != null) {
            building.setRegistryName(resource);
            building.initBuilding(cost, wood, stone);
            REGISTRY.register(building);
        }

        return building;
    }

    public static class SuperClassExclusionStrategy implements ExclusionStrategy {
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return field.getDeclaringClass().equals(Impl.class);
        }
    }
}
