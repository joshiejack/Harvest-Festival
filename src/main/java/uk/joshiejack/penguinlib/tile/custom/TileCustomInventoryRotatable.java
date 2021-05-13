package uk.joshiejack.penguinlib.tile.custom;

import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.PacketSetFacing;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.interfaces.Rotatable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;

@PenguinLoader("custom_inventory_rotatable")
public class TileCustomInventoryRotatable extends AbstractTileCustomInventory<TileCustomInventoryRotatable> implements Rotatable {
    private EnumFacing facing = EnumFacing.NORTH;

    public TileCustomInventoryRotatable(){}

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
