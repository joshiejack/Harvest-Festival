package joshie.harvest.animals.type;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalSpawner.Spawner;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.core.Size;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.animals.AnimalFoodType.SEED;

public class AnimalChicken extends AnimalAbstract {
    public AnimalChicken() {
        super("chicken", 3, 10, SEED);
    }

    @Override
    public ItemStack getIcon() {
        return HFAnimals.ANIMAL.getStackFromEnum(Spawner.CHICKEN);
    }

    @Override
    public int getRelationshipBonus(AnimalAction action) {
        switch (action) {
            case FEED:      return 30;
            case OUTSIDE:   return 5;
        }

        return super.getRelationshipBonus(action);
    }

    @Override
    public ItemStack getProduct(EntityPlayer player, AnimalStats stats) {
        Size size = null;
        int relationship = HFApi.player.getRelationsForPlayer(player).getRelationship(EntityHelper.getEntityUUID(stats.getAnimal()));
        for (Size s : Size.values()) {
            if (relationship >= s.getRelationshipRequirement()) size = s;
        }

        return HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.EGG, size);
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