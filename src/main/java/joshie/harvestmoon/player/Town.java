package joshie.harvestmoon.player;

import java.util.HashMap;

import joshie.harvestmoon.api.WorldLocation;
import joshie.harvestmoon.buildings.BuildingStage;
import joshie.harvestmoon.buildings.placeable.entities.PlaceableNPC;
import joshie.harvestmoon.core.util.IData;
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
            this.subType = building.subType;
            this.n1 = building.n1;
            this.n2 = building.n2;
            this.swap = building.swap;
            this.dimension = dimensionId;
            this.xCoord = building.xCoord;
            this.yCoord = building.yCoord;
            this.zCoord = building.zCoord;
        }

        public WorldLocation getRealCoordinatesFor(String npc_location) {
            PlaceableNPC offsets = building.getBuilding(subType).npc_offsets.get(npc_location);
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

    public static final String SUPERMARKET_TILL = "jennihome";
    public static final String SUPERMARKET_BEDROOM = "candicehome";
    public static final String CLOE_BEDROOM = "cloehome";
    public static final String ABI_BEDROOM = "abihome";
    public static final String TOWNHALL_STAGE = "jamihome";
    public static final String CLOCKMAKER_DOWNSTAIRS = "tiberiushome";
    public static final String CLOCKMAKER_UPSTAIRS = "fennhome";
    public static final String CARPENTER_UPSTAIRS = "jadehome";
    public static final String CAFE_KITCHEN = "katlinhome";
    public static final String CAFE_TILL = "liarahome";
    public static final String MINING_HUT = "brandonhome";

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
