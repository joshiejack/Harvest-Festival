package joshie.harvest.core.item;

import joshie.harvest.core.base.item.ItemBlockHF;
import joshie.harvest.core.block.BlockStorage;
import joshie.harvest.core.block.BlockStorage.Storage;
import joshie.harvest.core.tile.TileBasket;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemBlockStorage extends ItemBlockHF<BlockStorage> {
    private TileBasket basket;

    public ItemBlockStorage(BlockStorage block) {
        super(block);
    }

    public TileBasket onBasketUsed(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        EnumActionResult result = super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        return result == EnumActionResult.SUCCESS ? basket : null;
    }

    @Override
    public boolean placeBlockAt(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull IBlockState newState)  {
        Storage storage = getBlock().getEnumFromStack(stack);
        if (storage == Storage.MAILBOX && !player.isSneaking()) return false;
        if (!world.setBlockState(pos, newState, 3)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == getBlock()) {
            setTileEntityNBT(world, player, pos, stack);
            getBlock().onBlockPlacedBy(world, pos, state, player, stack, side);
            TileEntity tile = world.getTileEntity(pos);
             if (tile instanceof TileBasket) {
                 basket  = ((TileBasket)tile);
             }
        }

        return true;
    }
}
