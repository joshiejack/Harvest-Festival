package joshie.harvest.animals.entity.ai;

import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.animals.IAnimalFeeder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityAIEat extends EntityAIAnimal {
    private boolean isReadyToEat;
    private int currentTask;
    private int eatTimer;

    public EntityAIEat(EntityAnimal animal) {
        super(animal);
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (runDelay <= 0) {
            currentTask = -1;
            isReadyToEat = getStats() != null && !getStats().performTest(AnimalTest.HAS_EATEN);
        }

        return isReadyToEat && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return currentTask >= 0 && super.shouldContinueExecuting();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        World world = animal.world;
        if (animal.getDistance(destinationBlock.getX(), destinationBlock.getY(), destinationBlock.getZ()) <= 1D) {
            animal.getLookHelper().setLookPosition((double)destinationBlock.getX() + 0.5D, (double)(destinationBlock.getY()), (double)destinationBlock.getZ() + 0.5D, 10.0F, (float)animal.getVerticalFaceSpeed());
            if (eatTimer == 0) eatTimer = 50;
            else eatTimer--;

            if (eatTimer <= 0) {
                IBlockState iblockstate = world.getBlockState(destinationBlock);
                if (currentTask == 1 && isEdible(destinationBlock, iblockstate)) {
                    eat(destinationBlock, iblockstate);
                }

                currentTask = -1;
                runDelay = 10;
            }
        }
    }

    @Override
    protected boolean shouldMoveTo(@Nonnull World worldIn, @Nonnull BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (animal.getDistance(pos.getX(), pos.getY(), pos.getZ()) <= 64D && isEdible(pos, iblockstate)) {
            if (isReadyToEat && (currentTask == 1 || currentTask < 0)) {
                currentTask = 1;
                return true;
            }
        }

        return false;
    }

    boolean isEdible(BlockPos pos, IBlockState state) {
        return state.getBlock() instanceof IAnimalFeeder && ((IAnimalFeeder) state.getBlock()).feedAnimal(getStats(), animal.world, pos, state, true);
    }

    protected void eat(BlockPos pos, IBlockState state) {
        ((IAnimalFeeder) state.getBlock()).feedAnimal(getStats(), animal.world, pos, state, false);
    }
}