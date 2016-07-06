package joshie.harvest.api.animals;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Implement this on blocks that are able to have eggs placed in them **/
public interface INest {
    /** Call this to have an animal lay an egg in a nest
     * @param tracked       the animal
     * @param world         the world object
     * @param pos           the position of the nest
     * @param state         the state of the block
     * @return  if the animal was fed */
    boolean layEgg(IAnimalTracked tracked, World world, BlockPos pos, IBlockState state);
}