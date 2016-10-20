package joshie.harvest.api.animals;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Implement this on blocks that are able to feed animals **/
public interface IAnimalFeeder {
    /** Call this to feed the animal, returns true if it was possible
     * @param tracked       the animal to be fed
     * @param world         the world object
     * @param pos           the position of the feeder
     * @param state         the state of the block
     * @param simulate      whether to actually feed the animal
     * @return  if the animal was fed */
    boolean feedAnimal(IAnimalTracked tracked, World world, BlockPos pos, IBlockState state, boolean simulate);
}