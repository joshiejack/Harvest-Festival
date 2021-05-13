package uk.joshiejack.harvestcore.item;

import uk.joshiejack.harvestcore.block.BlockMailbox;
import uk.joshiejack.penguinlib.item.base.block.ItemBlockMulti;
import uk.joshiejack.penguinlib.block.interfaces.IPenguinBlock;
import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemBlockMailbox extends ItemBlockMulti<BlockMailbox.Material> {
    public ItemBlockMailbox(ResourceLocation registry, IPenguinBlock block) {
        super(registry, BlockMailbox.Material.class, block);
        setMaxStackSize(1);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.getBlockState(pos).getBlock() == getBlock()) return EnumActionResult.PASS;
        else return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean placeBlockAt(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, World world, @Nonnull BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull IBlockState newState) {
        IBlockState target = world.getBlockState(pos.offset(side.getOpposite()));
        return target.getBlock() instanceof BlockFence && side.getAxis() != EnumFacing.Axis.Y && super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
    }
}
