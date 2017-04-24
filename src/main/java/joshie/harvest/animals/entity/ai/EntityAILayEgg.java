package joshie.harvest.animals.entity.ai;

import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.animals.INest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityAILayEgg extends EntityAIAnimal {
    private boolean isReadyToLayEgg;
    private int currentTask;
    private int eggTimer;

    @SuppressWarnings("ConstantConditions")
    public EntityAILayEgg(EntityAnimal animal) {
        super(animal);
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (runDelay <= 0) {
            currentTask = -1;
            isReadyToLayEgg = getStats() != null && getStats().canProduce() && getStats().performTest(AnimalTest.HAS_EATEN);
        }

        return isReadyToLayEgg && super.shouldExecute();
    }

    @Override
    public boolean continueExecuting() {
        return currentTask >= 0 && super.continueExecuting();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        World world = animal.world;
        if (getIsAboveDestination()) {
            if (eggTimer == 0) eggTimer = 100;
            else eggTimer--;

            if (eggTimer <= 0) {
                if (currentTask == 1 && isEmptyNest(world, destinationBlock.up())) {
                    IBlockState iblockstate = world.getBlockState(destinationBlock.up());
                    ((INest) iblockstate.getBlock()).layEgg(getStats(), world, destinationBlock.up(), iblockstate);
                }

                currentTask = -1;
                runDelay = 10;
            }
        }
    }

    @Override
    protected boolean shouldMoveTo(@Nonnull World worldIn, @Nonnull BlockPos pos) {
        if (animal.getDistance(pos.getX(), pos.getY(), pos.getZ()) <= 32D && isEmptyNest(worldIn, pos.up())) {
            if (isReadyToLayEgg && (currentTask == 1 || currentTask < 0)) {
                currentTask = 1;
                return true;
            }
        }

        return false;
    }

    private boolean isEmptyNest(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof INest && ((INest)state.getBlock()).isNest(getStats(), world, pos, state);
    }

    /*

    @Override
    public boolean shouldExecute() {
        if(!animal.isChild() && getStats() != null && getStats().canProduce() && getStats().performTest(AnimalTest.HAS_EATEN)) {
            wanderTick--;

            return wanderTick <= 0;
        } else return false;
    }

    @Override
    public boolean continueExecuting() {
        return wanderTick <= 0;
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (wanderTick %20 == 0) {
            check:
            for (int x = -5; x <= 5; x++) {
                for (int z = -5; z <= 5; z++) {
                    for (int y = 0; y <= 3; y++) {
                        BlockPos position = new BlockPos(animal).add(x, y, z);
                        IBlockState state = animal.world.getBlockState(position);
                        Block block = state.getBlock();
                        if (block instanceof INest) {
                            if (((INest) block).layEgg(getStats(), animal.world, position, state)) {
                                wanderTick = 200;
                                break check;
                            }
                        }
                    }
                }
            }
        }

        wanderTick--;
        if (animal.world.rand.nextDouble() < 0.005D || wanderTick <= Short.MIN_VALUE) {
            wanderTick = 200;
        }
    }

    @Override
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        return false;
    } */
}