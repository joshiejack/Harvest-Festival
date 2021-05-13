package uk.joshiejack.husbandry.animals.traits.ai;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.entity.ai.EntityAIEatTallGrass;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntitySheep;

@PenguinLoader("eats_grass")
public class TraitEatsGrass extends AnimalTraitAI {
    public TraitEatsGrass(String name) {
        super(name);
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public EntityAIBase getAI(EntityAgeable ageable, AnimalStats<?> stats) {
        if (ageable instanceof EntitySheep) {
            EntitySheep sheep = (EntitySheep) ageable;
            sheep.tasks.removeTask(sheep.entityAIEatGrass);
        }
        return new EntityAIEatTallGrass(ageable, stats);
    }
}