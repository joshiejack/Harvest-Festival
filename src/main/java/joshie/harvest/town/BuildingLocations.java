package joshie.harvest.town;

import joshie.harvest.api.buildings.BuildingLocation;

import static joshie.harvest.buildings.HFBuildings.*;

@SuppressWarnings("all")
public class BuildingLocations {
    public static final BuildingLocation FISHING_HUT_UPSTAIRS = new BuildingLocation(FISHING_HUT, "fisher.upstairs").withDistance(8D);
    public static final BuildingLocation FISHING_HUT_DOWNSTAIRS = new BuildingLocation(FISHING_HUT, "jacob").withDistance(8D);
    public static final BuildingLocation FISHER_LEFT = new BuildingLocation(FISHING_HUT, "fisher.left").withDistance(16D);
    public static final BuildingLocation FISHING_POND_PIER = new BuildingLocation(FISHING_HOLE, "pond.pier").withDistance(16D);
    public static final BuildingLocation FISHING_POND_BACK = new BuildingLocation(FISHING_HOLE, "pond.back").withDistance(20D);
    public static final BuildingLocation FISHING_POND_LEFT = new BuildingLocation(FISHING_HOLE, "pond.left").withDistance(20D);
    public static final BuildingLocation FISHING_POND_RIGHT = new BuildingLocation(FISHING_HOLE, "pond.right").withDistance(20D);
    public static final BuildingLocation GODDESS_POND_BACK_RIGHT = new BuildingLocation(GODDESS_POND, "goddess.right.back").withDistance(24D);
    public static final BuildingLocation GODDESS_POND_FRONT = new BuildingLocation(GODDESS_POND, "goddess.front").withDistance(24D);
    public static final BuildingLocation GODDESS_POND_FRONT_LEFT = new BuildingLocation(GODDESS_POND, "goddess.left").withDistance(24D);
    public static final BuildingLocation GODDESS_POND_FRONT_RIGHT = new BuildingLocation(GODDESS_POND, "goddess.right").withDistance(24D);
    public static final BuildingLocation GODDESS_BACK_LEFT = new BuildingLocation(GODDESS_POND, "goddess.left.back").withDistance(16D);
    public static final BuildingLocation BARN_INSIDE = new BuildingLocation(BARN, "jim").withDistance(8D);
    public static final BuildingLocation BARN_LEFT_PEN = new BuildingLocation(BARN, "barn.left").withDistance(8D);
    public static final BuildingLocation BARN_DOOR = new BuildingLocation(BARN, "barn.door").withDistance(20D);
    public static final BuildingLocation BARN_RIGHT_PEN = new BuildingLocation(BARN, "barn.right").withDistance(20D);
    public static final BuildingLocation POULTRY_CENTRE = new BuildingLocation(POULTRY_FARM, "ashlee").withDistance(8D);
    public static final BuildingLocation POULTRY_DOOR = new BuildingLocation(POULTRY_FARM, "poultry.door").withDistance(20D);
    public static final BuildingLocation TOWNHALL_LEFT = new BuildingLocation(TOWNHALL, "townhall.wing.left").withDistance(10D);
    public static final BuildingLocation TOWNHALL_RIGHT = new BuildingLocation(TOWNHALL, "townhall.wing.right").withDistance(10D);
    public static final BuildingLocation TOWNHALL_ENTRANCE = new BuildingLocation(TOWNHALL, "townhall.door").withDistance(10D);
    public static final BuildingLocation TOWNAHLLCENTRE = new BuildingLocation(TOWNHALL, "townhall.entrance").withDistance(10D);
    public static final BuildingLocation TOWNHALL_STAGE = new BuildingLocation(TOWNHALL, "jamie");
    public static final BuildingLocation TOWNHALL_TEEN = new BuildingLocation(TOWNHALL, "townhall.bedroom.teen");
    public static final BuildingLocation TOWNHALL_TEEN_BED = new BuildingLocation(TOWNHALL, "cloe");
    public static final BuildingLocation TOWNHALL_ADULT_BED = new BuildingLocation(TOWNHALL, "townhall.bedroom.adult");
    public static final BuildingLocation TOWNHALL_CHILD_BED = new BuildingLocation(TOWNHALL, "abi");
    public static final BuildingLocation CHURCH_INSIDE = new BuildingLocation(CHURCH, "thomas").withDistance(10D);
    public static final BuildingLocation CHURCH_PEW_FRONT_LEFT = new BuildingLocation(CHURCH, "church.pew.left.front").withDistance(3D);
    public static final BuildingLocation CHURCHPEWFRONTRIGHT = new BuildingLocation(CHURCH, "church.pew.right.front").withDistance(3D);
    public static final BuildingLocation CHURCH_PEW_CENTRE = new BuildingLocation(CHURCH, "church.pew.centre").withDistance(3D);
    public static final BuildingLocation CHURCHPEWBACKLEFT = new BuildingLocation(CHURCH, "church.pew.left.back").withDistance(3D);
    public static final BuildingLocation CHURCH_PEW_BACK_RIGHT = new BuildingLocation(CHURCH, "church.pew.right.back").withDistance(3D);
    public static final BuildingLocation CHURCH_RIGHT = new BuildingLocation(CHURCH, "church.right").withDistance(20D);
    public static final BuildingLocation CHURCH_LEFT = new BuildingLocation(CHURCH, "church.left").withDistance(20D);
    public static final BuildingLocation GENERAL_CUSTOMER = new BuildingLocation(SUPERMARKET, "market.customer").withDistance(16D);
    public static final BuildingLocation GENERAL_STORE_FRONT = new BuildingLocation(SUPERMARKET, "market.front").withDistance(16D);
    public static final BuildingLocation GENERALGARDEN = new BuildingLocation(SUPERMARKET, "market.garden").withDistance(16D);
    public static final BuildingLocation GENERAL_BEDROOM = new BuildingLocation(SUPERMARKET, "market.bedroom").withDistance(4D);
    public static final BuildingLocation GENERAL_BED = new BuildingLocation(SUPERMARKET, "candice").withDistance(8D);
    public static final BuildingLocation GENERAL_TILL = new BuildingLocation(SUPERMARKET, "market.till").withDistance(1.5D);
    public static final BuildingLocation CARPENTER_FRONT = new BuildingLocation(CARPENTER, "carpenter.door").withDistance(24D);
    public static final BuildingLocation CARPENTER_DOWNSTAIRS = new BuildingLocation(CARPENTER, "carpenter.downstairs").withDistance(3D);
    public static final BuildingLocation CARPENTER_UPSTAIRS = new BuildingLocation(CARPENTER, "jade").withDistance(3D);
    public static final BuildingLocation CHURCH_FRONT = new BuildingLocation(CHURCH, "church.door").withDistance(24D);
    public static final BuildingLocation MINEENTRANCE = new BuildingLocation(MINING_HILL, "mine.front").withDistance(24D);
    public static final BuildingLocation CAFETILL = new BuildingLocation(CAFE, "cafe.till").withDistance(4D);
    public static final BuildingLocation CAFE_FRONT = new BuildingLocation(CAFE, "cafe.entrance").withDistance(16D);
    public static final BuildingLocation CAFE_CUSTOMER = new BuildingLocation(CAFE, "cafe.customer").withDistance(16D);
    public static final BuildingLocation CAFE_BALCONY = new BuildingLocation(CAFE, "liara").withDistance(16D);
    public static final BuildingLocation CAFE_KITCHEN = new BuildingLocation(CAFE, "katlin").withDistance(16D);
    public static final BuildingLocation BLACKSMITHFRONT = new BuildingLocation(BLACKSMITH, "blacksmith.entrance").withDistance(16D);
    public static final BuildingLocation BLACKSMITHDOOR = new BuildingLocation(BLACKSMITH, "blacksmith.door").withDistance(20D);
    public static final BuildingLocation BLACKSMITH_FURNACE = new BuildingLocation(BLACKSMITH, "daniel").withDistance(3D);
    public static final BuildingLocation CLOCKMAKER_DOOR = new BuildingLocation(CLOCKMAKER, "clockworker.door");
    public static final BuildingLocation CLOCKMAKER_UPSTAIRS = new BuildingLocation(CLOCKMAKER, "fenn");
    public static final BuildingLocation CLOCKMAKER_DOWNSTAIRS = new BuildingLocation(CLOCKMAKER, "tiberius");
    public static final BuildingLocation PARK_STALL = new BuildingLocation(FESTIVALS, "park.stall").withDistance(1D);
    public static final BuildingLocation PARK_CAFE = new BuildingLocation(FESTIVALS, "park.cafe").withDistance(1D);
    public static final BuildingLocation PARK_TABLE = new BuildingLocation(FESTIVALS, "park.table").withDistance(5D);
    public static final BuildingLocation PARK_LEFT = new BuildingLocation(FESTIVALS, "park.left").withDistance(5D);
    public static final BuildingLocation PARK_CUSTOMER = new BuildingLocation(FESTIVALS, "park.customer").withDistance(3D);
    public static final BuildingLocation PARK_BENCH = new BuildingLocation(FESTIVALS, "park.bench").withDistance(5D);
    public static final BuildingLocation PARK_CENTRE = new BuildingLocation(FESTIVALS, "park.centre").withDistance(5D);
    public static final BuildingLocation PARK_LAMP_BACK = new BuildingLocation(FESTIVALS, "park.back.lamps").withDistance(5D);
    public static final BuildingLocation PARK_BACK_LEFT = new BuildingLocation(FESTIVALS, "park.back.left").withDistance(5D);

    //TODO: Convert to building locations vvv
    public static final String TOWNHALL_CHILD_BEDROOM = "townhall.bedroom.child";
    public static final String TOWNHALL_FRONT_OF_STAGE = "townhall.stage.front";
    public static final String TOWNHALL_LEFT_OF_STAGE = "townhall.stage.left";
    public static final String TOWNHALL_RIGHT_OF_STAGE = "townhall.stage.right";
    public static final String CAFE_DOOR = "cafe.door";
    public static final String CAFE_STAIRS = "cafe.stairs";
    public static final String MINE_STAIRS = "mine.stairs";
    public static final String MINE_RIGHT = "mine.right";
    public static final String MINE_BACK = "mine.back";
    public static final String MINER_HALL = "miner.hall";
    public static final String POULTRY_FRONT = "poultry.front";
    public static final String FISHER_RIGHT = "fisher.right";

    public static final String FISHER_DOOR = "fisher.door";
    public static final String GODDESS_BACK = "goddess.back";
    public static final String GODDESS_MIDDLE_RIGHT = "goddess.right.middle";
    public static final String GODDESS_MIDDLE_LEFT = "goddess.left.middle";
    public static final String GODDESS_WATER = "goddess.water";
}
