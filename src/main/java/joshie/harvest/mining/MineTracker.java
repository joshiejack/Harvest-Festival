package joshie.harvest.mining;

import java.util.ArrayList;

import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import net.minecraft.world.World;

public abstract class MineTracker {
    public void completeMine(World world, int x, int y, int z, ArrayList<PlaceableBlock> completed) {}
    public void destroyLevel(World world, int x, int y, int z) {}
    public void newDay() {}
}
