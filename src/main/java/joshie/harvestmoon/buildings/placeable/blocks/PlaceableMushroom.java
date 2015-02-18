package joshie.harvestmoon.buildings.placeable.blocks;


public class PlaceableMushroom extends PlaceableBlock {
    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }
}
