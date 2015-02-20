package joshie.harvestmoon.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceableLilypad extends PlaceableBlock {
    public PlaceableLilypad(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }

    @Override
    public int getMetaData(boolean n1, boolean n2, boolean swap) {
        //TODO:

        return meta;
    }
}
