package joshie.harvest.blocks.tiles;

import joshie.harvest.core.util.generic.IFaceable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class TileFaceable extends TileEntity implements IFaceable {
    protected EnumFacing orientation = EnumFacing.NORTH;

    @Override
    public void setFacing(EnumFacing dir) {
        orientation = dir;
    }

    @Override
    public EnumFacing getFacing() {
        return orientation;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()  {
        return new SPacketUpdateTileEntity(getPos(), 1, writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        orientation = EnumFacing.values()[(nbt.getInteger("Orientation"))];
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (orientation != null) {
            nbt.setInteger("Orientation", orientation.ordinal());
        }

        return nbt;
    }
}
