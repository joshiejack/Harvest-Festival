package joshie.harvest.core.helpers;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.core.ISizeable.Size;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SizeableHelper {
    public static ItemStack getSizeable(Item item, int amount, Size size) {
        return new ItemStack(item, amount, size.ordinal());
    }
    
    public static ItemStack getEgg(EntityPlayer player, IAnimalTracked tracked) {
        return SizeableHelper.getSizeable(player, tracked, HFAnimals.EGG);
    }
    
    public static ItemStack getMilk(EntityPlayer player, IAnimalTracked tracked) {
        return SizeableHelper.getSizeable(player, tracked, HFAnimals.MILK);
    }

    public static ItemStack getSizeable(EntityPlayer player, IAnimalTracked tracked, Item item) {
        Size size = null;
        int relationship = HFApi.relations.getAdjustedRelationshipValue(player, tracked);
        for (Size s: Size.values()) {
            if (relationship >= s.getRelationshipRequirement()) size = s;
        }
        
        return SizeableHelper.getSizeable(item, tracked.getData().getProductsPerDay() , size);
    }
}
