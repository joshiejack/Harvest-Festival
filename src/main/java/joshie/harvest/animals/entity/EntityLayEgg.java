package joshie.harvest.animals.entity;

import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.animals.INest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityLayEgg extends EntityAIBase {
    private World worldObj;
    private EntityHarvestChicken animal;
    private IAnimalTracked tracked;

    public EntityLayEgg(IAnimalTracked animal) {
        this.worldObj = animal.getData().getAnimal().worldObj;
        this.animal = (EntityHarvestChicken) animal.getData().getAnimal();
        this.tracked = animal;
    }

    @Override
    public boolean shouldExecute() {
        return tracked.getData().canProduce();
    }

    @Override
    public boolean continueExecuting() {
        return tracked.getData().canProduce();
    }

    @Override
    public void updateTask() {
        BlockPos position = new BlockPos(animal).add(worldObj.rand.nextInt(6) - 3, 0, worldObj.rand.nextInt(6) - 3);
        IBlockState state = animal.worldObj.getBlockState(position);
        Block block = state.getBlock();
        if (block instanceof INest) {
            ((INest) block).layEgg(tracked, worldObj, position, state);
        }
    }
}