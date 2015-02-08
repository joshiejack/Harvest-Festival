package joshie.harvestmoon.buildings;

import java.util.ArrayList;

import joshie.harvestmoon.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableIFaceable;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableStairs;
import joshie.harvestmoon.init.HMBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BuildingTest extends Building {
    public BuildingTest() {
        reset();
    }

    @Override
    public boolean generate(World world, int xCoord, int yCoord, int zCoord) {
        if (!world.isRemote) {
            reset();
        }

        return super.generate(world, xCoord, yCoord, zCoord);
    }

    public void reset() {
        list = new ArrayList();
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 3));
        list.add(new PlaceableIFaceable(HMBlocks.tiles, 2, 0, 1, 3, ForgeDirection.WEST));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 5));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 6));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 3));
        list.add(new PlaceableIFaceable(HMBlocks.tiles, 2, 1, 1, 3, ForgeDirection.WEST));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 5));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 6));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 3));
        list.add(new PlaceableIFaceable(HMBlocks.tiles, 2, 2, 1, 3, ForgeDirection.WEST));
        list.add(new PlaceableStairs(Blocks.oak_stairs, 0, 2, 2, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 5));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 6));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 0));
        list.add(new PlaceableIFaceable(HMBlocks.tiles, 2, 3, 1, 0, ForgeDirection.NORTH));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 1));
        list.add(new PlaceableIFaceable(HMBlocks.tiles, 2, 3, 1, 1, ForgeDirection.NORTH));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 2));
        list.add(new PlaceableIFaceable(HMBlocks.tiles, 2, 3, 1, 2, ForgeDirection.NORTH));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 3));
        list.add(new PlaceableIFaceable(HMBlocks.tiles, 2, 3, 1, 3, ForgeDirection.EAST));
        list.add(new PlaceableIFaceable(HMBlocks.tiles, 2, 3, 2, 3, ForgeDirection.SOUTH));
        list.add(new PlaceableIFaceable(HMBlocks.tiles, 2, 3, 3, 3, ForgeDirection.SOUTH));
        list.add(new PlaceableIFaceable(HMBlocks.tiles, 2, 3, 4, 3, ForgeDirection.SOUTH));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 4));
        list.add(new PlaceableIFaceable(HMBlocks.tiles, 2, 3, 1, 4, ForgeDirection.SOUTH));
        list.add(new PlaceableStairs(Blocks.oak_stairs, 3, 3, 2, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 5));
        list.add(new PlaceableIFaceable(HMBlocks.tiles, 2, 3, 1, 5, ForgeDirection.SOUTH));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 6));
        list.add(new PlaceableIFaceable(HMBlocks.tiles, 2, 3, 1, 6, ForgeDirection.SOUTH));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 3));
        list.add(new PlaceableIFaceable(HMBlocks.tiles, 2, 4, 1, 3, ForgeDirection.EAST));
        list.add(new PlaceableStairs(Blocks.oak_stairs, 1, 4, 2, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 5));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 6));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 3));
        list.add(new PlaceableIFaceable(HMBlocks.tiles, 2, 5, 1, 3, ForgeDirection.EAST));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 5));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 6));
        list.add(new PlaceableBlock(Blocks.dirt, 0, 6, 0, 0));
        list.add(new PlaceableBlock(Blocks.planks, 0, 6, 1, 0));
        list.add(new PlaceableBlock(Blocks.planks, 0, 6, 2, 0));
        list.add(new PlaceableBlock(Blocks.planks, 0, 6, 3, 0));
        list.add(new PlaceableBlock(Blocks.planks, 0, 6, 4, 0));
        list.add(new PlaceableBlock(Blocks.planks, 0, 6, 5, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 6, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 6, 0, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 6, 0, 3));
        list.add(new PlaceableIFaceable(HMBlocks.tiles, 2, 6, 1, 3, ForgeDirection.EAST));
        list.add(new PlaceableBlock(Blocks.grass, 0, 6, 0, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 6, 0, 5));
        list.add(new PlaceableBlock(Blocks.grass, 0, 6, 0, 6));

    }
}
