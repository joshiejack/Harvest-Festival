package joshie.harvest.animals.entity;

import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.animals.INest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAILayEgg extends EntityAIBase {
    private final EntityHarvestChicken animal;
    private final IAnimalTracked tracked;
    private int wanderTick;

    public EntityAILayEgg(IAnimalTracked<EntityHarvestChicken> animal) {
        this.animal = animal.getAsEntity();
        this.tracked = animal;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if(!animal.isChild() && tracked.getData().canProduce() && !tracked.getData().isHungry()) {
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
        if (block instanceof INest) {
            if(((INest) block).layEgg(tracked, animal.worldObj, position, state)) {
                wanderTick = 200;
            }
        }

        wanderTick--;
        if (animal.worldObj.rand.nextDouble() < 0.005D || wanderTick < Short.MIN_VALUE) {
            wanderTick = 200;
        }
    }
}