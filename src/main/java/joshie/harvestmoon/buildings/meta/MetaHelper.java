package joshie.harvestmoon.buildings.meta;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;

public class MetaHelper {
    /** TODO:
     * Chests
     * Furnaces
     * Ladders
     * Anvil
     * Item Frames
     * Buttons
     * Painting
     * Signs
     * Lever
     * Trapdoors
     * Pumpkins
     * Doors
     * Gates
     * IFaceable
     * Jack o Lanterns
     * Lily Pads
     * Ender Chest
     * Trapped Chest
     * Bed
     * Skulls
     * Pistons
     * Redstone Torch
     * Tripwire
     * Dispenser
     * Dropper
     */
    public static int convert(Block block, int meta, boolean n1, boolean n2, boolean swap) {
        if(block instanceof BlockStairs) {
            return MetaStairs.getMetaData(n1, n2, swap, meta);
        } else if (block instanceof BlockTorch) {
            return MetaTorch.getMetaData(n1, n2, swap, meta);
        }
        
        return meta;
    }
}
