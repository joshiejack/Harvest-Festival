package uk.joshiejack.harvestcore.ticker.growable;

import uk.joshiejack.harvestcore.world.SpreadableNotifier;
import uk.joshiejack.penguinlib.ticker.DailyTicker;
import uk.joshiejack.penguinlib.ticker.TickerHelper;
import uk.joshiejack.seasons.handlers.SeasonHandler;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public class SpreadableTicker extends AbstractGrowableTicker implements INBTSerializable<NBTTagCompound> {
    private boolean starter;
    private int grown;
    public SpreadableTicker(String type, SeasonHandler seasons) {
        super(type, seasons);
    }

    @Override
    public DailyTicker newInstance() {
        return new SpreadableTicker(getType(), seasons);
    }

    public void setStarter() {
        this.starter = true;
    }

    @Override
    public void tickInSeason(World world, BlockPos pos, IBlockState state) {
        if (starter && grown <= 90) { //Lifespan 3 seasons
            if (world.rand.nextInt(3) == 0) {
                BlockPos target = world.getTopSolidOrLiquidBlock(pos.add(world.rand.nextInt(8) - 4, 0, world.rand.nextInt(8) - 4));
                boolean bush = state.getBlock() instanceof BlockBush;
                if (world.isAirBlock(target) && (!bush || ((BlockBush)state.getBlock()).canBlockStay(world, target, state))) {
                    world.setBlockState(target, state, 2); //Copy the state below
                    grown++;
                } else {
                    IBlockState other = world.getBlockState(target);
                    if (other == state && other.getBlock() instanceof IGrowable) {
                        IGrowable growable = (IGrowable) other.getBlock();
                        if (growable.canGrow(world, target, other, false)) {
                            growable.grow(world, world.rand, target, other);
                            DailyTicker entry = TickerHelper.getTicker(world, target);
                            if (pos.equals(target) || (entry instanceof SpreadableTicker && ((SpreadableTicker)entry).starter)) {
                                SpreadableNotifier.markAsSpreadable(world, target); //Readd as starter true after growing
                            }

                            grown++;
                        }
                    }
                }
            }
       }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        if (starter) {
            tag.setInteger("Grown", grown);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("Grown")) {
            starter = true;
            grown = nbt.getInteger("Grown");
        }
    }
}
