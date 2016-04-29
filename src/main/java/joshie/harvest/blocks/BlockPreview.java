package joshie.harvest.blocks;

import joshie.harvest.blocks.tiles.TileMarker;
import joshie.harvest.buildings.Building;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.General;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.util.base.BlockHFBaseMeta;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockPreview extends BlockHFBaseMeta<BlockPreview.Direction> {
    public static enum Direction implements IStringSerializable {
        N1_T__N2_T__SWAP_F(true, true, false),
        N1_T__N2_T__SWAP_T(true, true, true),
        N1_T__N2_F__SWAP_F(true, false, false),
        N1_T__N2_F__SWAP_T(true, false, true),
        N1_F__N2_F__SWAP_F(false, false, false),
        N1_F__N2_F__SWAP_T(false, false, true),
        N1_F__N2_T__SWAP_F(false, true, false),
        N1_F__N2_T__SWAP_T(false, true, true);

        private final boolean N1;
        private final boolean N2;
        private final boolean swap;

        Direction(boolean N1, boolean N2, boolean swap) {
            this.N1 = N1;
            this.N2 = N2;
            this.swap = swap;
        }

        public boolean isN1() {
            return this.N1;
        }

        public boolean isN2() {
            return this.N2;
        }

        public boolean isSwap() {
            return this.swap;
        }

        @Override
        public String getName() {
            return toString();
        }
    }

    public BlockPreview() {
        super(Material.WOOD, HFTab.TOWN, Direction.class);
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            int next = getEnumFromState(state).ordinal() + 1;
            if (next >= 7) {
                next = 0;
            }
            return world.setBlockState(pos, getStateFromMeta(next), 2);
        } else {
            Direction direction = getEnumFromState(state);
            TileMarker marker = (TileMarker) world.getTileEntity(pos);
            EntityNPCBuilder builder = HFTrackers.getPlayerTracker(player).getBuilder(world);
            if (builder != null && !builder.isBuilding()) {
                builder.setPosition(pos.getX(), pos.getY(), pos.getZ()); //Teleport the builder to the position
                builder.startBuilding(marker.getBuilding(), pos, direction.isN1(), direction.isN2(), direction.isSwap(), UUIDHelper.getPlayerUUID(player));
                world.setBlockToAir(pos);
                return true;
            } else return false;
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileMarker();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
        if (!(player instanceof EntityPlayer)) return;
        if (stack.getItemDamage() >= Building.buildings.size()) return;
        Building group = Building.buildings.get(stack.getItemDamage());
        if (group != null) {
            TileMarker marker = (TileMarker) world.getTileEntity(pos);
            marker.setBuilding(group);
            //Create a builder if none exists
            if (!world.isRemote) {
                HFTrackers.getPlayerTracker((EntityPlayer) player).getBuilder(world);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        if (tab != HFTab.TOWN) return;
        if (General.DEBUG_MODE) {
            super.getSubBlocks(item, tab, list);
        }
    }
}