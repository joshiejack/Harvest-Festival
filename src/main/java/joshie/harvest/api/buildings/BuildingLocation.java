package joshie.harvest.api.buildings;

public class BuildingLocation {
    private final Building building;
    private final String location;
    public double distance;
    public int updatesBeforeTeleport;

    public BuildingLocation(Building building, String location) {
        this.building = building;
        this.location = location;
        this.distance = 5D;
        this.updatesBeforeTeleport = 10;
    }

    public Building getBuilding() {
        return building;
    }

    public String getLocation() {
        return location;
    }

    public BuildingLocation withDistance(double distance) {
        this.distance = distance;
        return this;
    }

    public BuildingLocation withTime(int updatesBeforeTeleport) {
        this.updatesBeforeTeleport = updatesBeforeTeleport;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuildingLocation that = (BuildingLocation) o;
        return building != null ? building.equals(that.building) : that.building == null && (location != null ? location.equals(that.location) : that.location == null);
    }

    @Override
    public int hashCode() {
        int result = building != null ? building.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
}
