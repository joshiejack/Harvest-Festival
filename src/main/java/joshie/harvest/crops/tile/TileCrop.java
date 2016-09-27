package joshie.harvest.crops.tile;

import joshie.harvest.core.base.tile.TileDaily;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.crops.CropData;
import joshie.harvest.crops.CropHelper;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockHFCrops.CropType;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

import static joshie.harvest.core.network.PacketHandler.sendRefreshPacket;
import static joshie.harvest.crops.CropHelper.isWetSoil;

public class TileCrop extends TileDaily {
    public static class TileWithered extends TileCrop {
        @Override
        public void newDay(Phase phase) {}
    }

    protected final CropData data = new CropData();

    //Return and create new data if it doesn't exist yet
    @Nonnull
    public CropData getData() {
        return data;
    }

    @Override
    public boolean isPriority() {
        return true;
    }

    @Override
    public void newDay(Phase phase) {
        if (phase == Phase.PRE) {
            //Rain and soil check
            if (data.getCrop().requiresWater() && (CropHelper.isRainingAt(getWorld(), getPos().up()) || isWetSoil(getWorld().getBlockState(getPos().down())))) {
                data.setHydrated(); //If today is raining, hydrate the crop automatically
            }

            //If we were unable to survive the new day, let's destroy some things
            if (!data.newDay(getWorld(), getPos())) {
                if (HFCrops.CROPS_SHOULD_DIE) {
                    if (data.getCrop().isCurrentlyDouble(data.getStage())) {
                        getWorld().setBlockState(pos.up(), HFCrops.CROPS.getStateFromEnum(CropType.WITHERED_DOUBLE), 2);
                    }

                    //Prepare to save old data
                    NBTHelper.copyTileData(this, getWorld(), getPos(), HFCrops.CROPS.getStateFromEnum(CropType.WITHERED));
                }
            } else sendRefreshPacket(this);


            //Mark the crop as dirty
            markDirty();
        }
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
