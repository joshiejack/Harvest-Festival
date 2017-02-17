package joshie.harvest.core.base.tile;

import joshie.harvest.animals.packet.PacketClearNeighbours;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.core.helpers.MCServerHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

import static net.minecraft.util.EnumFacing.*;

public abstract class TileFillableConnected<T extends TileFillableConnected> extends TileFillable {
    private final int maxWidth;
    private boolean facingX;
    private int offsetX;
    private int offsetZ;
    private int size;

    public TileFillableConnected(AnimalFoodType foodType, int maxFill, int fillPer, int maxWidth) {
        super(foodType, maxFill, fillPer);
        this.maxWidth = maxWidth;
    }

    @Override
    protected T getTile() {
        return getMaster();
    }

    @Override
    public int getMaximumFill() {
        return size * super.getMaximumFill();
    }

    public abstract void resetClientData();

    @SuppressWarnings("unchecked")
    public T getMaster() {
        BlockPos connection = pos.add(offsetX, 0, offsetZ);
        return isValidConnection(connection) ? (T) worldObj.getTileEntity(connection) : (T) this;
    }

    @SuppressWarnings("unchecked, ConstantConditions")
    private boolean updateMasterInDirection(EnumFacing facing) {
        BlockPos offset = pos.offset(facing);
        if (isValidConnection(offset)) {
            TileFillableConnected master = ((TileFillableConnected)worldObj.getTileEntity(offset)).getMaster();
            if (master.getSize() < maxWidth) {
                int offsetX = master.getPos().getX() - getPos().getX();
                int offsetZ = master.getPos().getZ() - getPos().getZ();
                if (offsetX != 0 && offsetZ != 0) return false;
                if (master.size > 1) {
                    if (master.facingX && offsetX != 0) return false;
                    else if (!master.facingX && offsetZ != 0) return false;
                }

                if (offsetZ != 0) master.facingX = true;
                if (offsetX != 0) master.facingX = false;
                this.offsetX = offsetX;
                this.offsetZ = offsetZ;
                master.size++; //Increase the trough size
                master.markDirty();
                return true;
            }
        }

        return false;
    }

    private void updateMaster() {
        if (updateMasterInDirection(NORTH)) return;
        if (updateMasterInDirection(SOUTH)) return;
        if (updateMasterInDirection(EAST)) return;
        if (!updateMasterInDirection(WEST)) {
            //Make this block it's own master
            offsetX = 0;
            offsetZ = 0;
            size = 1;
            markDirty();
        }
    }

    public void onPlaced() {
        updateMaster();
        setFilled(getFillAmount());
        if (!worldObj.isRemote) {
            MCServerHelper.sendTileUpdate(this, new PacketClearNeighbours(pos));
        }
    }

    @SuppressWarnings("ConstantConditions")
    private BlockPos getNewMaster() {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                BlockPos offset = pos.add(x, 0, z);
                if (isValidConnection(offset)) {
                    TileFillableConnected connected = ((TileFillableConnected)worldObj.getTileEntity(offset));
                    if (connected.getMaster() == this) {
                        return getPos().add(x, 0, z);
                    }
                }
            }
        }

        return null;
    }

    @SuppressWarnings("ConstantConditions")
    private void setMaster(BlockPos pos) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                BlockPos offset = pos.add(x, 0, z);
                if (isValidConnection(offset)) {
                    TileFillableConnected tile = ((TileFillableConnected)worldObj.getTileEntity(offset));
                    if (tile.getMaster() == this) {
                        tile.offsetX = pos.getX() - tile.getPos().getX();
                        tile.offsetZ = pos.getZ() - tile.getPos().getZ();
                        tile.updateMaster(); //Refresh the master status
                        tile.markDirty();
                    }
                }
            }
        }
    }

    public void onRemoved() {
        TileFillableConnected master = getMaster();
        int volume = master.getFillAmount();
        int size = Math.max(0, master.size - 1);
        boolean facingX = master.facingX;
        BlockPos newMaster = null;
        if (master == this) {
            newMaster = getNewMaster();
            if (newMaster != null) {
                setMaster(newMaster);
            }
        }

        //Copy the contents from one tile to the next
        master = newMaster != null ? (TileFillableConnected) worldObj.getTileEntity(newMaster) : master;
        if (master != null) {
            master.size = size;
            master.facingX = facingX;
            master.setFilled(volume); //Update the volume in the new master
            master.markDirty();
            master.resetClientData();
        }

        //Reset everything
        if (!worldObj.isRemote) {
            MCServerHelper.sendTileUpdate(this, new PacketClearNeighbours(pos));
        }
    }

    protected abstract boolean isValidConnection(BlockPos pos);

    public int getSize() {
        return size;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        facingX = nbt.getBoolean("FacingX");
        offsetX = nbt.getInteger("OffsetX");
        offsetZ = nbt.getInteger("OffsetZ");
        size = nbt.getByte("Size");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("FacingX", facingX);
        nbt.setInteger("OffsetX", offsetX);
        nbt.setInteger("OffsetZ", offsetZ);
        nbt.setByte("Size", (byte) size);
        return super.writeToNBT(nbt);
    }
}
