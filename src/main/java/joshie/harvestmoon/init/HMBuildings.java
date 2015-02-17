package joshie.harvestmoon.init;

import joshie.harvestmoon.buildings.Building;
import joshie.harvestmoon.buildings.barn.BuildingBarn;
import joshie.harvestmoon.buildings.blacksmith.BuildingBlacksmith;
import joshie.harvestmoon.buildings.carpenter.BuildingCarpenter;
import joshie.harvestmoon.buildings.church.BuildingChurch;
import joshie.harvestmoon.buildings.clockmaker.BuildingClockmaker;
import joshie.harvestmoon.buildings.fishing.BuildingFishingHole;
import joshie.harvestmoon.buildings.fishing.BuildingFishingHut;
import joshie.harvestmoon.buildings.goddess.BuildingGoddess;
import joshie.harvestmoon.buildings.miner.BuildingMiningHill;
import joshie.harvestmoon.buildings.miner.BuildingMiningHut;
import joshie.harvestmoon.buildings.poultry.BuildingPoultryFarm;
import joshie.harvestmoon.buildings.supermarket.BuildingSupermarket;
import joshie.harvestmoon.buildings.townhall.BuildingTownhall;

public class HMBuildings {
    public static Building barn;
    public static Building blacksmith;
    public static Building carpenter;
    public static Building church;
    public static Building clockmaker;
    public static Building fishingHole;
    public static Building fishingHut;
    public static Building goddessPond;
    public static Building miningHill;
    public static Building miningHut;
    public static Building poultryFarm;
    public static Building supermarket;
    public static Building townhall;

    //TODO: Fix Furnace Rotations, Ladders
    public static void init() {
        barn = Building.register("barn", new BuildingBarn());
        blacksmith = Building.register("blacksmith", new BuildingBlacksmith());
        carpenter = Building.register("carpenter", new BuildingCarpenter());
        church = Building.register("church", new BuildingChurch());
        clockmaker = Building.register("clockmaker", new BuildingClockmaker());
        fishingHole = Building.register("fishingHole", new BuildingFishingHole());
        fishingHut = Building.register("fishingHut", new BuildingFishingHut());
        goddessPond = Building.register("goddessPond", new BuildingGoddess());
        miningHill = Building.register("miningHill", new BuildingMiningHill());
        miningHut = Building.register("miningHut", new BuildingMiningHut());
        poultryFarm = Building.register("poultryFarm", new BuildingPoultryFarm());
        supermarket = Building.register("supermarket", new BuildingSupermarket());
        townhall = Building.register("townhall", new BuildingTownhall());
    }
}
