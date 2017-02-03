package joshie.harvest.core.base.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileFillable extends TileHarvest {
    protected int fillAmount;

    public abstract boolean onActivated(ItemStack held);

    public int getFillAmount() {
        return fillAmount;
    }

    protected abstract int getMaximumFill();

    public void adjustFill(int amount) {
        setFilled(fillAmount + amount);
    }

    protected boolean setFilled(int isFilled) {
        fillAmount = Math.min(getMaximumFill(), isFilled);
        saveAndRefresh();
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        fillAmount = nbt.getByte("IsFilled");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("IsFilled", (byte) fillAmount);
        return super.writeToNBT(nbt);
    }
}
