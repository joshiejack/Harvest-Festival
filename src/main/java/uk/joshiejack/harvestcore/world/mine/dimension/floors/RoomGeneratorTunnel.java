package uk.joshiejack.harvestcore.world.mine.dimension.floors;

import uk.joshiejack.harvestcore.world.mine.dimension.MineChunkGenerator;
import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import uk.joshiejack.penguinlib.util.BlockStates;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class RoomGeneratorTunnel extends RoomGenerator {
    @Override
    public boolean isSimple() {
        return false;
    }

    void connect(AbstractDecoratorWrapper world, BlockPos ladder, BlockPos target) {
        int ladderX = ladder.getX();
        int ladderZ = ladder.getZ();
        Random rand = world.rand;
        int timeassameX = 0;
        int timeassameZ = 0;
        int prevX = ladderX;
        int prevZ = ladderZ;
        int walkawaycounter = 0;
        int randomisertimer = 0;
        while (ladderX != target.getX() || ladderZ != target.getZ()) {
            if (walkawaycounter <= 0) {
                if (rand.nextInt(16) == 0) {
                    walkawaycounter = 6 + rand.nextInt(6);
                }
            }

            boolean traverseX = rand.nextBoolean();
            int width = 2 + rand.nextInt(6);
            if (prevX == ladderX) timeassameX++;
            else timeassameX = 0;
            if (prevZ == ladderZ) timeassameZ++;
            else timeassameZ = 0;

            prevX = ladderX;
            prevZ = ladderZ;

            if ((timeassameX >= 5 || timeassameZ >= 5 || randomisertimer > 0) && ladderX > 7 && ladderX < 153 && ladderZ > 7 && ladderZ < 153) {
                ladderX = ladderX - 1 + rand.nextInt(3);
                ladderZ = ladderZ - 1 + rand.nextInt(3);
                if (randomisertimer <= 0) randomisertimer = 2 + rand.nextInt(3);
                else randomisertimer--;
            } else if (walkawaycounter > 0) {
                walkawaycounter--;
                if (ladderX > 7 && ladderX < 153 && ladderZ > 7 && ladderZ < 153) {
                    if (traverseX) {
                        if (ladderX > target.getX()) ladderX++;
                        else if (ladderX < target.getX()) ladderX--;
                    } else {
                        if (ladderZ > target.getZ()) ladderZ++;
                        else if (ladderZ < target.getZ()) ladderZ--;
                    }
                }
            } else if (traverseX) {
                if (ladderX > target.getX()) ladderX--;
                else if (ladderX < target.getX()) ladderX++;
            } else {
                if (ladderZ > target.getZ()) ladderZ--;
                else if (ladderZ < target.getZ()) ladderZ++;
            }

            for (int w = 0; w < width; w++) {
                BlockPos connectTarget = new BlockPos(ladderX + (!traverseX ? w : 0), 0, ladderZ + (traverseX ? w : 0));
                world.setBlockState(connectTarget, world.tier.getFloor(world.floor));
                for (int y = 1; y < 3 + rand.nextInt(3); y++) {
                    world.setBlockState(connectTarget.up(y), BlockStates.AIR);
                }
            }
        }
    }

    @Override
    public BlockPos generate(AbstractDecoratorWrapper world, BlockPos ladderIn) {
        RoomGenerator generator = world.tier.getValidGeneratorFromList(world.tier.getSimpleGenerators(), world.rand, ladderIn);
        generator.generate(world, ladderIn);
        int min = RoomGeneratorCircle.MAX + 1;
        int max = MineChunkGenerator.CHUNKS_PER_SECTION * 16 - (min * 2);
        BlockPos target = new BlockPos(min + world.rand.nextInt(max), 0, min + world.rand.nextInt(max));
        RoomGenerator generator2 = world.tier.getValidGeneratorFromList(world.tier.getSimpleGenerators(), world.rand, target);
        BlockPos ladder = generator2.generate(world, target);
        connect(world, ladderIn, ladder);
        return ladder;
    }
}
