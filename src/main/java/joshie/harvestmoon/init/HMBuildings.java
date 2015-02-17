package joshie.harvestmoon.init;

import joshie.harvestmoon.buildings.Building;
import joshie.harvestmoon.buildings.carpenter.BuildingCarpenter;

public class HMBuildings {
    public static Building carpenter;

    public static void init() {
        carpenter = Building.register(new Building().setName("carpenter").add(new BuildingCarpenter()));
    }
}
