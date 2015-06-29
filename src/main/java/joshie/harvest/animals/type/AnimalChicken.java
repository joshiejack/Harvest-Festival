package joshie.harvest.animals.type;

import static joshie.harvest.api.animals.AnimalFoodType.SEED;
import static joshie.harvest.core.helpers.generic.ItemHelper.spawnByEntity;
import joshie.harvest.animals.entity.EntityHarvestChicken;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.core.helpers.SizeableHelper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class AnimalChicken extends AbstractAnimal {
    public AnimalChicken() {
        super("chicken", 3, 10, SEED);
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

    @Override
    public void newDay(IAnimalData data, EntityAnimal entity) {
        EntityPlayer player = data.getOwner();
        if (player != null) {
            ItemStack egg = SizeableHelper.getEgg(player, (EntityHarvestChicken) entity);
            entity.playSound("mob.chicken.plop", 1.0F, (entity.worldObj.rand.nextFloat() - entity.worldObj.rand.nextFloat()) * 0.2F + 1.0F);
            spawnByEntity(entity, egg);
        }
    }
}
