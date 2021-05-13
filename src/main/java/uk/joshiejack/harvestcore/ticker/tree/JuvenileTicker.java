package uk.joshiejack.harvestcore.ticker.tree;

import uk.joshiejack.penguinlib.ticker.DailyTicker;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public class JuvenileTicker extends SeedlingTicker implements INBTSerializable<NBTTagCompound> {
    public JuvenileTicker(String type, IBlockState sapling, int days) {
        super(type, sapling, days);
    }

    @Override
    public DailyTicker newInstance() {
        return new JuvenileTicker(getType(), next, days);
    }

    @Override
    protected void grow(World world, BlockPos pos) {
        if (next.getBlock() instanceof IGrowable) {
            super.grow(world, pos);
            {
                ((IGrowable) next.getBlock()).grow(world, world.rand, pos, next);
            }
        }
    }
}
