package joshie.harvestmoon.core.util;

import java.util.ArrayList;
import java.util.HashMap;

import joshie.harvestmoon.buildings.Building;
import joshie.harvestmoon.buildings.placeable.Placeable;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvestmoon.core.helpers.generic.MCClientHelper;
import joshie.harvestmoon.crops.WorldLocation;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockAccessPreview implements IBlockAccess {
    private HashMap<WorldLocation, PlaceableBlock> blocks = new HashMap();
    private int previewX, previewY, previewZ; //The world location of the BASE BLOCK
    private boolean n1, n2, swap; //Which direction is this building facing?
    private Building building;

    public BlockAccessPreview(Building building, ArrayList<Placeable> list) {
        this.building = building;
        for (Placeable p : list) {
            if (p instanceof PlaceableBlock) {
                PlaceableBlock block = (PlaceableBlock) p;
                blocks.put(new WorldLocation(0, p.getX(), p.getY(), p.getZ()), block);
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
    public Block getBlock(int worldX, int worldY, int worldZ) {
        //This is calling for a blocks metadata at a certain world position
        int trueX = worldX - previewX; //The real x value is the world value, minus the position
        int trueY = worldY - previewY - building.getOffsetY(); //The real y value is the world value, minus the height of the current block
        int trueZ = worldZ - previewZ;

        if (!swap) {
            if (n1) {
                trueX = previewX - worldX;
            }

            if (n2) {
                trueZ = previewZ - worldZ;
            }
        }

        if (swap) {
            trueX = worldZ - previewZ;
            trueZ = worldX - previewX;

            if (n1) {
                trueX = previewZ - worldZ;
            }

            if (n2) {
                trueZ = previewX - worldX;
            }
        }

        PlaceableBlock block = blocks.get(new WorldLocation(0, trueX, trueY, trueZ));
        return block == null ? Blocks.air : block.getBlock();
    }

    @Override
    public TileEntity getTileEntity(int x, int y, int z) {
        return null;
    }

    @Override
    public int getLightBrightnessForSkyBlocks(int x, int y, int z, int p_72802_4_) {
        return 255;
    }

    @Override
    public int getBlockMetadata(int worldX, int worldY, int worldZ) {
        //This is calling for a blocks metadata at a certain world position
        int trueX = worldX - previewX; //The real x value is the world value, minus the position
        int trueY = worldY - previewY - building.getOffsetY(); //The real y value is the world value, minus the height of the current block
        int trueZ = worldZ - previewZ;

        if (!swap) {
            if (n1) {
                trueX = previewX - worldX;
            }

            if (n2) {
                trueZ = previewZ - worldZ;
            }
        }

        if (swap) {
            trueX = worldZ - previewZ;
            trueZ = worldX - previewX;

            if (n1) {
                trueX = previewZ - worldZ;
            }

            if (n2) {
                trueZ = previewX - worldX;
            }
        }

        PlaceableBlock block = blocks.get(new WorldLocation(0, trueX, trueY, trueZ));
        return block == null ? 0 : block.getMetaData(n1, n2, swap);
    }

    @Override
    public int isBlockProvidingPowerTo(int x, int y, int z, int p_72879_4_) {
        return 0;
    }

    @Override
    public boolean isAirBlock(int x, int y, int z) {
        return false;
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int x, int z) {
        return MCClientHelper.getWorld().getBiomeGenForCoords(x, z);
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public boolean extendedLevelsInChunkCache() {
        return false;
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
        return false;
    }
}
