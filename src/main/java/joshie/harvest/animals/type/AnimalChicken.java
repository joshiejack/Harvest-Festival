package joshie.harvest.animals.type;

import static joshie.harvest.api.animals.AnimalFoodType.SEED;
import static joshie.harvest.core.helpers.SizeableHelper.getEgg;
import static joshie.harvest.core.helpers.generic.ItemHelper.spawnByEntity;
import joshie.harvest.core.helpers.AnimalHelper;
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
    public void newDay(EntityAnimal entity) {
        EntityPlayer player = AnimalHelper.getOwner(entity);
        if (player != null) {
            ItemStack egg = getEgg(player, entity);
            entity.playSound("mob.chicken.plop", 1.0F, (entity.worldObj.rand.nextFloat() - entity.worldObj.rand.nextFloat()) * 0.2F + 1.0F);
            spawnByEntity(entity, egg);
        }
    }
}
