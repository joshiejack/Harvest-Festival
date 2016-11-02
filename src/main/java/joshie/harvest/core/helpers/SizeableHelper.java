package joshie.harvest.core.helpers;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.core.Size;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SizeableHelper {
    public static ItemStack getMilk(EntityPlayer player, EntityAnimal animal, AnimalStats stats) {
        return SizeableHelper.getSizeable(player, animal, stats, Sizeable.MILK);
    }

    public static ItemStack getWool(EntityPlayer player, EntityAnimal animal, AnimalStats stats) {
        return SizeableHelper.getSizeable(player, animal, stats, Sizeable.WOOL);
    }

    public static ItemStack getSizeable(EntityPlayer player, EntityAnimal animal, AnimalStats stats, Sizeable sizeable) {
        Size size = null;
        int relationship = HFApi.player.getRelationsForPlayer(player).getRelationship(EntityHelper.getEntityUUID(animal));
        for (Size s: Size.values()) {
            if (relationship >= s.getRelationshipRequirement()) size = s;
        }
        
        return sizeable.getStackOfSize(HFAnimals.ANIMAL_PRODUCT, size, stats.getProductsPerDay());
    }
}
