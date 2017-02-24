package joshie.harvest.fishing.block;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFEnum;
import joshie.harvest.core.base.item.ItemBlockHF;
import joshie.harvest.core.base.tile.TileSingleStack;
import joshie.harvest.fishing.block.BlockFloating.Floating;
import joshie.harvest.fishing.item.ItemBlockFishing;
import joshie.harvest.fishing.tile.TileHatchery;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import java.util.Locale;
import java.util.Random;

public class BlockFloating extends BlockHFEnum<BlockFloating, Floating> {
    public BlockFloating() {
        super(Material.PISTON, Floating.class, HFTab.FISHING);
        setTickRandomly(true);
        setHardness(0.5F);
    }

    @Override
    public ItemBlockHF getItemBlock() {
        return new ItemBlockFishing(this);
    }

    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof TileSingleStack && ((TileSingleStack) tile).onRightClicked(player, held);
    }

    @Override
    @SuppressWarnings("deprecation, unchecked")
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return new AxisAlignedBB(0.0D, -0.9D, 0.0D, 1.0D, 0.001D, 1.0D);
    }

    @Override
    @SuppressWarnings("deprecation, unchecked")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos) {
        return new AxisAlignedBB(0.0D, -0.9D, 0.0D, 1.0D, 0.001D, 1.0D);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        super.neighborChanged(state, worldIn, pos, blockIn);
        checkAndDropBlock(worldIn, pos, state);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        checkAndDropBlock(worldIn, pos, state);
    }

    private void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!canBlockStay(worldIn, pos)) {
            dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    private boolean canBlockStay(World worldIn, BlockPos pos) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            IBlockState iblockstate = worldIn.getBlockState(pos.down());
            Material material = iblockstate.getMaterial();
            return material == Material.WATER && iblockstate.getValue(BlockLiquid.LEVEL) == 0;
        } else return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        switch (getEnumFromState(state)) {
            case HATCHERY:      return new TileHatchery();
            default:            return null;
        }
    }

    public enum Floating implements IStringSerializable {
        HATCHERY;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
