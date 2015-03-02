package joshie.harvestmoon.blocks.tiles;

import java.util.UUID;

import joshie.harvestmoon.api.WorldLocation;
import joshie.harvestmoon.buildings.Building;
import joshie.harvestmoon.buildings.BuildingGroup;
import joshie.harvestmoon.core.helpers.generic.EntityHelper;
import joshie.harvestmoon.core.network.PacketHandler;
import joshie.harvestmoon.core.network.PacketSyncMarker;
import joshie.harvestmoon.npc.EntityNPCBuilder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class TileMarker extends TileEntity {
    private BuildingGroup group;
    private Building building;
    private EntityNPCBuilder builder;

    @Override
    public boolean canUpdate() {
        return false;
    }

    public void setBuilding(BuildingGroup group, Building building) {
        this.group = group;
        this.building = building;
        this.markDirty();
    }
    
    public void setBuilder(EntityNPCBuilder entity) {
        this.builder = entity;
    }
    
    public EntityNPCBuilder getBuilder() {
        return builder;
    }

    public Building getBuilding() {
        return building;
    }

    public IMessage getPacket() {
        return new PacketSyncMarker(new WorldLocation(worldObj.provider.dimensionId, xCoord, yCoord, zCoord), group, building);
    }

    @Override
    public Packet getDescriptionPacket() {
        return PacketHandler.getPacket(getPacket());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        group = BuildingGroup.getGroup(nbt.getString("Group"));
        building = group.getBuilding(nbt.getInteger("Type"));
        if (nbt.hasKey("UUIDMost")) {
            UUID uuid = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));
            builder = (EntityNPCBuilder) EntityHelper.getBuilderFromUUID(nbt.getInteger("UUIDDim"), uuid);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setString("Group", group.getName());
        nbt.setInteger("Type", building.getInt());
        nbt.setInteger("UUIDDim", worldObj.provider.dimensionId);
        if (builder != null) {
            nbt.setLong("UUIDMost", builder.getPersistentID().getMostSignificantBits());
            nbt.setLong("UUIDLeast", builder.getPersistentID().getLeastSignificantBits());
        }
    }
}
