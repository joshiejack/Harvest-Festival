package joshie.harvest.blocks.tiles;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.blocks.BlockPreview.Direction;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncMarker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.List;

public class TileMarker extends TileEntity {
    private boolean needsInit = true;
    private List<PlaceableBlock> clone;
    private Direction direction;
    private Building building;

    public void setBuilding(Building building) {
        this.direction = HFBlocks.PREVIEW.getEnumFromMeta(getBlockMetadata());
        this.building = building;
        this.needsInit = true;
        this.markDirty();
    }

    public List<PlaceableBlock> getList() {
        if (needsInit) {
            clone = new ArrayList<PlaceableBlock>();
            for (PlaceableBlock block: building.getPreviewList()) {
                clone.add(block.copyWithOffset(getPos(), direction));
            }
        }

        return building.getPreviewList();
    }

    public Building getBuilding() {
        return building;
    }

    public IMessage getPacket() {
        return new PacketSyncMarker(new WorldLocation(worldObj.provider.getDimension(), pos), building);
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public Packet<?> getDescriptionPacket() {
        return PacketHandler.getPacket(getPacket());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        building = BuildingRegistry.REGISTRY.getObject(new ResourceLocation(nbt.getString("Building")));
        if (nbt.hasKey("Direction")) {
            direction = Direction.valueOf(nbt.getString("Direction"));
        }

        needsInit = nbt.getBoolean("Init");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("Init", needsInit);
        nbt.setString("Direction", direction.name());
        if (building != null) {
            nbt.setString("Building", BuildingRegistry.REGISTRY.getNameForObject(building).toString());
        }
    }
}