package joshie.harvestmoon.buildings;

public class HMBuildings {
    public static void init() {
        Building.buildings.add(new BuildingTest().setName("test").init());
    }
}
