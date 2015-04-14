package joshie.harvestmoon.blocks.tiles;

import joshie.harvestmoon.api.WorldLocation;
import joshie.harvestmoon.buildings.Building;
import joshie.harvestmoon.buildings.BuildingGroup;
import joshie.harvestmoon.core.network.PacketHandler;
import joshie.harvestmoon.core.network.PacketSyncMarker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class TileMarker extends TileEntity {
    private BuildingGroup group;
    private Building building;

    @Override
    public boolean canUpdate() {
        return false;
    }

    public void setBuilding(BuildingGroup group, Building building) {
        this.group = group;
        this.building = building;
        this.markDirty();
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
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setString("Group", group.getName());
        nbt.setInteger("Type", building.getInt());
    }
}
