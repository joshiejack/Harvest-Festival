package uk.joshiejack.harvestcore.world.mine.dimension.decorators;

import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class DecoratorMushroom extends DecoratorWorldGen {
    @Override
    protected void decorate(AbstractDecoratorWrapper worldIn, Random rand, BlockPos position) {
        Block block = rand.nextBoolean() ? Blocks.BROWN_MUSHROOM_BLOCK : Blocks.RED_MUSHROOM_BLOCK;
        int i = rand.nextInt(3) + 4;

        if (rand.nextInt(12) == 0) {
            i *= 2;
        }

        if (position.getY() >= 1 && position.getY() + i + 1 < 256) {
            int k2 = position.getY() + i;

            if (block == Blocks.RED_MUSHROOM_BLOCK) {
                k2 = position.getY() + i - 3;
            }

            for (int l2 = k2; l2 <= position.getY() + i; ++l2) {
                int j3 = 1;

                if (l2 < position.getY() + i) {
                    ++j3;
                }

                if (block == Blocks.BROWN_MUSHROOM_BLOCK) {
                    j3 = 3;
                }

                int k3 = position.getX() - j3;
                int l3 = position.getX() + j3;
                int j1 = position.getZ() - j3;
                int k1 = position.getZ() + j3;

                for (int l1 = k3; l1 <= l3; ++l1) {
                    for (int i2 = j1; i2 <= k1; ++i2) {
                        int j2 = 5;

                        if (l1 == k3) {
                            --j2;
                        } else if (l1 == l3) {
                            ++j2;
                        }

                        if (i2 == j1) {
                            j2 -= 3;
                        } else if (i2 == k1) {
                            j2 += 3;
                        }

                        BlockHugeMushroom.EnumType blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.byMetadata(j2);

                        if (block == Blocks.BROWN_MUSHROOM_BLOCK || l2 < position.getY() + i) {
                            if ((l1 == k3 || l1 == l3) && (i2 == j1 || i2 == k1)) {
                                continue;
                            }

                            if (l1 == position.getX() - (j3 - 1) && i2 == j1) {
                                blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_WEST;
                            }

                            if (l1 == k3 && i2 == position.getZ() - (j3 - 1)) {
                                blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_WEST;
                            }

                            if (l1 == position.getX() + (j3 - 1) && i2 == j1) {
                                blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_EAST;
                            }

                            if (l1 == l3 && i2 == position.getZ() - (j3 - 1)) {
                                blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_EAST;
                            }

                            if (l1 == position.getX() - (j3 - 1) && i2 == k1) {
                                blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_WEST;
                            }

                            if (l1 == k3 && i2 == position.getZ() + (j3 - 1)) {
                                blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_WEST;
                            }

                            if (l1 == position.getX() + (j3 - 1) && i2 == k1) {
                                blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_EAST;
                            }

                            if (l1 == l3 && i2 == position.getZ() + (j3 - 1)) {
                                blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_EAST;
                            }
                        }

                        if (blockhugemushroom$enumtype == BlockHugeMushroom.EnumType.CENTER && l2 < position.getY() + i) {
                            blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.ALL_INSIDE;
                        }

                        if (position.getY() >= position.getY() + i - 1 || blockhugemushroom$enumtype != BlockHugeMushroom.EnumType.ALL_INSIDE) {
                            BlockPos blockpos = new BlockPos(l1, l2, i2);
                            if (worldIn.isAirBlock(blockpos)) {
                                worldIn.setBlockState(blockpos, block.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, blockhugemushroom$enumtype));
                            }
                        }
                    }
                }
            }

            for (int i3 = 0; i3 < i; ++i3) {
                if (worldIn.isAirBlock(position.up(i3))) {
                    worldIn.setBlockState(position.up(i3), block.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM));
                }
            }

        }
    }
}
