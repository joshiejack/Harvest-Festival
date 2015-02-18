package joshie.harvestmoon.buildings;

import java.util.ArrayList;
import java.util.Random;

public class BuildingGroup {
    public static final ArrayList<BuildingGroup> groups = new ArrayList(50);
    private ArrayList<Building> buildings = new ArrayList();

    //List of all placeable elements
    private String name;

    public static BuildingGroup register(String string, Building... buildingsList) {
        BuildingGroup toRegister = new BuildingGroup().setName(string);
        for (Building building : buildingsList) {
            building.init();
            toRegister.buildings.add(building);
        }

        BuildingGroup.groups.add(toRegister);
        return toRegister;
    }

    public static BuildingGroup getGroup(String string) {
        for (BuildingGroup b : groups) {
            if (b.getName().equals(string)) {
                return b;
            }
        }

        return null;
    }
    
    public Building getBuilding(int key) {
        return buildings.get(key);
    }

    /** Returns a random building key for this building**/
    public int random(Random rand) {
        if (buildings.size() == 0 || buildings.size() == 1) return 0;
        return rand.nextInt(buildings.size());
    }

    /** Returns a random building in this group **/
    public Building getRandom() {
        if (buildings.size() == 0) return null;
        else return buildings.get(new Random().nextInt(buildings.size()));
    }

    public String getName() {
        return name;
    }

    public BuildingGroup setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        return name.equals(o);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
