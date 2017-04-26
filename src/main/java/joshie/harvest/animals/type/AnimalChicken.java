package joshie.harvest.animals.type;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.animals.item.ItemAnimalSpawner.Spawner;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.core.helpers.SizeableHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

import static joshie.harvest.api.animals.AnimalFoodType.SEED;

public class AnimalChicken extends AnimalAbstract {
    public AnimalChicken() {
        super("chicken", 3, 10, SEED);
    }

    @Override
    @Nonnull
    public ItemStack getIcon() {
        return HFAnimals.ANIMAL.getStackFromEnum(Spawner.CHICKEN);
    }

    @Override
    public int getRelationshipBonus(AnimalAction action) {
        switch (action) {
            case OUTSIDE:   return 5;
            case FEED:      return 100;
        }

        return super.getRelationshipBonus(action);
    }

    @Override
    @Nonnull
    public ItemStack getProduct(AnimalStats stats) {
        return SizeableHelper.getEgg(stats);
    }

    @Override
    public NonNullList<ItemStack> getProductsForDisplay(AnimalStats stats) {
        return SizeableHelper.getSizeablesForDisplay(stats, Sizeable.EGG);
    }

    @Override
    public int getDaysBetweenProduction() {
        return 1;
    }

    @Override
    public int getGenericTreatCount() {
        return 5;
    }

    @Override
    public int getTypeTreatCount() {
        return 26;
    }
}