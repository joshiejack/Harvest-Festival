package joshie.harvest.town.data;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.util.Direction;
import joshie.harvest.core.util.HFTemplate;
import joshie.harvest.core.util.interfaces.INBTSerializableMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Map;

public class TownBuilding implements INBTSerializableMap<ResourceLocation, TownBuilding, NBTTagCompound> {
    public Building building;
    public Rotation rotation;
    public BlockPos pos;

    public TownBuilding() {}
    @SuppressWarnings("WeakerAccess")
    public TownBuilding(Building building, Rotation rotation, BlockPos pos) {
        this.building = building;
        this.rotation = rotation;
        this.pos = pos;
    }

    public Rotation getFacing() {
        return rotation;
    }

    @Nullable
    BlockPos getRealCoordinatesFor(String npc_location) {
        HFTemplate template = BuildingRegistry.INSTANCE.getTemplateForBuilding(building);
        if (template == null) return null; //Building should null out too...
        else {
            PlaceableNPC placeable = template.getNPCOffset(npc_location);
            return placeable != null ? placeable.getTransformedPosition(pos, rotation) : null;
        }
    }

    BlockPos getTransformedPosition(BlockPos target) {
        HFTemplate template = BuildingRegistry.INSTANCE.getTemplateForBuilding(building);
        if (template == null) return null; //We failed so let's null
        BlockPos adjusted = transformBlockPos(target, rotation);
        return new BlockPos(pos.getX() + adjusted.getX(), pos.getY() + adjusted.getY(), pos.getZ() + adjusted.getZ());
    }

    private BlockPos transformBlockPos(BlockPos target, Rotation rotation) {
        int i = target.getX();
        int j = target.getY();
        int k = target.getZ();
        switch (rotation)  {
            case COUNTERCLOCKWISE_90:
                return new BlockPos(k, j, -i);
            case CLOCKWISE_90:
                return new BlockPos(-k, j, i);
            case CLOCKWISE_180:
                return new BlockPos(-i, j, -k);
            default:
                return target;
        }
    }

    @Override
    public void buildMap(Map<ResourceLocation, TownBuilding> map) {
        if (building != null) map.put(building.getResource(), this);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void deserializeNBT(NBTTagCompound nbt) {
        building = Building.REGISTRY.get(new ResourceLocation(nbt.getString("Building")));
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
        nbt.setString("Building", building.getResource().toString());
        nbt.setString("Rotation", rotation.name());
        NBTHelper.writeBlockPos("Building", nbt, pos);
        return nbt;
    }
}