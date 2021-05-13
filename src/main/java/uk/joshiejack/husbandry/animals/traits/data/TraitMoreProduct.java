package uk.joshiejack.husbandry.animals.traits.data;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.generic.MathsHelper;
import net.minecraft.entity.EntityAgeable;

@PenguinLoader("more_product") //Animals with this, have their products reset quicker based on happiness
public class TraitMoreProduct extends AnimalTraitProduct {
    private int productsPerDay = 1; //How many products the animals give every 24 hours

    public TraitMoreProduct(String name) {
        super(name);
    }

    @Override
    public AnimalTraitData create() {
        return new TraitMoreProduct(getName());
    }

    protected int recalculateProductsPerDay() {
        return MathsHelper.convertRange(0, stats.getMaxRelationship(), 1, 5, stats.getHappiness());
    }

    @Override
    public AnimalTraitData setAnimal(AnimalStats<?> stats, EntityAgeable entity) {
        super.setAnimal(stats, entity);
        //Recalculate products per day
        productsPerDay = recalculateProductsPerDay();
        return this;
    }

    @Override
    public void onBihourlyTick() {
        if (productsProduced < productsPerDay) {
            stats.resetProduct(); //Reset the product every two hours
        }
    }

    @Override
    public void onNewDay() {
        productReset++;
        if (productReset >= stats.getType().getProducts().getDaysBetweenProducts()) {
            stats.resetProduct();
            productReset = 0;
        }

        //Recalculate products per day
        productsPerDay = recalculateProductsPerDay();
    }
}
