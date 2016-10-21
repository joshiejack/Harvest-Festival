package joshie.harvest.core.helpers;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.util.Sizeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SizeableHelper {
    public static ItemStack getMilk(EntityPlayer player, IAnimalTracked tracked) {
        return SizeableHelper.getSizeable(player, tracked, HFAnimals.MILK);
    }

    public static ItemStack getWool(EntityPlayer player, IAnimalTracked tracked) {
        return SizeableHelper.getSizeable(player, tracked, HFAnimals.WOOL);
    }

    public static ItemStack getSizeable(EntityPlayer player, IAnimalTracked tracked, Sizeable sizeable) {
        Size size = null;
        int relationship = HFApi.player.getRelationsForPlayer(player).getRelationship(tracked.getUUID());
        for (Size s: Size.values()) {
            if (relationship >= s.getRelationshipRequirement()) size = s;
        }
        
        return sizeable.getStackOfSize(size, tracked.getData().getProductsPerDay());
    }
}
