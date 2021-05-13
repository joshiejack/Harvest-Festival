package uk.joshiejack.harvestcore.world.mine.dimension.floors;

import uk.joshiejack.harvestcore.database.MineLoader;
import uk.joshiejack.harvestcore.world.mine.dimension.MineChunkGenerator;
import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import net.minecraft.util.math.BlockPos;

public class RoomGeneratorTunnelMulti extends RoomGeneratorTunnel {
    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public BlockPos generate(AbstractDecoratorWrapper world, BlockPos ladderIn) {
        MineLoader.CIRCLE.generate(world, ladderIn);
        int amount = 2 + world.rand.nextInt(4);
        int min = RoomGeneratorCircle.MAX + 1;
        int max = MineChunkGenerator.CHUNKS_PER_SECTION * 16 - (min * 2);
        while (true) {
            BlockPos target = new BlockPos(min + world.rand.nextInt(max), 0, min + world.rand.nextInt(max));
            RoomGenerator generator = world.tier.getValidGeneratorFromList(world.tier.getSimpleGenerators(), world.rand, target);
            BlockPos ladder = generator.generate(world, target);
            connect(world, ladderIn, ladder);
            ladderIn = ladder;
            if (amount == 0) {
                return ladder;
            }

            amount--;
        }
    }
}
