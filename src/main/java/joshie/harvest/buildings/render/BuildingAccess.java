package joshie.harvest.buildings.render;

import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.core.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import java.util.HashMap;

public class BuildingAccess implements IBlockAccess {
    private final HashMap<BlockPos, IBlockState> mapping = new HashMap<>();

    public BuildingAccess(BuildingImpl building, Direction direction) {
        for (PlaceableBlock block: building.getPreviewList()) {
            mapping.put(block.transformBlockPos(direction), block.getTransformedState(direction));
        }
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return null;
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue) {
        return 15;
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        IBlockState state = mapping.get(pos);
        return state != null ? state : Blocks.AIR.getDefaultState();
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        IBlockState state = mapping.get(pos);
        return state != null && state.getBlock() == Blocks.AIR;
    }

    @Override
    public Biome getBiome(BlockPos pos) {
        return Biomes.PLAINS;
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction) {
        return 0;
    }

    @Override
    public WorldType getWorldType() {
        return WorldType.DEFAULT;
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
        IBlockState state = mapping.get(pos);
        return state != null && state.isSideSolid(this, pos, side);
    }
}
