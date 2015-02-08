package joshie.harvestmoon.buildings;

import java.util.ArrayList;

import joshie.harvestmoon.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableSignFloor;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableSignWall;
import joshie.harvestmoon.init.HMBlocks;
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
        list.add(new PlaceableSignWall(Blocks.wall_sign, 4, 0, 1, 1, new String[] { "sssdsdsds", "d", "sd", "sd" } ));
        list.add(new PlaceableBlock(Blocks.grass, 0, 0, 0, 2));
        list.add(new PlaceableBlock(Blocks.wool, 3, 0, 3, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 0));
        list.add(new PlaceableSignWall(Blocks.wall_sign, 2, 1, 2, 0, new String[] { "", "", "", "fuuuuuuuuck" } ));
        list.add(new PlaceableBlock(Blocks.dirt, 0, 1, 0, 1));
        list.add(new PlaceableBlock(Blocks.wool, 3, 1, 1, 1));
        list.add(new PlaceableBlock(Blocks.wool, 3, 1, 2, 1));
        list.add(new PlaceableBlock(Blocks.wool, 3, 1, 3, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 1, 0, 2));
        list.add(new PlaceableSignWall(Blocks.wall_sign, 3, 1, 2, 2, new String[] { "", "sssssssss", "", "" } ));
        list.add(new PlaceableBlock(Blocks.wool, 3, 1, 3, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 1));
        list.add(new PlaceableSignFloor(Blocks.standing_sign, 12, 2, 1, 1, new String[] { "", "", "SIGN", "" } ));
        list.add(new PlaceableBlock(Blocks.grass, 0, 2, 0, 2));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 0));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 1));
        list.add(new PlaceableBlock(Blocks.grass, 0, 3, 0, 2));


    }
}
