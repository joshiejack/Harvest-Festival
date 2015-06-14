package joshie.harvest.animals.ai;

import joshie.harvest.api.animals.IAnimalFeeder;
import joshie.harvest.api.animals.IAnimalTracked;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;

public class EntityAIEat extends EntityAIBase {
    private EntityAnimal theAnimal;
    private IAnimalTracked tracked;

    public EntityAIEat(IAnimalTracked animal) {
        this.theAnimal = (EntityAnimal) animal;
        this.tracked = animal;
    }

    @Override
    public boolean shouldExecute() {
        return tracked.getData().isHungry();
    }

    @Override
    public boolean continueExecuting() {
        return tracked.getData().isHungry();
    }

    @Override
    public void updateTask() {
        int x = (int) (theAnimal.posX + 3 - theAnimal.worldObj.rand.nextInt(7));
        int y = (int) theAnimal.posY;
        int z = (int) (theAnimal.posZ + 3 - theAnimal.worldObj.rand.nextInt(7));
        Block block = theAnimal.worldObj.getBlock(x, y, z);
        if (block instanceof IAnimalFeeder) {
            if (((IAnimalFeeder)block).canFeedAnimal(tracked, theAnimal.worldObj, x, y, z)) {
                tracked.getData().setFed();
            }
        }
    }
}