package joshie.harvest.town;

import joshie.harvest.core.util.Direction;
import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.core.helpers.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

public class TownBuilding {
    public BuildingImpl building;
    public Rotation rotation;
    public BlockPos pos;

    public TownBuilding() {}
    public TownBuilding(BuildingImpl building, Rotation rotation, BlockPos pos) {
        this.building = building;
        this.rotation = rotation;
        this.pos = pos;
    }

    public Rotation getFacing() {
        return rotation;
    }

    public BlockPos getRealCoordinatesFor(Placeable placeable) {
        return placeable.getTransformedPosition(pos, rotation);
    }

    public BlockPos getRealCoordinatesFor(String npc_location) {
        PlaceableNPC offsets = building.getNPCOffset(npc_location);
        if (offsets == null) return null;
        return getRealCoordinatesFor(offsets);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        building = BuildingRegistry.REGISTRY.getValue(new ResourceLocation(nbt.getString("Building")));

        //TODO: Remove in 0.7+
        if (nbt.hasKey("Direction")) {
            Direction direction = Direction.valueOf(nbt.getString("Direction"));
            rotation = direction.getRotation();
        } else rotation = Rotation.valueOf(nbt.getString("Rotation"));

        pos = NBTHelper.readBlockPos("Building", nbt);
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("Building", BuildingRegistry.REGISTRY.getKey(building).toString());
        nbt.setString("Rotation", rotation.name());
        NBTHelper.writeBlockPos("Building", nbt, pos);
    }
}