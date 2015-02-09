package joshie.harvestmoon.init;

import joshie.harvestmoon.buildings.Building;
import joshie.harvestmoon.buildings.BuildingTest;

public class HMBuildings {
    public static void init() {
        Building.buildings.add(new BuildingTest().setName("test").init());
    }
}
