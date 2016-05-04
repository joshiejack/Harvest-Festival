package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class PlaceableSignFloor extends PlaceableSignWall {
    public PlaceableSignFloor(Block block, int meta, BlockPos offsetPos, String[] text) {
        super(block, meta, offsetPos, text);
    }
}