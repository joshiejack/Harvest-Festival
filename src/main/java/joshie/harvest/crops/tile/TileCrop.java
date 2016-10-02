package joshie.harvest.crops.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.ticking.IDailyTickable;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.crops.CropHelper;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockHFCrops.CropType;

import static joshie.harvest.core.helpers.MCServerHelper.markTileForUpdate;
import static joshie.harvest.crops.CropHelper.isWetSoil;

public class TileCrop extends TileWithered implements IDailyTickable {
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
                    HFApi.tickable.removeTickable(worldObj, this);
                    if (data.getCrop().isCurrentlyDouble(data.getStage())) {
                        getWorld().setBlockState(pos.up(), HFCrops.CROPS.getStateFromEnum(CropType.WITHERED_DOUBLE), 2);
                    }

                    //Prepare to save old data
                    NBTHelper.copyTileData(this, getWorld(), getPos(), HFCrops.CROPS.getStateFromEnum(CropType.WITHERED));
                }
            } else markTileForUpdate(this);


            //Mark the crop as dirty
            markDirty();
        }
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
}
