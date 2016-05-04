package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceableDecorative extends PlaceableBlock {
    public PlaceableDecorative(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }
}
