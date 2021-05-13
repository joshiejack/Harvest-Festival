package uk.joshiejack.husbandry.animals.traits.ai;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.entity.ai.EntityAILayEgg;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityChicken;

@PenguinLoader("lays_egg")
public class TraitLaysEgg extends AnimalTraitAI {
    public TraitLaysEgg(String name) {
        super(name);
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public EntityAIBase getAI(EntityAgeable ageable, AnimalStats<?> stats) {
        //Force the egg timer to max value
        if (ageable instanceof EntityChicken) {
            ((EntityChicken) ageable).timeUntilNextEgg = Integer.MAX_VALUE;
        }

        return new EntityAILayEgg(ageable, stats);
    }
}
