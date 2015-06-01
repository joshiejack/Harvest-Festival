package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceableLog extends PlaceableBlock {
    public PlaceableLog(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    public int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (swap) {
            if (meta == 7 || meta == 11) {
                return meta == 7 ? 11 : 7;
            } else if (meta == 6 || meta == 10) {
                return meta == 6 ? 10 : 6;
            } else if (meta == 5 || meta == 9) {
                return meta == 5 ? 9 : 5;
            } else if (meta == 4 || meta == 8) {
                return meta == 4 ? 8 : 4;
            }
        }

        return meta;
    }
}
