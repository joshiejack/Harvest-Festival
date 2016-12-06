package joshie.harvest.animals.entity.ai;

import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.BlockPos;

public abstract class EntityAIAnimal extends EntityAIMoveToBlock {
    protected final EntityAnimal animal;
    private AnimalStats stats;

    public EntityAIAnimal(EntityAnimal animal) {
        super(animal, 0.8D, 64);
        this.animal = animal;
    }

    @Override
    public boolean shouldExecute() {
        if (runDelay > 0) {
            --runDelay;
            return false;
        } else {
            runDelay = 200 + animal.getRNG().nextInt(200);
            return searchForDestination();
        }
    }

    protected AnimalStats getStats() {
        if (stats == null) {
            stats = EntityHelper.getStats(animal);
        }

        return stats;
    }

    private boolean searchForDestination() {
        int i = 64;
        BlockPos blockpos = new BlockPos(animal);
        for (int k = 0; k <= 1; k = k > 0 ? -k : 1 - k)  {
            for (int l = 0; l < i; ++l) {
                for (int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1)  {
                    for (int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                        BlockPos blockpos1 = blockpos.add(i1, k - 1, j1);
                        if (animal.isWithinHomeDistanceFromPosition(blockpos1) && shouldMoveTo(animal.worldObj, blockpos1)) {
                            destinationBlock = blockpos1;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}