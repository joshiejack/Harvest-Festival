package joshie.harvestmoon.init;

import joshie.harvestmoon.buildings.Building;
import joshie.harvestmoon.buildings.carpenter.BuildingCarpenter;

public class HMBuildings {
    public static void init() {
        Building.buildings.add(new Building().setName("carpenter").add(new BuildingCarpenter()));
    }
}
