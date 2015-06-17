package joshie.harvest.core.helpers;

import java.util.ArrayList;

import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.world.World;

public class MineHelper {
    public static void complete(World world, int x, int y, int z, ArrayList<PlaceableBlock> completed) {
        HFTrackers.getMineTracker().completeMine(world, x, y, z, completed);
    }

    public static void caveIn(World world, int x, int y, int z) {
        HFTrackers.getMineTracker().destroyLevel(world, x, y, z);
    }
}
