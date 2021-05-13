package uk.joshiejack.husbandry.animals.traits.data;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.generic.MathsHelper;

@PenguinLoader("faster_product_reset") //Animals with this, have their products reset quicker based on happiness
public class TraitFasterProductReset extends AnimalTraitProduct {

    public TraitFasterProductReset(String name) {
        super(name);
    }

    @Override
    public AnimalTraitData create() {
        return new TraitFasterProductReset(getName());
    }

    @Override
    public void onNewDay() {
        productReset++;
        int resetTarget = (1 - MathsHelper.convertRange(0, stats.getMaxRelationship(), 0, 0.4, stats.getHappiness())) * stats.getType().getProducts().getDaysBetweenProducts();
        if (productReset >= resetTarget) {
            stats.resetProduct();
            productReset = 0;
        }
    }
}
