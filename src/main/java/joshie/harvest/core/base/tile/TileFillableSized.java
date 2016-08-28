package joshie.harvest.core.base.tile;

import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.helpers.generic.MCServerHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileFillableSized extends TileDaily {
    protected int fillAmount = 0;
    protected Size size = Size.MEDIUM;

    public abstract boolean onActivated(ItemStack held);

    public Size getSize() {
        return size;
    }

    public int getFillAmount() {
        return fillAmount;
    }

    public void add(Size size, int amount) {
        setFilled(size, fillAmount + amount);
    }

    public void setFilled(Size size, int isFilled) {
        this.fillAmount = isFilled;
        this.size = size;
        saveAndRefresh();
    }

    public void saveAndRefresh() {
        MCServerHelper.markForUpdate(worldObj, getPos(), 3);
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        fillAmount = nbt.getByte("IsFilled");
        size = Size.values()[nbt.getByte("Size")];
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("IsFilled", (byte) fillAmount);
        nbt.setByte("Size", (byte) size.ordinal());
        return super.writeToNBT(nbt);
    }
}
