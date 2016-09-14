package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.state.IBlockState;

public class PlaceableMoveIn extends PlaceableBlock {
    public PlaceableMoveIn() {}
    public PlaceableMoveIn(IBlockState state, int x, int y, int z) {
        super(state, x, y, z);
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.MOVEIN;
    }
}
