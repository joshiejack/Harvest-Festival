package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class PlaceableWeb extends PlaceableBlock {
    public PlaceableWeb(Block web, int meta, int x, int y, int z) {
        super(web, meta, x, y, z);
    }

    @Override
    public boolean place(UUID uuid, World world, BlockPos pos, IBlockState state, boolean n1, boolean n2, boolean swap) {
        if (world.rand.nextInt(3) == 0) {
            return super.place(uuid, world, pos, state, n1, n2, swap);
        } else return false;
    }
}