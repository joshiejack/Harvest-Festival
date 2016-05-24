package joshie.harvest.crops.blocks;

import joshie.harvest.api.core.IDailyTickableBlock;
import joshie.harvest.crops.blocks.BlockHFFarmland.Moisture;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.core.util.base.BlockHFBaseEnum;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockHFFarmland extends BlockHFBaseEnum<Moisture> implements IDailyTickableBlock {
    private final AxisAlignedBB FARMLAND_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);
    private final IBlockState DRY;
    private final IBlockState WET;

    public BlockHFFarmland() {
        super(Material.GROUND, Moisture.class, null);
        setTickRandomly(false);
        setLightOpacity(255);
        setSoundType(SoundType.GROUND);
        DRY = getStateFromEnum(Moisture.DRY);
        WET = getStateFromEnum(Moisture.WET);
        setBlockUnbreakable();
    }

    public enum Moisture implements IStringSerializable {
        DRY, WET;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FARMLAND_AABB;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return FARMLAND_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (state != WET && world.isRainingAt(pos.up())) {
            world.setBlockState(pos, WET, 2);
        } else if (!hasCrops(world, pos)) {
            if (state == WET) world.setBlockState(pos, DRY, 2);
            else if (state == DRY) world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
        }
    }

    @Override
    public boolean newDay(World world, BlockPos pos, IBlockState state) {
        if (state != WET && world.isRainingAt(pos.up())) {
            world.setBlockState(pos, WET, 2);
        } else {
            if (state == WET) world.setBlockState(pos, DRY, 2);
            else if (state == DRY && (!hasCrops(world, pos))) world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
        }

        return world.getBlockState(pos).getBlock() == this;
    }

    private boolean hasCrops(World worldIn, BlockPos pos)  {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        return block instanceof net.minecraftforge.common.IPlantable;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock) {
        super.neighborChanged(state, worldIn, pos, neighborBlock);

        if (worldIn.getBlockState(pos.up()).getMaterial().isSolid()) {
            worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState());
        }
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (!worldIn.isRemote && worldIn.rand.nextFloat() < fallDistance - 0.5F && entityIn instanceof EntityPlayer && entityIn.width * entityIn.width * entityIn.height > 0.512F) {
            worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState());
        }

        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        switch (side) {
            case UP:
                return true;
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST:
                IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
                Block block = iblockstate.getBlock();
                return !iblockstate.isOpaqueCube() && block != HFBlocks.FARMLAND && block != Blocks.GRASS_PATH;
            default:
                return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Blocks.DIRT.getItemDropped(Blocks.DIRT.getDefaultState().withProperty(net.minecraft.block.BlockDirt.VARIANT, net.minecraft.block.BlockDirt.DirtType.DIRT), rand, fortune);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)  {
        return new ItemStack(Blocks.DIRT);
    }
}