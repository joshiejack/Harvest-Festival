package joshie.harvest.core.block;

import joshie.harvest.core.block.BlockStorage.Storage;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.base.block.BlockHFEnumRotatableTile;
import joshie.harvest.core.tile.TileShipping;
import joshie.harvest.player.PlayerTrackerServer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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

import static joshie.harvest.api.HFApi.shipping;
import static joshie.harvest.core.block.BlockStorage.Storage.SHIPPING;

public class BlockStorage extends BlockHFEnumRotatableTile<BlockStorage, Storage> {
    private static final AxisAlignedBB SHIPPING_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.6D, 1D);

    public enum Storage implements IStringSerializable {
        SHIPPING;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    public BlockStorage() {
        super(Material.WOOD, Storage.class);
        setHardness(1.5F);
        setSoundType(SoundType.WOOD);
    }

    @Override
    public String getToolType(Storage storage) {
        return "axe";
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        Storage storage = getEnumFromState(state);
        switch (storage) {
            case SHIPPING:
                return SHIPPING_AABB;
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
                    HFTrackers.<PlayerTrackerServer>getPlayerTracker(player).getTracking().addForShipping(held.copy());
                }

                held.splitStack(1);
                return true;
            }
        }

        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, entity, stack);

        if (entity instanceof  EntityPlayer && getEnumFromState(state) == SHIPPING) {
            ((TileShipping)world.getTileEntity(pos)).setOwner(UUIDHelper.getPlayerUUID((EntityPlayer) entity));
        }
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileShipping();
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TROUGH;
    }
}