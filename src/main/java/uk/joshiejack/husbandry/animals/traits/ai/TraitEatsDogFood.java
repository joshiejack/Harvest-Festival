package uk.joshiejack.husbandry.animals.traits.ai;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.block.BlockTray;
import uk.joshiejack.husbandry.entity.ai.EntityAIEatBowl;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;

@PenguinLoader("eats_dog_food")
public class TraitEatsDogFood extends AnimalTraitAI {
    public TraitEatsDogFood(String name) {
        super(name);
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public EntityAIBase getAI(EntityAgeable ageable, AnimalStats<?> stats) {
        return new EntityAIEatBowl(ageable, stats, BlockTray.Tray.BOWL_DOG);
    }
}
