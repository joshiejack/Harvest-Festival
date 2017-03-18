package joshie.harvest.mining.block;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFEnumRotatableMeta;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.mining.block.BlockLadder.Ladder;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;

import static joshie.harvest.mining.block.BlockLadder.Ladder.DECORATIVE;
import static joshie.harvest.mining.block.BlockLadder.Ladder.WOOD;

public class BlockLadder extends BlockHFEnumRotatableMeta<BlockLadder, Ladder> {
    private static final AxisAlignedBB LADDER_EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
    private static final AxisAlignedBB LADDER_WEST_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    private static final AxisAlignedBB LADDER_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    private static final AxisAlignedBB LADDER_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);

    public enum Ladder implements IStringSerializable {
        WOOD, DECORATIVE;

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }

    public BlockLadder() {
        super(Material.WOOD, Ladder.class, HFTab.MINING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return getEnumFromState(state) == DECORATIVE ? 1F: -1F;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return LADDER_NORTH_AABB;
            case SOUTH:
                return LADDER_SOUTH_AABB;
            case WEST:
                return LADDER_WEST_AABB;
            case EAST:
            default:
                return LADDER_EAST_AABB;
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String unlocalized = getUnlocalizedName();
        return TextHelper.localizeFully(unlocalized + ".wood");
    }

    @Override
    protected boolean shouldDisplayInCreative(Ladder ladder) {
        return ladder != WOOD;
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        //TODO: Readd in 1.0 as well as adding elevators to the mine on every 5th floor
        /*if (!entity.worldObj.isRemote && entity.worldObj.provider.getDimension() == HFMining.MINING_ID) {
            if (entity instanceof EntityPlayer) {
                HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(((EntityPlayer)entity)).getTracking().setMineFloorReached(MiningHelper.getFloor(pos));
            }
        }*/

        return true;
    }
}
