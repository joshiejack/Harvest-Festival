package joshie.harvest.animals.entity;

import joshie.harvest.api.animals.IAnimalFeeder;
import joshie.harvest.api.animals.IAnimalTracked;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIEat extends EntityAIBase {
    private World worldObj;
    private EntityAnimal animal;
    private IAnimalTracked tracked;
    private int wanderTick;

    public EntityAIEat(IAnimalTracked animal) {
        this.worldObj = animal.getAsEntity().worldObj;
        this.animal = animal.getAsEntity();
        this.tracked = animal;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if(tracked.getData().isHungry()) {
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
        BlockPos position = new BlockPos(animal).add(worldObj.rand.nextInt(8) - 4, 0, worldObj.rand.nextInt(8) - 4);
        IBlockState state = animal.worldObj.getBlockState(position);
        Block block = state.getBlock();
        if (block instanceof IAnimalFeeder) {
            if(((IAnimalFeeder) block).feedAnimal(tracked, worldObj, position, state)) {
                wanderTick = 200;
            }
        }


        wanderTick--;
        if (worldObj.rand.nextDouble() < 0.005D || wanderTick < Short.MIN_VALUE) {
            wanderTick = 200;
        }
    }
}