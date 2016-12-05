package joshie.harvest.api.animals;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Implement this on blocks that are able to have eggs placed in them **/
public interface INest {
    /** If this is block is a nest
     * @param world         the world object
     * @param pos           the position of the nest
     * @param state         the state of the block
     * @return true if it's a nest that this animal can lay eggs in**/
    boolean isNest(AnimalStats stats, World world, BlockPos pos, IBlockState state);

    /** Call this to have an animal lay an egg in a nest
     * @param stats       the animal
     * @param world         the world object
     * @param pos           the position of the nest
     * @param state         the state of the block*/
    void layEgg(AnimalStats stats, World world, BlockPos pos, IBlockState state);
}