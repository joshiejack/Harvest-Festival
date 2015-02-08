package joshie.harvestmoon.buildings;

import java.util.ArrayList;

import joshie.harvestmoon.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableDoor;
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
        list.add(new PlaceableDoor(Blocks.wooden_door, 2, 0, 1, 0));
        list.add(new PlaceableDoor(Blocks.wooden_door, 8, 0, 2, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 2));
        list.add(new PlaceableBlock(Blocks.dirt, 0, 1, 0, 0));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 1, 1, 0));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 1, 2, 0));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 1, 3, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 1));
        list.add(new PlaceableDoor(Blocks.wooden_door, 1, 1, 1, 1));
        list.add(new PlaceableDoor(Blocks.wooden_door, 8, 1, 2, 1));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 1, 3, 1));
        list.add(new PlaceableBlock(Blocks.dirt, 0, 1, 0, 2));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 1, 1, 2));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 1, 2, 2));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 1, 3, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 0));
        list.add(new PlaceableDoor(Blocks.wooden_door, 0, 2, 1, 0));
        list.add(new PlaceableDoor(Blocks.wooden_door, 8, 2, 2, 0));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 2, 3, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 0));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 3, 3, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 2));



    }
}
