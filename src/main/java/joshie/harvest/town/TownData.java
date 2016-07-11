package joshie.harvest.town;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class TownData {
    public static final String CARPENTER_DOWNSTAIRS = "yulifhome";
    public static final String CARPENTER_DOOR = "carpenterfrontdoor";
    public static final String JADE_HOME = "jadehome";
    public static final String TIBERIUS_HOME = "tiberiushome";
    public static final String FENN_HOME = "fennhome";
    public static final String CANDICE_HOME = "candicehome";
    public static final String MARKET_TILL = "marketwork";
    public static final String MARKET_STOREFRONT = "marketfront";
    public static final String JENNI_HOME = "jennihome";
    public static final String TOWNHALL_RIGHT_WING = "townhallrightwing";
    public static final String JAMIE_HOME = "mayorhome";
    public static final String CLOE_HOME = "cloehome";
    public static final String ABI_HOME = "abihome";
    public static final String TOWNHALL_ADULT_BEDROOM = "tomashome";
    public static final String TOWNHALL_ENTRANCE = "townhallentrance";
    public static final String TOWNHALL_CENTRE = "townhallcentre";
    public static final String TOWNHALL_LEFT_WING = "townhallleftwing";
    public static final String LIARA_HOME = "liarahome";
    public static final String KATLIN_HOME = "katlinhome";
    public static final String CAFE_FRONT = "cafefront";
    public static final String CAFE_TILL = "cafetill";
    public static final String MINE_ENTRANCE = "mineentrance";
    public static final String MINER_FRONT = "mininghutfront";
    public static final String MINER_GRAVEL = "mininghutgravel";
    public static final String BRANDON_HOME = "brandonhome";
    public static final String ASHLEE_HOME = "ashleehome";
    public static final String FISHING_POND = "fishingpond";
    public static final String JACOB_HOME = "jacobhome";
    public static final String THOMAS = "thomaswork";
    public static final String CHURCH_FRONT = "churchfront";
    public static final String DANIEL_HOME = "danielhome";
    public static final String BLACKSMITH_FRONT = "blacksmithfront";
    public static final String JIM_HOME = "jimhome";
    public static final String GODDESS_HOME = "goddesspond";
    //END NPC NAME OF LOCATIONS **/
    
    protected HashMap<ResourceLocation, TownBuilding> buildings = new HashMap<>();
    protected BlockPos townCentre;
    protected UUID uuid;

    public UUID getID() {
        return uuid;
    }
    
    public void addBuilding(World world, Building building, Direction direction, BlockPos pos) {
        buildings.put(BuildingRegistry.REGISTRY.getNameForObject(building), new TownBuilding(building, direction, pos));
        HFTrackers.markDirty(world);
    }

    public boolean hasBuilding(ResourceLocation building) {
        return buildings.get(building) != null;
    }

    public boolean hasBuildings(ResourceLocation[] buildings) {
        for (ResourceLocation building: buildings) {
            if (this.buildings.get(building) == null) return false;
        }

        return true;
    }

    public BlockPos getCoordinatesFor(BuildingLocation location) {
        TownBuilding building = buildings.get(location.getResource());
        if (building == null) return null;
        return building.getRealCoordinatesFor(location.getLocation());
    }

    public Direction getFacingFor(ResourceLocation resource) {
        TownBuilding building = buildings.get(resource);
        if (building == null) return null;
        return building.getFacing();
    }

    public BlockPos getTownCentre() {
        return townCentre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TownData townData = (TownData) o;
        return uuid != null ? uuid.equals(townData.uuid) : townData.uuid == null;

    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    public TownData setUUID(UUID UUID) {
        uuid = UUID;
        return this;
    }
}
