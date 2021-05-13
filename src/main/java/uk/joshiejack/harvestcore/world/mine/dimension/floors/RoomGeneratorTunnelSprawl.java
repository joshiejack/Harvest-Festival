package uk.joshiejack.harvestcore.world.mine.dimension.floors;

import uk.joshiejack.harvestcore.database.MineLoader;
import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import net.minecraft.util.math.BlockPos;

public class RoomGeneratorTunnelSprawl extends RoomGeneratorTunnel {
    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public BlockPos generate(AbstractDecoratorWrapper world, BlockPos ladderIn) {
        MineLoader.CIRCLE.generate(world, ladderIn);
        //BlockPos target2 = ladderIn.add(20, 0, 20);
        //boolean doReverse = world.rand.nextBoolean();
        boolean reverse = false;
        for (int i = 100; i >= 0; i -= 20) {
            int x = reverse ? 160 - i : i;
            BlockPos target = new BlockPos(x, 0, ladderIn.getZ());
            connect(world, ladderIn, target);

            int z = reverse ? 160 - i : i;
            ladderIn = new BlockPos(target.getX(), 0, z);
            connect(world, target, ladderIn);
            reverse = !reverse;
        }

        BlockPos end = new BlockPos(80 - world.rand.nextInt(30) + world.rand.nextInt(30), 0, 80 - world.rand.nextInt(30) + world.rand.nextInt(30));
        connect(world, ladderIn, end);
        RoomGenerator generator = world.tier.getValidGeneratorFromList(world.tier.getSimpleGenerators(), world.rand, end);
        return generator.generate(world, end);
    }
}
