package uk.joshiejack.piscary.block;

import uk.joshiejack.penguinlib.block.base.BlockMultiTile;
import uk.joshiejack.penguinlib.item.base.block.ItemBlockMulti;
import uk.joshiejack.piscary.Piscary;
import uk.joshiejack.piscary.tile.TileTrap;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;


public class BlockTrap extends BlockMultiTile<BlockTrap.Trap> {
    public BlockTrap() {
        super(new ResourceLocation(Piscary.MODID, "fish_trap"), Material.WATER, Trap.class);
        setHardness(0.1F);
        setCreativeTab(Piscary.TAB);
        setTickRandomly(true);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, BlockFluidBase.LEVEL, temporary);
        return new BlockStateContainer(this, BlockFluidBase.LEVEL, property);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ItemBlock createItemBlock() {
        return new ItemBlockMulti<Trap>(getRegistryName(), enumClass, this) {
            @Override
            @Nonnull
            public EnumActionResult onItemUse(@Nonnull EntityPlayer playerIn, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
                if (worldIn.getBlockState(pos.up(2)).getMaterial() == Material.WATER) return super.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
                else return EnumActionResult.PASS;
            }
        };
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if (tile instanceof TileTrap) {
            TileTrap trap = ((TileTrap)tile);
            if (trap.isBaited()) return getStateFromEnum(trap.getTrapEnum());
            else return getStateFromEnum(Trap.EMPTY);
        }

        return state;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        switch (getEnumFromState(state)) {
            case EMPTY:
            case BAITED:
            case BAITED_VANILLA:    return new TileTrap();
            default:                return null;
        }
    }

    @Nonnull
    @Override
    protected ItemStack getCreativeStack(Trap block) {
        return block != Trap.EMPTY ? ItemStack.EMPTY : super.getCreativeStack(block);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item) {
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(BlockFluidBase.LEVEL).build());
        super.registerModels(item);
    }

    public enum Trap implements IStringSerializable {
        EMPTY, BAITED, BAITED_VANILLA;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
