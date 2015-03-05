package joshie.harvestmoon.buildings.placeable.blocks;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class PlaceableWeb extends PlaceableBlock {
    public PlaceableWeb(Block web, int meta, int x, int y, int z) {
        super(web, meta, x, y, z);
    }

    @Override
    public boolean place(UUID uuid, World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        if (world.rand.nextInt(3) == 0) {
            return super.place(uuid, world, x, y, z, n1, n2, swap);
        } else return false;
    }
}
