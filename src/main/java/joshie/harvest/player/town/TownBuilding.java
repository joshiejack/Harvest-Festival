package joshie.harvest.player.town;

import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class TownBuilding extends BuildingStage {
    public int dimension;

    public TownBuilding() {}

    public TownBuilding(BuildingStage building, int dimensionId) {
        this.building = building.building;
        this.direction = building.direction;
        this.dimension = dimensionId;
        this.pos = building.pos;
    }

    public BlockPos getRealCoordinatesFor(Placeable placeable) {
        return placeable.getTransformedPosition(pos, direction);
    }

    public BlockPos getRealCoordinatesFor(String npc_location) {
        PlaceableNPC offsets = building.getProvider().getNPCOffset(npc_location);
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