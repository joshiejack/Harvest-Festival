package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceableWeb extends PlaceableDecorative {
    public PlaceableWeb() {}
    public PlaceableWeb(IBlockState state, int x, int y, int z) {
        super(state, x, y, z);
    }

    @Override
    public void postPlace (World world, BlockPos pos, Rotation rotation) {
        if (world.rand.nextInt(3) != 0) world.setBlockToAir(pos);
    }
}