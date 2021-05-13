package uk.joshiejack.piscary.item.block;

import uk.joshiejack.penguinlib.item.base.block.ItemBlockMulti;
import uk.joshiejack.penguinlib.block.interfaces.IPenguinBlock;
import uk.joshiejack.piscary.block.BlockTrap;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemBlockTrap extends ItemBlockMulti<BlockTrap.Trap> {
    public ItemBlockTrap(ResourceLocation registry, Class<BlockTrap.Trap> clazz, IPenguinBlock block) {
        super(registry, clazz, block);
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(@Nonnull EntityPlayer playerIn, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.getBlockState(pos.up(2)).getMaterial() == Material.WATER) return super.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        else return EnumActionResult.PASS;
    }
}
