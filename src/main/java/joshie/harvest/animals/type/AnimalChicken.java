package joshie.harvest.animals.type;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.animals.AnimalFoodType.SEED;

public class AnimalChicken extends AnimalAbstract {
    public AnimalChicken() {
        super("chicken", 3, 10, SEED);
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

        return HFAnimals.EGG.getStack(size);
    }

    @Override
    public int getDaysBetweenProduction() {
        return 1;
    }

    @Override
    public int getGenericTreatCount() {
        return 2;
    }

    @Override
    public int getTypeTreatCount() {
        return 29;
    }
}