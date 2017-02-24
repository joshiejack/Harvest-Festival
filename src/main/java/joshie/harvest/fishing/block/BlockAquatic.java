package joshie.harvest.fishing.block;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFEnum;
import joshie.harvest.core.base.item.ItemBlockHF;
import joshie.harvest.core.base.tile.TileSingleStack;
import joshie.harvest.fishing.block.BlockAquatic.FishingBlock;
import joshie.harvest.fishing.item.ItemBlockFishing;
import joshie.harvest.fishing.tile.TileHatchery;
import joshie.harvest.fishing.tile.TileTrap;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;

import static joshie.harvest.core.proxy.HFClientProxy.NO_WATER;
import static joshie.harvest.fishing.block.BlockAquatic.FishingBlock.HATCHERY;
import static joshie.harvest.fishing.block.BlockAquatic.FishingBlock.TRAP_BAITED;
import static net.minecraft.block.BlockLiquid.LEVEL;

public class BlockAquatic extends BlockHFEnum<BlockAquatic, FishingBlock> {
    public BlockAquatic() {
        super(Material.WATER, FishingBlock.class, HFTab.FISHING);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, LEVEL, temporary);
        return new BlockStateContainer(this, LEVEL, property);
    }

    @Override
    public ItemBlockHF getItemBlock() {
        return new ItemBlockFishing(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return getEnumFromState(state) != FishingBlock.HATCHERY;
    }

    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof TileSingleStack && ((TileSingleStack) tile).onRightClicked(player, held);
    }

    @Override
    @SuppressWarnings("deprecation")
    @Nonnull
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world instanceof ChunkCache ? ((ChunkCache)world).func_190300_a(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if (tile instanceof TileTrap) {
            TileTrap trap = ((TileTrap)tile);
            if (trap.isBaited()) return getStateFromEnum(TRAP_BAITED);
            else return getStateFromEnum(FishingBlock.TRAP);
        }

        return state;
    }

    @Override
    @SuppressWarnings("deprecation, unchecked")
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (getEnumFromState(state) == HATCHERY) {
            return new AxisAlignedBB(0.0D, -0.9D, 0.0D, 1.0D, 0.1D, 1.0D);
        } else return super.getBoundingBox(state, world, pos);
    }

    @Override
    @SuppressWarnings("deprecation, unchecked")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos) {
        if (getEnumFromState(state) == HATCHERY) {
            return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.001D, 1.0D);
        } else return super.getBoundingBox(state, world, pos);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        switch (getEnumFromState(state)) {
            case TRAP:
            case TRAP_BAITED:   return new TileTrap();
            case HATCHERY:      return new TileHatchery();
            default:            return null;
        }
    }

    @Override
    protected boolean shouldDisplayInCreative(FishingBlock block) {
        return block != TRAP_BAITED;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {
        ModelLoader.setCustomStateMapper(this, NO_WATER);
        super.registerModels(item, name);
    }

    public enum FishingBlock implements IStringSerializable {
        TRAP, TRAP_BAITED, HATCHERY;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }

        public boolean isTrap() {
            return this == TRAP || this == TRAP_BAITED;
        }
    }
}
