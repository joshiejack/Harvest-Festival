package uk.joshiejack.penguinlib.template.blocks;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@PenguinLoader("web")
public class PlaceableWeb extends PlaceableDecorative {
    @SuppressWarnings("unused")
    public PlaceableWeb() {}
    public PlaceableWeb(IBlockState state, BlockPos position) {
        super(state, position);
    }

    @Override
    public void postPlace (World world, BlockPos pos, Rotation rotation) {
        if (world.rand.nextInt(3) != 0) world.setBlockToAir(pos);
    }
}