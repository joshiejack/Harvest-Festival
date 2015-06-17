package joshie.harvest.blocks;

import joshie.harvest.api.core.IShippable;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.DirectionHelper;
import joshie.harvest.core.lib.RenderIds;
import joshie.harvest.core.util.base.BlockHFBaseMeta;
import joshie.harvest.core.util.generic.IFaceable;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockWood extends BlockHFBaseMeta {
    public static final int SHIPPING = 0;
    public static final int SHIPPING_2 = 1;
    public static final int NEST = 2;
    public static final int TROUGH = 3;
    public static final int TROUGH_2 = 4;

    public BlockWood() {
        super(Material.wood);
        setHardness(1.5F);
    }

    @Override
    public String getToolType(int meta) {
        return "axe";
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return RenderIds.ALL;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess block, int x, int y, int z) {
        int meta = block.getBlockMetadata(x, y, z);
        switch (meta) {
            default:
                setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
                break;
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x + minX, y + minY, z + minZ, x + maxX, y + maxY, z + maxZ);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        int meta = world.getBlockMetadata(x, y, z);
        if (player.isSneaking()) return false;
        else if ((meta == SHIPPING || meta == SHIPPING_2) && player.getCurrentEquippedItem() != null) {
            ItemStack held = player.getCurrentEquippedItem();
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        ForgeDirection dir = DirectionHelper.getFacingFromEntity(entity);
        int meta = stack.getItemDamage();
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IFaceable) {
            ((IFaceable) tile).setFacing(dir);
        }

        if (meta == SHIPPING || meta == SHIPPING_2) {
            if (dir == ForgeDirection.WEST || dir == ForgeDirection.EAST) {
                world.setBlockMetadataWithNotify(x, y, z, SHIPPING, 2);
            } else if (dir == ForgeDirection.NORTH || dir == ForgeDirection.SOUTH) {
                world.setBlockMetadataWithNotify(x, y, z, SHIPPING_2, 2);
            }
        } else if (meta == TROUGH || meta == TROUGH_2) {
            if (dir == ForgeDirection.WEST || dir == ForgeDirection.EAST) {
                world.setBlockMetadataWithNotify(x, y, z, TROUGH_2, 2);
            } else if (dir == ForgeDirection.NORTH || dir == ForgeDirection.SOUTH) {
                world.setBlockMetadataWithNotify(x, y, z, TROUGH, 2);
            }
        }
    }

    @Override
    public int damageDropped(int meta) {
        if (meta == SHIPPING_2) return SHIPPING;
        else if (meta == TROUGH_2) return TROUGH;
        else return super.damageDropped(meta);
    }

    @Override
    public boolean isValidTab(CreativeTabs tab, int meta) {
        return tab == HFTab.tabFarming;
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
