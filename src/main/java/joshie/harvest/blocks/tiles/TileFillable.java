package joshie.harvest.blocks.tiles;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.IDailyTickable;
import joshie.harvest.core.helpers.generic.MCServerHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileFillable extends TileEntity implements IDailyTickable {
    protected int fillAmount;

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()  {
        return new SPacketUpdateTileEntity(getPos(), 1, writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }

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
    public void validate() {
        super.validate();
        //Update the ticker
        HFApi.tickable.addTickable(worldObj, this);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        //Update the ticker
        HFApi.tickable.removeTickable(worldObj, this);
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
