package joshie.harvest.animals.blocks;

import joshie.harvest.animals.blocks.BlockTray.Tray;
import joshie.harvest.blocks.tiles.TileFillable;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.base.BlockHFEnum;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static joshie.harvest.animals.HFAnimals.EGG;

public class BlockTray extends BlockHFEnum<BlockTray, Tray> {
    private static final AxisAlignedBB NEST_AABB = new AxisAlignedBB(0.15D, 0D, 0.15D, 0.85D, 0.35D, 0.85D);
    private static final AxisAlignedBB FEEDER_AABB = new AxisAlignedBB(0.0D, 0D, 0.0D, 1.0D, 0.075D, 1.0D);

    public enum Tray implements IStringSerializable {
        NEST_EMPTY(null, 0), SMALL_CHICKEN(EGG, 0), MEDIUM_CHICKEN(EGG, 1), LARGE_CHICKEN(EGG, 2),
        FEEDER_EMPTY(null, 0), FEEDER_FULL(null, 0);

        private final ItemStack drop;

        Tray(Item item, int damage) {
            this.drop = new ItemStack(item, 1, damage);
        }

        public boolean isEmpty() {
            return this == NEST_EMPTY || this == FEEDER_EMPTY;
        }

        public boolean isFeeder() {
            return this == FEEDER_EMPTY || this == FEEDER_FULL;
        }

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    public BlockTray() {
        super(Material.WOOD, Tray.class);
        setHardness(0.5F);
        setSoundType(SoundType.WOOD);
    }

    @Override
    public String getToolType(Tray wood) {
        return "axe";
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return getEnumFromState(state).isFeeder() ? FEEDER_AABB : NEST_AABB;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) return false;
        else if (held == null) {
            Tray nest = getEnumFromState(state);
            if (nest.drop != null) {
                player.setHeldItem(hand, nest.drop);
                if (!world.isRemote) {
                    world.setBlockState(pos, getStateFromEnum(Tray.NEST_EMPTY));
                }

                return true;
            }
        } else {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileFillable) {
                return ((TileFillable)tile).onActivated(held);
            }
        }

        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return getEnumFromState(state) == Tray.FEEDER_EMPTY;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return getEnumFromState(state) == Tray.FEEDER_EMPTY ? new TileFeeder() : null;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileFeeder) {
            boolean isFilled = ((TileFeeder)tile).getFillAmount() > 0;
            if (isFilled) return getStateFromEnum(Tray.FEEDER_FULL);
            else return getStateFromEnum(Tray.FEEDER_EMPTY);
        }

        return state;
    }

    @Override
    protected boolean isValidTab(CreativeTabs tab, Tray tray) {
        return tray.isEmpty() && super.isValidTab(tab, tray);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TROUGH;
    }
}