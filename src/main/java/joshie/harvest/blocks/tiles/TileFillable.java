package joshie.harvest.blocks.tiles;

import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncFilled;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TileFillable extends TileEntity {
    private boolean isFilled = false;

    public IMessage getPacket() {
        return new PacketSyncFilled(this, isFilled);
    }

    @Override
    public Packet<?> getDescriptionPacket() {
        return PacketHandler.getPacket(getPacket());
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean isFilled) {
        this.isFilled = isFilled;
        worldObj.notifyBlockUpdate(getPos(), worldObj.getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
        this.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        isFilled = nbt.getBoolean("IsFilled");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("IsFilled", isFilled);
    }
}
