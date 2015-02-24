package joshie.harvestmoon.core.helpers;

import java.util.ArrayList;

import net.minecraft.world.World;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvestmoon.mining.MineTrackerServer;

public class MineHelper {
    public static void newDay() {
        getServerTracker().newDay();
    }

    public static MineTrackerServer getServerTracker() {
        return ServerHelper.getMineTracker();
    }

    public static void complete(World world, int x, int y, int z, ArrayList<PlaceableBlock> completed) {
        getServerTracker().completeMine(world, x, y, z, completed);
    }

    public static void caveIn(World world, int x, int y, int z) {
        getServerTracker().destroyLevel(world, x, y, z);
    }
}
