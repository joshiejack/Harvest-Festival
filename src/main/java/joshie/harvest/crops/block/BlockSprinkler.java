package joshie.harvest.crops.block;

import joshie.harvest.core.base.block.BlockHFEnum;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.crops.block.BlockSprinkler.Sprinkler;
import joshie.harvest.crops.tile.TileSprinkler;
import joshie.harvest.crops.tile.TileSprinklerOld;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;

import java.util.Locale;

import static joshie.harvest.crops.HFCrops.SPRINKLER_DRAIN_RATE;

public class BlockSprinkler extends BlockHFEnum<BlockSprinkler, Sprinkler> {
    private static final AxisAlignedBB IRON_AABB = new AxisAlignedBB(0.2D, 0D, 0.2D, 0.8D, 0.7D, 0.8D);
    private static final AxisAlignedBB OLD_AABB = new AxisAlignedBB(0.2D, 0D, 0.2D, 0.8D, 0.5D, 0.8D);

    public BlockSprinkler() {
        super(Material.WOOD, Sprinkler.class);
        setSoundType(SoundType.GROUND);
    }

    public enum Sprinkler implements IStringSerializable {
        IRON, OLD;

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (getEnumFromState(state) == Sprinkler.IRON) return IRON_AABB;
        else return OLD_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (SPRINKLER_DRAIN_RATE <= 0) return false;
        ItemStack heldItem = player.getHeldItem(hand);
        if (!heldItem.isEmpty()) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileSprinkler) {
                TileSprinkler sprinkler = ((TileSprinkler) tile);
                FluidActionResult result = FluidUtil.tryEmptyContainer(heldItem, sprinkler.getTank(), 1000, player, true);

                player.setHeldItem(hand, result.getResult());
                sprinkler.saveAndRefresh();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return getEnumFromState(state) == Sprinkler.IRON ? new TileSprinkler() : new TileSprinklerOld();
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TROUGH;
    }
}