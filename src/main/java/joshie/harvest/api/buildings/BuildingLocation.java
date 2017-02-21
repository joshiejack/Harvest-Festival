package joshie.harvest.api.buildings;

import net.minecraft.util.ResourceLocation;

public class BuildingLocation {
    private final ResourceLocation building;
    private final String location;
    public double distance;
    public int ticksBeforeTeleport;

    public BuildingLocation(Building building, String location) {
        this.building = Building.REGISTRY.getKey(building);
        this.location = location;
        this.distance = 5D;
        this.ticksBeforeTeleport = 1500;
    }

    public ResourceLocation getResource() {
        return building;
    }

    public String getLocation() {
        return location;
    }

    public BuildingLocation withDistance(double distance) {
        this.distance = distance;
        return this;
    }

    public BuildingLocation withTime(int ticksBeforeTeleport) {
        this.ticksBeforeTeleport = ticksBeforeTeleport;
        return this;
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
