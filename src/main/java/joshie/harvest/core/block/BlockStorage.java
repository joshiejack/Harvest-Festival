package joshie.harvest.core.block;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.block.BlockHFEnumRotatableTile;
import joshie.harvest.core.base.item.ItemBlockHF;
import joshie.harvest.core.block.BlockStorage.Storage;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.StackHelper;
import joshie.harvest.core.item.ItemBlockStorage;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.tile.TileMailbox;
import joshie.harvest.core.tile.TileShipping;
import joshie.harvest.knowledge.letter.LetterHelper;
import joshie.harvest.player.PlayerTrackerServer;
import net.minecraft.block.BlockFence;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

import static joshie.harvest.api.HFApi.shipping;
import static joshie.harvest.core.block.BlockStorage.Storage.MAILBOX;
import static joshie.harvest.core.block.BlockStorage.Storage.SHIPPING;

public class BlockStorage extends BlockHFEnumRotatableTile<BlockStorage, Storage> {
    private static final AxisAlignedBB SHIPPING_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.6D, 1D);

    public enum Storage implements IStringSerializable {
        SHIPPING, MAILBOX;

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }

    public BlockStorage() {
        super(Material.WOOD, Storage.class);
        setHardness(1.5F);
        setSoundType(SoundType.WOOD);
    }

    @Override
    public ItemBlockHF getItemBlock() {
        return new ItemBlockStorage(this);
    }

    @Override
    public String getToolType(Storage storage) {
        return "axe";
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        Storage storage = getEnumFromState(state);
        switch (storage) {
            case SHIPPING:
                return SHIPPING_AABB;
            case MAILBOX:
                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof TileMailbox) {
                    TileMailbox mailbox = ((TileMailbox)tile);
                    EnumFacing facing = mailbox.getFacing();
                    switch (facing) {
                        case NORTH:
                            return new AxisAlignedBB(0.2D, 0.2D, 0.6D, 0.8D, 0.9D, 1.4D);
                        case EAST:
                            return new AxisAlignedBB(-0.4D, 0.2D, 0.2D, 0.4D, 0.9D, 0.8D);
                        case SOUTH:
                            return new AxisAlignedBB(0.2D, 0.2D, -0.4D, 0.8D, 0.9D, 0.4D);
                        case WEST:
                            return new AxisAlignedBB(0.6D, 0.2D, 0.2D, 1.4D, 0.9D, 0.8D);
                    }
                }
            default:
                return FULL_BLOCK_AABB;
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
        Storage storage = getEnumFromState(state);
        if (player.isSneaking()) return false;
        else if (storage == SHIPPING && held != null) {
            long sell = shipping.getSellValue(held);
            if (sell > 0) {
                if (!world.isRemote) {
                    HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getTracking().addForShipping(StackHelper.toStack(held, 1));
                }

                held.splitStack(1);
                return true;
            }
        } else if (storage == MAILBOX) {
            if (!world.isRemote && LetterHelper.hasUnreadLetters(player)) {
                player.openGui(HarvestFestival.instance, GuiHandler.MAILBOX, world, 0, 0, 0);
            }
        }

        return false;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        Storage storage = getEnumFromState(state);
        if (storage == SHIPPING && entity instanceof EntityItem) {
            EntityItem item = ((EntityItem)entity);
            if (item.getThrower() != null) {
                EntityPlayer player = world.getPlayerEntityByName(item.getThrower());
                ItemStack stack = item.getEntityItem();
                long sell = shipping.getSellValue(stack);
                if (sell > 0) {
                    if (!world.isRemote) {
                        HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getTracking().addForShipping(StackHelper.toStack(stack, 1));
                    }

                    stack.splitStack(1);
                    if (stack.stackSize <= 0) {
                        item.setDead();
                    }
                }
            }
        }
    }

    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack, EnumFacing facing) {
        TileEntity tile = world.getTileEntity(pos);
        if (entity instanceof EntityPlayer & tile instanceof TileShipping) {
            super.onBlockPlacedBy(world, pos, state, entity, stack);
            ((TileShipping) tile).setOwner(EntityHelper.getPlayerUUID((EntityPlayer) entity));
        } else if (tile instanceof TileMailbox) {
            ((TileMailbox)tile).setFacing(facing);
        }
    }

    @Override
    public boolean canReplace(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing side, @Nullable ItemStack stack) {
        Storage storage = getEnumFromStack(stack);
        if (storage == MAILBOX) {
            IBlockState state = worldIn.getBlockState(pos.offset(side.getOpposite()));
            return side.getAxis() != EnumFacing.Axis.Y && state.getBlock() instanceof BlockFence;
        } else return super.canReplace(worldIn, pos, side, stack);
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return getEnumFromState(state) == SHIPPING ? new TileShipping() : new TileMailbox();
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TROUGH;
    }
}