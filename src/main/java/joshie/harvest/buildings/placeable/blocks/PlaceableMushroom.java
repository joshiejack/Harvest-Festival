package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.BlockBush;

public class PlaceableMushroom extends PlaceableBlock {
    public PlaceableMushroom(BlockBush mushroom, int meta, int x, int y, int z) {
        super(mushroom, meta, x, y, z);
    }

    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }
}
