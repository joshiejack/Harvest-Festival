package joshie.harvest.blocks.tiles;

import joshie.harvest.api.core.IDailyTickable;
import joshie.harvest.core.helpers.generic.MCServerHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileFillable extends TileDaily implements IDailyTickable {
    protected int fillAmount;

    public abstract boolean onActivated(ItemStack held);

    public int getFillAmount() {
        return fillAmount;
    }

    public void add(int amount) {
        setFilled(fillAmount + amount);
    }

    public void setFilled(int isFilled) {
        this.fillAmount = isFilled;
        MCServerHelper.markForUpdate(worldObj, getPos(), 3);
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        int prevFill = fillAmount;
        fillAmount = nbt.getByte("IsFilled");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("IsFilled", (byte) fillAmount);
        return super.writeToNBT(nbt);
    }
}
