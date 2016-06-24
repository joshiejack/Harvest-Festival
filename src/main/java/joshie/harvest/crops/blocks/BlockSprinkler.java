package joshie.harvest.crops.blocks;

import joshie.harvest.core.util.base.BlockHFEnum;
import joshie.harvest.crops.blocks.BlockSprinkler.Sprinkler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;

public class BlockSprinkler extends BlockHFEnum<BlockSprinkler, Sprinkler> {
    private static final AxisAlignedBB WOOD_AABB = new AxisAlignedBB(0.2D, 0D, 0.2D, 0.8D, 0.7D, 0.8D);

    public BlockSprinkler() {
        super(Material.WOOD, Sprinkler.class);
        setSoundType(SoundType.GROUND);
    }

    public enum Sprinkler implements IStringSerializable {
        WOOD;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return WOOD_AABB;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isFullCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (heldItem != null) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileSprinkler) {
                TileSprinkler sprinkler = ((TileSprinkler)tile);
                ItemStack result = FluidUtil.tryEmptyContainer(heldItem, sprinkler.getTank(), 1000, player, true);
                if (result != null) {
                    if (result.stackSize > 0) {
                        player.setHeldItem(hand, result);
                    }

                    if (!worldIn.isRemote) {
                        sprinkler.hydrateSoil();
                    }

                    sprinkler.saveAndRefresh();

                    return true;
                }

                return false;
            }
        }

        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileSprinkler();
    }
}