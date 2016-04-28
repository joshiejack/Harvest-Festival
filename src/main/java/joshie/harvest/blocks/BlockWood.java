package joshie.harvest.blocks;

import joshie.harvest.api.core.IShippable;
import joshie.harvest.blocks.BlockWood.Woodware;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.DirectionHelper;
import joshie.harvest.core.util.base.BlockHFBaseMeta;
import joshie.harvest.core.util.generic.IFaceable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
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

import static joshie.harvest.blocks.BlockWood.Woodware.*;

public class BlockWood extends BlockHFBaseMeta<Woodware> {
    public static enum Woodware implements IStringSerializable {
        SHIPPING, SHIPPING_2, NEST, TROUGH, TROUGH_2;

        @Override
        public String getName() {
            return toString();
        }
    }

    public BlockWood() {
        super(Material.WOOD, Woodware.class);
        setHardness(1.5F);
    }

    @Override
    public String getToolType(Woodware wood) {
        return "axe";
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        Woodware wood = getEnumFromBlockPos(world, pos);
        switch (wood) {
            default:
                return FULL_BLOCK_AABB;
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        Woodware wood = getEnumFromState(state);
        if (player.isSneaking()) return false;
        else if ((wood == SHIPPING || wood == SHIPPING_2) && player.getActiveItemStack() != null) {
            ItemStack held = player.getActiveItemStack();
            if (held.getItem() instanceof IShippable) {
                long sell = ((IShippable) held.getItem()).getSellValue(held);
                if (sell > 0) {
                    if (!player.capabilities.isCreativeMode) {
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    }

                    if (!world.isRemote) {
                        HFTrackers.getServerPlayerTracker(player).getTracking().addForShipping(held);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing facing = DirectionHelper.getFacingFromEntity(placer);
        Woodware wood = getEnumFromState(state);
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IFaceable) {
            ((IFaceable) tile).setFacing(facing);
        }

        if (wood == SHIPPING || wood == SHIPPING_2) {
            if (facing == EnumFacing.WEST || facing == EnumFacing.EAST) {
                world.setBlockMetadataWithNotify(pos, SHIPPING, 2);
            } else if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
                world.setBlockMetadataWithNotify(pos, SHIPPING_2, 2);
            }
        } else if (wood == TROUGH || wood == TROUGH_2) {
            if (facing == EnumFacing.WEST || facing == EnumFacing.EAST) {
                world.setBlockMetadataWithNotify(pos, TROUGH_2, 2);
            } else if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
                world.setBlockMetadataWithNotify(pos, TROUGH, 2);
            }
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        Woodware wood = getEnumFromState(state);
        if (wood == SHIPPING_2) return SHIPPING;
        else if (wood == TROUGH_2) return TROUGH;
        else return super.damageDropped(state);
    }

    @Override
    public boolean isValidTab(CreativeTabs tab, E e) {
        return tab == HFTab.FARMING;
    }

    @Override
    public boolean isActive(int meta) {
        return meta != TROUGH_2 && meta != SHIPPING_2;
    }

    @Override
    public int getMetaCount() {
        return 5;
    }
}