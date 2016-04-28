package joshie.harvest.blocks.tiles;

import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncOrientation;
import joshie.harvest.core.util.generic.IFaceable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/** Just a way to interfact with the fridge inventory, the fridge inventory is global though, not stored in this block **/
public class TileFridge extends TileEntity implements IFaceable {
    private EnumFacing orientation = EnumFacing.NORTH;
    private boolean isTop;

    @Override
    public void setFacing(EnumFacing dir) {
        orientation = dir;
    }

    @Override
    public EnumFacing getFacing() {
        return orientation;
    }

    public IMessage getPacket() {
        return new PacketSyncOrientation(worldObj.provider.getDimension(), getPos(), orientation);
    }

    @Override
    public Packet getDescriptionPacket() {
        return PacketHandler.getPacket(getPacket());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        orientation = EnumFacing.values()[(nbt.getInteger("Orientation"))];
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("Orientation", orientation.ordinal());
    }
}