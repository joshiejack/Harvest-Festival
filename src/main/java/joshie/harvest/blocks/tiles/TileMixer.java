package joshie.harvest.blocks.tiles;

import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.blocks.BlockCookware;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.cooking.Utensil;
import net.minecraft.block.Block;

public class TileMixer extends TileCooking {
    @Override
    public IUtensil getUtensil() {
        return Utensil.MIXER;
    }

    @Override
    public boolean hasPrerequisites() {
        Block block = worldObj.getBlock(xCoord, yCoord - 1, zCoord);
        int meta = worldObj.getBlockMetadata(xCoord, yCoord - 1, zCoord);
        if (block == HFBlocks.cookware && meta == BlockCookware.KITCHEN) {
            return true;
        } else return false;
    }
}
