package joshie.harvest.town.data;

import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.util.Direction;
import joshie.harvest.core.util.interfaces.INBTSerializableMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class TownBuilding implements INBTSerializableMap<ResourceLocation, TownBuilding, NBTTagCompound> {
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

    @Override
    public void buildMap(Map<ResourceLocation, TownBuilding> map) {
        map.put(BuildingRegistry.REGISTRY.getKey(building), this);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        building = BuildingRegistry.REGISTRY.getValue(new ResourceLocation(nbt.getString("Building")));
        pos = NBTHelper.readBlockPos("Building", nbt);
        //TODO: Remove in 0.7+
        if (nbt.hasKey("Direction")) {
            Direction direction = Direction.valueOf(nbt.getString("Direction"));
            rotation = direction.getRotation();
        } else rotation = Rotation.valueOf(nbt.getString("Rotation"));
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("Building", BuildingRegistry.REGISTRY.getKey(building).toString());
        nbt.setString("Rotation", rotation.name());
        NBTHelper.writeBlockPos("Building", nbt, pos);
        return nbt;
    }
}