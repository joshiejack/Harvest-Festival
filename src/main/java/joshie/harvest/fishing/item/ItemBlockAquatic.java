package joshie.harvest.fishing.item;

import joshie.harvest.core.base.item.ItemBlockHF;
import joshie.harvest.fishing.block.BlockAquatic;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemBlockAquatic extends ItemBlockHF<BlockAquatic> {
    public ItemBlockAquatic(BlockAquatic block) {
        super(block);
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(@Nonnull ItemStack stack, @Nonnull EntityPlayer playerIn, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.getBlockState(pos.up(2)).getMaterial() == Material.WATER) return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        else return EnumActionResult.PASS;
    }
}
