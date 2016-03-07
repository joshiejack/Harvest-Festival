package joshie.harvest.blocks.render;

import joshie.harvest.blocks.BlockPreview;
import joshie.harvest.blocks.tiles.TileMarker;
import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.core.util.RenderBase;

public class RenderPreview extends RenderBase {
    public static RenderBlocks renderer = new RenderBlocks();

    @Override
    public void renderBlock() {
        if (!isItem()) {
            int meta = world.getBlockMetadata(x, y, z);
            boolean n1 = BlockPreview.getN1FromMeta(meta);
            boolean n2 = BlockPreview.getN2FromMeta(meta);
            boolean swap = BlockPreview.getSwapFromMeta(meta);
            TileMarker marker = (TileMarker) world.getTileEntity(x, y, z);
            Building preview = marker.getBuilding();
            if (preview == null) return;

            renderer.blockAccess = preview.getBlockAccess(x, y, z, n1, n2, swap);

            for (Placeable placeable : preview.getList()) {
                if (placeable instanceof PlaceableBlock) {
                    PlaceableBlock block = (PlaceableBlock) placeable;
                    int xCoord = n1 ? -block.getX() : block.getX();
                    int yCoord = block.getY();
                    int zCoord = n2 ? -block.getZ() : block.getZ();

                    if (swap) {
                        int xClone = xCoord; //Create a copy of X
                        xCoord = zCoord; //Set x to z
                        zCoord = xClone; //Set z to the old value of x
                    }

                    renderer.renderBlockAllFaces(block.getBlock(), x + xCoord, y  + yCoord + preview.getOffsetY(), z + zCoord);
                }
            }
        }
    }
}
