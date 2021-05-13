package uk.joshiejack.harvestcore.block;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.item.ItemBlockMailbox;
import uk.joshiejack.harvestcore.tile.TileMailbox;
import uk.joshiejack.penguinlib.block.base.BlockMultiTileRotatable;
import uk.joshiejack.penguinlib.tile.TileRotatable;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BlockMailbox extends BlockMultiTileRotatable<BlockMailbox.Material> {
    private static final AxisAlignedBB MAILBOX_NORTH_AABB = new AxisAlignedBB(0.2D, 0.2D, 0.6D, 0.8D, 0.9D, 1.4D);
    private static final AxisAlignedBB MAILBOX_SOUTH_AABB = new AxisAlignedBB(0.2D, 0.2D, -0.4D, 0.8D, 0.9D, 0.4D);
    private static final AxisAlignedBB MAILBOX_EAST_AABB = new AxisAlignedBB(-0.4D, 0.2D, 0.2D, 0.4D, 0.9D, 0.8D);
    private static final AxisAlignedBB MAILBOX_WEST_AABB = new AxisAlignedBB(0.6D, 0.2D, 0.2D, 1.4D, 0.9D, 0.8D);

    public BlockMailbox() {
        super(new ResourceLocation(HarvestCore.MODID, "mailbox"), net.minecraft.block.material.Material.WOOD, Material.class);
        setHardness(1.5F);
        setSoundType(SoundType.WOOD);
        setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileRotatable) {
            TileRotatable mailbox = ((TileRotatable)tile);
            EnumFacing facing = mailbox.getFacing();
            switch (facing) {
                case NORTH:
                    return MAILBOX_NORTH_AABB;
                case EAST:
                    return MAILBOX_EAST_AABB;
                case SOUTH:
                    return MAILBOX_SOUTH_AABB;
                case WEST:
                    return MAILBOX_WEST_AABB;
            }
        }

        return FULL_BLOCK_AABB;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileMailbox();
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public ItemBlock createItemBlock() {
        return new ItemBlockMailbox(getRegistryName(), this);
    }

    public enum Material implements IStringSerializable {
        DEFAULT, OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK, NETHER_BRICK;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
