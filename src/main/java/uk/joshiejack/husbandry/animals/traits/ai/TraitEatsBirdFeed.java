package uk.joshiejack.husbandry.animals.traits.ai;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.entity.ai.EntityAIEatBirdFeed;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;

@PenguinLoader("eats_bird_feed")
public class TraitEatsBirdFeed extends AnimalTraitAI {
    public TraitEatsBirdFeed(String name) {
        super(name);
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public EntityAIBase getAI(EntityAgeable ageable, AnimalStats<?> stats) {
        return new EntityAIEatBirdFeed(ageable, stats);
    }
}
