package uk.joshiejack.husbandry.animals.traits.ai;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.entity.ai.EntityAIEatTrough;
import uk.joshiejack.husbandry.item.ItemFeed;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;

@PenguinLoader("eats_slop")
public class TraitEatsSlop extends AnimalTraitAI {
    public TraitEatsSlop(String name) {
        super(name);
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public EntityAIBase getAI(EntityAgeable ageable, AnimalStats<?> stats) {
        return new EntityAIEatTrough(ageable, stats, ItemFeed.Feed.SLOP);
    }
}
