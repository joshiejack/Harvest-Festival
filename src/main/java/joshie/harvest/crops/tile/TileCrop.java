package joshie.harvest.crops.tile;

import joshie.harvest.api.ticking.DailyTickableBlock;
import joshie.harvest.api.ticking.DailyTickableBlock.Phases;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.crops.CropData;
import joshie.harvest.crops.CropHelper;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockHFCrops.CropType;
import joshie.harvest.gathering.HFGathering;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static joshie.harvest.core.helpers.MCServerHelper.markTileForUpdate;
import static joshie.harvest.crops.CropHelper.isWetSoil;

public class TileCrop extends TileWithered {
    private static final DailyTickableBlock TICKABLE = new DailyTickableBlock(Phases.PRE) {
        @Override
        public boolean isStateCorrect(World world, BlockPos pos, IBlockState state) {
            return state.getBlock() == HFCrops.CROPS && !HFCrops.CROPS.getEnumFromState(state).isWithered();
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        public void newDay(World world, BlockPos pos, IBlockState state) {
            TileCrop crop = (TileCrop) world.getTileEntity(pos);
            CropData data = crop.getData();
            BlockPos soil = pos.down();
            if (!data.isWatered() && (data.getCrop().requiresWater() && (CropHelper.isRainingAt(world, pos.up()) || isWetSoil(world, soil, world.getBlockState(soil))))) {
                data.setHydrated(); //If today is raining, hydrate the crop automatically
            }

            //If we were unable to survive the new day, let's destroy some things
            if (!data.newDay(world, pos)) {
                if (world.rand.nextInt(4) == 0) {
                    if (crop.getData().getCrop().isCurrentlyDouble(crop.getData().getStage())) world.setBlockToAir(pos);
                    if (world.rand.nextInt(5) <= 1)
                        world.setBlockState(pos, HFGathering.WOOD.getStateFromMeta(world.rand.nextInt(6)));
                    else if (world.rand.nextInt(5) == 0)
                        world.setBlockState(pos, HFGathering.ROCK.getStateFromMeta(world.rand.nextInt(6)));
                    else world.setBlockState(pos, HFCore.FLOWERS.getStateFromEnum(FlowerType.WEED));
                } else {
                    if (crop.getData().getCrop().isCurrentlyDouble(crop.getData().getStage())) {
                        world.setBlockState(soil.up(), HFCrops.CROPS.getStateFromEnum(CropType.WITHERED_DOUBLE), 2);
                    }

                    //Prepare to save old data
                    NBTHelper.copyTileData(crop, world, pos, HFCrops.CROPS.getStateFromEnum(CropType.WITHERED));
                }
            } else markTileForUpdate(crop);

            //Save and Refresh the crop
            crop.saveAndRefresh();
        }
    };

    @Override
    public DailyTickableBlock getTickableForTile() {
        return TICKABLE;
    }
}
