package joshie.harvestmoon.buildings;

import java.util.ArrayList;

import joshie.harvestmoon.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableTorches;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

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
        list.add(new PlaceableTorches(Blocks.torch, 2, 0, 0, 1));
        list.add(new PlaceableTorches(Blocks.torch, 2, 0, 1, 1));
        list.add(new PlaceableTorches(Blocks.torch, 2, 0, 2, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 2, 0));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 1, 0, 1));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 1, 1, 1));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 1, 2, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 2, 0));
        list.add(new PlaceableTorches(Blocks.torch, 1, 2, 0, 1));
        list.add(new PlaceableTorches(Blocks.torch, 1, 2, 1, 1));
        list.add(new PlaceableTorches(Blocks.torch, 1, 2, 2, 1));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 2, 0, 2));
    }
}
