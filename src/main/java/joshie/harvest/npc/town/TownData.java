package joshie.harvest.npc.town;

import joshie.harvest.blocks.BlockPreview.Direction;
import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.UUID;

public class TownData {
    public static final String CARPENTER_DOWNSTAIRS = "yulifhome";
    public static final String CARPENTER_DOOR = "carpenterfrontdoor";
    public static final String JADE = "jadehome";
    public static final String TIBERIUS = "tiberiushome";
    public static final String FENN = "fennhome";
    public static final String CANDICE = "candicehome";
    public static final String MARKET_TILL = "marketwork";
    public static final String MARKET_STOREFRONT = "marketfront";
    public static final String JENNI = "jennihome";
    public static final String TOWNHALL_RIGHT_WING = "townhallrightwing";
    public static final String JAMIE = "mayorhome";
    public static final String CLOE = "cloehome";
    public static final String ABI = "abihome";
    public static final String TOWNHALL_ADULT_BEDROOM = "tomashome";
    public static final String TOWNHALL_ENTRANCE = "townhallentrance";
    public static final String TOWNHALL_CENTRE = "townhallcentre";
    public static final String TOWNHALL_LEFT_WING = "townhallleftwing";
    public static final String LIARA = "liarahome";
    public static final String KATLIN = "katlinhome";
    public static final String CAFE_FRONT = "cafefront";
    public static final String CAFE_TILL = "cafetill";
    public static final String MINE_ENTRANCE = "mineentrance";
    public static final String MINER_FRONT = "mininghutfront";
    public static final String MINER_GRAVEL = "mininghutgravel";
    public static final String BRANDON = "brandonhome";
    public static final String ONDRA = "ondrahome";
    public static final String FISHING_POND = "fishingpond";
    public static final String JACOB = "jacobhome";
    public static final String THOMAS = "thomaswork";
    public static final String CHURCH_FRONT = "churchfront";
    public static final String DANIEL = "danielhome";
    public static final String BLACKSMITH_FRONT = "blacksmithfront";
    public static final String JIM = "jimhome";
    public static final String GODDESS = "goddesspond";
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

    public boolean hasBuilding(ResourceLocation resource) {
        return buildings.get(resource) != null;
    }

    public boolean hasBuildings(ResourceLocation[] buildings) {
        for (ResourceLocation building: buildings) {
            if (this.buildings.get(building) == null) return false;
        }

        return true;
    }

    public BlockPos getCoordinatesFor(Pair<ResourceLocation, String> home) {
        TownBuilding building = buildings.get(home.getKey());
        if (building == null) return null;
        return building.getRealCoordinatesFor(home.getValue());
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
