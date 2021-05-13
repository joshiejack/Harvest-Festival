package uk.joshiejack.husbandry.animals.traits.ai;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.block.BlockTray;
import uk.joshiejack.husbandry.entity.ai.EntityAIEatBowl;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;

@PenguinLoader("eats_cat_food")
public class TraitEatsCatFood extends AnimalTraitAI {
    public TraitEatsCatFood(String name) {
        super(name);
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public EntityAIBase getAI(EntityAgeable ageable, AnimalStats<?> stats) {
        return new EntityAIEatBowl(ageable, stats, BlockTray.Tray.BOWL_CAT);
    }
}
