package joshie.harvestmoon.buildings.meta;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

public class MetaHelper {
    public static int convert(Block block, int meta, boolean n1, boolean n2, boolean swap) {
        if(block instanceof BlockStairs) {
            return MetaStairs.getMetaData(n1, n2, swap, meta);
        }
        
        return meta;
    }
}
