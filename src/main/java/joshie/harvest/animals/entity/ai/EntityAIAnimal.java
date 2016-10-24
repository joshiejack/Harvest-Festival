package joshie.harvest.animals.entity.ai;

import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;

public abstract class EntityAIAnimal extends EntityAIBase {
    protected final EntityAnimal animal;
    protected int wanderTick;
    private AnimalStats stats;

    public EntityAIAnimal(EntityAnimal animal) {
        this.animal = animal;
    }

    protected AnimalStats getStats() {
        if (stats == null) {
            stats = EntityHelper.getStats(animal);
        }

        return stats;
    }
}