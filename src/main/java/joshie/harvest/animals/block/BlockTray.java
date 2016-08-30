package joshie.harvest.animals.block;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.block.BlockTray.Tray;
import joshie.harvest.animals.tile.TileFeeder;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.animals.IAnimalFeeder;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.animals.INest;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.base.tile.TileFillable;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.base.block.BlockHFEnum;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static joshie.harvest.animals.block.BlockTray.Tray.*;

public class BlockTray extends BlockHFEnum<BlockTray, Tray> implements IAnimalFeeder, INest {
    private static final AxisAlignedBB NEST_AABB = new AxisAlignedBB(0.15D, 0D, 0.15D, 0.85D, 0.35D, 0.85D);
    private static final AxisAlignedBB FEEDER_AABB = new AxisAlignedBB(0.0D, 0D, 0.0D, 1.0D, 0.075D, 1.0D);

    public enum Tray implements IStringSerializable {
        NEST_EMPTY, SMALL_CHICKEN, MEDIUM_CHICKEN, LARGE_CHICKEN, FEEDER_EMPTY, FEEDER_FULL;

        public ItemStack getDrop() {
            if (this == SMALL_CHICKEN) return HFAnimals.EGG.getStack(Size.SMALL);
            else if (this == MEDIUM_CHICKEN) return HFAnimals.EGG.getStack(Size.MEDIUM);
            else if (this == LARGE_CHICKEN) return HFAnimals.EGG.getStack(Size.LARGE);
            else return null;
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

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return getEnumFromState(state).isFeeder() ? FEEDER_AABB : NEST_AABB;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) return false;
        else if (held == null) {
            Tray nest = getEnumFromState(state);
            if (nest.getDrop() != null) {
                if (!world.isRemote) {
                    Block.spawnAsEntity(world, pos, nest.getDrop());
                    world.setBlockState(pos, getStateFromEnum(NEST_EMPTY));
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
    public boolean feedAnimal(IAnimalTracked tracked, World world, BlockPos pos, IBlockState state) {
        if (getEnumFromState(state).isFeeder()) {
            if (HFApi.animals.canAnimalEatFoodType(tracked, AnimalFoodType.SEED)) {
                TileFeeder feeder = ((TileFeeder) world.getTileEntity(pos));
                if (feeder.getFillAmount() > 0) {
                    feeder.adjustFill(-1);
                    tracked.getData().feed(null);

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean layEgg(IAnimalTracked tracked, World world, BlockPos pos, IBlockState state) {
        EntityPlayer player = tracked.getData().getOwner();
        if (player != null && getEnumFromState(state) == NEST_EMPTY && tracked.getData().getType().getName().equals("chicken")) {
            Size size = null;
            int relationship = HFApi.relationships.getRelationship(player, tracked);
            for (Size s : Size.values()) {
                if (relationship >= s.getRelationshipRequirement()) size = s;
            }

            if (size == Size.SMALL) world.setBlockState(pos, getStateFromEnum(SMALL_CHICKEN));
            else if (size == Size.MEDIUM) world.setBlockState(pos, getStateFromEnum(MEDIUM_CHICKEN));
            else if (size == Size.LARGE) world.setBlockState(pos, getStateFromEnum(LARGE_CHICKEN));
            EntityAnimal entity = tracked.getData().getAnimal();
            entity.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (entity.worldObj.rand.nextFloat() - entity.worldObj.rand.nextFloat()) * 0.2F + 1.0F);
            return true;
        }

        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return getEnumFromState(state).isFeeder();
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return getEnumFromState(state).isFeeder() ? new TileFeeder() : null;
    }

    @SuppressWarnings("deprecation")
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
    protected boolean shouldDisplayInCreative(Tray tray) {
        return tray.isEmpty();
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TROUGH;
    }
}