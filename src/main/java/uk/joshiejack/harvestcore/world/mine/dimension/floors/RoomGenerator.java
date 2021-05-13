package uk.joshiejack.harvestcore.world.mine.dimension.floors;

import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public abstract class RoomGenerator {
    public boolean isSimple() {
        return true;
    }

    /**
     * @param   world       the wrapper for modifying the statemap
     * @param   ladder      the x and z where the ladder enters this floor
     * @return the location that we spawned a ladder,
     * int[] must be length of two with the x and z values in 0 and 1 **/
    public abstract BlockPos generate(AbstractDecoratorWrapper world, BlockPos origin);

    public boolean canGenerate(BlockPos target) {
        return true;
    }

    BlockPos getLadderPosition(AbstractDecoratorWrapper world, List<BlockPos> pairs, int distance) {
        while(true) {
            BlockPos pair = pairs.get(world.rand.nextInt(pairs.size()));
            if (world.hasBuffer(pair, distance)) {
                return pair;
            }
        }
    }
}
