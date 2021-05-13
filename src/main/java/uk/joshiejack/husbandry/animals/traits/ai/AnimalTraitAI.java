package uk.joshiejack.husbandry.animals.traits.ai;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.animals.traits.AnimalTrait;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;

public abstract class AnimalTraitAI extends AnimalTrait {
    public AnimalTraitAI(String name) {
        super(name);
    }

    public abstract EntityAIBase getAI(EntityAgeable ageable, AnimalStats<?> stats);

    public int getPriority() {
        return 5;
    }
}
