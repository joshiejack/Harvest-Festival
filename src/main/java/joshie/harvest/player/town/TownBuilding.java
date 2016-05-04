package joshie.harvest.player.town;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import net.minecraft.nbt.NBTTagCompound;

public class TownBuilding extends BuildingStage {
    public int dimension;

    public TownBuilding() {}

    public TownBuilding(BuildingStage building, int dimensionId) {
        this.building = building.building;
        this.mirror = building.mirror;
        this.rotation = building.rotation;
        this.dimension = dimensionId;
        this.pos = building.pos;
    }

    public WorldLocation getRealCoordinatesFor(Placeable placeable) {
        return new WorldLocation(dimension, placeable.getTransformedPosition(pos, mirror, rotation));
    }

    public WorldLocation getRealCoordinatesFor(String npc_location) {
        PlaceableNPC offsets = building.getNPCOffset(npc_location);
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