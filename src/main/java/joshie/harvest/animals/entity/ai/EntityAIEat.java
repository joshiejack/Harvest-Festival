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
    public boolean continueExecuting() {
        return currentTask >= 0 && super.continueExecuting();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        animal.getLookHelper().setLookPosition((double)destinationBlock.getX() + 0.5D, (double)(destinationBlock.getY() + 1), (double)destinationBlock.getZ() + 0.5D, 10.0F, (float)animal.getVerticalFaceSpeed());
        World world = animal.worldObj;
        BlockPos blockpos = destinationBlock.up();
        if (animal.getDistanceSq(blockpos) <= 3D) {
            IBlockState iblockstate = world.getBlockState(blockpos);
            if (currentTask == 1 && isEdible(iblockstate)) {
                eat(blockpos, iblockstate);
            }

            currentTask = -1;
            runDelay = 0;
        }
    }

    @Override
    protected boolean shouldMoveTo(@Nonnull World worldIn, @Nonnull BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos.up());
        if (animal.getDistanceSq(pos.up()) <= 32D && isEdible(iblockstate)) {
            if (isReadyToEat && (currentTask == 1 || currentTask < 0)) {
                currentTask = 1;
                return true;
            }
        }

        return false;
    }

    protected boolean isEdible(IBlockState iblockstate) {
        return iblockstate.getBlock() instanceof IAnimalFeeder;
    }

    protected void eat(BlockPos pos, IBlockState state) {
        ((IAnimalFeeder) state.getBlock()).feedAnimal(getStats(), animal.worldObj, pos, state, false);
    }
}