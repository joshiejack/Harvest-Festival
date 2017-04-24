package joshie.harvest.core.item;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.item.ItemBlockHF;
import joshie.harvest.core.block.BlockStorage;
import joshie.harvest.core.block.BlockStorage.Storage;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.tile.TileBasket;
import joshie.harvest.player.PlayerTrackerServer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

import static joshie.harvest.core.tile.TileBasket.BASKET_INVENTORY;

public class ItemBlockStorage extends ItemBlockHF<BlockStorage> {
    private TileBasket basket;

    public ItemBlockStorage(BlockStorage block) {
        super(block);
        setMaxStackSize(1);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (playerIn.isSneaking() && getBlock().getEnumFromStack(playerIn.getHeldItem(hand)) == Storage.BASKET) {
            playerIn.openGui(HarvestFestival.instance, GuiHandler.BASKET, worldIn, 0, 0, hand.ordinal());
            return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
        } else return super.onItemRightClick(worldIn, playerIn, hand);
    }

    @Override
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (getBlock().getEnumFromStack(stack) == Storage.BASKET) {
            if (player.isSneaking()) return EnumActionResult.PASS;
            else {
                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == getBlock() && getBlock().getEnumFromState(state) == Storage.SHIPPING) {
                    if (stack.hasTagCompound() && stack.getTagCompound().hasKey("inventory")) {
                        ItemStackHandler handler = new ItemStackHandler(BASKET_INVENTORY);
                        handler.deserializeNBT(stack.getTagCompound().getCompoundTag("inventory")); //Load from the nbt the inventory
                        stack.getTagCompound().removeTag("inventory");
                        stack.getTagCompound().removeTag("item");
                        if (!world.isRemote) {
                            for (int i = 0; i < handler.getSlots(); i++) {
                                if (handler.getStackInSlot(i) != null) {
                                    HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getTracking().addForShipping(handler.getStackInSlot(i));
                                }
                            }
                        }
                    }

                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    public TileEntity onBasketUsed(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        EnumActionResult result = super.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
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
