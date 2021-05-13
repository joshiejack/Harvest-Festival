package uk.joshiejack.horticulture.block;

import uk.joshiejack.horticulture.Horticulture;
import uk.joshiejack.horticulture.client.renderer.block.statemap.StateMapperStump;
import uk.joshiejack.horticulture.tileentity.TileStump;
import uk.joshiejack.penguinlib.block.base.BlockMultiTile;
import uk.joshiejack.penguinlib.util.PropertyBlockState;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

import static uk.joshiejack.horticulture.Horticulture.MODID;

public class BlockStump extends BlockMultiTile<BlockPlanks.EnumType> implements IGrowable {
    public static final IUnlistedProperty<Integer> stage = Properties.toUnlisted(PropertyInteger.create("stage", 0, 4));
    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.5D, 0.9D);

    public BlockStump() {
        super(new ResourceLocation(MODID, "stump"), Material.WOOD, BlockPlanks.EnumType.class);
        setTickRandomly(true);
        setHardness(2F);
        setCreativeTab(Horticulture.TAB);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        if (property == null)
            return new ExtendedBlockState(this, new IProperty[]{temporary}, new IUnlistedProperty[]{PropertyBlockState.INSTANCE, stage});
        return new ExtendedBlockState(this, new IProperty[]{property}, new IUnlistedProperty[]{PropertyBlockState.INSTANCE, stage});
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos) {
        return BOUNDING_BOX;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileStump();
    }

    @Nonnull
    @Override
    public IBlockState getExtendedState(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        TileEntity tile = world instanceof ChunkCache ? ((ChunkCache) world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if (tile instanceof TileStump && state instanceof IExtendedBlockState) {  // avoid crash in case of mismatch
            TileStump stump = (TileStump) tile;
            return ((IExtendedBlockState) state).withProperty(PropertyBlockState.INSTANCE, stump.getMushroom()).withProperty(stage, stump.getStage());
        }

        return state;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item) {
        super.registerModels(item);
        ModelLoader.setCustomStateMapper(this, new StateMapperStump());
    }

    @Override
    public boolean canGrow(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isClient) {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof TileStump && ((TileStump) tile).getStage() < 4;
    }

    @Override
    public boolean canUseBonemeal(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof TileStump && ((TileStump)tile).getMushroom() != Blocks.AIR.getDefaultState();
    }

    @Override
    public void grow(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        updateTick(world, pos, state, rand); //YES!
    }
}
