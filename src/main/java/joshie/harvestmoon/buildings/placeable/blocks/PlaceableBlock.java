package joshie.harvestmoon.buildings.placeable.blocks;

import joshie.harvestmoon.buildings.placeable.Placeable;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class PlaceableBlock extends Placeable {
    private Block block;
    protected int meta;
    private int offsetX;
    private int offsetY;
    private int offsetZ;

    public PlaceableBlock(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(offsetX, offsetY, offsetZ);
        this.block = block;
        this.meta = meta;
    }

    protected int getMetaData(boolean n1, boolean n2, boolean swap) {
        return meta;
    }

    @Override
    protected boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.BLOCKS;
    }

    @Override
    public void place(World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        int meta = getMetaData(n1, n2, swap);
        if (meta == 0) {
            world.setBlock(x, y, z, block);
        } else {
            world.setBlock(x, y, z, block, meta, 2);
        }
    }
}
