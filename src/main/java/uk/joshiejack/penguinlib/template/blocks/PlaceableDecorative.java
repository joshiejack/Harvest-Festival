package uk.joshiejack.penguinlib.template.blocks;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

@PenguinLoader("decorative")
public class PlaceableDecorative extends PlaceableBlock {
    @SuppressWarnings("WeakerAccess")
    public PlaceableDecorative() {}
    public PlaceableDecorative(IBlockState state, BlockPos position) {
        super(state, position);
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }
}
