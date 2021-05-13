package uk.joshiejack.penguinlib.template.blocks;

import com.google.common.collect.Sets;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

@PenguinLoader("opposite")
public class PlaceableDoubleOpposite extends PlaceableDecorative {
    @SuppressWarnings("unused")
    public PlaceableDoubleOpposite(){}
    public PlaceableDoubleOpposite(IBlockState state, BlockPos position) {
        super(state, position);
    }

    @Override
    public Set<BlockPos> getPositions(BlockPos transformed) {
        return Sets.newHashSet(transformed, transformed.up());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void postPlace(World world, BlockPos pos, Rotation rotation) {
        world.setBlockState(pos.up(), getState().getBlock().getStateFromMeta(9), 2);
    }
}