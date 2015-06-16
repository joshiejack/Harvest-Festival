package joshie.harvest.core.helpers;

import java.util.ArrayList;

import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.core.handlers.DataHelper;
import net.minecraft.world.World;

public class MineHelper {
    public static void complete(World world, int x, int y, int z, ArrayList<PlaceableBlock> completed) {
        DataHelper.getMineTracker().completeMine(world, x, y, z, completed);
    }

    public static void caveIn(World world, int x, int y, int z) {
        DataHelper.getMineTracker().destroyLevel(world, x, y, z);
    }
}
