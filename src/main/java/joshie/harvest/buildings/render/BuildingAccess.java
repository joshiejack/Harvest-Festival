package joshie.harvest.buildings.render;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.buildings.placeable.blocks.PlaceableDouble;
import joshie.harvest.buildings.placeable.blocks.PlaceableDoubleOpposite;
import joshie.harvest.core.util.HFTemplate;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class BuildingAccess implements IBlockAccess {
    private final Map<BlockPos, IBlockState> mapping = new HashMap<>();

    @SuppressWarnings("deprecation")
    public BuildingAccess(Building building, Rotation rotation) {
        HFTemplate template = BuildingRegistry.INSTANCE.getTemplateForBuilding(building);
        if (template == null || template.getComponents() == null) return;
        for (Placeable placeable : template.getComponents()) {
            if (placeable == null) continue;
            if (placeable instanceof PlaceableBlock) {
                PlaceableBlock block = (PlaceableBlock) placeable;
                if (isValidBlock(block)) {
                    IBlockState state = block.getTransformedState(rotation);
                    BlockPos pos = block.transformBlockPos(rotation);
                    mapping.put(pos, state);
                    if (block instanceof PlaceableDouble) {
                        mapping.put(pos.up(), state.getBlock().getStateFromMeta(8));
                    } else if (block instanceof PlaceableDoubleOpposite) {
                        mapping.put(pos.up(), state.getBlock().getStateFromMeta(9));
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private boolean isValidBlock(PlaceableBlock placeable) {
        if (placeable == null || placeable.getBlock() == null || placeable.getBlock() == Blocks.AIR) return false;
        if (HFBuildings.FULL_BUILDING_RENDER) return true;
        else {
            Block block = placeable.getBlock();
            return block.isFullCube(block.getDefaultState()) || block instanceof BlockStairs || block instanceof BlockSlab || block instanceof BlockPane || block instanceof BlockLeaves || block instanceof BlockFence || block instanceof BlockWall;
        }
    }

    public Map<BlockPos, IBlockState> getBlockMap() {
        return mapping;
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return null;
    }

    @Override
    public int getCombinedLight(@Nonnull BlockPos pos, int lightValue) {
        return 15;
    }

    @Override
    @Nonnull
    public IBlockState getBlockState(@Nonnull BlockPos pos) {
        IBlockState state = mapping.get(pos);
        return state != null ? state : Blocks.AIR.getDefaultState();
    }

    @Override
    public boolean isAirBlock(@Nonnull BlockPos pos) {
        IBlockState state = mapping.get(pos);
        return state != null && state.getBlock() == Blocks.AIR;
    }

    @Override
    @Nonnull
    public Biome getBiome(@Nonnull BlockPos pos) {
        return Biomes.PLAINS;
    }

    @Override
    public int getStrongPower(@Nonnull BlockPos pos, @Nonnull EnumFacing direction) {
        return 0;
    }

    @Override
    @Nonnull
    public WorldType getWorldType() {
        return WorldType.DEFAULT;
    }

    @Override
    public boolean isSideSolid(@Nonnull BlockPos pos, @Nonnull EnumFacing side, boolean _default) {
        IBlockState state = mapping.get(pos);
        return state != null && state.isSideSolid(this, pos, side);
    }
}
