package joshie.harvest.core.util;

import java.util.HashMap;

import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;

public class BlockAccessPreview implements IBlockAccess {
    private HashMap<PlaceableBlock, PlaceableBlock> blocks = new HashMap();
    private int previewX, previewY, previewZ; //The world location of the BASE BLOCK
    private boolean n1, n2, swap; //Which direction is this building facing?
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

    public BlockAccessPreview setCoordinatesAndDirection(int worldX, int worldY, int worldZ, boolean n1, boolean n2, boolean swap) {
        this.previewX = worldX;
        this.previewY = worldY;
        this.previewZ = worldZ;
        this.n1 = n1;
        this.n2 = n2;
        this.swap = swap;
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

    @SuppressWarnings("null")
    @Override
    public IBlockState getBlockState(BlockPos pos) {
        //This is calling for a blocks metadata at a certain world position
        int trueX = pos.getX() - previewX; //The real x value is the world value, minus the position
        int trueY = pos.getY() - previewY - building.getOffsetY(); //The real y value is the world value, minus the height of the current block
        int trueZ = pos.getZ() - previewZ;

        if (!swap) {
            if (n1) {
                trueX = previewX - pos.getX();
            }

            if (n2) {
                trueZ = previewZ - pos.getZ();
            }
        }

        if (swap) {
            trueX = pos.getZ() - previewZ;
            trueZ = pos.getX() - previewX;

            if (n1) {
                trueX = previewZ - pos.getZ();
            }

            if (n2) {
                trueZ = previewX - pos.getX();
            }
        }

        PlaceableBlock block = blocks.get(new PlaceableBlock(trueX, trueY, trueZ));
        return block == null ? Blocks.stone.getDefaultState() : block.getBlockState(n1, n2, swap);
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
