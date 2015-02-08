package joshie.harvestmoon.buildings;

import java.util.ArrayList;

import joshie.harvestmoon.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableFurnace;
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
        list.add(new PlaceableBlock(Blocks.dirt, 0, 0, 0, 1));
        list.add(new PlaceableFurnace(Blocks.furnace, 3, 0, 1, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 2));
        list.add(new PlaceableFurnace(Blocks.ladder, 4, 0, 1, 2));
        list.add(new PlaceableFurnace(Blocks.ladder, 4, 0, 2, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 1));
        list.add(new PlaceableFurnace(Blocks.furnace, 3, 1, 1, 1));
        list.add(new PlaceableFurnace(Blocks.furnace, 3, 1, 2, 1));
        list.add(new PlaceableFurnace(Blocks.furnace, 3, 1, 3, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 2));
        list.add(new PlaceableFurnace(Blocks.furnace, 3, 1, 1, 2));
        list.add(new PlaceableFurnace(Blocks.furnace, 3, 1, 2, 2));
        list.add(new PlaceableFurnace(Blocks.ladder, 3, 1, 3, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 3));
        list.add(new PlaceableFurnace(Blocks.furnace, 3, 1, 1, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 1));
        list.add(new PlaceableFurnace(Blocks.ender_chest, 5, 2, 1, 1));
        list.add(new PlaceableFurnace(Blocks.ladder, 5, 2, 2, 1));
        list.add(new PlaceableFurnace(Blocks.ladder, 5, 2, 3, 1));
        list.add(new PlaceableBlock(Blocks.dirt, 0, 2, 0, 2));
        list.add(new PlaceableFurnace(Blocks.furnace, 3, 2, 1, 2));
        list.add(new PlaceableFurnace(Blocks.ladder, 5, 2, 2, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 3));
        list.add(new PlaceableFurnace(Blocks.ender_chest, 5, 2, 1, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 1));
        list.add(new PlaceableFurnace(Blocks.ender_chest, 5, 3, 1, 1));
        list.add(new PlaceableBlock(Blocks.dirt, 0, 3, 0, 2));
        list.add(new PlaceableBlock(Blocks.planks, 0, 3, 1, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 2));
        list.add(new PlaceableFurnace(Blocks.ender_chest, 5, 4, 1, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 2));
        list.add(new PlaceableFurnace(Blocks.ender_chest, 5, 5, 1, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 6, 0, 0));
        list.add(new PlaceableBlock(Blocks.planks, 0, 6, 1, 0));
        list.add(new PlaceableBlock(Blocks.planks, 0, 6, 2, 0));
        list.add(new PlaceableBlock(Blocks.planks, 0, 6, 3, 0));
        list.add(new PlaceableBlock(Blocks.planks, 0, 6, 4, 0));
        list.add(new PlaceableBlock(Blocks.planks, 0, 6, 5, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 6, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 6, 0, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 6, 0, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 6, 0, 4));

    }
}
