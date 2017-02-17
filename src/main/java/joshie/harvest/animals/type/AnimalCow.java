package joshie.harvest.animals.type;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalSpawner.Spawner;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.core.helpers.SizeableHelper;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.animals.AnimalFoodType.GRASS;

public class AnimalCow extends AnimalLivestock {
    public AnimalCow() {
        super("cow", 12, 20, GRASS);
    }

    @Override
    public ItemStack getIcon() {
        return HFAnimals.ANIMAL.getStackFromEnum(Spawner.COW);
    }

    @Override
    public int getDaysBetweenProduction() {
        return 1;
    }

    @Override
    public int getGenericTreatCount() {
        return 7;
    }

    @Override
    public int getTypeTreatCount() {
        return 24;
    }

    @Override
    public int getRelationshipBonus(AnimalAction action) {
        switch (action) {
            case OUTSIDE:       return 3;
            case CLAIM_PRODUCT: return 10;
        }

        return super.getRelationshipBonus(action);
    }

    @Override
    public ItemStack getProduct(AnimalStats stats) {
        return SizeableHelper.getMilk(stats);
    }
}
