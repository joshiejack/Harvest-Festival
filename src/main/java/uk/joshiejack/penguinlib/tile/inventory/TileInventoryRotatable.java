package uk.joshiejack.penguinlib.tile.inventory;

import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.PacketSetFacing;
import uk.joshiejack.penguinlib.util.interfaces.Rotatable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;

public abstract class TileInventoryRotatable extends TileInventory implements Rotatable {
    private EnumFacing facing = EnumFacing.NORTH;

    public TileInventoryRotatable(int size) {
        super(size);
    }

    @Override
    public void setFacing(EnumFacing facing) {
        this.facing = facing;
        this.markDirty();
        if (!world.isRemote) {
            PenguinNetwork.sendToNearby(this, new PacketSetFacing(pos, facing));
        }
    }

    @Nonnull
    @Override
    public EnumFacing getFacing() {
        return facing;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        facing = EnumFacing.values()[nbt.getByte("Facing")];
        if (facing == null || facing.getHorizontalIndex() < 0) facing = EnumFacing.NORTH;
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("Facing", (byte) facing.ordinal());
        return super.writeToNBT(nbt);
    }
}

