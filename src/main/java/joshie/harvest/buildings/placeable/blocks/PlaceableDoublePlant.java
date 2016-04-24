package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.UUID;

public class PlaceableDoublePlant extends PlaceableBlock {
    public PlaceableDoublePlant(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }
    
    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }

    @Override
    public boolean place(UUID uuid, World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        if (!super.place(uuid, world, x, y, z, n1, n2, swap)) return false;
        else return world.setBlock(x, y + 1, z, block, 8, 2);
    }
}
