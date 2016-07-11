package joshie.harvest.api.buildings;

import joshie.harvest.api.HFApi;
import net.minecraft.util.ResourceLocation;

public class BuildingLocation {
    private ResourceLocation building;
    private String location;

    public BuildingLocation(IBuilding building, String location) {
        this.building = HFApi.buildings.getNameForBuilding(building);
        this.location = location;
    }

    public ResourceLocation getResource() {
        return building;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuildingLocation that = (BuildingLocation) o;
        if (building != null ? !building.equals(that.building) : that.building != null) return false;
        return location != null ? location.equals(that.location) : that.location == null;

    }

    @Override
    public int hashCode() {
        int result = building != null ? building.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
}
