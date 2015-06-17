package joshie.harvest.animals.type;

import static joshie.harvest.api.animals.AnimalFoodType.SEED;
import static joshie.harvest.core.helpers.generic.ItemHelper.spawnByEntity;
import joshie.harvest.animals.entity.EntityHarvestChicken;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.helpers.SizeableHelper;
import joshie.harvest.core.lib.SizeableMeta;
import joshie.harvest.init.HFConfig;
import joshie.harvest.relations.RelationshipHelper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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
    public void newDay(IAnimalData data, EntityAnimal entity) {
        EntityPlayer player = data.getOwner();
        if (player != null) {
            ItemStack egg = getEgg(player, (EntityHarvestChicken) entity);
            entity.playSound("mob.chicken.plop", 1.0F, (entity.worldObj.rand.nextFloat() - entity.worldObj.rand.nextFloat()) * 0.2F + 1.0F);
            spawnByEntity(entity, egg);
        }
    }

    private ItemStack getEgg(EntityPlayer player, EntityHarvestChicken animal) {
        Size size = Size.SMALL;
        int relationship = HFApi.RELATIONS.getAdjustedRelationshipValue(player, animal);
        int chance = Math.max(1, RelationshipHelper.ADJUSTED_MAX - relationship);
        int chance2 = Math.max(1, chance / 3);
        if (rand.nextInt(chance) == 0) size = Size.LARGE;
        else if (rand.nextInt(chance2) == 0) size = Size.MEDIUM;
        ItemStack sizeable = SizeableHelper.getSizeable(relationship, SizeableMeta.EGG, size);
        if (HFConfig.asm.EGG_OVERRIDE) {
            return new ItemStack(Items.egg, 1, sizeable.getItemDamage());
        } else return sizeable;
    }
}
