package joshie.harvest.buildings;

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

import java.util.Collection;
import java.util.HashMap;

public class BuildingRegistry implements IBuildingRegistry {
    private final HashMap<ResourceLocation, IBuilding> buildings = new HashMap<ResourceLocation, IBuilding>();

    @Override
    public Collection<IBuilding> getBuildings() {
        return buildings.values();
    }

    @Override
    public IBuilding registerBuilding(IBuilding building) {
        buildings.put(building.getResource(), building);
        return building;
    }


    @Override
    public IBuilding getBuildingFromName(ResourceLocation name) {
        return buildings.get(name);
    }

    @Override
    public IBuilding registerBuilding(ResourceLocation resource) {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        builder.registerTypeAdapter(Placeable.class, new PlaceableAdapter());
        builder.registerTypeAdapter(IBlockState.class, new StateAdapter());
        builder.registerTypeAdapter(ItemStack.class, new StackAdapter());
        builder.registerTypeAdapter(ResourceLocation.class, new ResourceAdapter());
        builder.registerTypeAdapter(TextComponentString.class, new TextComponentAdapter());
        Building building = builder.create().fromJson(ResourceLoader.getJSONResource(resource, "buildings"), Building.class);
        if (building != null) {
            building.resource = resource;
            buildings.put(resource, building);
            BuildingProvider provider = new BuildingProvider();
            provider.setList(building.components);
            for (Placeable placeable: provider.getFullList()) {
                provider.addToList(placeable);
            }

            building.setProvider(provider);
            building.components = null; //Clear the memory
        }

        return building;
    }
}
