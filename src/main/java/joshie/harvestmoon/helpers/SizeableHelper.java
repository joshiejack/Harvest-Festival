package joshie.harvestmoon.helpers;

import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.lib.SizeableMeta;
import joshie.harvestmoon.lib.SizeableMeta.Size;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SizeableHelper {    
    public static ItemStack getSizeable(EntityPlayer player, EntityAnimal animal, SizeableMeta meta, Size size) {
        int relationship = RelationsHelper.getRelationshipValue(animal, player);
        return getSizeable(relationship, meta, size);
    }
    
    public static ItemStack getSizeable(int relationship, SizeableMeta meta, Size size) {
        double quality = 1D + (relationship / RelationsHelper.ADJUSTED_MAX) * 99;
        return new ItemStack(HMItems.sized, 1, getDamage(meta, (int) quality, size));
    }
    
    /** Converts the meta, quality and size of a product to the proper meta data **/
    public static int getDamage(SizeableMeta meta, int quality, Size size) {
        int sizeableSize = getSize(size);        
        int sizeableMeta = meta.ordinal();
        int sizeableQuality = ((quality - 1) * 100);   
        
        return sizeableSize + sizeableMeta + sizeableQuality;
    }

    /** @return returns the Quality of this sizeable, Returns a vlue between 0-109 **/
    public static int getQuality(int meta) {
        return (int) Math.ceil(getInternalMeta(meta) / 100);
    }

    /** @return Returns the SizeableMeta for this sizeable, returns a value between 0-90 **/
    public static int getType(int meta) {
        return getInternalMeta(meta) % 100;
    }
    
    public static SizeableMeta getSizeableFromStack(ItemStack stack) {
        return SizeableMeta.values()[getType(stack.getItemDamage())];
    }
    
    public static Size getSize(int meta) {
        int size = Math.max(0, meta / 10000);
        return Size.values()[Math.min(2, size)];
    }
    
    //Internal Meta value
    private static int getInternalMeta(int meta) {
        return meta % 10000;
    }

    /** Returns the stack that this size should return **/
    private static int getSize(Size size) {
        return size.ordinal() * 10000;
    }
}
