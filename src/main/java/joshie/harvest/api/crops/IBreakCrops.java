package joshie.harvest.api.crops;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Items that implement this interface will be able to break the crops*/
public interface IBreakCrops {
    /** Returns the relative strength versus crops
     *  @return     the relative strength */
    float getStrengthVSCrops(EntityPlayer player, World world, BlockPos pos, IBlockState state, ItemStack stack);
}