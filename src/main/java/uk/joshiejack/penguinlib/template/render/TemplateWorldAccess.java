package uk.joshiejack.penguinlib.template.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class TemplateWorldAccess implements IBlockAccess {
    protected final Map<BlockPos, IBlockState> mapping = new HashMap<>();

    public Map<BlockPos, IBlockState> getBlockMap() {
        return mapping;
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(@Nonnull BlockPos pos) {
        return null;
    }

    @Override
    public int getCombinedLight(@Nonnull BlockPos pos, int lightValue) {
        return 15;
    }

    @Nonnull
    @Override
    public IBlockState getBlockState(@Nonnull BlockPos pos) {
        IBlockState state = mapping.get(pos);
        return state != null ? state : Blocks.AIR.getDefaultState();
    }

    @Override
    public boolean isAirBlock(@Nonnull BlockPos pos) {
        IBlockState state = mapping.get(pos);
        return state != null && state.getBlock() == Blocks.AIR;
    }

    @Nonnull
    @Override
    public Biome getBiome(@Nonnull BlockPos pos) {
        return Biomes.PLAINS;
    }

    @Override
    public int getStrongPower(@Nonnull BlockPos pos, @Nonnull EnumFacing direction) {
        return 0;
    }

    @Nonnull
    @Override
    public WorldType getWorldType() {
        return WorldType.DEFAULT;
    }

    @Override
    public boolean isSideSolid(@Nonnull BlockPos pos, @Nonnull EnumFacing side, boolean _default) {
        IBlockState state = mapping.get(pos);
        return state != null && state.isSideSolid(this, pos, side);
    }
}