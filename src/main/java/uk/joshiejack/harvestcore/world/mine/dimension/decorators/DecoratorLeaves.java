package uk.joshiejack.harvestcore.world.mine.dimension.decorators;

import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import uk.joshiejack.penguinlib.util.BlockStates;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class DecoratorLeaves extends AbstractDecoratorModifyable {
    @Override
    public void decorate(AbstractDecoratorWrapper world, BlockPos pos) {
        for (int y = world.rand.nextInt(3); y < 6; y++) {
            for (int i = -2 + world.rand.nextInt(3); i <= 2 - world.rand.nextInt(3); i++) {
                for (int j = -2 + world.rand.nextInt(3); j <= 2 - world.rand.nextInt(3); j++) {
                    BlockPos target = pos.add(i, y, j);
                    if (world.isAirBlock(target) && world.isNextTo(target, BlockStates.AIR) && (world.isNextTo(target, world.tier.getWall(world.floor)) || world.isNextTo(target, Blocks.LOG.getDefaultState()))) {
                        world.setBlockState(target, state);
                        if (world.rand.nextInt(8) == 0) {
                            BlockPos blockpos3 = target.west();
                            BlockPos blockpos4 = target.east();
                            BlockPos blockpos1 = target.north();
                            BlockPos blockpos2 = target.south();
                            if (world.rand.nextInt(4) == 0 && world.isAirBlock(blockpos3)) {
                                addVine(world, blockpos3, BlockVine.EAST);
                            }

                            if (world.rand.nextInt(4) == 0 && world.isAirBlock(blockpos4)) {
                                addVine(world, blockpos4, BlockVine.WEST);
                            }

                            if (world.rand.nextInt(4) == 0 && world.isAirBlock(blockpos1)) {
                                addVine(world, blockpos1, BlockVine.SOUTH);
                            }

                            if (world.rand.nextInt(4) == 0 && world.isAirBlock(blockpos2)) {
                                addVine(world, blockpos2, BlockVine.NORTH);
                            }
                        }
                    }
                }
            }
        }
    }

    private void addVine(AbstractDecoratorWrapper wrapper, BlockPos pos, PropertyBool prop) {
        IBlockState iblockstate = Blocks.VINE.getDefaultState().withProperty(prop, Boolean.TRUE);
        int i = 4;
        for (BlockPos blockpos = pos.down(); wrapper.isAirBlock(blockpos) && i > 0; --i) {
            wrapper.setBlockState(blockpos, iblockstate);
            blockpos = blockpos.down();
        }
    }
}
