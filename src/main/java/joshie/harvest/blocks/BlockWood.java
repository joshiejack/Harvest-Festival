package joshie.harvest.blocks;

import joshie.harvest.blocks.BlockWood.Woodware;
import joshie.harvest.blocks.tiles.TileFillable;
import joshie.harvest.blocks.tiles.TileNest;
import joshie.harvest.blocks.tiles.TileTrough;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.ToolHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.base.BlockHFBaseEnumRotatableMeta;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
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

import static joshie.harvest.api.HFApi.shipping;
import static joshie.harvest.blocks.BlockWood.Woodware.NEST;

public class BlockWood extends BlockHFBaseEnumRotatableMeta<Woodware> {
    public static final PropertyBool FILLED = PropertyBool.create("filled");

    public enum Woodware implements IStringSerializable {
        SHIPPING, NEST, TROUGH;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    public BlockWood() {
        super(Material.WOOD, Woodware.class);
        setHardness(1.5F);
        setSoundType(SoundType.WOOD);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, temporary, FACING, FILLED);
        return new BlockStateContainer(this, property, FACING, FILLED);
    }

    @Override
    public String getToolType(Woodware wood) {
        return "axe";
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        Woodware wood = getEnumFromState(state);
        switch (wood) {
            case SHIPPING:
                return new AxisAlignedBB(0D, 0D, 0D, 1D, 0.6D, 1D);
            default:
                return FULL_BLOCK_AABB;
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
        System.out.println(state);
        Woodware wood = getEnumFromState(state);
        if (player.isSneaking()) return false;
        else if ((wood == Woodware.SHIPPING) && held != null) {
            long sell = shipping.getSellValue(held);
            if (sell > 0) {
                if (!world.isRemote) {
                    HFTrackers.getServerPlayerTracker(player).getTracking().addForShipping(held.copy());
                }

                held.splitStack(1);
                return true;
            }
        } else if (wood == NEST) {
            if (held != null && ToolHelper.isEgg(held)) {
                held.splitStack(1);
                ((TileNest)world.getTileEntity(pos)).setFilled(true);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return getEnumFromState(state) != Woodware.SHIPPING;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        Woodware woodware = getEnumFromState(state);
        switch (woodware) {
            case NEST:
                return new TileNest();
            case TROUGH:
                return new TileTrough();
            default:
                return null;
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileFillable) {
            IBlockState state1 = state.withProperty(FILLED, ((TileFillable)tile).isFilled());
            return state1;
        }

        return state;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        Woodware wood = getEnumFromState(state);
        if (wood != NEST) {
            super.onBlockPlacedBy(world, pos, state, placer, stack);
        }
    }

    @Override
    public boolean isValidTab(CreativeTabs tab, Woodware wood) {
        return tab == HFTab.FARMING;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TROUGH;
    }
}