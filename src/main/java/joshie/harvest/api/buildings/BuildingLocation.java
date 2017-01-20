package joshie.harvest.api.buildings;

import net.minecraft.util.ResourceLocation;

public class BuildingLocation {
    private final ResourceLocation building;
    private final String location;
    private final double distance;

    public BuildingLocation(Building building, String location) {
        this.building = Building.REGISTRY.getKey(building);
        this.location = location;
        this.distance = 5D;
    }

    private BuildingLocation(BuildingLocation location, double distance) {
        this.building = location.building;
        this.location = location.location;
        this.distance = distance;
    }

    public BuildingLocation withDistance(double distance) {
        if (this.distance == distance) return this;
        else return new BuildingLocation(this, distance);
    }

    public double getDistanceRequired() {
        return distance;
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
