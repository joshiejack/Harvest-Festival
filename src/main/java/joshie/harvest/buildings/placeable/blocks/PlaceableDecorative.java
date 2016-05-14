package joshie.harvest.buildings.placeable.blocks;

public class PlaceableDecorative extends PlaceableBlock {
    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }
}
