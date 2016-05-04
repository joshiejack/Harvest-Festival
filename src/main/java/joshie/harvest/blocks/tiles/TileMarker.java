package joshie.harvest.blocks.tiles;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.buildings.Building;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncMarker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TileMarker extends TileEntity {
    private IBuilding group;

    public void setBuilding(IBuilding group) {
        this.group = group;
        this.markDirty();
    }

    public IBuilding getBuilding() {
        return group;
    }

    public IMessage getPacket() {
        return new PacketSyncMarker(new WorldLocation(worldObj.provider.getDimension(), pos), group);
    }

    @Override
    public Packet<?> getDescriptionPacket() {
        return PacketHandler.getPacket(getPacket());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        group = Building.getGroup(nbt.getString("Group"));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (group != null) {
            nbt.setString("Group", group.getName());
        }
    }
}