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

    @Override
    public void buildMap(Map<ResourceLocation, TownBuilding> map) {
        if (building != null) map.put(Building.REGISTRY.getKey(building), this);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void deserializeNBT(NBTTagCompound nbt) {
        building = Building.REGISTRY.getValue(new ResourceLocation(nbt.getString("Building")));
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
        nbt.setString("Building", Building.REGISTRY.getKey(building).toString());
        nbt.setString("Rotation", rotation.name());
        NBTHelper.writeBlockPos("Building", nbt, pos);
        return nbt;
    }
}