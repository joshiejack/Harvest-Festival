package joshie.harvest.npc.town;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.blocks.BlockPreview.Direction;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.core.helpers.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class TownBuilding {
    public IBuilding building;
    public Direction direction;
    public BlockPos pos;

    public TownBuilding() {}
    public TownBuilding(IBuilding building, Direction direction, BlockPos pos) {
        this.building = building;
        this.direction = direction;
        this.pos = pos;
    }

    public BlockPos getRealCoordinatesFor(Placeable placeable) {
        return placeable.getTransformedPosition(pos, direction);
    }

    public BlockPos getRealCoordinatesFor(String npc_location) {
        PlaceableNPC offsets = building.getProvider().getNPCOffset(npc_location);
        if (offsets == null) return null;
        return getRealCoordinatesFor(offsets);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        building = HFApi.buildings.getBuildingFromName(new ResourceLocation(nbt.getString("Building")));
        direction = Direction.valueOf(nbt.getString("Direction"));
        pos = NBTHelper.readBlockPos("Building", nbt);
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("Building", building.getResource().toString());
        nbt.setString("Direction", direction.name());
        NBTHelper.writeBlockPos("Building", nbt, pos);
    }
}