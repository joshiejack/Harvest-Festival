package joshie.harvest.core.block;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFEnumRotatableTile;
import joshie.harvest.core.block.BlockStand.Stand;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.tile.TileCookingStand;
import joshie.harvest.core.tile.TilePlate;
import joshie.harvest.core.base.tile.TileStand;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BlockStand extends BlockHFEnumRotatableTile<BlockStand, Stand> {
    public static final AxisAlignedBB PLATE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1D, 1.0D);

    @SuppressWarnings("WeakerAccess")
    public enum Stand implements IStringSerializable {
        COOKING, CHICKEN, LIVESTOCK, POT, PLATE;

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }

    public BlockStand() {
        super(Material.PISTON, Stand.class, HFTab.TOWN);
        setHardness(2.5F);
        setSoundType(SoundType.WOOD);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        switch (getEnumFromState(state)) {
            case PLATE:
                return PLATE_AABB;
            default:
                return FULL_BLOCK_AABB;
        }
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        switch (getEnumFromState(state)) {
            case PLATE:
                return 0.2F;
            default:
                return super.getBlockHardness(state, world, pos);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileStand) {
            TileStand stand = ((TileStand)tile);
            if (stand.isEmpty() && held != null && stand.isItemValid(held) && stand.setContents(player, held)) {
                return true;
            } else if (!stand.isEmpty()) {
                ItemStack contents = stand.removeContents();
                if (contents != null) {
                    SpawnItemHelper.addToPlayerInventory(player, contents);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        switch (getEnumFromState(state)) {
            case COOKING:   return new TileCookingStand();
            case PLATE:     return new TilePlate();
            default:        return new TileCookingStand();
        }
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileStand) {
            TileStand stand = ((TileStand)tile);
            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stand.getContents());
        }

        super.breakBlock(world, pos, state);
    }
}
