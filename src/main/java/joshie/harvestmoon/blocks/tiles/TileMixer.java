package joshie.harvestmoon.blocks.tiles;

import joshie.harvestmoon.blocks.BlockCookware;
import joshie.harvestmoon.cooking.Utensil;
import joshie.harvestmoon.init.HMBlocks;
import net.minecraft.block.Block;

public class TileMixer extends TileCooking {
    @Override
    public Utensil getUtensil() {
        return Utensil.MIXER;
    }

    @Override
    public boolean hasPrerequisites() {
        Block block = worldObj.getBlock(xCoord, yCoord - 1, zCoord);
        int meta = worldObj.getBlockMetadata(xCoord, yCoord - 1, zCoord);
        if (block == HMBlocks.cookware && meta == BlockCookware.KITCHEN) {
            return true;
        } else return false;
    }
}
