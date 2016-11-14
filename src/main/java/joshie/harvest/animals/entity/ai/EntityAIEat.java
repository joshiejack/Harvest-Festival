package joshie.harvest.animals.entity.ai;

import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.animals.IAnimalFeeder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.BlockPos;

public class EntityAIEat extends EntityAIAnimal {
    public EntityAIEat(EntityAnimal animal) {
        super(animal);
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if(getStats() != null && !getStats().performTest(AnimalTest.HAS_EATEN)) {
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
        BlockPos position = new BlockPos(animal).add(animal.worldObj.rand.nextInt(8) - 4, animal.worldObj.rand.nextInt(3), animal.worldObj.rand.nextInt(8) - 4);
        IBlockState state = animal.worldObj.getBlockState(position);
        Block block = state.getBlock();
        if (!attemptToEat(position, state, block)) {
            wanderTick--;
            if (animal.worldObj.rand.nextDouble() < 0.005D || wanderTick < Short.MIN_VALUE || getStats().performTest(AnimalTest.HAS_EATEN)) {
                wanderTick = 200;
            }
        }
    }

    protected boolean attemptToEat(BlockPos position, IBlockState state, Block block) {
        if (block instanceof IAnimalFeeder) {
            if(((IAnimalFeeder) block).feedAnimal(getStats(), animal.worldObj, position, state, false)) {
                wanderTick = 200;
                return true;
            }
        }

        return false;
    }
}