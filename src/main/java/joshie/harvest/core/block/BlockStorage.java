package joshie.harvest.core.block;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.block.BlockHFEnumRotatableTile;
import joshie.harvest.core.base.item.ItemBlockHF;
import joshie.harvest.core.block.BlockStorage.Storage;
import joshie.harvest.core.entity.EntityBasket;
import joshie.harvest.core.handlers.BasketHandler;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.StackHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.item.ItemBlockStorage;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.tile.TileBasket;
import joshie.harvest.core.tile.TileMailbox;
import joshie.harvest.core.tile.TileShipping;
import joshie.harvest.core.util.interfaces.IFaceable;
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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static joshie.harvest.api.HFApi.shipping;
import static joshie.harvest.core.block.BlockStorage.Storage.*;

public class BlockStorage extends BlockHFEnumRotatableTile<BlockStorage, Storage> {
    private static final AxisAlignedBB SHIPPING_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.6D, 1D);
    private static final AxisAlignedBB MAILBOX_NORTH_AABB = new AxisAlignedBB(0.2D, 0.2D, 0.6D, 0.8D, 0.9D, 1.4D);
    private static final AxisAlignedBB MAILBOX_SOUTH_AABB = new AxisAlignedBB(0.2D, 0.2D, -0.4D, 0.8D, 0.9D, 0.4D);
    private static final AxisAlignedBB MAILBOX_EAST_AABB = new AxisAlignedBB(-0.4D, 0.2D, 0.2D, 0.4D, 0.9D, 0.8D);
    private static final AxisAlignedBB MAILBOX_WEST_AABB = new AxisAlignedBB(0.6D, 0.2D, 0.2D, 1.4D, 0.9D, 0.8D);
    private static final AxisAlignedBB BASKET_AABB = new AxisAlignedBB(0.2F, 0.0F, 0.2F, 0.8F, 0.5F, 0.8F);

    public enum Storage implements IStringSerializable {
        SHIPPING, MAILBOX, BASKET;

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
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        switch (getEnumFromState(state)) {
            case BASKET:    return 0.5F;
            default:        return 1.5F;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        switch (getEnumFromState(state)) {
            case SHIPPING:
                return SHIPPING_AABB;
            case MAILBOX:
                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof TileMailbox) {
                    TileMailbox mailbox = ((TileMailbox)tile);
                    EnumFacing facing = mailbox.getFacing();
                    switch (facing) {
                        case NORTH:
                            return MAILBOX_NORTH_AABB;
                        case EAST:
                            return MAILBOX_EAST_AABB;
                        case SOUTH:
                            return MAILBOX_SOUTH_AABB;
                        case WEST:
                            return MAILBOX_WEST_AABB;
                    }
                }
            case BASKET:
                return BASKET_AABB;
            default:
                return FULL_BLOCK_AABB;
        }
    }

    private static boolean hasShippedItem(World world, EntityPlayer player, @Nonnull ItemStack stack) {
        long sell = shipping.getSellValue(stack);
        if (sell > 0) {
            if (!world.isRemote) {
                HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getTracking().addForShipping(StackHelper.toStack(stack, 1));
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        Storage storage = getEnumFromState(state);
        ItemStack held = player.getHeldItem(hand);
        if (player.isSneaking()) return false;
        else if (storage == SHIPPING) {
            if (held.isEmpty()) {
                EntityBasket basket = BasketHandler.getWearingBasket(player);
                if (basket != null) {
                    boolean shipped = false;
                    for (int i = 0; i < basket.handler.getSlots(); i++) {
                        if (!basket.handler.getStackInSlot(i).isEmpty()) {
                            if (!world.isRemote) {
                                HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getTracking().addForShipping(basket.handler.getStackInSlot(i));
                            }

                            shipped = true;
                            basket.handler.setStackInSlot(i, ItemStack.EMPTY); //Remove the items from the inventory
                        }
                    }

                    return shipped;
                }
            } else if (hasShippedItem(world, player, held)) {
                held.splitStack(1);
                return true;
            }

            return true;
        } else if (storage == MAILBOX) {
            if (!world.isRemote && LetterHelper.hasUnreadLetters(player)) {
                player.openGui(HarvestFestival.instance, GuiHandler.MAILBOX, world, 0, 0, 0);
            }

            return true;
        } else if (storage == BASKET) {
            if (!player.isBeingRidden()) {
                if (!world.isRemote) {
                    EntityBasket basket = new EntityBasket(world);
                    basket.setPositionAndUpdate(player.posX, player.posY + 1.5D, player.posZ);
                    basket.setEntityInvulnerable(true);
                    basket.startRiding(player, true);
                    TileEntity tile = world.getTileEntity(pos);
                    if (tile instanceof TileBasket) {
                        basket.setAppearanceAndContents(((TileBasket)tile).getStack(), ((TileBasket)tile).handler);
                    }

                    world.spawnEntity(basket);
                    ((EntityPlayerMP)player).connection.sendPacket(new SPacketSetPassengers(player));
                    world.setBlockToAir(pos); //Remove the basket
                }

                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    private UUID getPlayer(EntityItem item , World world, BlockPos pos) {
        if (item.getThrower() != null) {
            EntityPlayer player = world.getPlayerEntityByName(item.getThrower());
            if (player != null) return EntityHelper.getPlayerUUID(player);
        }

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileShipping) {
            return ((TileShipping)tile).getOwner();
        }

        return null;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        Storage storage = getEnumFromState(state);
        if (storage == SHIPPING && entity instanceof EntityItem && !world.isRemote) {
            EntityItem item = ((EntityItem)entity);
            UUID uuid = getPlayer(item, world, pos);
            if (uuid != null) {
                ItemStack stack = item.getItem();
                long sell = shipping.getSellValue(stack);
                if (sell > 0) {
                    HFTrackers.<PlayerTrackerServer>getPlayerTracker(world, uuid).getTracking().addForShipping(StackHelper.toStack(stack, 1));
                    stack.shrink(1);
                    if (stack.getCount() <= 0) {
                        item.setDead();
                    }
                }
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, @Nonnull ItemStack stack, EnumFacing facing) {
        TileEntity tile = world.getTileEntity(pos);
        if (entity instanceof EntityPlayer & tile instanceof TileShipping) {
            super.onBlockPlacedBy(world, pos, state, entity, stack);
            ((TileShipping) tile).setOwner(EntityHelper.getPlayerUUID((EntityPlayer) entity));
        } else if (tile instanceof TileBasket)  {
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("inventory")) {
                ((TileBasket)tile).setAppearanceAndContents(
                        new ItemStack(stack.getTagCompound().getCompoundTag("item")),
                        stack.getTagCompound().getCompoundTag("inventory"));
            }
        } else if (tile instanceof IFaceable) {
            ((IFaceable)tile).setFacing(facing);
        }
    }

    @Override
    public boolean canPlaceBlockOnSide(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
        Storage storage = getEnumFromState(world.getBlockState(pos));
        if (storage == MAILBOX) {
            IBlockState state = world.getBlockState(pos.offset(side.getOpposite()));
            return side.getAxis() != EnumFacing.Axis.Y && state.getBlock() instanceof BlockFence;
        } else return super.canPlaceBlockOnSide(world, pos, side);
    }

    @Nonnull
    @Override
    @SuppressWarnings("ConstantConditions")
    public NonNullList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
        if (getEnumFromState(state) == BASKET) {
            TileEntity tile = world.getTileEntity(pos);
            ItemStack stack = getStackFromEnum(BASKET);
            if (tile instanceof TileBasket) {
                TileBasket basket = ((TileBasket) tile);
                NBTTagCompound tag = new NBTTagCompound();
                tag.setTag("inventory", basket.handler.serializeNBT());
                if (basket.getStack() != null) {
                    tag.setTag("item", basket.getStack().serializeNBT());
                }

                stack.setTagCompound(tag);
            }

            return NonNullList.withSize(1, stack);
        }

        return (NonNullList<ItemStack>) super.getDrops(world, pos, state, fortune);
    }

    @Override
    public boolean removedByPlayer(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {
        if (getEnumFromState(state) == BASKET) {
            if (willHarvest) {
                harvestBlock(world, player, pos, state, world.getTileEntity(pos), player.getHeldItemMainhand());
            }

            world.setBlockToAir(pos);
            return false;
        } else return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        switch(getEnumFromState(state)) {
            case SHIPPING:
                return new TileShipping();
            case MAILBOX:
                return new TileMailbox();
            default:
                return new TileBasket();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        if (getEnumFromMeta(stack.getItemDamage()) == Storage.MAILBOX) {
            list.add(TextHelper.translate("tooltip.mailbox"));
        }
    }

    @Override
    public int getSortValue(@Nonnull ItemStack stack) {
        return CreativeSort.TROUGH;
    }
}