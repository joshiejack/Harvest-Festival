package joshie.harvestmoon.api.crops;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/** Items that implement this interface will be able to break the crops*/
public interface IBreakCrops {
    /** Returns the relative strength versus crops
     *  @return     the relative strength */
    float getStrengthVSCrops(EntityPlayer player, World world, int x, int y, int z, ItemStack stack);
}
