package uk.joshiejack.harvestcore.world.mine.dimension.floors;

import com.google.common.collect.Lists;
import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import uk.joshiejack.penguinlib.util.BlockStates;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class RoomGeneratorCircle extends RoomGenerator {
    public static final int MAX = 12;

    @Override
    public BlockPos generate(AbstractDecoratorWrapper world, BlockPos ladder) {
        int radius = (MAX / 4) + 1 + world.rand.nextInt((MAX / 2) + 1);
        int adjustTimer = MAX * 2;
        while(adjustTimer > 0) {
            int x = -(radius / 2) + world.rand.nextInt(radius);
            int z = -(radius / 2) + world.rand.nextInt(radius);
            BlockPos target = ladder.add(x, 0, z);
            if (world.hasBuffer(target, MAX) && world.rand.nextInt(radius * 10) == 0) {
                ladder = target;
                break;
            }

            adjustTimer--;
        }

        List<BlockPos> positions = Lists.newArrayList();
        for (int i = -radius; i <= radius; i++) {
            for (int l = -radius; l <= radius; l++) {
                if (i * i + l * l >= (radius + 0.50f) * (radius + 0.50f)) {
                    continue;
                }

                BlockPos target = ladder.add(i, 0, l);
                world.setBlockState(target, world.tier.getFloor(world.floor));
                int minY = i == -radius || i == radius || l == -radius || l == radius ? 2 : (i < -radius + 4 || i > radius - 4 || l < -radius + 4 || l > radius - 4) ? 3: 4;
                for (int y = 1; y < minY + world.rand.nextInt(6 - minY); y++) {
                    world.setBlockState(target.up(y), BlockStates.AIR);
                }

                positions.add(target);
            }
        }

        return getLadderPosition(world, positions, MAX);
    }
}
