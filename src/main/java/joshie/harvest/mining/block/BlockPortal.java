package joshie.harvest.mining.block;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFEnum;
import joshie.harvest.core.helpers.WorldHelper;
import joshie.harvest.core.util.Text;
import joshie.harvest.mining.MiningHelper;
import joshie.harvest.mining.MiningTicker;
import joshie.harvest.mining.block.BlockPortal.Portal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static joshie.harvest.mining.block.BlockPortal.Portal.*;
import static joshie.harvest.mining.block.BlockPortal.Type.MINE;
import static joshie.harvest.mining.block.BlockPortal.Type.OVERWORLD;


public class BlockPortal extends BlockHFEnum<BlockPortal, Portal> {
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D);

    public enum Portal implements IStringSerializable {
        MINE_TL, MINE_TM, MINE_TR, MINE_BL, MINE_BM, MINE_BR,
        MINE_TL_EW, MINE_TM_EW, MINE_TR_EW, MINE_BL_EW, MINE_BM_EW, MINE_BR_EW,
        INTERNAL_TL, INTERNAL_TM, INTERNAL_TR, INTERNAL_BL, INTERNAL_BM, INTERNAL_BR,
        INTERNAL_TL_EW, INTERNAL_TM_EW, INTERNAL_TR_EW, INTERNAL_BL_EW, INTERNAL_BM_EW, INTERNAL_BR_EW,
        STONE_TL, STONE_TM, STONE_TR, STONE_BL, STONE_BM, STONE_BR,
        STONE_TL_EW, STONE_TM_EW, STONE_TR_EW, STONE_BL_EW, STONE_BM_EW, STONE_BR_EW;

        public boolean isMine() {
            return ordinal() <= MINE_BR_EW.ordinal();
        }

        public boolean isInternal() {
            return ordinal() >= INTERNAL_TL.ordinal() && ordinal() <= INTERNAL_BR_EW.ordinal();
        }

        public boolean isStone() {
            return ordinal() >= STONE_TL.ordinal();
        }

        public boolean isEW() {
            return ordinal() % 12 >= 6;
        }

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public BlockPortal() {
        super(Material.ROCK, Portal.class, HFTab.MINING);
        setSoundType(SoundType.STONE);
    }

    //TECHNICAL
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getBlock() == this ? getEnumFromState(state).ordinal() % 6 : 0;
    }

    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return -1F;
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return false;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        return 60000000F;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Text.localizeFully(getUnlocalizedName());
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return AABB;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (!world.isRemote) {
            IBlockState actual = getActualState(state, world, pos);
            if (actual.getBlock() == this) {
                Portal portal = getEnumFromState(actual);
                if (portal.isInternal()) {
                    MiningHelper.teleportBetweenMine(entity);
                } else if (portal.isMine()) {
                   MiningHelper.teleportToOverworld(entity);
                } else {
                   MiningHelper.teleportToMine(entity);
                }
            }
        }
    }

    private boolean isSameBlock(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == this;
    }

    protected enum Type {
        MINE, INTERNAL, OVERWORLD;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess access, BlockPos pos) {
        World world = WorldHelper.getWorld(access);
        Type type = world.provider.getDimension() == 0 ? OVERWORLD : MINE;
        boolean internal = type == MINE && MiningTicker.getFloor(pos.getX() >> 4, pos.getY()) != 1;
        boolean connectedUp = isSameBlock(access, pos.up());
        boolean connectedDown = isSameBlock(access, pos.down());
        boolean connectedEast = isSameBlock(access, pos.east());
        boolean connectedWest = isSameBlock(access, pos.west());
        boolean connectedSouth = isSameBlock(access, pos.south());
        boolean connectedNorth = isSameBlock(access, pos.north());
        if (connectedDown && ((!connectedEast && connectedWest) || (connectedNorth && !connectedSouth))) {
            if (connectedWest) return type == MINE ? internal ? getStateFromEnum(INTERNAL_TL_EW) : getStateFromEnum(MINE_TL_EW) : getStateFromEnum(STONE_TL_EW);
            else return type == MINE ? internal ? getStateFromEnum(INTERNAL_TL) : getStateFromEnum(MINE_TL) : getStateFromEnum(STONE_TL);
        } else if (connectedDown && ((connectedEast && !connectedWest) || (!connectedNorth && connectedSouth))) {
            if (connectedEast) return type == MINE ? internal ? getStateFromEnum(INTERNAL_TR_EW) : getStateFromEnum(MINE_TR_EW) : getStateFromEnum(STONE_TR_EW);
            return type == MINE ? internal ? getStateFromEnum(INTERNAL_TR) : getStateFromEnum(MINE_TR) : getStateFromEnum(STONE_TR);
        } else if (connectedDown && ((connectedEast && connectedWest) || (connectedNorth && connectedSouth))) {
            if (connectedWest) return type == MINE ? internal ? getStateFromEnum(INTERNAL_TM_EW) : getStateFromEnum(MINE_TM_EW) : getStateFromEnum(STONE_TM_EW);
            return type == MINE ? internal ? getStateFromEnum(INTERNAL_TM) : getStateFromEnum(MINE_TM) : getStateFromEnum(STONE_TM);
        } else if (connectedUp && ((!connectedEast && connectedWest) || (connectedNorth && !connectedSouth))) {
            if (connectedWest) return type == MINE ? internal ? getStateFromEnum(INTERNAL_BL_EW) : getStateFromEnum(MINE_BL_EW) : getStateFromEnum(STONE_BL_EW);
            return type == MINE ? internal ? getStateFromEnum(INTERNAL_BL) : getStateFromEnum(MINE_BL) : getStateFromEnum(STONE_BL);
        } else if (connectedUp && ((connectedEast && !connectedWest) || (!connectedNorth && connectedSouth))) {
            if (connectedEast) return type == MINE ? internal ? getStateFromEnum(INTERNAL_BR_EW) : getStateFromEnum(MINE_BR_EW) : getStateFromEnum(STONE_BR_EW);
            return type == MINE ? internal ? getStateFromEnum(INTERNAL_BR) : getStateFromEnum(MINE_BR) : getStateFromEnum(STONE_BR);
        } else if(connectedUp && ((connectedEast && connectedWest) || (connectedNorth && connectedSouth))) {
            if (connectedEast) return type == MINE ? internal ? getStateFromEnum(INTERNAL_BM_EW) : getStateFromEnum(MINE_BM_EW) : getStateFromEnum(STONE_BM_EW);
            return type == MINE ? internal ? getStateFromEnum(INTERNAL_BM) : getStateFromEnum(MINE_BM) : getStateFromEnum(STONE_BM);
        } else return state;
    }

    @Override
    protected boolean shouldDisplayInCreative(Portal e) {
        return false;
    }
}