package joshie.harvest.crops.tile;

import joshie.harvest.core.base.tile.TileDaily;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.crops.CropData;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockHFCrops;
import joshie.harvest.crops.block.BlockHFCrops.Stage;
import net.minecraft.nbt.NBTTagCompound;

import static joshie.harvest.crops.CropHelper.isWetSoil;
import static joshie.harvest.core.network.PacketHandler.sendRefreshPacket;

public class TileCrop extends TileDaily {
    public static class TileWithered extends TileCrop {
        @Override
        public void newDay() {}
    }

    protected CropData data = new CropData();

    //Return and create new data if it doesn't exist yet
    public CropData getData() {
        return data;
    }

    @Override
    public boolean isPriority() {
        return true;
    }

    @Override
    public void newDay() {
        //Rain and soil check
        if (data.getCrop().requiresWater() && (getWorld().isRainingAt(getPos().up()) || isWetSoil(getWorld().getBlockState(getPos().down())))) {
            data.setHydrated(); //If today is raining, hydrate the crop automatically
        }

        //If we were unable to survive the new day, let's destroy some things
        if (!data.newDay(getWorld(), getPos())) {
            if (data.getCrop().isDouble(data.getStage())) {
                getWorld().setBlockState(pos.up(), HFCrops.CROPS.getStateFromEnum(BlockHFCrops.Stage.WITHERED_DOUBLE), 2);
            }

            //Prepare to save old data
            NBTHelper.copyTileData(this, getWorld(), getPos(), HFCrops.CROPS.getStateFromEnum(Stage.WITHERED));
        } else sendRefreshPacket(this);

        //Mark the crop as dirty
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
