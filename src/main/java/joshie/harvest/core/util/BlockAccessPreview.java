package joshie.harvest.core.util;

import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.HashMap;

public class BlockAccessPreview implements IBlockAccess {
    private HashMap<PlaceableBlock, PlaceableBlock> blocks = new HashMap<PlaceableBlock, PlaceableBlock>();
    private int previewX, previewY, previewZ; //The world location of the BASE BLOCK
    private Mirror mirror;
    private Rotation rotation;
    private Building building;

    public BlockAccessPreview(Building building) {
        this.building = building;
        for (Placeable p : building.getList()) {
            if (p instanceof PlaceableBlock) {
                PlaceableBlock block = (PlaceableBlock) p;
                blocks.put(block, block);
            }
        }
    }

    public BlockAccessPreview setCoordinatesAndDirection(int worldX, int worldY, int worldZ, Mirror mirror, Rotation rotation) {
        this.previewX = worldX;
        this.previewY = worldY;
        this.previewZ = worldZ;
        this.mirror = mirror;
        this.rotation = rotation;
        return this;
    }

    @Override
    public boolean extendedLevelsInChunkCache() {
        return false;
    }

    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return null;
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue) {
        return 255;
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        return Blocks.STONE.getDefaultState(); //TODO: Rendering the preview
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return false;
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(BlockPos pos) {
        return MCClientHelper.getWorld().getBiomeGenForCoords(pos);
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
        return false;
    }
}
