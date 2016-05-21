package joshie.harvest.api.animals;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Implement this on blocks that are able to feed animals **/
public interface IAnimalFeeder {
    boolean canFeedAnimal(IAnimalTracked tracked, World world, BlockPos pos, IBlockState state);
}