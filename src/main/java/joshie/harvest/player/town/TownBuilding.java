package joshie.harvest.player.town;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import net.minecraft.nbt.NBTTagCompound;

public class TownBuilding extends BuildingStage {
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
    
    public WorldLocation getRealCoordinatesFor(Placeable placeable) {
        int y = placeable.getY();
        int x = n1 ? -placeable.getX() : placeable.getX();
        int z = n2 ? -placeable.getZ() : placeable.getZ();
        if (swap) {
            int xClone = x; //Create a copy of X
            x = z; //Set x to z
            z = xClone; //Set z to the old value of x
        }

        return new WorldLocation(dimension, xCoord + x, yCoord + y, zCoord + z);
    }

    public WorldLocation getRealCoordinatesFor(String npc_location) {
        PlaceableNPC offsets = building.npc_offsets.get(npc_location);
        if (offsets == null) return null;
        return getRealCoordinatesFor(offsets);
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
