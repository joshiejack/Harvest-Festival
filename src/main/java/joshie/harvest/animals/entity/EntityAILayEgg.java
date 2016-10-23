package joshie.harvest.animals.entity;

import joshie.harvest.api.animals.INest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.BlockPos;

public class EntityAILayEgg extends EntityAIAnimal {
    private int wanderTick;

    @SuppressWarnings("ConstantConditions")
    public EntityAILayEgg(EntityAnimal animal) {
        super(animal);
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if(!animal.isChild() && getStats().canProduce() && !getStats().isHungry()) {
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
}