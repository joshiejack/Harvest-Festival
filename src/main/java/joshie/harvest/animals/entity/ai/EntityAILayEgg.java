package joshie.harvest.animals.entity.ai;

import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.animals.INest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityAILayEgg extends EntityAIAnimal {
    private boolean isReadyToLayEgg;
    private int currentTask;

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

        return false;
    }

    @Override
    public boolean continueExecuting() {
        return currentTask >= 0 && super.continueExecuting();
    }

    @Override
    public void updateTask() {
        System.out.println("Chicken looking for egg nest?");

        super.updateTask();
        animal.getLookHelper().setLookPosition((double)destinationBlock.getX() + 0.5D, (double)(destinationBlock.getY() + 1), (double)destinationBlock.getZ() + 0.5D, 10.0F, (float)animal.getVerticalFaceSpeed());

        if (getIsAboveDestination()) {
            World world = animal.worldObj;
            BlockPos blockpos = destinationBlock.up();
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();
            if (currentTask == 1 && iblockstate.getBlock() instanceof INest) {
                ((INest) block).layEgg(getStats(), animal.worldObj, blockpos, iblockstate);
            }

            currentTask = -1;
            runDelay = 10;
        }
    }

    @Override
    protected boolean shouldMoveTo(@Nonnull World worldIn, @Nonnull BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos.up());
        if (iblockstate.getBlock() instanceof INest) {
            if (isReadyToLayEgg && (currentTask == 1 || currentTask < 0)) {
                currentTask = 1;
                return true;
            }
        }

        return false;
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
                        IBlockState state = animal.worldObj.getBlockState(position);
                        Block block = state.getBlock();
                        if (block instanceof INest) {
                            if (((INest) block).layEgg(getStats(), animal.worldObj, position, state)) {
                                wanderTick = 200;
                                break check;
                            }
                        }
                    }
                }
            }
        }

        wanderTick--;
        if (animal.worldObj.rand.nextDouble() < 0.005D || wanderTick <= Short.MIN_VALUE) {
            wanderTick = 200;
        }
    }

    @Override
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        return false;
    } */
}