package joshie.harvest.core.base.tile;

import joshie.harvest.api.ticking.IDailyTickable;
import joshie.harvest.core.helpers.MCServerHelper;
import joshie.harvest.core.network.PacketHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileFillable extends TileDaily implements IDailyTickable {
    protected int fillAmount;

    public abstract boolean onActivated(ItemStack held);

    public int getFillAmount() {
        return fillAmount;
    }

    public void adjustFill(int amount) {
        setFilled(fillAmount + amount);
    }

    public void setFilled(int isFilled) {
        this.fillAmount = isFilled;
        saveAndRefresh();
    }

    public void saveAndRefresh() {
        MCServerHelper.markForUpdate(worldObj, getPos(), 3);
        if (!worldObj.isRemote) {
            PacketHandler.sendRefreshPacket(this);
        }

        markDirty();
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
