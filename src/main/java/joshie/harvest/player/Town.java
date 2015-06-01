package joshie.harvest.player;

import java.util.HashMap;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.core.util.IData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class Town implements IData {
    public HashMap<String, TownBuilding> buildings = new HashMap();

    public Town(PlayerDataServer master) {}
    
    public void addBuilding(World world, BuildingStage building) {
        buildings.put(building.building.getName(), new TownBuilding(building, world.provider.dimensionId));
    }

    public static class TownBuilding extends BuildingStage {
        private int dimension;

        public TownBuilding() {}

        public TownBuilding(BuildingStage building, int dimensionId) {
            this.building = building.building;
            this.n1 = building.n1;
            this.n2 = building.n2;
            this.swap = building.swap;
            this.dimension = dimensionId;
            this.xCoord = building.xCoord;
            this.yCoord = building.yCoord;
            this.zCoord = building.zCoord;
        }

        public WorldLocation getRealCoordinatesFor(String npc_location) {
            PlaceableNPC offsets = building.npc_offsets.get(npc_location);
            if (offsets == null) return null;
            
            int y = offsets.getY();
            int x = n1 ? -offsets.getX() : offsets.getX();
            int z = n2 ? -offsets.getZ() : offsets.getZ();
            if (swap) {
                int xClone = x; //Create a copy of X
                x = z; //Set x to z
                z = xClone; //Set z to the old value of x
            }

            return new WorldLocation(dimension, xCoord + x, yCoord + y, zCoord + z);
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {
            super.readFromNBT(nbt);
            dimension = nbt.getInteger("Dimension");
        }

        @Override
        public void writeToNBT(NBTTagCompound nbt) {
            super.writeToNBT(nbt);
            nbt.setInteger("Dimension", dimension);
        }
    }
    
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

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("TownBuildingList", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            String name = tag.getString("BuildingKey");
            TownBuilding building = new TownBuilding();
            building.readFromNBT(tag);
            buildings.put(name, building);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (String name : buildings.keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("BuildingKey", name);
            buildings.get(name).writeToNBT(tag);
            list.appendTag(tag);
        }

        nbt.setTag("TownBuildingList", list);
    }
}
