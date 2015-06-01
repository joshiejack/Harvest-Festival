package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.BlockFlower;

public class PlaceableFlower extends PlaceableBlock {
    public PlaceableFlower(BlockFlower flower, int meta, int x, int y, int z) {
        super(flower, meta, x, y, z);
    }

    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }
}
