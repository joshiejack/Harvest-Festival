package joshie.harvest.mining;

import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public abstract class MineTracker {
    public void completeMine(World world, BlockPos pos, ArrayList<PlaceableBlock> completed) {}
    public void destroyLevel(World world, BlockPos pos) {}
    public void newDay() {}
}