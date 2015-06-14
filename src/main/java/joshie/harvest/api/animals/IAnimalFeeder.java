package joshie.harvest.api.animals;

import net.minecraft.world.World;

/** Implement this on blocks that are able to feed animals **/
public interface IAnimalFeeder {
    public boolean canFeedAnimal(IAnimalTracked tracked, World world, int x, int y, int z);
}
