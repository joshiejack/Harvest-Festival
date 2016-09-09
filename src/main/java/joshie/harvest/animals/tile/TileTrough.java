package joshie.harvest.animals.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.base.tile.TileFillable;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import static joshie.harvest.api.animals.AnimalFoodType.GRASS;
import static net.minecraft.util.EnumFacing.*;

public class TileTrough extends TileFillable {
    private static final int MAX_WIDTH = 3;
    private boolean facingX;
    private int offsetX;
    private int offsetZ;
    private int size;

    public TileTrough getMaster() {
        TileEntity tile = worldObj.getTileEntity(getPos().add(offsetX, 0, offsetZ));
        return tile instanceof TileTrough ? (TileTrough) tile: this;
    }

    public int getSize() {
        return size;
    }

    private boolean updateMasterInDirection(EnumFacing facing) {
        if (worldObj.getTileEntity(getPos().offset(facing)) instanceof TileTrough) {
            TileTrough trough = ((TileTrough)worldObj.getTileEntity(getPos().offset(facing))).getMaster();
            if (trough.getSize() < MAX_WIDTH) {
                int offsetX = trough.getPos().getX() - getPos().getX();
                int offsetZ = trough.getPos().getZ() - getPos().getZ();
                if (offsetX != 0 && offsetZ != 0) return false;
                if (trough.size > 1) {
                    if (trough.facingX && offsetX != 0) return false;
                    else if (!trough.facingX && offsetZ != 0) return false;
                }

                if (offsetZ != 0) trough.facingX = true;
                if (offsetX != 0) trough.facingX = false;
                this.offsetX = offsetX;
                this.offsetZ = offsetZ;
                trough.size++; //Increase the trough size
                trough.markDirty();
                return true;
            }
        }

        return false;
    }

    private void updateMaster() {
        if (updateMasterInDirection(NORTH)) return;
        if (updateMasterInDirection(SOUTH)) return;
        if (updateMasterInDirection(EAST)) return;
        if (updateMasterInDirection(WEST)) return;
        else { //Make this block it's own master
            offsetX = 0;
            offsetZ = 0;
            size = 1;
            markDirty();
        }
    }

    public void onPlaced() {
        updateMaster();
        setFilled(getFillAmount());
    }

    private BlockPos getNewMaster() {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                TileEntity tile = worldObj.getTileEntity(getPos().add(x, 0, z));
                if (tile instanceof TileTrough) {
                    if (((TileTrough)tile).getMaster() == this) {
                        return getPos().add(x, 0, z);
                    }
                }
            }
        }

        return null;
    }

    private void setMaster(BlockPos pos) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                TileEntity tile = worldObj.getTileEntity(getPos().add(x, 0, z));
                if (tile instanceof TileTrough) {
                    TileTrough trough = ((TileTrough)tile);
                    if (trough.getMaster() == this) {
                        trough.offsetX = pos.getX() - trough.getPos().getX();
                        trough.offsetZ = pos.getZ() - trough.getPos().getZ();
                        trough.updateMaster(); //Refresh the master status
                        trough.markDirty();
                    }
                }
            }
        }
    }

    public void onRemoved() {
        int volume = getMaster().getFillAmount();
        int size = Math.max(0, getMaster().size - 1);
        boolean facingX = getMaster().facingX;
        TileTrough master = getMaster();
        if (getMaster() == this) {
            BlockPos newMaster = getNewMaster();
            if (newMaster != null) {
                setMaster(newMaster);
            }
        }

        master.setFilled(Math.min(7 * size, volume)); //Update the volume in the new master
        master.size = size;
        master.facingX = facingX;
        master.markDirty();
    }

    @Override
    public boolean onActivated(ItemStack held) {
        if (HFApi.animals.canEat(held, GRASS)) {
            TileTrough master = getMaster();
            if (master != null) {
                boolean processed = false;
                for (int i = 0; i < 6 && held.stackSize > 0; i++) {
                    if (held.stackSize >= 1) {
                        if (master.hasRoomAndFill()) {
                            held.splitStack(1);
                            processed = true;
                        } else break;
                    }
                }

                return processed;
            }
        }

        return false;
    }

    private boolean hasRoomAndFill() {
        if (fillAmount < (2 * size)) {
            setFilled(getFillAmount() + 1);
            return true;
        }

        return false;
    }

    private boolean hasFoodAndFeed() {
        if (fillAmount > 0) {
            setFilled(getFillAmount() - 1);
            return true;
        }

        return false;
    }

    @Override
    public void newDay() {
        for (EntityAnimal animal: EntityHelper.getEntities(EntityAnimal.class, getWorld(), 32D)) {
            if (animal instanceof IAnimalTracked) { //Feed all the local animals
                IAnimalTracked tracked = ((IAnimalTracked) animal);
                if (tracked.getData().isHungry() && HFApi.animals.canAnimalEatFoodType(tracked, GRASS) && hasFoodAndFeed()) {
                    tracked.getData().feed(null);
                } else break;
            }
        }
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
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("FacingX", facingX);
        nbt.setInteger("OffsetX", offsetX);
        nbt.setInteger("OffsetZ", offsetZ);
        nbt.setByte("Size", (byte) size);
        return super.writeToNBT(nbt);
    }
}