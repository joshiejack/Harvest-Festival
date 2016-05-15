package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.state.IBlockState;

public class PlaceableDecorative extends PlaceableBlock {
    public PlaceableDecorative() {}
    public PlaceableDecorative(IBlockState state, int x, int y, int z) {
        super(state, x, y, z);
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }
}
