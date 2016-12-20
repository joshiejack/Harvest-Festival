package joshie.harvest.crops.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.ticking.IDailyTickable;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.crops.CropHelper;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockHFCrops.CropType;
import joshie.harvest.gathering.HFGathering;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static joshie.harvest.core.helpers.MCServerHelper.markTileForUpdate;
import static joshie.harvest.crops.CropHelper.isWetSoil;

public class TileCrop extends TileWithered implements IDailyTickable {
    @Override
    public Phase[] getPhases() {
        return new Phase[] { Phase.PRIORITY };
    }

    @Override
    public void newDay() {
        //Rain and soil check
        World world = getWorld();
        BlockPos pos = getPos().down();
        if (data.getCrop().requiresWater() && (CropHelper.isRainingAt(getWorld(), getPos().up()) || isWetSoil(world, pos, world.getBlockState(pos)))) {
            data.setHydrated(); //If today is raining, hydrate the crop automatically
        }

        //If we were unable to survive the new day, let's destroy some things
        if (!data.newDay(getWorld(), getPos())) {
            if (HFCrops.CROPS_SHOULD_DIE) {
                HFApi.tickable.removeTickable(worldObj, this);
                if (world.rand.nextInt(4) == 0) {
                    if (data.getCrop().isCurrentlyDouble(data.getStage())) world.setBlockToAir(pos.up());
                    if (world.rand.nextInt(5) <= 1)
                        world.setBlockState(getPos(), HFGathering.WOOD.getStateFromMeta(world.rand.nextInt(6)));
                    else if (world.rand.nextInt(5) == 0)
                        world.setBlockState(getPos(), HFGathering.ROCK.getStateFromMeta(world.rand.nextInt(6)));
                    else world.setBlockState(getPos(), HFCore.FLOWERS.getStateFromEnum(FlowerType.WEED));
                } else {
                    if (data.getCrop().isCurrentlyDouble(data.getStage())) {
                        getWorld().setBlockState(pos.up(), HFCrops.CROPS.getStateFromEnum(CropType.WITHERED_DOUBLE), 2);
                    }

                    //Prepare to save old data
                    NBTHelper.copyTileData(this, getWorld(), getPos(), HFCrops.CROPS.getStateFromEnum(CropType.WITHERED));
                }
            }
        } else markTileForUpdate(this);


        //Mark the crop as dirty
        saveAndRefresh();
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
