package joshie.harvest.animals.block;

import joshie.harvest.animals.block.BlockTray.Tray;
import joshie.harvest.animals.tile.TileFeeder;
import joshie.harvest.animals.tile.TileNest;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.*;
import joshie.harvest.api.core.Size;
import joshie.harvest.core.achievements.HFAchievements;
import joshie.harvest.core.base.block.BlockHFEnum;
import joshie.harvest.core.base.tile.TileFillable;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.lib.CreativeSort;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import java.util.Locale;

import static joshie.harvest.animals.block.BlockTray.Tray.FEEDER_EMPTY;
import static joshie.harvest.animals.block.BlockTray.Tray.NEST_EMPTY;

public class BlockTray extends BlockHFEnum<BlockTray, Tray> implements IAnimalFeeder, INest {
    private static final AxisAlignedBB NEST_COLLISION = new AxisAlignedBB(0.15D, 0D, 0.15D, 0.85D, 0.2D, 0.85D);
    private static final AxisAlignedBB NEST_AABB = new AxisAlignedBB(0.15D, 0D, 0.15D, 0.85D, 0.35D, 0.85D);
    private static final AxisAlignedBB FEEDER_AABB = new AxisAlignedBB(0.0D, 0D, 0.0D, 1.0D, 0.075D, 1.0D);

    public enum Tray implements IStringSerializable {
        NEST_EMPTY, SMALL_CHICKEN, MEDIUM_CHICKEN, LARGE_CHICKEN, FEEDER_EMPTY, FEEDER_FULL;

        public boolean isFeeder() {
            return this == FEEDER_EMPTY || this == FEEDER_FULL;
        }

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
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
    @SuppressWarnings("deprecation, unchecked")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos) {
        return getEnumFromState(state).isFeeder() ? super.getCollisionBoundingBox(state, world, pos) : NEST_COLLISION;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return getEnumFromState(state).isFeeder() ? FEEDER_AABB : NEST_AABB;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) return false;
        TileEntity tile = world.getTileEntity(pos);
        if (held == null && tile instanceof TileNest) {
            TileNest nest = (TileNest) tile;
            if (nest.getDrop() != null) {
                ItemStack drop = nest.getDrop().copy();
                if (nest.getMother() != null) {
                    NBTTagCompound tag = drop.getSubCompound("Data", true);
                    int relationship = HFApi.player.getRelationsForPlayer(player).getRelationship(nest.getMother());
                    tag.setInteger("Relationship", (relationship - (relationship % 2500)));
                }

                nest.clear();
                SpawnItemHelper.addToPlayerInventory(player, drop);

                if (!world.isRemote) {
                    world.setBlockState(pos, getStateFromEnum(NEST_EMPTY));
                }

                player.addStat(HFAchievements.egger);
                if (HFApi.sizeable.getSize(drop) == Size.LARGE) {
                    player.addStat(HFAchievements.eggerLarge);
                }

                return true;
            }
        } else if (tile instanceof TileFillable) {
            return ((TileFillable)tile).onActivated(held);
        }

        return false;
    }

    @Override
    public boolean feedAnimal(AnimalStats stats, World world, BlockPos pos, IBlockState state, boolean simulate) {
        if (getEnumFromState(state).isFeeder() && HFApi.animals.canAnimalEatFoodType(stats, AnimalFoodType.SEED)) {
            TileFeeder feeder = ((TileFeeder) world.getTileEntity(pos));
            if (feeder != null && feeder.getFillAmount() > 0) {
                if (!simulate) {
                    feeder.adjustFill(-1);
                    stats.performAction(world, null, AnimalAction.FEED);
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isNest(AnimalStats stats, World world, BlockPos pos, IBlockState state) {
        return getEnumFromState(state.getActualState(world, pos)) == NEST_EMPTY;
    }

    @Override
    public void layEgg(AnimalStats stats, World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (stats.getAnimal() != null && tile instanceof TileNest) {
            if (!world.isRemote) {
                ((TileNest) tile).setDrop(EntityHelper.getEntityUUID(stats.getAnimal()), stats.getType().getProduct(stats));
                stats.setProduced(1); //Product one egg
            }

            EntityAnimal animal = stats.getAnimal();
            animal.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (animal.worldObj.rand.nextFloat() - animal.worldObj.rand.nextFloat()) * 0.2F + 1.0F);
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return getEnumFromState(state).isFeeder() ? new TileFeeder() : new TileNest();
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world instanceof ChunkCache ? ((ChunkCache)world).func_190300_a(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if (tile instanceof TileFeeder) {
            boolean isFilled = ((TileFeeder)tile).getFillAmount() > 0;
            if (isFilled) return getStateFromEnum(Tray.FEEDER_FULL);
            else return getStateFromEnum(Tray.FEEDER_EMPTY);
        } else if (tile instanceof TileNest) {
            TileNest nest = ((TileNest)tile);
            if (nest.getDrop() == null) return getStateFromEnum(Tray.NEST_EMPTY);
            else {
                Size size = nest.getSize();
                if (size == null || size == Size.SMALL) return getStateFromEnum(Tray.SMALL_CHICKEN);
                else if (size == Size.MEDIUM) return getStateFromEnum(Tray.MEDIUM_CHICKEN);
                else if (size == Size.LARGE) return getStateFromEnum(Tray.LARGE_CHICKEN);
            }
        }

        return state;
    }

    @Override
    protected boolean shouldDisplayInCreative(Tray tray) {
        return tray == NEST_EMPTY || tray == FEEDER_EMPTY;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TROUGH;
    }
}