package joshie.harvest.player.town;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.world.World;

import java.util.HashMap;

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
    
    protected HashMap<String, TownBuilding> buildings = new HashMap();
    
    public void addBuilding(World world, BuildingStage building) {
        buildings.put(building.building.getName(), new TownBuilding(building, world.provider.dimensionId));
        HFTrackers.markDirty();
    }

    public boolean hasBuilding(IBuilding building) {
        return buildings.get(building.getName()) != null;
    }

    public WorldLocation getCoordinatesFor(IBuilding home, String npc_location) {
        TownBuilding building = buildings.get(home.getName());
        if (building == null) return null;
        return building.getRealCoordinatesFor(npc_location);
    }
}
