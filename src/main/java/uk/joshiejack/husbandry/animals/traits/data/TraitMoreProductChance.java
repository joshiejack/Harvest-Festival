package uk.joshiejack.husbandry.animals.traits.data;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.generic.MathsHelper;

@PenguinLoader("more_product_chance") //Animals with this, have their products reset quicker based on happiness
public class TraitMoreProductChance extends TraitMoreProduct {
    public TraitMoreProductChance(String name) {
        super(name);
    }

    @Override
    public AnimalTraitData create() {
        return new TraitMoreProductChance(getName());
    }

    @Override
    protected int recalculateProductsPerDay() {
        return 3;
    }

    @Override
    public void onBihourlyTick() {
        if ((productsProduced == 1 && (101 - MathsHelper.convertRange(0, stats.getMaxRelationship(), 1, 100, stats.getHappiness())) == 0) ||
                (productsProduced == 2 && (201 - MathsHelper.convertRange(0, stats.getMaxRelationship(), 1, 200, stats.getHappiness())) == 0)) {
            stats.resetProduct();
        }
    }
}
