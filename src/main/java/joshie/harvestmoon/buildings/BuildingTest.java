package joshie.harvestmoon.buildings;

import java.util.ArrayList;

import joshie.harvestmoon.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableTrapDoor;
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
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 2));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 0, 0, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 5));
        list.add(new PlaceableBlock(Blocks.dirt, 0, 0, 0, 6));
        list.add(new PlaceableBlock(Blocks.planks, 0, 0, 1, 6));
        list.add(new PlaceableBlock(Blocks.planks, 0, 0, 2, 6));
        list.add(new PlaceableBlock(Blocks.planks, 0, 0, 3, 6));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 2));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 1, 0, 3));
        list.add(new PlaceableTrapDoor(Blocks.trapdoor, 14, 1, 2, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 5));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 6));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 2, 0, 0));
        list.add(new PlaceableBlock(Blocks.standing_sign, 0, 2, 1, 0));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 2, 0, 1));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 2, 0, 2));
        list.add(new PlaceableTrapDoor(Blocks.trapdoor, 12, 2, 1, 2));
        list.add(new PlaceableTrapDoor(Blocks.trapdoor, 8, 2, 2, 2));
        list.add(new PlaceableBlock(Blocks.dirt, 0, 2, 0, 3));
        list.add(new PlaceableBlock(Blocks.planks, 0, 2, 1, 3));
        list.add(new PlaceableBlock(Blocks.planks, 0, 2, 2, 3));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 2, 0, 4));
        list.add(new PlaceableTrapDoor(Blocks.trapdoor, 13, 2, 2, 4));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 2, 0, 5));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 2, 0, 6));
        list.add(new PlaceableBlock(Blocks.standing_sign, 8, 2, 1, 6));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 2));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 3, 0, 3));
        list.add(new PlaceableTrapDoor(Blocks.trapdoor, 3, 3, 1, 3));
        list.add(new PlaceableTrapDoor(Blocks.trapdoor, 11, 3, 2, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 5));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 6));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 2));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 4, 0, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 5));
        list.add(new PlaceableBlock(Blocks.grass, 0, 4, 0, 6));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 2));
        list.add(new PlaceableBlock(Blocks.cobblestone, 0, 5, 0, 3));
        list.add(new PlaceableBlock(Blocks.standing_sign, 4, 5, 1, 3));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 4));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 5));
        list.add(new PlaceableBlock(Blocks.grass, 0, 5, 0, 6));

    }
}
