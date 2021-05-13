package uk.joshiejack.penguinlib.template.blocks;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

@PenguinLoader("movein")
public class PlaceableMoveIn extends PlaceableBlock {
    @SuppressWarnings("unused")
    public PlaceableMoveIn() {}
    public PlaceableMoveIn(IBlockState state, BlockPos position) {
        super(state, position);
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.MOVEIN;
    }
}
