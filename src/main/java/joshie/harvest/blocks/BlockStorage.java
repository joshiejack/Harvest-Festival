package joshie.harvest.blocks;

import joshie.harvest.blocks.BlockStorage.Storage;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.base.BlockHFEnumRotatableMeta;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static joshie.harvest.api.HFApi.shipping;
import static joshie.harvest.blocks.BlockStorage.Storage.SHIPPING;

public class BlockStorage extends BlockHFEnumRotatableMeta<BlockStorage, Storage> {
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
                    HFTrackers.getServerPlayerTracker(player).getTracking().addForShipping(held.copy());
                }

                held.splitStack(1);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isValidTab(CreativeTabs tab, Storage wood) {
        return tab == HFTab.FARMING;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TROUGH;
    }
}