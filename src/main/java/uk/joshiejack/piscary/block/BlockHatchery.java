package uk.joshiejack.piscary.block;

import uk.joshiejack.penguinlib.block.base.BlockMultiTile;
import uk.joshiejack.piscary.Piscary;
import uk.joshiejack.piscary.item.block.ItemBlockHatchery;
import uk.joshiejack.piscary.tile.TileHatchery;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Random;

public class BlockHatchery extends BlockMultiTile<BlockHatchery.Hatchery> {
    private static final AxisAlignedBB HATCHERY_BOUNDING = new AxisAlignedBB(0.0D, -0.9D, 0.0D, 1.0D, 0.001D, 1.0D);
    private static final AxisAlignedBB HATCHERY_COLLISION = new AxisAlignedBB(0.0D, -0.9D, 0.0D, 1.0D, 0.001D, 1.0D);

    public BlockHatchery() {
        super(new ResourceLocation(Piscary.MODID, "hatchery"), Material.PISTON, Hatchery.class);
        setHardness(0.5F);
        setCreativeTab(Piscary.TAB);
        setTickRandomly(true);
    }

    @Override
    public ItemBlock createItemBlock() {
        return new ItemBlockHatchery(getRegistryName(), enumClass, this);
    }

    @Override
    @SuppressWarnings("deprecation, unchecked")
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return HATCHERY_BOUNDING;
    }

    @SuppressWarnings("deprecation, unchecked")
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return HATCHERY_COLLISION;
    }

    //Can we stay afloat
    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, blockIn, fromPos);
        checkAndDropBlock(world, pos, state);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        checkAndDropBlock(worldIn, pos, state);
        super.updateTick(worldIn, pos, state, rand);
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
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileHatchery && !world.isRemote) {
            ItemStack stack = (((TileHatchery) tile).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN).getStackInSlot(0));
            if (!stack.isEmpty()) {
                for (int i = 0; i < stack.getCount(); i++) {
                    ItemStack stack2 = stack.copy();
                    stack2.setCount(1);
                    stack2.setTagCompound(new NBTTagCompound());
                    stack2.getTagCompound().setLong("Rand", world.rand.nextLong());
                    EntityItem item = new EntityItem(world, pos.getX() + 0.5D, pos.getY() - 1.5D, pos.getZ() + 0.5D, stack2);
                    item.setPickupDelay(Integer.MAX_VALUE);
                    item.lifespan = 20;
                    item.addVelocity((-0.5D + world.rand.nextDouble()), 0D, (-0.5D + world.rand.nextDouble()));
                    world.spawnEntity(item);
                }
            }
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        switch (getEnumFromState(state)) {
            case FISH:         return new TileHatchery();
            default:           return null;
        }
    }

    public enum Hatchery implements IStringSerializable {
        FISH;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
