package joshie.harvest.blocks;

import joshie.harvest.blocks.BlockWood.Woodware;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.base.BlockHFBaseEnumRotatableMeta;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static joshie.harvest.api.HFApi.SHIPPING;
import static joshie.harvest.blocks.BlockWood.Woodware.NEST;

public class BlockWood extends BlockHFBaseEnumRotatableMeta<Woodware> {
    public enum Woodware implements IStringSerializable {
        SHIPPING, NEST, TROUGH;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    public BlockWood() {
        super(Material.WOOD, Woodware.class);
        setHardness(1.5F);
        setSoundType(SoundType.WOOD);
    }

    @Override
    public String getToolType(Woodware wood) {
        return "axe";
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        Woodware wood = getEnumFromState(state);
        switch (wood) {
            default:
                return FULL_BLOCK_AABB;
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        Woodware wood = getEnumFromState(state);
        if (player.isSneaking()) return false;
        else if ((wood == Woodware.SHIPPING) && player.getActiveItemStack() != null) {
            ItemStack held = player.getActiveItemStack();
            long sell = SHIPPING.getSellValue(held);
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
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        Woodware wood = getEnumFromState(state);
        if (wood != NEST) {
            super.onBlockPlacedBy(world, pos, state, placer, stack);
        }
    }

    @Override
    public boolean isValidTab(CreativeTabs tab, Woodware wood) {
        return tab == HFTab.FARMING;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TROUGH;
    }
}