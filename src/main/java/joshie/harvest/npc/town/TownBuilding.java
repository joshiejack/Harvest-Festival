package joshie.harvest.npc.town;

import joshie.harvest.core.util.Direction;
import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.core.helpers.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class TownBuilding {
    public Building building;
    public Direction direction;
    public BlockPos pos;

    public TownBuilding() {}
    public TownBuilding(Building building, Direction direction, BlockPos pos) {
        this.building = building;
        this.direction = direction;
        this.pos = pos;
    }

    public BlockPos getRealCoordinatesFor(Placeable placeable) {
        return placeable.getTransformedPosition(pos, direction);
    }

    public BlockPos getRealCoordinatesFor(String npc_location) {
        PlaceableNPC offsets = building.getNPCOffset(npc_location);
        if (offsets == null) return null;
        return getRealCoordinatesFor(offsets);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        building = BuildingRegistry.REGISTRY.getObject(new ResourceLocation(nbt.getString("Building")));
        direction = Direction.valueOf(nbt.getString("Direction"));
        pos = NBTHelper.readBlockPos("Building", nbt);
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("Building", BuildingRegistry.REGISTRY.getNameForObject(building).toString());
        nbt.setString("Direction", direction.name());
        NBTHelper.writeBlockPos("Building", nbt, pos);
    }
}