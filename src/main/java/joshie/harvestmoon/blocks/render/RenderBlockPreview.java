package joshie.harvestmoon.blocks.render;

import joshie.harvestmoon.blocks.BlockPreview;
import joshie.harvestmoon.buildings.Building;
import joshie.harvestmoon.buildings.placeable.Placeable;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvestmoon.init.HMBuildings;
import joshie.harvestmoon.util.RenderBase;
import net.minecraft.client.renderer.RenderBlocks;

public class RenderBlockPreview extends RenderBase {
    public static RenderBlocks renderer = new RenderBlocks();

    public static Building preview;
    
    public static int[] getAdjustedCoordinates(PlaceableBlock block, boolean n1, boolean n2, boolean swap) {
        int yCoord = block.getY();
        int xCoord = n1 ? -block.getX() : block.getX();
        int zCoord = n2 ? -block.getZ() : block.getZ();
        if (swap) {
            int xClone = xCoord; //Create a copy of X
            xCoord = zCoord; //Set x to z
            zCoord = xClone; //Set z to the old value of x
        }
        
        return new int[] { xCoord, yCoord, zCoord };
    }

    @Override
    public void renderBlock() {
        if (!isItem()) {
            int meta = world.getBlockMetadata(x, y, z);
            boolean n1 = BlockPreview.getN1FromMeta(meta);
            boolean n2 = BlockPreview.getN2FromMeta(meta);
            boolean swap = BlockPreview.getSwapFromMeta(meta);
            preview = HMBuildings.carpenter.getBuilding(0);
            if (preview == null) return;
            
            renderer.blockAccess = preview.getBlockAccess(x, y, z, n1, n2, swap);
            
            for (Placeable placeable : preview.getList()) {
                if (placeable instanceof PlaceableBlock) {
                    PlaceableBlock block = (PlaceableBlock) placeable;
                    int xCoord = n1? -block.getX(): block.getX();
                    int yCoord = block.getY();
                    int zCoord = n2? -block.getZ(): block.getZ();
                    
                    if (swap) {
                        int xClone = xCoord; //Create a copy of X
                        xCoord = zCoord; //Set x to z
                        zCoord = xClone; //Set z to the old value of x
                    }
                    
                    renderer.renderBlockAllFaces(block.getBlock(), x + xCoord, y + yCoord, z + zCoord);
                }
            }
        }
    }
}
