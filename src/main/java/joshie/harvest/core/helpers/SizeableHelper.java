package joshie.harvest.core.helpers;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.lib.SizeableMeta;
import joshie.harvest.items.HFItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SizeableHelper {
    public static ItemStack getSizeable(SizeableMeta meta, int amount, Size size) {
        return new ItemStack(HFItems.sized.get(meta), amount, size.ordinal());
    }

    public static Size getSize(int meta) {
        return Size.values()[Math.min(2, meta)];
    }
    
    public static ItemStack getEgg(EntityPlayer player, IAnimalTracked tracked) {
        return SizeableHelper.getSizeable(player, tracked, SizeableMeta.EGG);
    }
    
    public static ItemStack getMilk(EntityPlayer player, IAnimalTracked tracked) {
        return SizeableHelper.getSizeable(player, tracked, SizeableMeta.MILK);
    }

    public static ItemStack getSizeable(EntityPlayer player, IAnimalTracked tracked, SizeableMeta milk) {
        Size size = null;
        int relationship = HFApi.RELATIONS.getAdjustedRelationshipValue(player, tracked);
        for (Size s: Size.values()) {
            if (relationship >= s.getRelationshipRequirement()) size = s;
        }
        
        return SizeableHelper.getSizeable(SizeableMeta.MILK, tracked.getData().getProductsPerDay() , size);
    }
}
