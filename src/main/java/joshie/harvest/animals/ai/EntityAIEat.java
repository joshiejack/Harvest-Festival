package joshie.harvest.animals.ai;

import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalFeeder;
import joshie.harvest.api.animals.IAnimalTracked;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;

public class EntityAIEat extends EntityAIBase {
    private World worldObj;
    private EntityAnimal animal;
    private IAnimalTracked tracked;
    private IAnimalData data;

    public EntityAIEat(IAnimalTracked animal) {
        this.worldObj = animal.getData().getAnimal().worldObj;
        this.animal = animal.getData().getAnimal();
        this.tracked = animal;
        this.data = animal.getData();
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
        int x = (int) (animal.posX + 3 - worldObj.rand.nextInt(7));
        int y = (int) animal.posY;
        int z = (int) (animal.posZ + 3 - worldObj.rand.nextInt(7));
        Block block = animal.worldObj.getBlock(x, y, z);
        if (block instanceof IAnimalFeeder) {
            if (((IAnimalFeeder) block).canFeedAnimal(tracked, worldObj, x, y, z)) {
                tracked.getData().feed(tracked.getData().getOwner());
            }
        }
    }
}