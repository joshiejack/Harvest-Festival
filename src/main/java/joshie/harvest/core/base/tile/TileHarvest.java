package joshie.harvest.core.base.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.ticking.DailyTickableBlock;
import joshie.harvest.core.helpers.MCServerHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileHarvest extends TileEntity {
    private boolean hasChanged = false;

    @Nullable
    protected DailyTickableBlock getTickableForTile() {
        return null;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()  {
        return new SPacketUpdateTileEntity(getPos(), 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void validate() {
        super.validate();
        DailyTickableBlock tickable = getTickableForTile();
        if (tickable != null) {
            HFApi.tickable.addTickable(world, pos, tickable);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("HasChanged")) {
            world.markBlockRangeForRenderUpdate(getPos(), getPos());
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (hasChanged) {
            nbt.setBoolean("HasChanged", true);
            hasChanged = false;
        }

        return super.writeToNBT(nbt);
    }

    public void saveAndRefresh() {
        MCServerHelper.markForUpdate(world, pos, 3);
        if (!world.isRemote) {
            MCServerHelper.markTileForUpdate(this);
        }

        markDirty();
    }
}
