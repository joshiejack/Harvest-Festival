package joshie.harvest.core.base.tile;

import joshie.harvest.api.core.Size;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public abstract class TileFillableSized extends TileHarvest {
    protected int fillAmount = 0;
    protected Size size = Size.MEDIUM;

    public abstract boolean onActivated(EntityPlayer player, ItemStack held);

    public Size getSize() {
        if (size == null) {
            size = Size.MEDIUM;
        }

        return size;
    }

    public int getFillAmount() {
        return fillAmount;
    }

    public void add(Size size, int amount) {
        setFilled(size, fillAmount + amount);
    }

    protected void setFilled(Size size, int isFilled) {
        this.fillAmount = isFilled;
        this.size = size;
        saveAndRefresh();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        fillAmount = nbt.getByte("IsFilled");
        size = Size.values()[nbt.getByte("Size")];
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("IsFilled", (byte) fillAmount);
        nbt.setByte("Size", (byte) getSize().ordinal());
        return super.writeToNBT(nbt);
    }
}
