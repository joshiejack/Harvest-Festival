package joshie.harvest.crops.tile;

import joshie.harvest.core.base.tile.TileHarvest;
import joshie.harvest.core.helpers.MCServerHelper;
import joshie.harvest.crops.CropData;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class TileWithered extends TileHarvest {
    private final CropData data = new CropData();

    //Return and create new data if it doesn't exist yet
    @Nonnull
    public CropData getData() {
        return data;
    }

    public void saveAndRefresh() {
        MCServerHelper.markForUpdate(world, getPos(), 3);
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        data.readFromNBT(nbt.getCompoundTag("CropData"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("CropData", data.writeToNBT(new NBTTagCompound()));
        return super.writeToNBT(nbt);
    }
}
