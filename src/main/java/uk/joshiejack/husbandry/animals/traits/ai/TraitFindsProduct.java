package uk.joshiejack.husbandry.animals.traits.ai;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.entity.ai.EntityAIFindBlock;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;

@PenguinLoader("finds_product")
public class TraitFindsProduct extends AnimalTraitAI {
    public TraitFindsProduct(String name) {
        super(name);
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public EntityAIBase getAI(EntityAgeable ageable, AnimalStats<?> stats) {
        return new EntityAIFindBlock(ageable, stats);
    }
}
