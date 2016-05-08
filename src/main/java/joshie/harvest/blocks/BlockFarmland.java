package joshie.harvest.blocks;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.blocks.BlockFarmland.Moisture;
import joshie.harvest.core.util.base.BlockHFBaseEnum;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
import java.util.WeakHashMap;

public class BlockFarmland extends BlockHFBaseEnum<Moisture> {
    private final WeakHashMap<WorldLocation, Long> timePlaced = new WeakHashMap<WorldLocation, Long>();
    private final AxisAlignedBB FARMLAND_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);
    private final IBlockState WET;

    public BlockFarmland() {
        super(Material.GROUND, Moisture.class, null);
        setTickRandomly(true);
        setLightOpacity(255);
        setSoundType(SoundType.GROUND);
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
        return FULL_BLOCK_AABB;
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
        if (!world.isRainingAt(pos.up())) {
            WorldLocation location = new WorldLocation(world.provider.getDimension(), pos);
            Long time = timePlaced.get(location);
            if (time == null) {
                timePlaced.put(location, System.currentTimeMillis());
            } else if (System.currentTimeMillis() - time >= 60000) {
                world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            }
        } else if (state != WET) {
            world.setBlockState(pos, WET, 2);
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);

        if (worldIn.getBlockState(pos.up()).getMaterial().isSolid()) {
            worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState());
        }
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (!worldIn.isRemote && worldIn.rand.nextFloat() < fallDistance - 0.5F && entityIn instanceof EntityLivingBase && (entityIn instanceof EntityPlayer || worldIn.getGameRules().getBoolean("mobGriefing")) && entityIn.width * entityIn.width * entityIn.height > 0.512F)
        {
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