package uk.joshiejack.husbandry.scripting.wrappers;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.animals.traits.data.TraitCleanable;
import uk.joshiejack.penguinlib.scripting.wrappers.AbstractJS;

@SuppressWarnings({"unused", "rawtypes"})
public class AnimalStatsJS extends AbstractJS<AnimalStats> {
    public AnimalStatsJS(AnimalStats<?> stats) {
        super(stats);
    }

    public boolean isClean() {
        return penguinScriptingObject.hasTrait("cleanable")
                && ((TraitCleanable)penguinScriptingObject.getTrait("cleanable")).isClean();
    }

    public boolean canProduce() {
        return penguinScriptingObject.canProduceProduct();
    }
}
