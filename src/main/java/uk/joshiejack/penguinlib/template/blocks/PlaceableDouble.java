package uk.joshiejack.penguinlib.template.blocks;

import com.google.common.collect.Sets;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

@PenguinLoader("double")
public class PlaceableDouble extends PlaceableDecorative {
    @SuppressWarnings("unused")
    public PlaceableDouble(){}
    public PlaceableDouble(IBlockState state, BlockPos position) {
        super(state, position);
    }

    @Override
    public Set<BlockPos> getPositions(BlockPos transformed) {
        return Sets.newHashSet(transformed, transformed.up());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void postPlace(World world, BlockPos pos, Rotation rotation) {
        if (getBlock() == Blocks.DOUBLE_PLANT) {
            if (world.rand.nextBoolean()) world.setBlockState(pos, getState().getBlock().getStateFromMeta(5));
            else world.setBlockState(pos, getState().getBlock().getStateFromMeta(4));
        }

        world.setBlockState(pos.up(), getState().getBlock().getStateFromMeta(8), 2);
    }
}