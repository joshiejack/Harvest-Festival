package joshie.harvest.mining.block;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFEnumRotatableTile;
import joshie.harvest.core.base.item.ItemBlockHF;
import joshie.harvest.core.helpers.ChatHelper;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.MiningHelper;
import joshie.harvest.mining.TeleportPlayer;
import joshie.harvest.mining.block.BlockElevator.Elevator;
import joshie.harvest.mining.item.ItemBlockElevator;
import joshie.harvest.mining.tile.TileElevator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;

import static joshie.harvest.mining.TeleportPlayer.isTeleporting;

public class BlockElevator extends BlockHFEnumRotatableTile<BlockElevator, Elevator> {
    public enum Elevator implements IStringSerializable {
        EMPTY, JUNK;

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }

    public BlockElevator() {
        super(Material.WOOD, Elevator.class, HFTab.MINING);
    }

    @Override
    public ItemBlockHF getItemBlock() {
        return new ItemBlockElevator(this);
    }

    private TileElevator getElevator(World world, BlockPos pos, IBlockState state) {
        Elevator elevator = getEnumFromState(state);
        if (elevator == Elevator.EMPTY) {
            TileEntity tile = world.getTileEntity(pos.down());
            if (tile instanceof TileElevator) {
                return ((TileElevator)tile);
            }
        } else {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileElevator) {
                return ((TileElevator)tile);
            }
        }

        return null;
    }

    @Override
    @SuppressWarnings("deprecation, unchecked")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (entity instanceof EntityPlayer && getEnumFromState(state) == Elevator.JUNK) {
            TileElevator elevator = getElevator(world, pos, state);
            if (elevator != null) {
                BlockPos twin = elevator.getTwin();
                if (twin == null) {
                    ChatHelper.displayChat("This elevator is not currently linked to another one");
                } else {
                    EntityPlayer player = (EntityPlayer) entity;
                    TileEntity target = world.getTileEntity(twin);
                    if (!world.isRemote) {
                        if (target instanceof TileElevator) {
                            EnumFacing facing = ((TileElevator) target).getFacing();
                            BlockPos location = twin.offset(facing);
                            if (!isTeleporting(player, location)) {
                                TeleportPlayer.initiate(player, pos, facing, location);
                            } else TeleportPlayer.onCollide(player);
                        }
                    } else {
                        if (!isTeleporting(player, twin)) {
                            TeleportPlayer.initiate(player, pos, EnumFacing.NORTH, twin);
                        } else TeleportPlayer.onCollide(player);
                    }
                }
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileElevator elevator = getElevator(world, pos, state);
        if (elevator != null) {
            if (heldItem != null && heldItem.getItem() == HFMining.MINING_TOOL) return false;
            BlockPos twin = elevator.getTwin();
            if (twin == null) {
                ChatHelper.displayChat("This elevator is not currently linked to another one");
            } else {
                ChatHelper.displayChat("This elevator will take you to floor " + MiningHelper.getFloor(twin));
            }

            return true;
        }

        return false;
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileElevator elevator = getElevator(world, pos, state);
        if (elevator != null) {
            elevator.onBreakBlock();
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    protected boolean shouldDisplayInCreative(Elevator elevator) {
        return elevator == Elevator.JUNK;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileElevator();
    }
}
