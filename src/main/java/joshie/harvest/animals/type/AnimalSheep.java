package joshie.harvest.animals.type;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalSpawner.Spawner;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.core.helpers.SizeableHelper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.animals.AnimalFoodType.GRASS;

public class AnimalSheep extends AnimalLivestock {
    public AnimalSheep() {
        super("sheep", 8, 12, GRASS);
    }

    @Override
    public ItemStack getIcon() {
        return HFAnimals.ANIMAL.getStackFromEnum(Spawner.SHEEP);
    }

    @Override
    public int getDaysBetweenProduction() {
        return 7;
    }

    @Override
    public int getGenericTreatCount() {
        return 2;
    }

    @Override
    public int getTypeTreatCount() {
        return 29;
    }

    @Override
    public int getRelationshipBonus(AnimalAction action) {
        switch (action) {
            case OUTSIDE:       return 2;
            case CLAIM_PRODUCT: return 20;
        }

        return super.getRelationshipBonus(action);
    }

    @Override
    public ItemStack getProduct(AnimalStats stats) {
        return SizeableHelper.getWool(stats);
    }

    @Override
    public void refreshProduct(AnimalStats stats, EntityAnimal entity) {
        entity.eatGrassBonus();
    }
}
